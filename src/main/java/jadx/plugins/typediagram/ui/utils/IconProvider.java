package jadx.plugins.typediagram.ui.utils;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import jadx.api.JavaClass;
import jadx.core.dex.info.AccessInfo;
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

import static jadx.plugins.typediagram.ui.utils.Icons.*;

public class IconProvider {

	@Nullable
	public static Image resolveImageForClass(JavaClass cls) {
		AccessInfo accessInfo = cls.getAccessInfo();

		Icon icon;
		if (accessInfo.isEnum()) {
			icon = ENUM;
		} else if (accessInfo.isAnnotation()) {
			icon = ANNOTATION;
		} else if (accessInfo.isInterface()) {
			icon = INTERFACE;
		} else if (accessInfo.isAbstract()) {
			icon = CLASS_ABSTRACT;
		} else if (accessInfo.isProtected()) {
			icon = CLASS_PROTECTED;
		} else if (accessInfo.isPrivate()) {
			icon = CLASS_PRIVATE;
		} else {
			icon = accessInfo.isPublic() ? CLASS_PUBLIC : CLASS;
		}
		return toImage(icon);
	}

	public static void setWindowIcons(Window window) {
		try {
			jadx.gui.utils.UiUtils.setWindowIcons(window);
		}
		catch (Exception ex) {
			System.err.println("ERROR: GraphSceneImpl: " + ex.getMessage());
		}
	}

	public static FlatSVGIcon openBuiltinSvgIcon(String name) {
		String iconPath = "icons/" + name + ".svg";
		FlatSVGIcon icon = new FlatSVGIcon(iconPath, IconProvider.class.getClassLoader());
		boolean found;
		try {
			found = icon.hasFound();
		} catch (Exception e) {
			throw new JadxRuntimeException("Failed to load icon: " + iconPath, e);
		}
		if (!found) {
			throw new JadxRuntimeException("Icon not found: " + iconPath);
		}
		return icon;
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
