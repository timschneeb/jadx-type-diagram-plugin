package jadx.plugins.typediagram.model;

import org.netbeans.api.visual.widget.Widget;

import javax.annotation.Nullable;
import java.awt.Stroke;

public abstract class BaseEdge {
	private final Widget source;
	private final Widget target;

	public BaseEdge(Widget source, Widget target) {
		this.source = source;
		this.target = target;
	}

	public Widget getSource() {
		return source;
	}

	public Widget getTarget() {
		return target;
	}

	@Nullable
	public abstract Stroke getStrokeStyle();
}
