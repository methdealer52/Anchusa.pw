package sn0w.features.modules.combat;

import sn0w.event.events.UpdateWalkingPlayerEvent;
import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import sn0w.util.BlockUtil;
import sn0w.util.EntityUtil;
import sn0w.util.InventoryUtil;
import sn0w.util.Timer;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class Selftrap
        extends Module {
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("BlocksPerTick", 8, 1, 20));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 50, 0, 250));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Integer> disableTime = this.register(new Setting<Integer>("DisableTime", 200, 50, 300));
    private final Setting<Boolean> disable = this.register(new Setting<Boolean>("AutoDisable", true));
    private final Setting<Boolean> packet = this.register(new Setting<Boolean>("PacketPlace", false));
    private final Timer offTimer = new Timer();
    private final Timer timer = new Timer();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private final Timer retryTimer = new Timer();
    private int blocksThisTick = 0;
    private boolean isSneaking;
    private boolean hasOffhand = false;

    public Selftrap() {
        super("Selftrap", "Lure your enemies in!", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (Selftrap.fullNullCheck()) {
            this.disable();
        }
        this.offTimer.reset();
    }

    @Override
    public void onTick() {
        if (this.isOn() && (this.blocksPerTick.getValue(true) != 1 || !this.rotate.getValue(true).booleanValue())) {
            this.doHoleFill();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (this.isOn() && event.getStage() == 0 && this.blocksPerTick.getValue(true) == 1 && this.rotate.getValue(true).booleanValue()) {
            this.doHoleFill();
        }
    }

    @Override
    public void onDisable() {
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.retries.clear();
        this.hasOffhand = false;
    }

    private void doHoleFill() {
        if (this.check()) {
            return;
        }
        for (BlockPos position : this.getPositions()) {
            int placeability = BlockUtil.isPositionPlaceable(position, false);
            if (placeability == 1 && (this.retries.get(position) == null || this.retries.get(position) < 4)) {
                this.placeBlock(position);
                this.retries.put(position, this.retries.get(position) == null ? 1 : this.retries.get(position) + 1);
            }
            if (placeability != 3) continue;
            this.placeBlock(position);
        }
    }

    private List<BlockPos> getPositions() {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        positions.add(new BlockPos(Selftrap.mc.player.posX, Selftrap.mc.player.posY + 2.0, Selftrap.mc.player.posZ));
        int placeability = BlockUtil.isPositionPlaceable(positions.get(0), false);
        switch (placeability) {
            case 0: {
                return new ArrayList<BlockPos>();
            }
            case 3: {
                return positions;
            }
            case 1: {
                if (BlockUtil.isPositionPlaceable(positions.get(0), false, false) == 3) {
                    return positions;
                }
            }
            case 2: {
                positions.add(new BlockPos(Selftrap.mc.player.posX + 1.0, Selftrap.mc.player.posY + 1.0, Selftrap.mc.player.posZ));
                positions.add(new BlockPos(Selftrap.mc.player.posX + 1.0, Selftrap.mc.player.posY + 2.0, Selftrap.mc.player.posZ));
            }
        }
        positions.sort(Comparator.comparingDouble(Vec3i::getY));
        return positions;
    }

    private void placeBlock(BlockPos pos) {
        if (this.blocksThisTick < this.blocksPerTick.getValue(true)) {
            boolean smartRotate = this.blocksPerTick.getValue(true) == 1 && this.rotate.getValue(true) != false;
            int originalSlot = Selftrap.mc.player.inventory.currentItem;
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            Selftrap.mc.player.inventory.currentItem = obbySlot == -1 ? eChestSot : obbySlot;
            Selftrap.mc.playerController.updateController();
            this.isSneaking = smartRotate ? BlockUtil.placeBlockSmartRotate(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, this.packet.getValue(true), this.isSneaking) : BlockUtil.placeBlock(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(true), this.packet.getValue(true), this.isSneaking);
            Selftrap.mc.player.inventory.currentItem = originalSlot;
            Selftrap.mc.playerController.updateController();
            this.timer.reset();
            ++this.blocksThisTick;
        }
    }

    private boolean check() {
        if (Selftrap.fullNullCheck()) {
            this.disable();
            return true;
        }
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            this.toggle();
        }
        this.blocksThisTick = 0;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (this.retryTimer.passedMs(2000L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (!EntityUtil.isSafe(Selftrap.mc.player)) {
            this.offTimer.reset();
            return true;
        }
        if (this.disable.getValue(true).booleanValue() && this.offTimer.passedMs(this.disableTime.getValue(true).intValue())) {
            this.disable();
            return true;
        }
        return !this.timer.passedMs(this.delay.getValue(true).intValue());
    }
}

