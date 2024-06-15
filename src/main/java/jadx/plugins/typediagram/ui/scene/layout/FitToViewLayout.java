package jadx.plugins.typediagram.ui.scene.layout;

import jadx.plugins.typediagram.ui.scene.TypeGraphScene;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.widget.Widget;

import javax.annotation.Nullable;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;

public class FitToViewLayout extends SceneLayout {

	private final TypeGraphScene depScene;
	private List<? extends Widget> widgets;
	private final JScrollPane parentScrollPane;

	public FitToViewLayout(TypeGraphScene scene, JScrollPane parentScrollPane) {
		super(scene);
		this.depScene = scene;
		this.parentScrollPane = parentScrollPane;
	}

	public void fitToView(@Nullable List<? extends Widget> widgets) {
		this.widgets = widgets;
		this.invokeLayout();
	}

	@Override
	protected void performLayout() {
		Rectangle rectangle = null;
		List<? extends Widget> toFit = widgets != null ? widgets : depScene.getChildren();
		if (toFit == null || toFit.isEmpty()) {
			return;
		}

		for (Widget widget : toFit) {
			Rectangle bounds = widget.getBounds();
			if (bounds == null) {
				continue;
			}
			if (rectangle == null) {
				rectangle = widget.convertLocalToScene(bounds);
			} else {
				rectangle = rectangle.union(widget.convertLocalToScene(bounds));
			}
		}
		// margin around
		assert rectangle != null;
		if (widgets == null) {
			rectangle.grow(5, 5);
		} else {
			rectangle.grow(25, 25);
		}
		Dimension dim = rectangle.getSize();
		Dimension viewDim = parentScrollPane.getViewportBorderBounds().getSize ();
		double zf = Math.min ((double) viewDim.width / dim.width, (double) viewDim.height / dim.height);
		depScene.setZoomFactor (zf);
	}
}
