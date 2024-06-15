package jadx.plugins.typediagram.model;

import jadx.api.JavaClass;
import jadx.api.JavaField;
import jadx.api.JavaMethod;
import jadx.plugins.typediagram.ui.scene.SimpleBorder;
import org.netbeans.api.visual.border.Border;

import javax.annotation.Nullable;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseNode {
	protected static final Border DEFAULT_BORDER = new SimpleBorder(1);
	protected static final Border DEFAULT_ROOT_BORDER = new SimpleBorder(2);

	private final String name;
	@Nullable
	private Image icon;
	private boolean isRoot;
	@Nullable
	private final JavaClass javaClass;

	public BaseNode(String name, @Nullable JavaClass javaClass) {
		this.name = name;
		this.icon = null;
		this.javaClass = javaClass;
	}

	public String getName() {
		return name;
	}

	@Nullable
	public Image getIcon() {
		return icon;
	}

	public void setIcon(@Nullable Image icon) {
		this.icon = icon;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean root) {
		isRoot = root;
	}

	@Nullable
	public JavaClass getJavaClass() {
		return javaClass;
	}

	public List<JavaField> getFields() {
		return javaClass != null ? javaClass.getFields() : new ArrayList<>(0);
	}

	public List<JavaMethod> getMethods() {
		return javaClass != null ? javaClass.getMethods() : new ArrayList<>(0);
	}

	@Nullable
	public Border getBorderStyle() {
		return isRoot() ? DEFAULT_ROOT_BORDER : DEFAULT_BORDER;
	}
}
