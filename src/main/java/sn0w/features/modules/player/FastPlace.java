package sn0w.features.modules.player;

import sn0w.features.modules.Module;
import sn0w.util.InventoryUtil;
import net.minecraft.item.ItemExpBottle;

public class FastPlace
        extends Module {
    public FastPlace() {
        super("FastPlace", "Fast everything.", Module.Category.PLAYER);
    }

    @Override
    public int onUpdate() {
        if (FastPlace.fullNullCheck()) {
            return 0;
        }
        if (InventoryUtil.holdingItem(ItemExpBottle.class)) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
        return 0;
    }
}
