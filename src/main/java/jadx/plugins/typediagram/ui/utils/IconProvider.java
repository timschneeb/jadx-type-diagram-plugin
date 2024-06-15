package jadx.plugins.typediagram.ui.utils;

import jadx.api.JavaClass;
import jadx.core.utils.exceptions.JadxRuntimeException;

import javax.annotation.Nullable;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class IconProvider {

	@Nullable
	public static Image resolveImageForClass(JavaClass cls) {
		return resolveImageFor(cls, "jadx.gui.treemodel.JClass");
	}

	@Nullable
	public static Image resolveImageForField(JavaClass cls) {
		return resolveImageFor(cls, "jadx.gui.treemodel.JField");
	}

	@Nullable
	public static Image resolveImageForMethod(JavaClass cls) {
		return resolveImageFor(cls, "jadx.gui.treemodel.JMethod");
	}

	@Nullable
	public static Image resolveImageFor(JavaClass cls, String className) {
		try {
			Class<?> cl = Class.forName(className);
			Constructor<?> cons = cl.getConstructor(JavaClass.class, cl);
			Object jclass = cons.newInstance(cls, null);
			Icon icon = (Icon) cl.getMethod("getIcon").invoke(jclass);
			return icon.getIconHeight() <= 0 || icon.getIconWidth() <= 0 ? null : toImage(icon);
		}
		catch (Exception ex) {
			System.err.println("ERROR: GraphSceneImpl: " + ex.getMessage());
			return null;
		}
	}

	public static void setWindowIcons(Window window) {
		try {
			Class<?> cl = Class.forName("jadx.gui.utils.UiUtils");
			Method mth = cl.getMethod("setWindowIcons", Window.class);
			mth.invoke(null, window);
		}
		catch (Exception ex) {
			System.err.println("ERROR: GraphSceneImpl: " + ex.getMessage());
		}
	}

	public static ImageIcon openSvgIcon(String name) {
		try {
			Class<?> cl = Class.forName("jadx.gui.utils.UiUtils");
			Method mth = cl.getMethod("openSvgIcon", String.class);
			return (ImageIcon) mth.invoke(null, name);
		}
		catch (Exception ex) {
			System.err.println("ERROR: GraphSceneImpl: " + ex.getMessage());
			throw new JadxRuntimeException("Failed to load icon: " + name, ex);
		}
	}

	private static Image toImage(Icon icon) {
		if (icon instanceof ImageIcon) {
			return ((ImageIcon)icon).getImage();
		}
		else {
			int w = icon.getIconWidth();
			int h = icon.getIconHeight();
			GraphicsEnvironment ge =
					GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gd.getDefaultConfiguration();
			BufferedImage image = gc.createCompatibleImage(w, h);
			Graphics2D g = image.createGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			return image;
		}
	}
}
