
package sn0w.features.modules.render;

import sn0w.features.modules.Module;
import sn0w.features.setting.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.entity.player.*;
import net.minecraft.entity.effect.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;

public class KillEffects extends Module
{
    public Setting<Boolean> thunder;
    public Setting<Integer> numbersThunder;
    public Setting<Boolean> sound;
    public Setting<Integer> numberSound;
    public Setting<Integer> timeActive;
    public Setting<Boolean> lightning;
    public Setting<Boolean> totemPop;
    public Setting<Boolean> totemPopSound;
    public Setting<Boolean> firework;
    public Setting<Boolean> fire;
    public Setting<Boolean> water;
    public Setting<Boolean> smoke;
    public Setting<Boolean> players;
    public Setting<Boolean> animals;
    public Setting<Boolean> mobs;
    public Setting<Boolean> all;
    ArrayList<EntityPlayer> playersDead;
    final Object sync;

    public KillEffects() {
        super("DeathEffects", "When you kill something it spawns shit.", Module.Category.RENDER, true, false, false);
        this.thunder = (Setting<Boolean>)this.register(new Setting("Thunder", (Object)true));
        this.numbersThunder = (Setting<Integer>)this.register(new Setting("Number Thunder", (Object)1, (Object)1, (Object)10));
        this.sound = (Setting<Boolean>)this.register(new Setting("Sound", (Object)true));
        this.numberSound = (Setting<Integer>)this.register(new Setting("Number Sound", (Object)1, (Object)1, (Object)10));
        this.timeActive = (Setting<Integer>)this.register(new Setting("TimeActive", (Object)20, (Object)0, (Object)50));
        this.lightning = (Setting<Boolean>)this.register(new Setting("Lightning", (Object)true));
        this.totemPop = (Setting<Boolean>)this.register(new Setting("TotemPop", (Object)true));
        this.totemPopSound = (Setting<Boolean>)this.register(new Setting("TotemPopSound", (Object)false));
        this.firework = (Setting<Boolean>)this.register(new Setting("FireWork", (Object)false));
        this.fire = (Setting<Boolean>)this.register(new Setting("Fire", (Object)false));
        this.water = (Setting<Boolean>)this.register(new Setting("Water", (Object)false));
        this.smoke = (Setting<Boolean>)this.register(new Setting("Smoke", (Object)false));
        this.players = (Setting<Boolean>)this.register(new Setting("Players", (Object)true));
        this.animals = (Setting<Boolean>)this.register(new Setting("Animals", (Object)true));
        this.mobs = (Setting<Boolean>)this.register(new Setting("Mobs", (Object)true));
        this.all = (Setting<Boolean>)this.register(new Setting("All", (Object)true));
        this.playersDead = new ArrayList<EntityPlayer>();
        this.sync = new Object();
    }

    public void onEnable() {
        this.playersDead.clear();
    }

    public int onUpdate() {
        if (KillEffects.mc.world == null) {
            this.playersDead.clear();
        }
        AtomicInteger i = new AtomicInteger();
        AtomicInteger j = new AtomicInteger();
        KillEffects.mc.world.playerEntities.forEach(entity -> {
            if (this.playersDead.contains(entity)) {
                if (entity.getHealth() > 0.0f) {
                    this.playersDead.remove(entity);
                }
            }
            else if (entity.getHealth() == 0.0f) {
                if (this.thunder.getValue()) {
                    for (i.set(0); i.get() < (int)this.numbersThunder.getValue(); i.incrementAndGet()) {
                        KillEffects.mc.world.spawnEntity((Entity)new EntityLightningBolt((World)KillEffects.mc.world, entity.posX, entity.posY, entity.posZ, true));
                    }
                }
                if (this.sound.getValue()) {
                    for (j.set(0); j.get() < (int)this.numberSound.getValue(); j.incrementAndGet()) {
                        KillEffects.mc.player.playSound(SoundEvents.ENTITY_LIGHTNING_THUNDER, 0.5f, 1.0f);
                    }
                }
                this.playersDead.add(entity);
            }
        });
        return 0;
    }

    @SubscribeEvent
    public void onDeath(final LivingDeathEvent event) {
        if (event.getEntity() == KillEffects.mc.player) {
            return;
        }
        if (this.shouldRenderParticle(event.getEntity())) {
            if (this.lightning.getValue()) {
                KillEffects.mc.world.addEntityToWorld(-999, (Entity)new EntityLightningBolt((World)KillEffects.mc.world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, true));
            }
            if (this.totemPop.getValue()) {
                this.totemPop(event.getEntity());
            }
            if (this.firework.getValue()) {
                KillEffects.mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.FIREWORKS_SPARK, (int)this.timeActive.getValue());
            }
            if (this.fire.getValue()) {
                KillEffects.mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.FLAME, (int)this.timeActive.getValue());
                KillEffects.mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.DRIP_LAVA, (int)this.timeActive.getValue());
            }
            if (this.water.getValue()) {
                KillEffects.mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.WATER_BUBBLE, (int)this.timeActive.getValue());
                KillEffects.mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.WATER_DROP, (int)this.timeActive.getValue());
            }
            if (this.smoke.getValue()) {
                KillEffects.mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.SMOKE_NORMAL, (int)this.timeActive.getValue());
            }
        }
    }

    public boolean shouldRenderParticle(final Entity entity) {
        return entity != KillEffects.mc.player && ((boolean)this.all.getValue() || (entity instanceof EntityPlayer && (boolean)this.players.getValue()) || entity instanceof EntityMob || (entity instanceof EntitySlime && (boolean)this.mobs.getValue()) || (entity instanceof EntityAnimal && (boolean)this.animals.getValue()));
    }

    public void totemPop(final Entity entity) {
        KillEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.TOTEM, (int)this.timeActive.getValue());
        if (this.totemPopSound.getValue()) {
            KillEffects.mc.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0f, 1.0f, false);
        }
    }
}
