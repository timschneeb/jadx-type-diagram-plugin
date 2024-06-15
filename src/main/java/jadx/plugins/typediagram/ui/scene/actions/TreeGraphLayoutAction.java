package jadx.plugins.typediagram.ui.scene.actions;

import jadx.plugins.typediagram.model.BaseEdge;
import jadx.plugins.typediagram.model.BaseNode;
import jadx.plugins.typediagram.ui.scene.TypeGraphScene;
import org.netbeans.api.visual.graph.layout.GraphLayout;
import org.netbeans.api.visual.graph.layout.GraphLayoutFactory;
import org.netbeans.api.visual.graph.layout.GraphLayoutSupport;
import org.netbeans.api.visual.layout.LayoutFactory;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public class TreeGraphLayoutAction extends AbstractAction {
	private final TypeGraphScene scene;
	private final boolean isVertical;

	public TreeGraphLayoutAction(TypeGraphScene scene, boolean isVertical, boolean hasRootNode) {
		this.scene = scene;
		this.isVertical = isVertical;
		putValue(NAME, isVertical ? "Tree Graph Layout (Vertical)" : "Tree Graph Layout (Horizontal)");
		this.setEnabled(hasRootNode);
	}
	@Override public void actionPerformed(ActionEvent e) {
		final GraphLayout<BaseNode, BaseEdge> layout = GraphLayoutFactory.createTreeGraphLayout(10, 10, 50, 50, isVertical);
		GraphLayoutSupport.setTreeGraphLayoutRootNode(layout, scene.getRootNode());
		scene.setSceneLayout(LayoutFactory.createSceneGraphLayout(scene, layout));
		scene.doLayout();
	}
}
