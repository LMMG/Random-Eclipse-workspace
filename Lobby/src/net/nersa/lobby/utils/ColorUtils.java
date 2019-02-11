package net.nersa.lobby.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ColorUtils
{
  public String translateFromString(String text)
  {
    return StringEscapeUtils.unescapeJava(ChatColor.translateAlternateColorCodes('&', text));
  }

  public List<String> translateFromArray(List<String> text)
  {
    List<String> messages = new ArrayList<>();
    
    for (String string : text)
    {
      messages.add(translateFromString(string));
    }
    return messages;
  }
}