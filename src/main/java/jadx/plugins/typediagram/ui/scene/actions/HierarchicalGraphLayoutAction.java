package jadx.plugins.typediagram.ui.scene.actions;

import jadx.plugins.typediagram.model.BaseEdge;
import jadx.plugins.typediagram.model.BaseNode;
import jadx.plugins.typediagram.ui.scene.TypeGraphScene;
import org.netbeans.api.visual.graph.layout.GraphLayout;
import org.netbeans.api.visual.graph.layout.GraphLayoutFactory;
import org.netbeans.api.visual.layout.LayoutFactory;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public class HierarchicalGraphLayoutAction extends AbstractAction {
	private final TypeGraphScene scene;
	private final boolean inverted;

	public HierarchicalGraphLayoutAction(TypeGraphScene scene, boolean inverted) {
		this.scene = scene;
		this.inverted = inverted;
		putValue(NAME, inverted ? "Hierarchical Graph Layout (Inverted)" : "Hierarchical Graph Layout");
	}
	@Override public void actionPerformed(ActionEvent e) {
		final GraphLayout<BaseNode, BaseEdge> layout = GraphLayoutFactory.createHierarchicalGraphLayout(scene, true, inverted);
		scene.setSceneLayout(LayoutFactory.createSceneGraphLayout(scene, layout));
		scene.doLayout();
	}
}
