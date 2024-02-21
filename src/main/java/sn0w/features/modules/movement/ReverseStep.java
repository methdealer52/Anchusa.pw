package sn0w.features.modules.movement;

import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class ReverseStep extends Module {
    private static ReverseStep INSTANCE = new ReverseStep();
    private final Setting<Boolean> twoBlocks = this.register(new Setting<Boolean>("2Blocks", Boolean.FALSE));

    public ReverseStep() {
        super("ReverseStep", "ReverseStep.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static ReverseStep getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReverseStep();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public int onUpdate() {
        if (fullNullCheck()) return 0;
        IBlockState touchingState = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ).down(2));
        IBlockState touchingState2 = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ).down(3));
        if (mc.player.isInLava() || mc.player.isInWater()) return 0;
        if (touchingState.getBlock() == Blocks.BEDROCK || touchingState.getBlock() == Blocks.OBSIDIAN) {
            if (mc.player.onGround) mc.player.motionY -= 1.0;
        } else if ((this.twoBlocks.getValue().booleanValue() && touchingState2.getBlock() == Blocks.BEDROCK || this.twoBlocks.getValue().booleanValue() && touchingState2.getBlock() == Blocks.OBSIDIAN) && ReverseStep.mc.player.onGround) {
            mc.player.motionY -= 1.0;
        }
        return 0;
    }
}

