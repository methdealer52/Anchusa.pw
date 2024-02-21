package sn0w.features.modules.combat;

import sn0w.OyVey;
import sn0w.event.events.UpdateWalkingPlayerEvent;
import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import sn0w.util.DamageUtil;
import sn0w.util.EntityUtil;
import sn0w.util.MathUtil;
import sn0w.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Killaura extends Module {
    public static Entity target;
    private final Timer timer = new Timer();
    public Setting<Float> range = register(new Setting("Range", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F)));
    public Setting<Boolean> delay = register(new Setting("HitDelay", Boolean.valueOf(true)));
    public Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
    public Setting<Boolean> onlySharp = register(new Setting("SwordOnly", Boolean.valueOf(true)));
    public Setting<Float> raytrace = register(new Setting("Raytrace", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F), "Wall Range."));
    public Setting<Boolean> players = register(new Setting("Players", Boolean.valueOf(true)));
    public Setting<Boolean> mobs = register(new Setting("Mobs", Boolean.valueOf(false)));
    public Setting<Boolean> animals = register(new Setting("Animals", Boolean.valueOf(false)));
    public Setting<Boolean> vehicles = register(new Setting("Entities", Boolean.valueOf(false)));
    public Setting<Boolean> projectiles = register(new Setting("Projectiles", Boolean.valueOf(false)));
    public Setting<Boolean> tps = register(new Setting("TpsSync", Boolean.valueOf(true)));
    public Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));

    public Killaura() {
        super("Killaura", "Kills aura.", Module.Category.COMBAT, true, false, false);
    }

    public void onTick() {
        if (!this.rotate.getValue(true).booleanValue())
            doKillaura();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.rotate.getValue(true).booleanValue())
            doKillaura();
    }

    private void doKillaura() {
        if (this.onlySharp.getValue(true).booleanValue() && !EntityUtil.holdingWeapon(mc.player)) {
            target = null;
            return;
        }
        int wait = !this.delay.getValue(true).booleanValue() ? 0 : (int) (DamageUtil.getCooldownByWeapon(mc.player) * (this.tps.getValue(true).booleanValue() ? OyVey.serverManager.getTpsFactor() : 1.0F));
        if (!this.timer.passedMs(wait))
            return;
        target = getTarget();
        if (target == null)
            return;
        if (this.rotate.getValue(true).booleanValue())
            OyVey.rotationManager.lookAtEntity(target);
        EntityUtil.attackEntity(target, this.packet.getValue(true).booleanValue(), true);
        this.timer.reset();
    }

    private Entity getTarget() {
        Entity target = null;
        double distance = this.range.getValue(true).floatValue();
        double maxHealth = 36.0D;
        for (Entity entity : mc.world.playerEntities) {
            if (((!this.players.getValue(true).booleanValue() || !(entity instanceof EntityPlayer)) && (!this.animals.getValue(true).booleanValue() || !EntityUtil.isPassive(entity)) && (!this.mobs.getValue(true).booleanValue() || !EntityUtil.isMobAggressive(entity)) && (!this.vehicles.getValue(true).booleanValue() || !EntityUtil.isVehicle(entity)) && (!this.projectiles.getValue(true).booleanValue() || !EntityUtil.isProjectile(entity))) || (entity instanceof net.minecraft.entity.EntityLivingBase &&
                    EntityUtil.isntValid(entity, distance)))
                continue;
            if (!mc.player.canEntityBeSeen(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && mc.player.getDistanceSq(entity) > MathUtil.square(this.raytrace.getValue(true).floatValue()))
                continue;
            if (target == null) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
                continue;
            }
            if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer) entity, 18)) {
                target = entity;
                break;
            }
            if (mc.player.getDistanceSq(entity) < distance) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
            if (EntityUtil.getHealth(entity) < maxHealth) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
        }
        return target;
    }

    public String getDisplayInfo() {
        if (target instanceof EntityPlayer)
            return target.getName();
        return null;
    }
}
