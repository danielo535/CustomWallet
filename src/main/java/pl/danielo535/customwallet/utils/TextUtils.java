package pl.danielo535.customwallet.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("(&#[0-9a-fA-F]{6})");

    public static String format(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1).substring(1);
            matcher.appendReplacement(sb, "" + ChatColor.of(hex));
        }
        matcher.appendTail(sb);

        String hexColored = sb.toString();

        return ChatColor.translateAlternateColorCodes('&', hexColored);
    }
}
