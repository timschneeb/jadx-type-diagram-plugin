package jadx.plugins.typediagram.ui.utils;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.Desktop;
import java.util.Locale;
import java.util.Map;

public class UiUtils {
	public static final String OS_NAME = System.getProperty("os.name");
	private static final String LOWER_OS_NAME = OS_NAME.toLowerCase(Locale.ENGLISH);
	public static final boolean IS_WINDOWS = LOWER_OS_NAME.startsWith("windows");
	public static final boolean IS_MAC = LOWER_OS_NAME.startsWith("mac");

	public static void invokeOnEventDispatchThread(Runnable run) {
		if (SwingUtilities.isEventDispatchThread()) {
			run.run();
		} else {
			SwingUtilities.invokeLater(run);
		}
	}

	public static void openUrl(String url) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					desktop.browse(new java.net.URI(url));
					return;
				} catch (Exception e) {
					System.err.println("Failed to open url: " + e.getMessage());
				}
			}
		}
		try {
			if (IS_WINDOWS) {
				new ProcessBuilder()
						.command(new String[] { "rundll32", "url.dll,FileProtocolHandler", url })
						.start();
				return;
			}
			if (IS_MAC) {
				new ProcessBuilder()
						.command(new String[] { "open", url })
						.start();
				return;
			}
			Map<String, String> env = System.getenv();
			if (env.get("BROWSER") != null) {
				new ProcessBuilder()
						.command(new String[] { env.get("BROWSER"), url })
						.start();
				return;
			}
		} catch (Exception e) {
			System.err.println("Failed to open url: " + e.getMessage());
		}
		showUrlDialog(url);
	}

	private static void showUrlDialog(String url) {
		JTextArea urlArea = new JTextArea("Can't open browser. Please browse to:\n" + url);
		JOptionPane.showMessageDialog(null, urlArea);
	}
}
