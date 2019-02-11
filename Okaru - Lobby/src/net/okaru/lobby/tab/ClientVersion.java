package net.okaru.lobby.tab;

import net.okaru.lobby.tab.reflection.ReflectionConstants;

import org.bukkit.entity.Player;

public enum ClientVersion
{
  v1_7,  v1_8;
  
  public static ClientVersion getVersion(Player player)
  {
    Object handle = ReflectionConstants.GET_HANDLE_METHOD.invoke(player, new Object[0]);
    Object connection = ReflectionConstants.PLAYER_CONNECTION.get(handle);
    Object manager = ReflectionConstants.NETWORK_MANAGER.get(connection);
    Object version = ReflectionConstants.VERSION_METHOD.invoke(manager, new Object[0]);
    if ((version instanceof Integer)) {
      return ((Integer)version).intValue() > 5 ? v1_8 : v1_7;
    }
    return v1_7;
  }
}
