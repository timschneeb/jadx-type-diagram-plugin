package jadx.plugins.typediagram.ui.utils;

import jadx.api.JavaClass;
import jadx.core.dex.instructions.args.ArgType;
import jadx.core.dex.nodes.RootNode;

import java.util.Objects;
import java.util.function.BiConsumer;

public class Visitors {
	public static void visitSubTypes(RootNode root, JavaClass current, BiConsumer<ArgType, ArgType> consumer) {
		ArgType currentType = current.getClassNode().getType();
		current.getUseIn().forEach((usage) -> {
			if(usage instanceof JavaClass) {
				JavaClass usageCls = (JavaClass) usage;

				if(current.getAccessInfo().isInterface() &&
						usageCls.getClassNode().getInterfaces().contains(currentType)) {

					// Found interface implementation
					consumer.accept(currentType, usageCls.getClassNode().getType());
					visitSubTypes(root, usageCls, consumer);
				}
				else if(!current.getAccessInfo().isInterface() &&
						Objects.equals(usageCls.getClassNode().getSuperClass(), currentType)) {

					// Found class extension
					consumer.accept(currentType, usageCls.getClassNode().getType());
					visitSubTypes(root, usageCls, consumer);
				}
			}
		});
	}
}
