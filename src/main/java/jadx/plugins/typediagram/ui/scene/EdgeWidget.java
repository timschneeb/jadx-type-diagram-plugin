package jadx.plugins.typediagram.ui.scene;

import jadx.plugins.typediagram.model.BaseEdge;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;

import java.awt.Stroke;

public class EdgeWidget extends ConnectionWidget {
	/**
	 * Creates a connection widget.
	 *
	 * @param scene the scene
	 */
	public EdgeWidget(Scene scene, BaseEdge edge) {
		super(scene);

		Stroke stroke = edge.getStrokeStyle();
		if(stroke != null) {
			setStroke(stroke);
		}

		setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
		setTargetAnchor(AnchorFactory.createRectangularAnchor(edge.getTarget()));
		setSourceAnchor(AnchorFactory.createRectangularAnchor(edge.getSource()));
	}
}
