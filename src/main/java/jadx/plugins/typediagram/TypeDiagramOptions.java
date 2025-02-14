package jadx.plugins.typediagram;

import jadx.api.plugins.options.impl.BasePluginOptionsBuilder;

public class TypeDiagramOptions extends BasePluginOptionsBuilder {

	private float initialZoom;

	@Override
	public void registerOptions() {
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

	public float initialZoom() {
		return initialZoom;
	}
}
