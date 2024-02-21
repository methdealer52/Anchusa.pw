package sn0w.features.modules.player;

import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import sn0w.util.Timer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class Replenish extends Module {
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 0, 0, 10));
    private final Setting<Integer> gapStack = this.register(new Setting<Integer>("GapStack", 1, 50, 64));
    private final Setting<Integer> xpStackAt = this.register(new Setting<Integer>("XPStack", 1, 50, 64));
    private final Timer timer = new Timer();
    private final ArrayList<Item> hotbar = new ArrayList();

    public Replenish() {
        super("Replenish", "Replenishes your hotbar", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        if (fullNullCheck()) return;
        this.hotbar.clear();
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && !this.hotbar.contains(stack.getItem())) {
                this.hotbar.add(stack.getItem());
                continue;
            }
            this.hotbar.add(Items.AIR);
        }
    }

    @Override
    public void onUpdate() {
        if (mc.currentScreen != null) return 0;
        if (!this.timer.passedMs(this.delay.getValue(true) * 1000)) return 0;
        for (int i = 0; i < 9; ++i) {
            if (!this.RefillSlotIfNeed(i)) continue;
            this.timer.reset();
            return i;
        }
        return 0;
    }

    private boolean RefillSlotIfNeed(int p_Slot) {
        ItemStack stack = mc.player.inventory.getStackInSlot(p_Slot);
        if (stack.isEmpty() || stack.getItem() == Items.AIR) return false;
        if (!stack.isStackable()) return false;
        if (stack.getCount() >= stack.getMaxStackSize()) return false;
        if (stack.getItem().equals(Items.GOLDEN_APPLE) && stack.getCount() >= this.gapStack.getValue(true)) return false;
        if (stack.getItem().equals(Items.EXPERIENCE_BOTTLE) && stack.getCount() > this.xpStackAt.getValue(true)) return false;
        for (int i = 9; i < 36; ++i) {
            ItemStack item = mc.player.inventory.getStackInSlot(i);
            if (item.isEmpty() || !this.CanItemBeMergedWith(stack, item)) continue;
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.QUICK_MOVE, Replenish.mc.player);
            mc.playerController.updateController();
            return true;
        }
        return false;
    }

    private boolean CanItemBeMergedWith(ItemStack stackSource, ItemStack stackTarget) {
        return stackSource.getItem() == stackTarget.getItem() && stackSource.getDisplayName().equals(stackTarget.getDisplayName());
    }
}

