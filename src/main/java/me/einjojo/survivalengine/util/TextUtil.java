package me.einjojo.survivalengine.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

public class TextUtil {

    public static String getTimeString(long time) {
        long second = (time / 1000) % 60;
        long minute = (time / (1000 * 60)) % 60;
        long hour = (time / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);

    }

    public static TextComponent createUsageComponent(Player player, String command, String description, String suggest, String hover) {
        TextComponent lineComponent = new TextComponent("§8 - ");
        TextComponent commandComponent = new TextComponent("§b" + command);
        TextComponent textComponent = new TextComponent(description + "\n");

        commandComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggest));
        commandComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));

        commandComponent.addExtra(textComponent);
        lineComponent.addExtra(commandComponent);
        return lineComponent;
    }

    public static TextComponent combineTextComponents(TextComponent... textComponent) {
        TextComponent TEXT = new TextComponent();
        if(textComponent.length == 0) {
            return TEXT;
        }

        for (TextComponent component : textComponent) {
            if(component.getHoverEvent() == null) {
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("")));
            }
            TEXT.addExtra(component);
        }

        return TEXT;
    }

}
