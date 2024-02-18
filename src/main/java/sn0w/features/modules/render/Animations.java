//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Mason\Downloads\1.12 stable mappings"!

//Decompiled by Procyon!

package sn0w.features.modules.render;

import sn0w.event.events.PacketEvent;
import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.potion.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Animations extends Module
{
    private final Setting<Mode> mode;
    private final Setting<Swing> swing;
    private final Setting<Boolean> slow;

    public Animations() {
        super("HandTweaks", "Change animations", Module.Category.RENDER, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.Low));
        this.swing = (Setting<Swing>)this.register(new Setting("Swing", Swing.Mainhand));
        this.slow = (Setting<Boolean>)this.register(new Setting("Slow", true));
    }

    public int onUpdate() {
        if (nullCheck()) {
            return 0;
        }
        if (this.swing.getValue() == Swing.Offhand) {
            Animations.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (this.mode.getValue() == Mode.High && Animations.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            Animations.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            Animations.mc.entityRenderer.itemRenderer.itemStackMainHand = Animations.mc.player.getHeldItemMainhand();
        }
        return 0;
    }

    public void onRender3D() {
    }

    public void onEnable() {
        if (this.slow.getValue()) {
            Animations.mc.player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 255000));
        }
    }

    public void onDisable() {
        if (this.slow.getValue()) {
            Animations.mc.player.removePotionEffect(MobEffects.MINING_FATIGUE);
        }
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        final Object raw = event.getPacket();
        if (raw instanceof CPacketAnimation) {
            final CPacketAnimation packet = (CPacketAnimation)raw;
            if (this.swing.getValue() == Swing.Packet) {
                event.setCanceled(true);
            }
        }
    }

    private enum Mode
    {
        Low,
        High;
    }

    private enum Swing
    {
        Mainhand,
        Offhand,
        Packet;
    }
}
