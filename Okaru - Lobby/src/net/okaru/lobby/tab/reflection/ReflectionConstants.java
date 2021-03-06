package net.okaru.lobby.tab.reflection;

import java.util.Map;
import java.util.UUID;

import net.minecraft.server.v1_7_R4.EnumProtocol;
import net.okaru.lobby.tab.Reflection;

public class ReflectionConstants
{
  public static final Class<?> TAB_PACKET_CLASS = Reflection.getMinecraftClass("PacketPlayOutPlayerInfo");
  public static final Reflection.ConstructorInvoker TAB_PACKET_CONSTRUCTOR = Reflection.getConstructor(TAB_PACKET_CLASS, new Class[0]);
  public static final Reflection.FieldAccessor<Integer> TAB_PACKET_ACTION = Reflection.getField(TAB_PACKET_CLASS, Integer.TYPE, 5);
  public static final Reflection.FieldAccessor<String> TAB_PACKET_NAME = Reflection.getField(TAB_PACKET_CLASS, String.class, 0);
  public static final Class<Object> GAME_PROFILE_CLASS = getUntypedClasses(new String[] { "net.minecraft.util.com.mojang.authlib.GameProfile", "com.mojang.authlib.GameProfile" });
  public static final Reflection.ConstructorInvoker GAME_PROFILE_CONSTRUCTOR = Reflection.getConstructor(GAME_PROFILE_CLASS, new Class[] { UUID.class, String.class });
  public static final Reflection.FieldAccessor<String> GAME_PROFILE_NAME = Reflection.getField(GAME_PROFILE_CLASS, String.class, 0);
  public static final Reflection.FieldAccessor<Object> TAB_PACKET_PROFILE = Reflection.getField(TAB_PACKET_CLASS, GAME_PROFILE_CLASS, 0);
  public static final Class<?> CRAFT_PLAYER_CLASS = Reflection.getCraftBukkitClass("entity.CraftPlayer");
  public static final Class<?> NMS_PACKET_CLASS = Reflection.getMinecraftClass("Packet");
  public static final Class<?> NMS_PLAYER_CLASS = Reflection.getMinecraftClass("EntityPlayer");
  public static final Class<?> PLAYER_CONNECTION_CLASS = Reflection.getMinecraftClass("PlayerConnection");
  public static final Class<?> NETWORK_MANAGER_CLASS = Reflection.getMinecraftClass("NetworkManager");
  public static final Reflection.MethodInvoker GET_HANDLE_METHOD = Reflection.getMethod(CRAFT_PLAYER_CLASS, "getHandle", new Class[0]);
  public static final Reflection.MethodInvoker GET_PROFILE_METHOD = Reflection.getMethod(CRAFT_PLAYER_CLASS, "getProfile", new Class[0]);
  public static final Reflection.MethodInvoker VERSION_METHOD = Reflection.getMethod(NETWORK_MANAGER_CLASS, "getVersion", new Class[0]);
  public static final Reflection.MethodInvoker SEND_PACKET = Reflection.getMethod(PLAYER_CONNECTION_CLASS, "sendPacket", new Class[] { NMS_PACKET_CLASS });
  public static final Reflection.FieldAccessor<?> PLAYER_CONNECTION = Reflection.getField(NMS_PLAYER_CLASS, PLAYER_CONNECTION_CLASS, 0);
  public static final Reflection.FieldAccessor<?> NETWORK_MANAGER = Reflection.getField(PLAYER_CONNECTION_CLASS, NETWORK_MANAGER_CLASS, 0);
  public static final Class<?> ENUM_PROTOCOL_CLASS = Reflection.getMinecraftClass("EnumProtocol");
  public static final Reflection.FieldAccessor<?> ENUM_PROTOCOL_PLAY = Reflection.getField(ENUM_PROTOCOL_CLASS, ENUM_PROTOCOL_CLASS, 1);
  public static final Reflection.FieldAccessor<Map> ENUM_PROTOCOL_REGISTRY = Reflection.getField(ENUM_PROTOCOL_CLASS, Map.class, 0);
  
  public static Class<Object> getUntypedClasses(String... lookupNames)
  {
    EnumProtocol.class.getName();
    String[] arrayOfString = lookupNames;int j = lookupNames.length;
    for (int i = 0; i < j; i++)
    {
      String lookupName = arrayOfString[i];
      try
      {
        return Reflection.getUntypedClass(lookupName);
      }
      catch (IllegalArgumentException localIllegalArgumentException) {}
    }
    throw new IllegalArgumentException("No class found in selection given");
  }
}
