package jadx.plugins.typediagram.ui.scene;

import org.netbeans.api.visual.border.Border;
import org.netbeans.modules.visual.laf.DefaultLookFeel;
import org.netbeans.modules.visual.util.RenderUtil;

import javax.annotation.Nullable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

public class SimpleBorder implements Border {
	private final int thickness;
	private final Color color;

	public SimpleBorder(int thickness) {
		this(thickness, null);
	}

	public SimpleBorder(int thickness, @Nullable Color color) {
		this.thickness = thickness;
		this.color = color == null ? new DefaultLookFeel().getForeground() :  color;
	}

	@Override
	public Insets getInsets() {
		return new Insets(thickness, thickness, thickness, thickness);
	}

	public void paint (Graphics2D gr, Rectangle bounds) {
		gr.setColor (color);
		gr.setStroke(new BasicStroke(thickness));
		RenderUtil.drawRect(gr, bounds);
	}

	public boolean isOpaque () {
		return true;
	}
}
