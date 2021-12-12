package me.einjojo.survivalengine.entity;


import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.entity.pathfinder.FollowTargetGoal;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TransportChicken extends Chicken implements ConfigurationSerializable {


    private final UUID OWNER;
    private final Inventory inventory;
    private Location location;

    private ServerLevel serverLevel;
    private boolean spawned;

    public TransportChicken(Location location, UUID uuid, Inventory inventory, boolean spawned) {
        super(EntityType.CHICKEN, ((CraftWorld) location.getWorld()).getHandle());
        this.OWNER = uuid;
        this.inventory = inventory;
        this.spawned = spawned;
        this.location = location;


        this.setHealth(2);
        this.setBaby(true);
        this.setCustomNameVisible(true);
    }

    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FollowTargetGoal(this, 1.7, 14));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8, 0.9F));
    }

    private void setTarget(org.bukkit.entity.Player player) {
        this.setTarget(((CraftPlayer) player).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
    }

    public UUID getOwner() {
        return this.OWNER;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean spawn(org.bukkit.entity.Player target) {
        Location location = target.getLocation();
        serverLevel = ((CraftWorld) this.location.getWorld()).getHandle();

        this.unsetRemoved();
        this.setPos(location.getX(), location.getY(), location.getZ());
        this.setCustomName(new TextComponent("§e" + target.getName() + "'s Transporter"));
        this.setInvulnerable(true);
        if(this.isInWall()) {
            this.setPos(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ());
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(SurvivalEngine.getInstance(), ()->{
            this.setInvulnerable(false);
        }, 100L);

        setTarget(target);
        setSpawned(true);

        return serverLevel.addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public void remove() {
        this.remove(RemovalReason.DISCARDED);
    }


    public boolean isSpawned() {
        return spawned;
    }

    /**
     * Creates a Map representation of this class.
     * <p>
     * This class must provide a method to restore this class, as defined in
     * the {@link ConfigurationSerializable} interface javadocs.
     *
     * @return Map containing the current state of this class
     */
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        Map<Integer, ItemStack> inventoryMap = new HashMap<>();

        int i = 0;
        for (ItemStack itemStack : getInventory().getContents()) {
            if(itemStack != null) {
                inventoryMap.put(i, itemStack);
            }
            i++;
        }

        map.put("spawned", isSpawned());
        map.put("inventory", inventoryMap);
        return map;
    }
}


