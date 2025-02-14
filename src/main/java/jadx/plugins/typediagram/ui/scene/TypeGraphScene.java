package jadx.plugins.typediagram.ui.scene;

import jadx.api.JavaClass;
import jadx.api.plugins.JadxPluginContext;
import jadx.core.dex.instructions.args.ArgType;
import jadx.plugins.typediagram.TypeDiagramOptions;
import jadx.plugins.typediagram.model.AccessFlagFilters;
import jadx.plugins.typediagram.model.BaseEdge;
import jadx.plugins.typediagram.model.BaseNode;
import jadx.plugins.typediagram.model.ClassNode;
import jadx.plugins.typediagram.model.ExtendsEdge;
import jadx.plugins.typediagram.model.ExternalClassNode;
import jadx.plugins.typediagram.model.ImplementsEdge;
import jadx.plugins.typediagram.model.InterfaceNode;
import jadx.plugins.typediagram.ui.scene.layout.FitToViewLayout;
import jadx.plugins.typediagram.ui.scene.actions.HierarchicalGraphLayoutAction;
import jadx.plugins.typediagram.ui.scene.actions.TreeGraphLayoutAction;
import jadx.plugins.typediagram.ui.scene.actions.ZoomAction;
import jadx.plugins.typediagram.ui.utils.IconProvider;
import jadx.plugins.typediagram.ui.utils.UiUtils;
import jadx.plugins.typediagram.ui.utils.Visitors;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.export.SceneExporter;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.graph.layout.GraphLayout;
import org.netbeans.api.visual.graph.layout.GraphLayoutFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TypeGraphScene extends GraphScene<BaseNode, BaseEdge> {

	private final LayerWidget mainLayer;
	private final LayerWidget connectionLayer;

	private final FitToViewLayout fitToViewLayout;
	private SceneLayout sceneLayout;
	private BaseNode rootNode;
	@Nullable
	private JavaClass targetObject;

	private final JadxPluginContext context;
	private final TypeDiagramOptions options;
	private final Mode mode;

	private boolean showFields = false;
	private boolean showMethods = false;
	private AccessFlagFilters accessFilter = AccessFlagFilters.All;

	public enum Mode {
		ALL, UPSTREAM_ONLY
	}

	public TypeGraphScene(JadxPluginContext context, TypeDiagramOptions options, JScrollPane scrollPane, Mode mode) {
		this.context = context;
		this.options = options;
		this.mode = mode;
		this.mainLayer = new LayerWidget(this);
		this.connectionLayer = new LayerWidget(this);
		this.fitToViewLayout = new FitToViewLayout(this, scrollPane);

		addChild(mainLayer);
		addChild(connectionLayer);

		setZoomFactor(options.initialZoom());
		resetLayout();

		getActions().addAction(ActionFactory.createPopupMenuAction(new MenuActionsProvider()));
		getActions().addAction(ActionFactory.createZoomAction());
		getActions().addAction(ActionFactory.createWheelPanAction());
	}

	public void setShowFields(boolean showFields) {
		this.showFields = showFields;
		refresh();
	}

	public void setShowMethods(boolean showMethods) {
		this.showMethods = showMethods;
		refresh();
	}

	public AccessFlagFilters getAccessFilter() {
		return accessFilter;
	}

	public void setAccessFilter(AccessFlagFilters accessFilter) {
		this.accessFilter = accessFilter;
		refresh();
	}

	public void setSceneLayout(SceneLayout sceneLayout) {
		this.sceneLayout = sceneLayout;
	}

	public BaseNode getRootNode() {
		return rootNode;
	}

	@Override
	protected Widget attachNodeWidget(BaseNode node) {
		NodeWidget widget = new NodeWidget(context, this, node, showFields, showMethods);
		mainLayer.addChild(widget);
		return widget;
	}

	@Override
	protected Widget attachEdgeWidget(BaseEdge edge) {
		EdgeWidget widget = new EdgeWidget(this, edge);
		connectionLayer.addChild(widget);
		return widget;
	}

	@Override
	protected void attachEdgeSourceAnchor(BaseEdge edge, BaseNode oldSourceNode, BaseNode sourceNode) {
		Widget sourceNodeWidget = findWidget(sourceNode);
		Anchor sourceAnchor = AnchorFactory.createRectangularAnchor(sourceNodeWidget);
		ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
		edgeWidget.setSourceAnchor(sourceAnchor);
	}

	@Override
	protected void attachEdgeTargetAnchor(BaseEdge edge, BaseNode oldTargetNode, BaseNode targetNode) {
		Widget targetNodeWidget = findWidget(targetNode);
		Anchor targetAnchor = AnchorFactory.createRectangularAnchor(targetNodeWidget);
		ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
		edgeWidget.setTargetAnchor(targetAnchor);
	}

	private BaseNode resolveNode(ArgType type, Map<ArgType, Widget> nodeWidgetMap, boolean isRoot) {
		BaseNode nodeToAdd = null;

		if(type.isObject()) {
			String targetClass = type.getObject().replace('$', '.');
			JavaClass result = context.getDecompiler().searchJavaClassByOrigFullName(targetClass);

			if(result != null) {
				boolean isInterface = result.getClassNode().getAccessFlags().isInterface();
				nodeToAdd = isInterface ? new InterfaceNode(result) : new ClassNode(result);

				// Add matching icon
				Image image = IconProvider.resolveImageForClass(result);
				if(image != null) {
					image = image.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
				}
				nodeToAdd.setIcon(image);
			}
		}

		if(nodeToAdd == null) {
			nodeToAdd = new ExternalClassNode(type.toString());
		}

		nodeToAdd.setRoot(isRoot);

		Widget w = addNode(nodeToAdd);
		nodeWidgetMap.put(type, w);
		return nodeToAdd;
	}

	private void linkNodes(JavaClass targetObject, ArgType t1, ArgType t2, Map<ArgType, Widget> nodeWidgetMap){
		if (!nodeWidgetMap.containsKey(t1)) {
			// Find and set root node
			boolean isRoot = t1.isObject() && Objects.equals(targetObject.getClassNode().getClassInfo().getFullName(), t1.getObject());
			BaseNode node = resolveNode(t1, nodeWidgetMap, isRoot);
			if(isRoot) {
				rootNode = node;
			}
		}
		if (!nodeWidgetMap.containsKey(t2)) {
			resolveNode(t2, nodeWidgetMap, false);
		}

		Widget w1 = nodeWidgetMap.get(t1);
		Widget w2 = nodeWidgetMap.get(t2);

		BaseNode n1 = (BaseNode) findObject(w1);
		BaseNode n2 = (BaseNode) findObject(w2);

		BaseEdge e;
		if (n1 instanceof InterfaceNode || n2 instanceof InterfaceNode) {
			e = new ImplementsEdge(w1, w2);
		} else {
			e = new ExtendsEdge(w1, w2);
		}

		addEdge(e);
		setEdgeSource(e, n1);
		setEdgeTarget(e, n2);
	}

	public void setTargetObject(JavaClass targetObject) {
		this.targetObject = targetObject;
		refresh();
	}

	public void refresh() {
		UiUtils.invokeOnEventDispatchThread(() -> {
			JavaClass object = targetObject;
			if(object == null)
				return;

			// Clear old widgets
			this.mainLayer.removeChildren();
			this.connectionLayer.removeChildren();

			// Clear old nodes & edges
			Collection<BaseNode> oldNodes = getNodes();
			for (int i = oldNodes.size() - 1; i >= 0; i--) {
				removeNodeWithEdges(oldNodes.iterator().next());
			}

			this.doLayout();

			Map<ArgType, Widget> nodeWidgetMap = new HashMap<>();
			rootNode = resolveNode(object.getClassNode().getType(), nodeWidgetMap, true);

			object.getClassNode().visitSuperTypes((ArgType t1, ArgType t2) -> {
				linkNodes(object, t1, t2, nodeWidgetMap);
			});

			if(mode != Mode.UPSTREAM_ONLY) {
				jadx.core.dex.nodes.ClassNode cls = object.getClassNode();
				Visitors.visitSubTypes(cls.root(), object, (ArgType t1, ArgType t2) -> {
					linkNodes(object, t2, t1, nodeWidgetMap);
				});
			}

			resetLayout();
		});
	}

	public void doLayout(){
		UiUtils.invokeOnEventDispatchThread(() -> {
			sceneLayout.invokeLayout();
			validate();
		});
	}

	public void resetLayout() {
		final GraphLayout<BaseNode, BaseEdge> layout =
				GraphLayoutFactory.createHierarchicalGraphLayout(this, true);
		sceneLayout = LayoutFactory.createSceneGraphLayout(this, layout);
		doLayout();
	}

	public void captureImage(File file) {
		try {
			SceneExporter.createImage(this, file, SceneExporter.ImageType.PNG,
					SceneExporter.ZoomType.CURRENT_ZOOM_LEVEL, false, false, 100, 0, 0);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this.getView(),
					"Failed to save image:\n" + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void resetZoomFactor() {
		setZoomFactor(options.initialZoom());
	}

	public void fitToView() {
		fitToViewLayout.fitToView(null);
	}

	public JMenu getLayoutMenu() {
		final JMenu layoutMenu = new JMenu("Layout");
		List.of(new HierarchicalGraphLayoutAction(this, false),
				new HierarchicalGraphLayoutAction(this, true),
				new TreeGraphLayoutAction(this, true, mode == Mode.UPSTREAM_ONLY),
				new TreeGraphLayoutAction(this, false, mode == Mode.UPSTREAM_ONLY))
				.forEach(layoutMenu::add);
		return layoutMenu;
	}

	public List<AbstractAction> getZoomActions() {
		return List.of(new ZoomAction(this, ZoomAction.ZoomType.IN),
				new ZoomAction(this, ZoomAction.ZoomType.OUT),
				new ZoomAction(this, ZoomAction.ZoomType.RESET),
				new ZoomAction(this, ZoomAction.ZoomType.FIT));
	}

	private class MenuActionsProvider implements PopupMenuProvider {
		@Override public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
			JPopupMenu popupMenu = new JPopupMenu();
			if (widget == TypeGraphScene.this) {
				popupMenu.add(getLayoutMenu());
				getZoomActions().forEach(popupMenu::add);
			}
			return popupMenu;
		}
	}
}
