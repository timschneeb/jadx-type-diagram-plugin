package jadx.plugins.typediagram.ui;

import jadx.api.JavaClass;
import jadx.api.plugins.JadxPluginContext;
import jadx.plugins.typediagram.TypeDiagramOptions;
import jadx.plugins.typediagram.ui.scene.TypeGraphScene;
import jadx.plugins.typediagram.ui.scene.actions.AccessFilterMenuAction;
import jadx.plugins.typediagram.ui.utils.Icons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;

public class DiagramPanel extends JPanel {
	private static final Logger LOG = LoggerFactory.getLogger(DiagramPanel.class);

	private final TypeGraphScene scene;
	private final JScrollPane scrollPane;

	public DiagramPanel(JadxPluginContext context, TypeDiagramOptions options, TypeGraphScene.Mode mode) {
		scrollPane = new JScrollPane();
		scene = new TypeGraphScene(context, options, scrollPane, mode);
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);

		setupToolbar();

		// Add the scene to the JScrollPane
		scrollPane.setViewportView(scene.createView());
		// Add the SatelliteView to the scene
		add(scene.createSatelliteView(), BorderLayout.WEST);
	}

	private void setupToolbar() {
		JPanel toolbarPanel = new JPanel();
		toolbarPanel.setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar();

		JToggleButton fieldsBtn = new JToggleButton("Fields");
		fieldsBtn.setIcon(Icons.FIELD);
		fieldsBtn.addActionListener(e -> {
			JToggleButton src = (JToggleButton)e.getSource();
			scene.setShowFields(src.isSelected());
		});

		JToggleButton methodsBtn = new JToggleButton("Methods");
		methodsBtn.setIcon(Icons.METHOD);
		methodsBtn.addActionListener(e -> {
			JToggleButton src = (JToggleButton)e.getSource();
			scene.setShowMethods(src.isSelected());
		});

		toolBar.add(fieldsBtn);
		toolBar.add(methodsBtn);
		toolBar.addSeparator();
		toolBar.add(new JToggleButton(new AccessFilterMenuAction(scene)));
		toolBar.add(new JSeparator(SwingConstants.VERTICAL));

		scene.getZoomActions().forEach(toolBar::add);

		toolbarPanel.add(toolBar, BorderLayout.NORTH);
		toolbarPanel.add(new JSeparator(), BorderLayout.SOUTH);
		add(toolbarPanel, BorderLayout.NORTH);
	}

	public void setTargetObject(JavaClass targetObject) {
		LOG.debug("Set target object: {}", targetObject);
		scene.setTargetObject(targetObject);
	}

	public TypeGraphScene getScene() {
		return scene;
	}
}

