package sn0w.features.modules.player;

import sn0w.features.command.Command;
import sn0w.features.modules.Module;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;

public class ChestSwap
        extends Module {
    public ChestSwap() {
        super("Chest Swap", "Highlights the block u look at.", Category.PLAYER, true, false, false);
    }



    @Override
    public void onEnable() {
        Command.sendMessage("Swapped the chest slot");
        if (fullNullCheck()) return;
        ItemStack itemStack = mc.player.inventoryContainer.getSlot(6).getStack();
        if (itemStack.isEmpty()) {
            int n = this.FindChestItem(true);
            if (n != -1) {
                mc.playerController.windowClick(ChestSwap.mc.player.inventoryContainer.windowId, n, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(ChestSwap.mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(ChestSwap.mc.player.inventoryContainer.windowId, n, 0, ClickType.PICKUP, mc.player);
                mc.playerController.updateController();
            }
            this.toggle();
            return;
        }
        int n = this.FindChestItem(itemStack.getItem() instanceof ItemArmor);
        if (n != -1) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, n, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, n, 0, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
        }
        this.toggle();
    }

    private int FindChestItem(boolean bl) {
        int n = -1;
        float f = 0.0f;
        for (int i = 0; i < mc.player.inventoryContainer.getInventory().size(); ++i) {
            ItemStack itemStack;
            if (i == 0 || i == 5 || i == 6 || i == 7 || i == 8 || (itemStack = (ItemStack)ChestSwap.mc.player.inventoryContainer.getInventory().get(i)) == null || itemStack.getItem() == Items.AIR) continue;
            if (itemStack.getItem() instanceof ItemArmor) {
                ItemArmor itemArmor = (ItemArmor)((Object)itemStack.getItem());
                if (itemArmor.armorType != EntityEquipmentSlot.CHEST) continue;
                float f2 = itemArmor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel((Enchantment) Enchantments.PROTECTION, (ItemStack)itemStack);
                boolean bl2 = EnchantmentHelper.hasBindingCurse((ItemStack)itemStack);
                if (!(f2 > f) || bl2) continue;
                f = f2;
                n = i;
                continue;
            }
            if (!bl || !(itemStack.getItem() instanceof ItemElytra)) continue;
            return i;
        }
        return n;
    }
}