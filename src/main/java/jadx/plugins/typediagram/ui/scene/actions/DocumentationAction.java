package jadx.plugins.typediagram.ui.scene.actions;

import jadx.plugins.typediagram.ui.utils.ActionHandler;
import jadx.plugins.typediagram.ui.utils.Icons;

import javax.swing.JOptionPane;
import java.awt.Window;
import java.awt.event.ActionEvent;

public class DocumentationAction extends ActionHandler {
	private final Window host;

	public DocumentationAction(Window host) {
		this.host = host;
		setNameAndDesc("Documentation");
		setIcon(Icons.HELP);
	}

	@Override public void actionPerformed(ActionEvent e) {
		// open html message box
		JOptionPane.showMessageDialog(host,
				"<html><p>This plugin can be used to generate class diagrams inside JADX. Classes or interfaces are displayed as nodes and relationships between them as edges.</p>" +
						"</p>" +
						"<br/>" +
						"<p><b>Nodes</b>" +
						"<ul>" +
						"<li><i>Node with regular border</i>: Regular class or interface.</li>" +
						"<li><i>Node with thick border</i>: Root node. You are currently inspecting sub- and supertypes of this particular node.</li>" +
						"<li><i>Node with dashed border</i>: External class or interface. This means that the class or interface is not present in the current project.</li>" +
						"</ul>" +
						"</p>" +
						"<p><b>Edges</b>" +
						"<ul>" +
						"<li><i>Edge with regular line</i>: 'Extends' relationship. A class node is subclassing another class node.</li>" +
						"<li><i>Edge with dashed line</i>: 'Implements' relationship. A node is implementing an interface node.</li>" +
						"</ul>" +
						"</p>" +
						"<p><b>Keyboard &amp; mouse controls</b>" +
						"<ul>" +
						"<li><i>Single-click</i>: Single-click any node, method or field to jump to its declaration in the JADX code viewer.</li>" +
						"<li><i>Mouse wheel</i>: Scroll the diagram.</li>" +
						"<li><i>Ctrl+Mouse wheel</i>: Zoom in and out.</li>" +
						"</ul>" +
						"</p>" +
						"</html>",
				"Documentation",
				JOptionPane.INFORMATION_MESSAGE);

	}
}
