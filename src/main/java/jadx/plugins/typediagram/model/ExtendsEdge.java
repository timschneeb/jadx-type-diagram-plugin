package jadx.plugins.typediagram.model;

import org.netbeans.api.visual.widget.Widget;

import javax.annotation.Nullable;
import java.awt.Stroke;

public class ExtendsEdge extends BaseEdge {

	public ExtendsEdge(Widget source, Widget classNode) {
		super(source, classNode);
	}

	@Nullable
	@Override
	public Stroke getStrokeStyle() {
		return null;
	}
}
