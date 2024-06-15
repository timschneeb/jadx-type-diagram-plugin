package jadx.plugins.typediagram.model;

import jadx.api.JavaClass;

public class ClassNode extends BaseNode {
	public ClassNode(JavaClass javaClass) {
		super(javaClass.getFullName(), javaClass);
	}
}
