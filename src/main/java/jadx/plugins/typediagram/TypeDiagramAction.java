package jadx.plugins.typediagram;

import jadx.api.JavaClass;
import jadx.api.JavaNode;
import jadx.api.metadata.ICodeAnnotation;
import jadx.api.metadata.ICodeNodeRef;
import jadx.api.plugins.JadxPluginContext;
import jadx.plugins.typediagram.ui.DiagramDialog;
import jadx.plugins.typediagram.ui.scene.TypeGraphScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.function.Consumer;

public class TypeDiagramAction implements Consumer<ICodeNodeRef>
{
	private static final Logger LOG = LoggerFactory.getLogger(TypeDiagramAction.class);

	private final JadxPluginContext context;
	private final TypeDiagramOptions options;
	private final TypeGraphScene.Mode mode;

	public TypeDiagramAction(JadxPluginContext context, TypeDiagramOptions options, TypeGraphScene.Mode mode) {
		this.context = context;
		this.options = options;
		this.mode = mode;
	}

	public static void addToPopupMenu(JadxPluginContext context, TypeDiagramOptions options) {
		if(context.getGuiContext() == null) {
			return;
		}

		TypeDiagramAction actionAll = new TypeDiagramAction(context, options, TypeGraphScene.Mode.ALL);
		context.getGuiContext().addPopupMenuAction("Show type hierarchy", TypeDiagramAction::canActivate, null, actionAll);
		TypeDiagramAction actionOnlyUpstream = new TypeDiagramAction(context, options, TypeGraphScene.Mode.UPSTREAM_ONLY);
		context.getGuiContext().addPopupMenuAction("Show type hierarchy (supertypes only)", TypeDiagramAction::canActivate, null, actionOnlyUpstream);
	}

	public static Boolean canActivate(ICodeNodeRef ref) {
		return ref.getAnnType() == ICodeAnnotation.AnnType.CLASS || ref.getAnnType() == ICodeAnnotation.AnnType.DECLARATION;
	}

	@Override
	public void accept(ICodeNodeRef iCodeNodeRef) {
		JavaNode node = context.getDecompiler().getJavaNodeByRef(iCodeNodeRef);
		if(node instanceof JavaClass) {
			DiagramDialog.open(context, options, (JavaClass) node, mode);
		}
		else {
			LOG.error("Invalid node type: {}", node);
		}
	}
}
