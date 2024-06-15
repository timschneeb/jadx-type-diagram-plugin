package jadx.plugins.typediagram.model;

import org.netbeans.api.visual.border.Border;
import org.netbeans.modules.visual.border.DashedBorder;

import javax.annotation.Nullable;

public class ExternalClassNode extends BaseNode {
	public ExternalClassNode(String name) {
		super(name, null);
	}

	@Nullable
	@Override
	public Border getBorderStyle() {
		return new DashedBorder(null, 4, 3);
	}
}
