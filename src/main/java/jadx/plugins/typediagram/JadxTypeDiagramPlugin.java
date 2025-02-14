package jadx.plugins.typediagram;

import jadx.api.plugins.JadxPlugin;
import jadx.api.plugins.JadxPluginContext;
import jadx.api.plugins.JadxPluginInfo;
import jadx.api.plugins.JadxPluginInfoBuilder;

public class JadxTypeDiagramPlugin implements JadxPlugin {
	public static final String PLUGIN_ID = "type-diagram-plugin";

	private final TypeDiagramOptions options = new TypeDiagramOptions();

	@Override
	public JadxPluginInfo getPluginInfo() {
		return JadxPluginInfoBuilder.pluginId(PLUGIN_ID)
				.name("Type diagram plugin")
				.description("Adds an option to view type diagrams for objects")
				.homepage("https://github.com/timschneeb/jadx-type-diagram-plugin")
				.requiredJadxVersion("1.5.2, r2372")
				.build();
	}

	@Override
	public void init(JadxPluginContext context) {
		context.registerOptions(options);
		TypeDiagramAction.addToPopupMenu(context, options);
	}
}
