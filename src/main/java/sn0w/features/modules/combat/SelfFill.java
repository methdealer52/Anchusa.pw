package sn0w.features.modules.combat;

import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import sn0w.util.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class SelfFill
        extends Module {
    private final Setting<Boolean> packet = this.register(new Setting<Boolean>("PacketPlace", Boolean.FALSE));

    public SelfFill() {
        super("SelfFill", "SelfFills yourself in a hole.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        SelfFill.mc.player.jump();
        SelfFill.mc.player.jump();
    }

    @Override
    public int onUpdate() {
        BlockPos pos = new BlockPos(SelfFill.mc.player.posX, SelfFill.mc.player.posY, SelfFill.mc.player.posZ);
        if (SelfFill.mc.world.getBlockState(pos.down()).getBlock() == Blocks.AIR && BlockUtil.isPositionPlaceable(pos.down(), false) == 3) {
            BlockUtil.placeBlock(pos.down(), EnumHand.MAIN_HAND, false, this.packet.getValue(true), false);
        }
        if (SelfFill.mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN) {
            SelfFill.mc.player.connection.sendPacket(new CPacketPlayer.Position(SelfFill.mc.player.posX, SelfFill.mc.player.posY - 1.3, SelfFill.mc.player.posZ, false));
            SelfFill.mc.player.setPosition(SelfFill.mc.player.posX, SelfFill.mc.player.posY - 1.3, SelfFill.mc.player.posZ);
            this.toggle();
        }
        return 0;
    }
}

