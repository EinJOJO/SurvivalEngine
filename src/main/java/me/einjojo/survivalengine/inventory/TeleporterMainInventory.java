package me.einjojo.survivalengine.inventory;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.InventoryManager;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import me.einjojo.survivalengine.object.Team;
import me.einjojo.survivalengine.object.Teleporter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TeleporterMainInventory {

    private final SurvivalEngine plugin;
    private final InventoryManager inventoryManager;
    private final static String name = "§cTeleporter verwalten";
    private Inventory inventory;

    public TeleporterMainInventory() {
        this.plugin = SurvivalEngine.getInstance();
        this.inventoryManager = plugin.getInventoryManager();
    }

    public static String getName() {
        return name;
    }


    public Inventory getInventory(Player player, Teleporter teleporter) {
        inventory = Bukkit.getServer().createInventory(null, 54, name);
        inventoryManager.fillGuiWithGlass(inventory);

        addLinkedTeleporterItem(30);
        addUnbindItem( 32);
        addInformationItem( teleporter, 13);
        addAccessModifierItem(teleporter, 49);

        if(teleporter.getType().equals(Teleporter.Type.TEAM)) {
            SurvivalPlayer survivalPlayer = plugin.getPlayerManager().getPlayer(player);
            Team team = survivalPlayer.getTeam();


            if ( team != null && teleporter.isOwner(team)) {
                addDismountItem(53);
            }
        } else {
            if(player.getUniqueId().equals(teleporter.getOwner())) {
                addDismountItem(53);
            }
        }
        return inventory;
    }
    public void openInventory(Player player, Teleporter teleporter) {
        player.openInventory(getInventory(player, teleporter));
    }
    private void addUnbindItem( int position) {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7Platziere deinen §5Teleport");
        lore.add("§5Kristall §7hier rein, um");
        lore.add("§7ihn zurückzusetzten");
        lore.add("");
        lore.add("§4FEHLERHAFT: §7Nutze §b/fix §7alternativ");
        inventoryManager.addItemStack(inventory, new ItemStack(Material.FLOWER_POT),  position, "§6Disbinder", lore);
    }
    private void addDismountItem( int position) {
        ItemStack itemStack = new ItemStack(Material.BARRIER, 1);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§cKlick mich, um den");
        lore.add("§cTeleporter abzubauen");
        inventoryManager.addItemStack(inventory, itemStack, position, "§4Teleporter abbauen", lore );
    }
    private void addInformationItem(Teleporter teleporter, int position) {
        List<String> lore = new ArrayList<>();
        lore.add("");
        if(teleporter.getType().equals(Teleporter.Type.PLAYER)) {
            lore.add("§7Besitzer: §c" + Bukkit.getOfflinePlayer(teleporter.getOwner()).getName());
        } else {
            lore.add("§7Besitzer: §c" + SurvivalEngine.getInstance().getTeamManager().getTeam(teleporter.getOwner()).getName());
        }
        lore.add("§7Benutzt: §c" + teleporter.getUsedCounter());
        lore.add("§7Status: " + ((teleporter.isActivated()) ? "§aAktiviert": "§cDeaktiviert"));
        lore.add("");

        inventoryManager.addItemStack(inventory, new ItemStack(Material.TOTEM_OF_UNDYING), position, "§c"+teleporter.getName(), lore );
    }
    private void addLinkedTeleporterItem(int position) {
        ItemStack itemStack = new ItemStack(Material.WRITABLE_BOOK, 1);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§c[Rechtsklick] §7Sync-Splitter erhalten.");
        lore.add("§c[Linksklick] §7Teleporter Menü öffnen");
        lore.add("");
        inventoryManager.addItemStack(inventory, itemStack, position, "§6Verlinkte Teleporter", lore );
    }

    private void addAccessModifierItem(Teleporter teleporter, int position) {
        ItemStack itemStack = new ItemStack(Material.OAK_SIGN, 1);
        List<String> lore = new ArrayList<>();
        lore.add("");
        if(teleporter.getType().equals(Teleporter.Type.TEAM)) {
            lore.add("§8» §cTeam");
            lore.add("§7Spieler");
        } else {
            lore.add("§7Team");
            lore.add("§8» §cSpieler");
        }
        lore.add("");
        inventoryManager.addItemStack(inventory, itemStack, position, "§cAccess-Modifier", lore );
    }




}
