package sn0w.features.modules.combat;

import sn0w.features.modules.*;
import sn0w.features.setting.*;
import net.minecraft.client.gui.inventory.*;
import sn0w.event.events.*;
import net.minecraft.network.play.server.*;
import sn0w.features.command.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.enchantment.*;

public class Armor extends Module
{
    Setting<Mode> mode;
    Setting<Boolean> predict;

    public Armor() {
        super("Armor", Category.COMBAT, "");
        this.mode = (Setting<Mode>)this.register(new Setting("ClickType", Mode.QuickMove));
        this.predict = (Setting<Boolean>)this.register(new Setting("Predict", true));
    }

    @Override
    public int onUpdate() {
        if (fullNullCheck() || (Armor.mc.currentScreen instanceof GuiContainer && !(Armor.mc.currentScreen instanceof GuiInventory))) {
            return 0;
        }
        final ItemStack helmSlot = Armor.mc.player.inventoryContainer.getSlot(5).getStack();
        final ItemStack chestSlot = Armor.mc.player.inventoryContainer.getSlot(6).getStack();
        final ItemStack legsSlot = Armor.mc.player.inventoryContainer.getSlot(7).getStack();
        final ItemStack feetSlot = Armor.mc.player.inventoryContainer.getSlot(8).getStack();
        final int helmItem = this.findArmorSlot(EntityEquipmentSlot.HEAD, false);
        final int chestItem = this.findArmorSlot(EntityEquipmentSlot.CHEST, false);
        final int legsItem = this.findArmorSlot(EntityEquipmentSlot.LEGS, false);
        final int feetItem = this.findArmorSlot(EntityEquipmentSlot.FEET, false);
        if (helmSlot.getItem() == Items.AIR && helmItem != -1) {
            this.putOn(helmItem);
        }
        if (chestSlot.getItem() == Items.AIR && chestItem != -1) {
            this.putOn(chestItem);
        }
        if (legsSlot.getItem() == Items.AIR && legsItem != -1) {
            this.putOn(legsItem);
        }
        if (feetSlot.getItem() == Items.AIR && feetItem != -1) {
            this.putOn(feetItem);
        }
        return helmItem;
    }

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive e) {
        if (fullNullCheck() || this.isDisabled() || !this.predict.getValue(true)) {
            return;
        }
        if (e.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect p = (SPacketSoundEffect)e.getPacket();
            if (p.getSound() == SoundEvents.ENTITY_ITEM_BREAK) {
                Command.sendMessage("@EntityItemBreak");
            }
        }
    }

    void putOn(final int slot) {
        final ClickType clickType = (this.mode.getValue(true) == Mode.Pick) ? ClickType.PICKUP : ClickType.QUICK_MOVE;
        Armor.mc.playerController.windowClick(Armor.mc.player.inventoryContainer.windowId, slot, 0, clickType, (EntityPlayer)Armor.mc.player);
        Armor.mc.playerController.updateController();
    }

    int findArmorSlot(final EntityEquipmentSlot type, final boolean binding) {
        int slot = -1;
        float damage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            final ItemStack is = Armor.mc.player.inventoryContainer.getSlot(i).getStack();
            final ItemArmor armor;
            if (is.getItem() != Items.AIR && is.getItem() instanceof ItemArmor && (armor = (ItemArmor)is.getItem()).getEquipmentSlot() == type) {
                final float currentDamage = (float)(armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, is));
                final boolean cursed = binding && EnchantmentHelper.hasBindingCurse(is);
                if (currentDamage > damage && !cursed) {
                    damage = currentDamage;
                    slot = i;
                }
            }
        }
        return slot;
    }

    int findEmptySlot() {
        int slot = -1;
        for (int i = 9; i < 45; ++i) {
            final ItemStack is = Armor.mc.player.inventoryContainer.getSlot(i).getStack();
            if (is.getItem() == Items.AIR) {
                slot = i;
            }
        }
        return slot;
    }

    enum Mode
    {
        QuickMove,
        Pick;
    }
}
