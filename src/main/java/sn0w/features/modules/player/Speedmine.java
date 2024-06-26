package sn0w.features.modules.player;

import sn0w.OyVey;
import sn0w.event.events.BlockEvent;
import sn0w.event.events.Render3DEvent;
import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import sn0w.util.BlockUtil;
import sn0w.util.InventoryUtil;
import sn0w.util.RenderUtil;
import sn0w.util.Timer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class Speedmine
        extends Module {
    private static Speedmine INSTANCE = new Speedmine();
    private final Timer timer = new Timer();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.PACKET));
    public Setting<Float> damage = this.register(new Setting<Object>("Damage", Float.valueOf(0.7f), Float.valueOf(0.0f), Float.valueOf(1.0f), v -> this.mode.getValue(true) == Mode.DAMAGE));
    public Setting<Boolean> webSwitch = this.register(new Setting<Boolean>("WebSwitch", false));
    public Setting<Boolean> doubleBreak = this.register(new Setting<Boolean>("DoubleBreak", false));
    public Setting<Boolean> render = this.register(new Setting<Boolean>("Render", false));
    public Setting<Boolean> box = this.register(new Setting<Object>("Box", Boolean.valueOf(false), v -> this.render.getValue(true)));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(85), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue(true) != false && this.render.getValue(true) != false));
    public Setting<Boolean> outline = this.register(new Setting<Object>("Outline", Boolean.valueOf(true), v -> this.render.getValue(true)));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("Width", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue(true) != false && this.render.getValue(true) != false));
    public BlockPos currentPos;
    public IBlockState currentBlockState;

    public Speedmine() {
        super("Speedmine", "Speeds up mining.", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static Speedmine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Speedmine();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        if (this.currentPos != null) {
            if (!Speedmine.mc.world.getBlockState(this.currentPos).equals(this.currentBlockState) || Speedmine.mc.world.getBlockState(this.currentPos).getBlock() == Blocks.AIR) {
                this.currentPos = null;
                this.currentBlockState = null;
            } else if (this.webSwitch.getValue(true).booleanValue() && this.currentBlockState.getBlock() == Blocks.WEB && Speedmine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                InventoryUtil.switchToHotbarSlot(ItemSword.class, false);
            }
        }
    }

    @Override
    public int onUpdate() {
        if (fullNullCheck()) return 0;
        mc.playerController.blockHitDelay = 0;
        return 0;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.render.getValue(true).booleanValue() && this.currentPos != null && this.currentBlockState.getBlock() == Blocks.OBSIDIAN) {
            Color color = new Color(this.timer.passedMs((int) (2000.0f * OyVey.serverManager.getTpsFactor())) ? 0 : 255, this.timer.passedMs((int) (2000.0f * OyVey.serverManager.getTpsFactor())) ? 255 : 0, 0, 255);
            RenderUtil.drawBoxESP(this.currentPos, color, false, color, this.lineWidth.getValue(true).floatValue(), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), false);
        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (Speedmine.fullNullCheck()) return;
        if (event.getStage() == 3 && Speedmine.mc.playerController.curBlockDamageMP > 0.1f) mc.playerController.isHittingBlock = true;
        if (event.getStage() == 4) {
            BlockPos above;
            if (BlockUtil.canBreak(event.pos)) {
                Speedmine.mc.playerController.isHittingBlock = false;
                switch (this.mode.getValue(true)) {
                    case PACKET: {
                        if (this.currentPos == null) {
                            this.currentPos = event.pos;
                            this.currentBlockState = Speedmine.mc.world.getBlockState(this.currentPos);
                            this.timer.reset();
                        }
                        Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        event.setCanceled(true);
                        break;
                    }
                    case DAMAGE: {
                        if (!(Speedmine.mc.playerController.curBlockDamageMP >= this.damage.getValue(true).floatValue()))
                            break;
                        Speedmine.mc.playerController.curBlockDamageMP = 1.0f;
                        break;
                    }
                    case INSTANT: {
                        Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.playerController.onPlayerDestroyBlock(event.pos);
                        Speedmine.mc.world.setBlockToAir(event.pos);
                    }
                }
            }
            if (this.doubleBreak.getValue(true).booleanValue() && BlockUtil.canBreak(above = event.pos.add(0, 1, 0)) && Speedmine.mc.player.getDistance(above.getX(), above.getY(), above.getZ()) <= 5.0) {
                Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.facing));
                Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.facing));
                Speedmine.mc.playerController.onPlayerDestroyBlock(above);
                Speedmine.mc.world.setBlockToAir(above);
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    public enum Mode {
        PACKET,
        DAMAGE,
        INSTANT
    }
}

