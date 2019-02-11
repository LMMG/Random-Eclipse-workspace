package org.hcgames.icefyre.profile;

import com.google.gson.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import net.minecraft.server.v1_7_R4.Packet;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.arena.Arena;
import org.hcgames.icefyre.game.fight.Fight;
import org.hcgames.icefyre.game.fight.FightType;
import org.hcgames.icefyre.game.fight.cache.CachedFight;
import org.hcgames.icefyre.game.fight.duel.DuelRequest;
import org.hcgames.icefyre.game.fight.inventory.FightInventory;
import org.hcgames.icefyre.game.kit.Kit;
import org.hcgames.icefyre.game.kit.builder.KitBuilderProfile;
import org.hcgames.icefyre.game.kit.selection.KitSelectionProfile;
import org.hcgames.icefyre.game.ladder.Ladder;
import org.hcgames.icefyre.game.queue.QueueProfile;
import org.hcgames.icefyre.party.Party;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hcgames.icefyre.party.fight.PartyFight;
import org.hcgames.icefyre.util.InventorySerialisation;
import org.hcgames.icefyre.util.ItemBuilder;

import javax.print.Doc;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class Profile {

    private static Set<Profile> profiles = new HashSet<>();
    private static Icefyre main = Icefyre.getInstance();

    @Getter private Player player;
    @Getter @Setter UUID uuid;
    @Getter @Setter private ProfileState state;
    @Getter private Map<Ladder, Integer> rankings;
    @Getter private Map<Ladder, Kit[]> kits;
    @Getter @Setter private KitBuilderProfile builderProfile;
    @Getter @Setter private QueueProfile queueProfile;
    @Getter @Setter private KitSelectionProfile selectionProfile;
    @Getter @Setter private FightInventory fightInventory;
    @Getter @Setter private Fight fight;
    @Getter @Setter private Map<UUID, DuelRequest> duelRequests;
    @Getter private List<Packet> packets;
    @Getter private List<Location> potions;
    @Getter @Setter private String chatToken;
    @Getter private List<UUID> invitedPlayers;
    @Getter @Setter private PartyFight partyFight;
    @Getter List<CachedFight> cachedFights;
    @Getter @Setter private Location pearlLocation;

    public Profile(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.state = ProfileState.IN_LOBBY;
        this.rankings = new HashMap<>();
        this.kits = new HashMap<>();
        this.duelRequests = new HashMap<>();
        this.packets = new ArrayList<>();
        this.potions = new ArrayList<>();
        this.invitedPlayers = new ArrayList<>();
        this.cachedFights = new ArrayList<>();

        for (Ladder ladder : Ladder.getLadders()) {
            rankings.put(ladder, ladder.getDefaultRanking());
            kits.put(ladder, new Kit[5]);
        }

        profiles.add(this);

        load();
    }

    public DuelRequest getMostRecentDuelRequest() {
        DuelRequest toReturn = null;

        for (DuelRequest duelRequest : duelRequests.values()) {

            if (toReturn == null) {
                toReturn = duelRequest;
                continue;
            }

            if (duelRequest.getInit() > toReturn.getInit()) {
                toReturn = duelRequest;
            }

        }

        return toReturn;
    }

    public UUID getUuidByDuelRequest(DuelRequest duelRequest) {
        for (UUID uuid : new HashSet<>(duelRequests.keySet())) {
            if (duelRequests.get(uuid).equals(duelRequest)) {
                return uuid;
            }
        }
        return null;
    }

    public Player getPlayerByDuelRequest(DuelRequest duelRequest) {
        Player toReturn = null;

        for (UUID uuid : new HashSet<>(duelRequests.keySet())) {
            if (duelRequests.get(uuid).equals(duelRequest)) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    toReturn = player;
                } else {
                    duelRequests.remove(uuid);
                }
            }
        }

        return toReturn;
    }

    public DuelRequest getDuelRequestByPlayer(Player player) {
        for (UUID uuid : new HashSet<>(duelRequests.keySet())) {
            if (player.getUniqueId().equals(player.getUniqueId())) {
                return duelRequests.get(uuid);
            }
        }
        return null;
    }

    public Inventory getKitSaveInventory(Ladder ladder) {
        Inventory toReturn = Bukkit.createInventory(null, 9 * 4, main.getLangFile().getString("KIT_BUILDER.SAVE_INVENTORY.TITLE").replace("%LADDER%", ladder.getName()));

        for (int i = 0; i < kits.get(ladder).length; i++) {
            int x = (int) Math.ceil(i * 1.8);
            Kit kit = kits.get(ladder)[i];

            if (kit == null) {
                toReturn.setItem(x, new ItemBuilder(Material.CHEST).name(main.getLangFile().getString("KIT_BUILDER.SAVE_INVENTORY.CHEST_SAVE").replace("%KIT%", ladder.getName() + " Kit #" + (i + 1))).build());
            } else {
                ItemStack chest = new ItemBuilder(Material.CHEST).name(main.getLangFile().getString("KIT_BUILDER.SAVE_INVENTORY.KIT_NAME").replace("%KIT%", kit.getName())).build();
                ItemStack book = new ItemBuilder(Material.ENCHANTED_BOOK).name(main.getLangFile().getString("KIT_BUILDER.SAVE_INVENTORY.MODIFY").replace("%KIT%", kit.getName())).build();
                ItemStack nameTag = new ItemBuilder(Material.NAME_TAG).name(main.getLangFile().getString("KIT_BUILDER.SAVE_INVENTORY.RENAME").replace("%KIT%", kit.getName())).build();
                ItemStack fire = new ItemBuilder(Material.FIRE).name(main.getLangFile().getString("KIT_BUILDER.SAVE_INVENTORY.DELETE").replace("%KIT%", kit.getName())).build();

                toReturn.setItem(x, chest);
                toReturn.setItem(x + 9, book);
                toReturn.setItem(x + 18, nameTag);
                toReturn.setItem(x + 27, fire);
            }
        }

        return toReturn;
    }

    public boolean hasKits(Ladder ladder) {
        for (Kit kit : kits.get(ladder)) {
            if (kit != null) {
                return true;
            }
        }
        return false;
    }

    public void remove() {
        profiles.remove(this);
        save();
    }

    private void load() {
        Document document = (Document) main.getIcefyreDatabase().getProfiles().find(eq("uuid", uuid.toString())).first();
        if (document != null) {
            if (document.containsKey("fights")) {

                Document elo = (Document) document.get("rankings");
                for (String ladderName : elo.keySet()) {
                    rankings.put(Ladder.getByName(ladderName), elo.getInteger(ladderName));
                }

                JsonArray fights = (JsonArray) new JsonParser().parse(document.getString("fights"));
                for (JsonElement element : fights) {
                    JsonObject fight = (JsonObject) element;

                    UUID uuid = UUID.fromString(fight.get("uuid").getAsString());
                    FightType type = FightType.valueOf(fight.get("type").getAsString());
                    String ladder = fight.get("ladder").getAsString();
                    String arena = fight.get("arena").getAsString();
                    long startedAt = fight.get("startedAt").getAsLong();
                    long duration = fight.get("duration").getAsLong();
                    boolean won = fight.get("won").getAsBoolean();

                    Map<UUID, FightInventory> participants = new HashMap<>();

                    for (JsonElement participantElement : fight.get("participants").getAsJsonArray()) {
                        JsonObject participant = (JsonObject) participantElement;
                        UUID participantUuid = UUID.fromString(participant.get("uuid").getAsString());
                        int health = participant.get("health").getAsInt();

                        List<ItemStack> contents;
                        List<ItemStack> armor;
                        try {
                            contents = new ArrayList<>(Arrays.asList(InventorySerialisation.itemStackArrayFromBase64(participant.get("contents").getAsString())));
                            armor = new ArrayList<>(Arrays.asList(InventorySerialisation.itemStackArrayFromBase64(participant.get("armor").getAsString())));
                        } catch (IOException e) {
                            e.printStackTrace();
                            continue;
                        }

                        participants.put(participantUuid, new FightInventory(null, contents, armor, health));
                    }

                    cachedFights.add(new CachedFight(uuid, participants, ladder, arena, type, startedAt, duration, won));
                }
            }
        }
    }

    private void save() {
        MongoCollection collection = main.getIcefyreDatabase().getProfiles();
        Document document = new Document();
        document.put("uuid", player.getUniqueId().toString());

        JsonArray fights = new JsonArray();
        for (CachedFight fight : cachedFights) {
            JsonObject object = new JsonObject();
            object.addProperty("uuid", fight.getUuid().toString());
            object.addProperty("type", fight.getType().name());
            object.addProperty("ladder", fight.getLadder());
            object.addProperty("arena", fight.getArena());
            object.addProperty("startedAt", fight.getStartedAt());
            object.addProperty("duration", fight.getDuration());
            object.addProperty("won", fight.isWon());
            JsonArray participants = new JsonArray();
            for (UUID uuid : fight.getParticipants().keySet()) {
                JsonObject participant = new JsonObject();
                FightInventory inventory = fight.getParticipants().get(uuid);

                participant.addProperty("uuid", uuid.toString());
                participant.addProperty("health", inventory.getHealth());
                participant.addProperty("contents", InventorySerialisation.itemStackArrayToBase64(inventory.getArmor().toArray(new ItemStack[inventory.getContents().size()])));
                participant.addProperty("armor", InventorySerialisation.itemStackArrayToBase64(inventory.getArmor().toArray(new ItemStack[inventory.getArmor().size()])));

                participants.add(participant);
            }

            object.add("participants", participants);
            fights.add(object);
        }

        if (fights.size() > 0) {
            document.put("fights", fights.toString());
        }

        Document elo = new Document();
        for (Ladder ladder : rankings.keySet()) {
            elo.put(ladder.getName(), rankings.get(ladder));
        }

        document.put("rankings", elo);

        collection.replaceOne(eq("uuid", uuid.toString()), document, new UpdateOptions().upsert(true));
    }

    public int getUnrankedMatchesWon() {
        int toReturn = 0;

        for (CachedFight fight : cachedFights) {
            if (fight.getType() == FightType.UNRANKED && fight.isWon()) {
                toReturn++;
            }
        }

        return toReturn;
    }

    public Party getParty() {
        return Party.getByPlayer(player);
    }

    public String getName() {
        return player.getName();
    }

    public static Profile getByPlayer(Player player) {
        for (Profile profile : profiles) {
            if (profile.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                return profile;
            }
        }
        return null;
    }

    public static void setup() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            new Profile(player);
            main.getFightHandler().setupPlayer(player);
        }
    }

    public static Set<Profile> getProfiles() {
        return profiles;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }

    public String getChatToken() {
        return chatToken;
    }

    public List<Location> getPotions() {
        return potions;
    }

    public List<Packet> getPackets() {
        return packets;
    }

    public Player getPlayer() {
        return player;
    }
}
