package jadx.plugins.typediagram.ui.scene.actions;

import jadx.plugins.typediagram.ui.scene.TypeGraphScene;
import jadx.plugins.typediagram.ui.utils.ActionHandler;
import jadx.plugins.typediagram.ui.utils.Icons;

import java.awt.event.ActionEvent;

public class ZoomAction extends ActionHandler {
	private final ZoomType type;
	private final TypeGraphScene scene;

	public enum ZoomType {
		IN, OUT, RESET, FIT
	}

	public ZoomAction(TypeGraphScene scene, ZoomType type) {
		this.type = type;
		this.scene = scene;

		switch (type) {
			case IN:
				setNameAndDesc("Zoom in");
				setIcon(Icons.ZOOM_IN);
				break;
			case OUT:
				setNameAndDesc("Zoom out");
				setIcon(Icons.ZOOM_OUT);
				break;
			case RESET:
				setNameAndDesc("Reset zoom");
				setIcon(Icons.RESET_ZOOM);
				break;
			case FIT:
				setNameAndDesc("Zoom to fit");
				setIcon(Icons.ZOOM_FIT);
				break;
		}
	}

	@Override public void actionPerformed(ActionEvent e) {
		switch (type) {
			case IN:
				scene.setZoomFactor(scene.getZoomFactor() * 1.2);
				break;
			case OUT:
				scene.setZoomFactor(scene.getZoomFactor() / 1.2);
				break;
			case RESET:
				scene.resetZoomFactor();
				break;
			case FIT:
				scene.fitToView();
				break;
		}
		scene.doLayout();
	}
}
