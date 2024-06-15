package jadx.plugins.typediagram;

import jadx.api.plugins.JadxPlugin;
import jadx.api.plugins.JadxPluginContext;
import jadx.api.plugins.JadxPluginInfo;

public class JadxTypeDiagramPlugin implements JadxPlugin {
	public static final String PLUGIN_ID = "type-diagram-plugin";

	private final TypeDiagramOptions options = new TypeDiagramOptions();

	@Override
	public JadxPluginInfo getPluginInfo() {
		return new JadxPluginInfo(PLUGIN_ID, "Type diagram plugin", "Adds an option to view type diagrams for objects");
	}

	@Override
	public void init(JadxPluginContext context) {
		context.registerOptions(options);
		if (options.isEnable()) {
			TypeDiagramAction.addToPopupMenu(context, options);
		}
	}
}
