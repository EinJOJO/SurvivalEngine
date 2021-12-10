package me.einjojo.survivalengine.entity;


import me.einjojo.survivalengine.entity.pathfinder.FollowTargetGoal;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.event.entity.EntityTargetEvent;

public class TransportChicken extends Chicken {


    private final org.bukkit.entity.Player OWNER;

    public TransportChicken(Location location, org.bukkit.entity.Player owner) {
        super(EntityType.CHICKEN, ((CraftWorld) location.getWorld()).getHandle());
        this.setHealth(2);
        this.setBaby(true);
        this.setPos(location.getX(), location.getY(), location.getZ());
        this.setCustomName(new TextComponent("§e" + owner.getName() + "'s Transporter"));
        this.setCustomNameVisible(true);
        this.OWNER = owner;
        setTarget(owner);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FollowTargetGoal(this, 1.2, 8));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8, 0.9F));
    }

    private void setTarget(org.bukkit.entity.Player player) {
        this.setTarget(((CraftPlayer) player).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
    }

    public void leave() {
        this.goalSelector.removeAllGoals();
    }


}


