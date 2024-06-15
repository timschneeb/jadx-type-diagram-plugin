package jadx.plugins.typediagram.model;

import org.netbeans.api.visual.widget.Widget;

import javax.annotation.Nullable;
import java.awt.BasicStroke;
import java.awt.Stroke;

public class ImplementsEdge extends BaseEdge {

	public ImplementsEdge(Widget source, Widget classNode) {
		super(source, classNode);
	}

	@Nullable
	@Override
	public Stroke getStrokeStyle() {
		return new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_MITER, new float[]{4, 3}, 0);
	}
}
