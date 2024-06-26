package sn0w.features.modules.player;

import sn0w.features.modules.Module;
import sn0w.features.setting.Bind;
import sn0w.features.setting.Setting;
import sn0w.util.InventoryUtil;
import org.lwjgl.input.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class SilentXP extends Module
{
    public Setting<Mode> mode;
    public Setting<Boolean> antiFriend;
    public Setting<Bind> key;
    public Setting<Boolean> groundOnly;
    private boolean last;
    private boolean on;

    public SilentXP() {
        super("PacketEXP",  "Silent XP.",  Module.Category.PLAYER, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.MIDDLECLICK));
        this.antiFriend = (Setting<Boolean>)this.register(new Setting("AntiFriend", true));
        this.key = (Setting<Bind>)this.register(new Setting("Key", new Bind(-1), v -> this.mode.getValue(true) != Mode.MIDDLECLICK));
        this.groundOnly = (Setting<Boolean>)this.register(new Setting("BelowHorizon", false));
    }

    public int onUpdate() {
        if (fullNullCheck()) {
            return 0;
        }
        switch (this.mode.getValue(true)) {
            case PRESS: {
                if (this.key.getValue(true).isDown()) {
                    this.throwXP(false);
                    break;
                }
                break;
            }
            case TOGGLE: {
                if (this.toggled()) {
                    this.throwXP(false);
                    break;
                }
                break;
            }
            default: {
                if (this.groundOnly.getValue(true) && SilentXP.mc.player.rotationPitch < 0.0f) {
                    return 0;
                }
                if (Mouse.isButtonDown(2)) {
                    this.throwXP(true);
                    break;
                }
                break;
            }
        }
        return 0;
    }

    private boolean toggled() {
        if (this.key.getValue(true).getKey() == -1) return false;
        if (!Keyboard.isKeyDown(this.key.getValue(true).getKey())) {
            this.last = true;
        } else {
            if (Keyboard.isKeyDown(this.key.getValue(true).getKey()) && this.last && !this.on) {
                this.last = false;
                return this.on = true;
            }
            if (Keyboard.isKeyDown(this.key.getValue(true).getKey()) && this.last && this.on) {
                this.last = false;
                return this.on = false;
            }
        }
        return this.on;
    }

    private void throwXP(final boolean mcf) {
        final RayTraceResult result;
        if (mcf && this.antiFriend.getValue(true) && (result = SilentXP.mc.objectMouseOver) != null && result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityPlayer) return;
        final int xpSlot = InventoryUtil.findHotbarBlock(ItemExpBottle.class);
        final boolean offhand = SilentXP.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE;
        if (xpSlot != -1 || offhand) {
            final int oldslot = SilentXP.mc.player.inventory.currentItem;
            if (!offhand) InventoryUtil.switchToHotbarSlot(xpSlot,  false);
            SilentXP.mc.playerController.processRightClick((EntityPlayer)SilentXP.mc.player,  (World)SilentXP.mc.world,  offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) InventoryUtil.switchToHotbarSlot(oldslot,  false);
        }
    }

    public enum Mode {
        MIDDLECLICK,
        TOGGLE,
        PRESS;
    }
}