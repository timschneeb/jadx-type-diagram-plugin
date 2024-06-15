package jadx.plugins.typediagram.ui.scene;

import jadx.api.JavaClass;
import jadx.api.JavaField;
import jadx.api.JavaMethod;
import jadx.api.metadata.ICodeNodeRef;
import jadx.api.plugins.JadxPluginContext;
import jadx.api.plugins.gui.JadxGuiContext;
import jadx.core.dex.info.AccessInfo;
import jadx.core.dex.instructions.args.ArgType;
import jadx.plugins.typediagram.model.AccessFlagFilters;
import jadx.plugins.typediagram.model.BaseNode;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modules.visual.experimental.widget.general.ListItemWidget;
import org.netbeans.modules.visual.experimental.widget.general.ListWidget;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.util.stream.Collectors;

public class NodeWidget extends ListWidget {
	private static final Font font = new Font(null, Font.PLAIN, 12);

	/**
	 * Creates a list item widget.
	 *
	 * @param scene the scene
	 */
	public NodeWidget(JadxPluginContext context, TypeGraphScene scene, BaseNode node, boolean showFields, boolean showMethods) {
		super(scene);

		getLabelWidget().setFont(font);

		Border border = node.getBorderStyle();
		if(border != null) {
			setBorder(border);
		}

		Image icon = node.getIcon();
		if(icon != null) {
			setImage(icon);
		}

		// Remove ListWidget's built-in separator if there are no fields or methods
		if(!showFields && !showMethods ||
				node.getFields().isEmpty() && node.getMethods().isEmpty())
			removeChildren(getChildren().stream()
					.filter(w -> w instanceof SeparatorWidget)
					.collect(Collectors.toList()));

		if(showFields) {
			for (JavaField field : node.getFields()) {
				// Apply access filter
				if(!AccessFlagFilters.accept(field.getAccessFlags(), scene.getAccessFilter()))
					continue;

				ListItemWidget item = new ListItemWidget(scene);
				item.setFont(font);
				item.setLabel("  " + getAccessChar(field.getAccessFlags()) + " " + field.getName() + ": " + field.getType() + "  ");

				setupLink(context, item, field.getCodeNodeRef());
				addChild(item);
			}
		}

		if(showFields && showMethods && !node.getFields().isEmpty() && !node.getMethods().isEmpty())
			addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

		if(showMethods) {
			for (JavaMethod method : node.getMethods()) {
				// Apply access filter
				if(!AccessFlagFilters.accept(method.getAccessFlags(), scene.getAccessFilter()))
					continue;

				String args = method.getArguments().stream().map(ArgType::toString).collect(Collectors.joining(", "));

				ListItemWidget item = new ListItemWidget(scene);
				item.setFont(font);
				item.setLabel("  " + getAccessChar(method.getAccessFlags()) + " " + method.getName() + "(" + args + "): " + method.getReturnType() + "  ");

				setupLink(context, item, method.getCodeNodeRef());
				addChild(item);
			}
		}

		// Add some static padding
		setLabel("  " + node.getName() + "  ");

		JavaClass cls = node.getJavaClass();
		if(cls != null) {
			setupLink(context, getHeader(), cls.getCodeNodeRef());
		}
	}

	private static void setupLink(JadxPluginContext context, Widget widget, ICodeNodeRef cls) {
		widget.setCursor(new Cursor(Cursor.HAND_CURSOR));
		widget.getActions().addAction(new WidgetAction.Adapter() {
			@Override
			public State mouseClicked(Widget widget, WidgetMouseEvent event) {

				JadxGuiContext guiContext = context.getGuiContext();
				assert guiContext != null;
				guiContext.open(cls);

				return State.CONSUMED;
			}
		});
	}

	private static char getAccessChar(AccessInfo info) {
		char accessChar = ' ';
		if(info.isPublic()) {
			accessChar = '+';
		} else if(info.isPrivate()) {
			accessChar = '-';
		} else if(info.isProtected()) {
			accessChar = '#';
		} else if(info.isPackagePrivate()) {
			accessChar = '~';
		}
		return accessChar;
	}
}
