package jadx.plugins.typediagram.model;

import jadx.api.JavaClass;

public class InterfaceNode extends BaseNode {
	public InterfaceNode(JavaClass javaClass) {
		super(javaClass.getFullName(), javaClass);
	}
}
