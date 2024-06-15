package jadx.plugins.typediagram.ui.scene.actions;

import jadx.plugins.typediagram.ui.utils.ActionHandler;
import jadx.plugins.typediagram.ui.utils.Icons;
import jadx.plugins.typediagram.ui.utils.UiUtils;

import java.awt.event.ActionEvent;

public class UrlAction extends ActionHandler {
	private final String url;

	public UrlAction(String name, String url) {
		this.url = url;
		setNameAndDesc(name);
		setIcon(Icons.OPEN_IN_NEW_WINDOW);
	}

	@Override public void actionPerformed(ActionEvent e) {
		UiUtils.openUrl(url);
	}
}
