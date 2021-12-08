package me.einjojo.survivalengine.entity.pathfinder;

import com.mojang.math.Vector3f;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FollowTargetGoal extends Goal {

    private final Mob entity;
    private LivingEntity player;

    private final double speed;
    private final float distance;

    private double x;
    private double y;
    private double z;

    public FollowTargetGoal(Mob entity, double speed, float distance) {
        this.speed = speed;
        this.distance = distance;
        this.entity = entity;

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }


    @Override
    public boolean canUse() {
        this.player = this.entity.getTarget();
        if(this.player == null) {
            return false;
        } else if (this.entity.getDisplayName() == null) {
            return false;
        } else if (!(this.entity.getDisplayName().getContents().contains(player.getName().getContents()))) {
            return false;
        } else if (this.player.distanceToSqr(this.entity) > (double) (this.distance * this.distance)) {
            entity.setPos(player.getX(), player.getY(), player.getZ());
            return false;
        } else {

            this.x = this.player.xOld;
            this.y = this.player.yOld;
            this.z = this.player.zOld;

            return true;
        }
    }

    @Override
    public void start() {
        this.entity.getNavigation().moveTo(this.x, this.y, this.z, this.speed);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.getNavigation().isDone() && this.player.distanceToSqr(this.entity) > ((double) (this.distance * this.distance));
    }

    @Override
    public void stop() {
        this.player = null;
    }
}
