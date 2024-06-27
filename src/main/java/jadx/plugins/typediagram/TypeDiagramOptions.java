package jadx.plugins.typediagram;

import jadx.api.plugins.options.impl.BasePluginOptionsBuilder;

public class TypeDiagramOptions extends BasePluginOptionsBuilder {

	private boolean enable;
	private float initialZoom;

	@Override
	public void registerOptions() {
		boolOption(JadxTypeDiagramPlugin.PLUGIN_ID + ".enable")
				.description("Register context menu options")
				.defaultValue(true)
				.setter(v -> enable = v);

		option(JadxTypeDiagramPlugin.PLUGIN_ID + ".initial-zoom", float.class)
				.description("Initial zoom level")
				.defaultValue(1.0f)
				.formatter(String::valueOf)
				.parser(s -> {
					try {
						return Float.parseFloat(s);
					} catch (NumberFormatException e) {
						return 1.0f;
					}
				})
				.setter(v -> initialZoom = v);
	}

	public boolean isEnable() {
		return enable;
	}
	public float initialZoom() {
		return initialZoom;
	}
}
