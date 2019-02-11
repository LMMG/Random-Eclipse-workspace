package com.hcempire.horus.listerners.staffmode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.hcempire.horus.Horus;
import com.hcempire.horus.util.Color;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class StaffModeListener implements Listener
{
  private final Horus utilities = Horus.getInstance();
  
  private final Set<UUID> staffMode = new HashSet<>();
  private final Set<UUID> staffChat = new HashSet<>();   
  private final Set<UUID> vanished = new HashSet<>();
  
  private final HashMap<UUID, UUID> inspectedPlayer = new HashMap<>();
  private final HashMap<UUID, ItemStack[]> contents = new HashMap<>();
  private final HashMap<UUID, ItemStack[]> armorContents = new HashMap<>();
  public Inventory inv;
  
  public Player getInspectedPlayer(Player player)
  {
    return Bukkit.getServer().getPlayer(inspectedPlayer.get(player.getUniqueId()));
  }
  
  public boolean isVanished(Player player)
  {
    return vanished.contains(player.getUniqueId());
  }
  
  public boolean isStaffChatActive(Player player)
  {
    return staffChat.contains(player.getUniqueId());
  }
  
  public boolean isStaffModeActive(Player player)
  {
    return staffMode.contains(player.getUniqueId());
  }
  
  public boolean hasPreviousInventory(Player player)
  {
    return contents.containsKey(player.getUniqueId()) && armorContents.containsKey(player.getUniqueId());
  }
  
  public void saveInventory(Player player)
  {
    contents.put(player.getUniqueId(), player.getInventory().getContents());
    armorContents.put(player.getUniqueId(), player.getInventory().getArmorContents());
  }
  
  public void loadInventory(Player player)
  {
    PlayerInventory playerInventory = player.getInventory();
    playerInventory.setContents((ItemStack[]) contents.get(player.getUniqueId()));
    playerInventory.setArmorContents((ItemStack[]) armorContents.get(player.getUniqueId()));
    contents.remove(player.getUniqueId());
    armorContents.remove(player.getUniqueId());
  }
  
  public void setStaffChat(Player player, boolean status)
  {
    if (status)
    {
      if (player.hasPermission("utilities.player.staff"))
      {
        staffChat.add(player.getUniqueId());
      }
    }
    else
    {
      staffChat.remove(player.getUniqueId());
    }
  }
  
  public void setStaffMode(Player player, boolean status)
  {
    if (status)
    {
      if (player.hasPermission("utilities.player.staff"))
      {
        staffMode.add(player.getUniqueId());
        saveInventory(player);
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setArmorContents(new ItemStack[] { new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
        playerInventory.clear();
        setItems(player);
        setVanished(player, true);
        player.updateInventory();
        if (player.getGameMode() == GameMode.SURVIVAL)
        {
          player.setGameMode(GameMode.CREATIVE);
        }
      }
      else
      {
        player.sendMessage(Color.translate("&cYou do not have permissions to enable the staffmode."));
      }
    }
    else
    {
      staffMode.remove(player.getUniqueId());
      PlayerInventory playerInventory = player.getInventory();
      playerInventory.setArmorContents(new ItemStack[] { new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
      playerInventory.clear();
      setVanished(player, false);
      if (hasPreviousInventory(player))
      {
        loadInventory(player);
      }
      player.updateInventory();
      if (!player.hasPermission("essentials.creative") && player.getGameMode() == GameMode.CREATIVE)
      {
        player.setGameMode(GameMode.SURVIVAL);
      }
    }
  }
  
  public void setVanished(Player player, boolean status)
  {
    if (status)
    {
      vanished.add(player.getUniqueId());
      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        if (!online.hasPermission("utilities.player.staff"))
        {
          online.hidePlayer(player);
        }
      }
      
      if (isStaffModeActive(player))
      {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setItem(4, getVanishItemFor(player));
      }
    }
    else
    {
      vanished.remove(player.getUniqueId());
      for (Player online : Bukkit.getServer().getOnlinePlayers())
      {
        online.showPlayer(player);
      }
      
      if (isStaffModeActive(player))
      {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setItem(4, getVanishItemFor(player));
      }
    }
  }
  
  public void setItems(Player player)
  {
    PlayerInventory playerInventory = player.getInventory();
    
    ItemStack compass = new ItemStack(Material.COMPASS, 1);
    ItemMeta compassMeta = compass.getItemMeta();
    compassMeta.setDisplayName(Color.translate("&6Compass"));
    compassMeta.setLore(Color.translateFromArray(Arrays.asList(new String[] { "&7Right click block: Move through", "&7Left click: Move to block in line of sight" })));
    compass.setItemMeta(compassMeta);
    
    ItemStack book = new ItemStack(Material.BOOK, 1);
    ItemMeta bookMeta = book.getItemMeta();
    bookMeta.setDisplayName(Color.translate("&6Inventory Inspector"));
    bookMeta.setLore(Color.translateFromArray(Arrays.asList(new String[] { "&7Right click player to inspect inventory" })));
    book.setItemMeta(bookMeta);
    
    ItemStack blazeRod = new ItemStack(Material.ICE, 1);
    ItemMeta blazeRodMeta = blazeRod.getItemMeta();
    blazeRodMeta.setDisplayName(Color.translate("&6Player Freezer"));
    blazeRodMeta.setLore(Color.translateFromArray(Arrays.asList(new String[] { "&7Right click player to update freeze status" })));
    blazeRod.setItemMeta(blazeRodMeta);

    ItemStack record10 = new ItemStack(Material.WATCH, 1);
    ItemMeta record10Meta = record10.getItemMeta();
    record10Meta.setDisplayName(Color.translate("&6Random Teleport"));
    record10Meta.setLore(Color.translateFromArray(Arrays.asList(new String[] { "&7Right click to teleport to a random player" })));
    record10.setItemMeta(record10Meta);
    
    playerInventory.setItem(7, compass);
    playerInventory.setItem(8, book);
    playerInventory.setItem(1, blazeRod);
    playerInventory.setItem(4, getVanishItemFor(player));
    playerInventory.setItem(0, record10);
  }
  
  private ItemStack getVanishItemFor(Player player)
  {
    ItemStack inkSack = null; 
    
    if (isVanished(player))
    {
      inkSack = new ItemStack(Material.NETHER_STAR, 1);
      ItemMeta inkSackMeta = inkSack.getItemMeta();
      inkSackMeta.setDisplayName(Color.translate("&6Vanished: &aTrue"));
      inkSackMeta.setLore(Color.translateFromArray(Arrays.asList(new String[] { "&7Right click to update your vanish status." })));
      inkSack.setItemMeta(inkSackMeta);
    }
    else
    {
      inkSack = new ItemStack(Material.NETHER_STAR, 1);
      ItemMeta inkSackMeta = inkSack.getItemMeta();
      inkSackMeta.setDisplayName(Color.translate("&6Vanished: &cFalse"));
      inkSackMeta.setLore(Color.translateFromArray(Arrays.asList(new String[] { "&7Right click to update your vanish status." })));
      inkSack.setItemMeta(inkSackMeta);
    }
    
    return inkSack;
  }
  
  public void openInspectionMenu(Player player, Player target)
  {
    Inventory inventory = Bukkit.getServer().createInventory(null, 9 * 6, Color.translate("&eInspecting: &c" + target.getName()));
    
    new BukkitRunnable()
    {
      @Override
      public void run()
      {
        inspectedPlayer.put(player.getUniqueId(), target.getUniqueId());
        
        PlayerInventory playerInventory = target.getInventory();
        
        ItemStack speckledMelon = new ItemStack(Material.SPECKLED_MELON);
        ItemMeta speckledMelonMeta = speckledMelon.getItemMeta();
        speckledMelonMeta.setDisplayName(Color.translate("&aHealth"));
        speckledMelon.setItemMeta(speckledMelonMeta);
        
        ItemStack cookedBeef = new ItemStack(Material.COOKED_BEEF, target.getFoodLevel());
        ItemMeta cookedBeefMeta = cookedBeef.getItemMeta();
        cookedBeefMeta.setDisplayName(Color.translate("&aHunger"));
        cookedBeef.setItemMeta(cookedBeefMeta);
        
        ItemStack compass = new ItemStack(Material.COMPASS, 1);
        ItemMeta compassMeta = compass.getItemMeta();
        compassMeta.setDisplayName(Color.translate("&aPlayer Location"));
        compassMeta.setLore(Color.translateFromArray(Arrays.asList(new String[] { "&eWorld&7: &a" + player.getWorld().getName(), "&eCoords", "  &eX&7: &c" + target.getLocation().getBlockX(), "  &eY&7: &c" + target.getLocation().getBlockY(), "  &eZ&7: &c" + target.getLocation().getBlockZ() })));
        compass.setItemMeta(compassMeta);
        
        
        ItemStack orangeGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
        
        inventory.setContents(playerInventory.getContents());
        inventory.setItem(36, playerInventory.getHelmet());
        inventory.setItem(37, playerInventory.getChestplate());
        inventory.setItem(38, playerInventory.getLeggings());
        inventory.setItem(39, playerInventory.getBoots());
        inventory.setItem(40, playerInventory.getItemInHand());
        for (int i = 41; i <= 46; i++)
        {
          inventory.setItem(i, orangeGlassPane);
        }
        inventory.setItem(47, speckledMelon);
        inventory.setItem(48, cookedBeef);
        inventory.setItem(50, compass);
        for (int i = 52; i <= 53; i++)
        {
          inventory.setItem(i, orangeGlassPane);
        }
        
        if (!player.getOpenInventory().getTitle().equals(Color.translate("&eInspecting: &c" + target.getName())))
        {
          cancel();
          inspectedPlayer.remove(player.getUniqueId());
        }
      }
    }.runTaskTimer(utilities, 0L, 5L);
    
    player.openInventory(inventory);
  }

  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    Action action = event.getAction();
    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
    {
      Player player = event.getPlayer();
      if (isStaffModeActive(player) && player.hasPermission("utilities.player.staff"))
      {
        ItemStack itemStack = player.getItemInHand();
        if (itemStack != null)
        {
            if (itemStack.getType() == Material.DIAMOND_PICKAXE){
              int Counter = 0;
              this.inv = Bukkit.createInventory(null, 54, "Xrayer Gui");
              
    		for (Player players : Bukkit.getOnlinePlayers()){
                    Counter++;
                    if (Counter < 54){
    			if (players.getLocation().getBlockY() < 20){
    			ItemStack xSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    			ItemMeta xSkullMeta = xSkull.getItemMeta();
    			xSkullMeta.setDisplayName(players.getName());
                        xSkullMeta.setLore(Color.translateFromArray(Arrays.asList(new String[] { "&eEste jugador esta en la capa &6&l" + players.getLocation().getBlockY() })));
    			xSkull.setItemMeta(xSkullMeta);
    			inv.addItem(xSkull);
                        }
                    }
                    else {
                        event.getPlayer().sendMessage(Color.translate("&cThere are too many players mining right now."));
                    }
    		}
    		player.openInventory(inv);
    	}
          if (itemStack.getType() == Material.WATCH)
          {
            if (Bukkit.getServer().getOnlinePlayers().length > 1)
            {
              Random random = new Random();
              int size = random.nextInt(Bukkit.getServer().getOnlinePlayers().length);
              event.setCancelled(true);
            }
            else
            {
              player.sendMessage(Color.translate("&cCould not find players to teleport."));
            }
          }
          else if (itemStack.getType() == Material.NETHER_STAR)
          {
            if (isVanished(player))
            {
              setVanished(player, false);
            }
            else
            {
              setVanished(player, true);
            }
          }
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
  {
    if (event.getRightClicked() != null && event.getRightClicked() instanceof Player)
    {
      Player player = event.getPlayer();
      if (isStaffModeActive(player) && player.hasPermission("utilities.player.staff"))
      {
        ItemStack itemStack = player.getItemInHand();
        if (itemStack != null)
        {
          Player target = (Player) event.getRightClicked();
          if (itemStack.getType() == Material.BOOK)
          {
            if (target != null && !player.getName().equals(target.getName()))
            {
              openInspectionMenu(player, target);
              player.sendMessage(Color.translate("&eYou are now inspecting the inventory of &c" + target.getName() + "&e."));
            }
          }
          
          else if (itemStack.getType() == Material.CARROT_STICK)
          {
            if (target != null && !player.getName().equals(target.getName()))
            {
              if (player.getVehicle() != null)
              {
                player.getVehicle().eject();
              }
              target.setPassenger(player);
            }
          }
          
          else if (itemStack.getType() == Material.ICE)
          {
            if (target != null && !player.getName().equals(target.getName()))
            {
              {
                if (target.hasPermission("utilities.player.staff") || target.isOp())
                {
                  player.sendMessage(Color.translate("&cYou can not Freeze an Staff Member."));
                }
                else
                {
                  Bukkit.dispatchCommand(player, "ss " + target.getName());
                }
              }
            }
          }
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    if (!player.hasPermission("essentials.gamemode.creative") && player.getGameMode() == GameMode.CREATIVE)
    {
      player.setGameMode(GameMode.CREATIVE);
    }
    
    if (player.hasPermission("utilities.player.staff"))
    {
      Bukkit.getServer().getConsoleSender().sendMessage(Color.translate("&aStaff Connected: &f" + player.getName() + "."));
      for (Player staff : Bukkit.getServer().getOnlinePlayers())
      {
        if (staff.hasPermission("utilities.player.staff"))
        {
          staff.sendMessage(Color.translate("&aStaff Connected: &f" + player.getName() + "."));
        }
      }
    }
    else
    {
      if (vanished.size() > 0)
      {
        for (UUID uuid : vanished)
        {
          Player vanishedPlayer = Bukkit.getServer().getPlayer(uuid);
          if (vanishedPlayer != null) 
          {
            player.hidePlayer(vanishedPlayer);
          }
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    Player player = event.getPlayer();
    if (player.hasPermission("utilities.player.staff"))
    {
      Bukkit.getServer().getConsoleSender().sendMessage(Color.translate("&cStaff Disconnected: &f" + player.getName() + "."));
      for (Player staff : Bukkit.getServer().getOnlinePlayers())
      {
        if (staff.hasPermission("utilities.player.staff"))
        {
          staff.sendMessage(Color.translate("&cStaff Disconnected: &f" + player.getName() + "."));
        }
      }
    }
    if (isStaffModeActive(player))
    {
      staffMode.remove(player.getUniqueId());
      inspectedPlayer.remove(player.getUniqueId());
      PlayerInventory playerInventory = player.getInventory();
      playerInventory.setArmorContents(new ItemStack[] { new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
      playerInventory.clear();
      setVanished(player, false);
      if (hasPreviousInventory(player))
      {
        loadInventory(player);
      }
      if (!player.hasPermission("essentials.gamemode.creative") && player.getGameMode() == GameMode.CREATIVE)
      {
        player.setGameMode(GameMode.SURVIVAL);
      }
    }
  }
  
  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent event)
  {
    Player player = event.getPlayer();
    if (isStaffModeActive(player))
    {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onPlayerPickupItem(PlayerPickupItemEvent event)
  {
    Player player = event.getPlayer();
    if (isVanished(player) || isStaffModeActive(player))
    {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event)
  {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    if (isVanished(player) || isStaffModeActive(player))
    {
      if (block != null)
      {
        event.setCancelled(true);
      }
    }
  }
  
  @EventHandler
  public void onBlockBreak(BlockBreakEvent event)
  {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    if (isVanished(player) || isStaffModeActive(player))
    {
      if (block != null)
      {
        event.setCancelled(true);
      }
    }
  }
  
  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
  {
    if (event.getEntity() instanceof Player && event.getDamager() instanceof Player)
    {
      Player player = (Player) event.getDamager();
      if (isVanished(player) || isStaffModeActive(player) && player.hasPermission("utilities.player.staff")) 
      {
        event.setCancelled(true);
      }
    }
    else if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player)
    {
      Player player = (Player) event.getDamager();
      if (isVanished(player) || isStaffModeActive(player) && player.hasPermission("utilities.player.staff")) 
      {
        event.setCancelled(true);
      }
    }
  }
}