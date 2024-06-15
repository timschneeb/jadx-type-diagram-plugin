package jadx.plugins.typediagram.ui.scene.actions;

import jadx.plugins.typediagram.model.AccessFlagFilters;
import jadx.plugins.typediagram.ui.scene.TypeGraphScene;
import jadx.plugins.typediagram.ui.utils.ActionHandler;
import jadx.plugins.typediagram.ui.utils.Icons;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;

public class AccessFilterMenuAction extends ActionHandler {
	private final TypeGraphScene scene;

	public AccessFilterMenuAction(TypeGraphScene scene) {
		this.scene = scene;
		setName("Access filter");
		setShortDescription("Filter members by access level");
		setIcon(Icons.SHOW);
	}

	@Override public void actionPerformed(ActionEvent e) {
		createAndShowMenu((JToggleButton)e.getSource());
	}

	private void createAndShowMenu(final AbstractButton component) {
		JPopupMenu menu = new JPopupMenu();
		menu.add(new AccessFilterMenuItem("All", AccessFlagFilters.All));
		menu.add(new AccessFilterMenuItem("Public only", AccessFlagFilters.PublicOnly));
		menu.add(new AccessFilterMenuItem("Public + package", AccessFlagFilters.PublicPackage));
		menu.add(new AccessFilterMenuItem("Public + package + protected", AccessFlagFilters.PublicPackageProtected));

		menu.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				component.setSelected(false);
			}

			public void popupMenuCanceled(PopupMenuEvent e) {
				component.setSelected(false);
			}
		});

		menu.show(component, 0, component.getHeight());
	}

	private class AccessFilterMenuItem extends JRadioButtonMenuItem {

		public AccessFilterMenuItem(String text, AccessFlagFilters accessFilter) {
			super(text, scene.getAccessFilter() == accessFilter);
			this.setAction(new ActionHandler(() -> {
				if(this.isSelected()) {
					scene.setAccessFilter(accessFilter);
				}
			}).withNameAndDesc(text));
		}
	}
}
