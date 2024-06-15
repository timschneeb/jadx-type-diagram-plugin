package jadx.plugins.typediagram.ui;

import jadx.api.JavaClass;
import jadx.api.plugins.JadxPluginContext;
import jadx.plugins.typediagram.TypeDiagramOptions;
import jadx.plugins.typediagram.ui.scene.TypeGraphScene;
import jadx.plugins.typediagram.ui.scene.actions.DocumentationAction;
import jadx.plugins.typediagram.ui.scene.actions.UrlAction;
import jadx.plugins.typediagram.ui.utils.ActionHandler;
import jadx.plugins.typediagram.ui.utils.IconProvider;
import jadx.plugins.typediagram.ui.utils.Icons;
import jadx.plugins.typediagram.ui.utils.UiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.io.File;

public class DiagramDialog extends JFrame {
	private static final Logger LOG = LoggerFactory.getLogger(DiagramDialog.class);

	private final JadxPluginContext context;
	private final TypeDiagramOptions options;
	private final TypeGraphScene.Mode mode;
	private final DiagramPanel panel;

	private static final double BORDER_RATIO = 0.3;
	private static final double WINDOW_RATIO = 1 - BORDER_RATIO * 2;

	public static void open(JadxPluginContext context, TypeDiagramOptions options, JavaClass targetObject, TypeGraphScene.Mode mode) {
		UiUtils.invokeOnEventDispatchThread(() -> {
			DiagramDialog logDialog;
			logDialog = new DiagramDialog(context, options, mode);
			logDialog.setTargetObject(targetObject);
			logDialog.setVisible(true);
			logDialog.toFront();
		});
	}

	private DiagramDialog(JadxPluginContext context, TypeDiagramOptions options, TypeGraphScene.Mode mode) {
		this.context = context;
		this.options = options;
		this.mode = mode;
		this.panel = new DiagramPanel(context, options, mode);

		setupMenu();
		setContentPane(panel);
		setTitle("Empty type diagram");
		IconProvider.setWindowIcons(this);
		pack();

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		DisplayMode displayMode = gd.getDisplayMode();
		AffineTransform trans = gd.getDefaultConfiguration().getDefaultTransform();
		int w = (int) (displayMode.getWidth() / trans.getScaleX());
		int h = (int) (displayMode.getHeight() / trans.getScaleY());
		setBounds((int) (w * BORDER_RATIO), (int) (h * BORDER_RATIO),
				(int) (w * WINDOW_RATIO), (int) (h * WINDOW_RATIO));

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		KeyStroke strokeEsc = KeyStroke.getKeyStroke("ESCAPE");
		InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(strokeEsc, "ESCAPE");
		getRootPane().getActionMap().put("ESCAPE", new ActionHandler(this::close));
	}

	private void setupMenu() {
		JMenuBar menu = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem saveAsImage = new JMenuItem(new SaveImageAction());
		fileMenu.add(saveAsImage);
		menu.add(fileMenu);

		menu.add(panel.getScene().getLayoutMenu());

		JMenu helpMenu = new JMenu("Help");
		JMenuItem help = new JMenuItem(new DocumentationAction(this));
		JMenuItem github = new JMenuItem(new UrlAction("Visit GitHub repository...", "https://github.com/timschneeb/jadx-type-diagram-plugin"));
		helpMenu.add(help);
		helpMenu.add(github);
		menu.add(helpMenu);

		setJMenuBar(menu);
	}

	public void close() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public void setTargetObject(JavaClass targetObject) {
		this.panel.setTargetObject(targetObject);
		String title = "Type diagram for '" + targetObject.getName() + "'";
		if(mode == TypeGraphScene.Mode.UPSTREAM_ONLY) {
			title += " (supertypes only)";
		}
		setTitle(title);
	}

	private class SaveImageAction extends ActionHandler {
		SaveImageAction() {
			setNameAndDesc("Save image as...");
			setIcon(Icons.IMAGE);
		}

		@Override public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save image...");
			fileChooser.setFileFilter(new FileNameExtensionFilter("PNG files", "png"));
			fileChooser.setAcceptAllFileFilterUsed(false);

			int userSelection = fileChooser.showSaveDialog(DiagramDialog.this);
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if(!file.toPath().endsWith(".png")) {
					file = new File(file.getAbsolutePath() + ".png");
				}

				panel.getScene().captureImage(file);
			}
		}
	};
}
