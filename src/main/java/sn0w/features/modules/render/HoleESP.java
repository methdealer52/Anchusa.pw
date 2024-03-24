//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\jedav\Documents\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package sn0w.features.modules.render;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import sn0w.event.events.Render3DEvent;
import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import sn0w.util.BlockUtil;
import sn0w.util.RenderUtil;

import java.awt.*;

public class HoleESP extends Module
{
    private static HoleESP INSTANCE;
    private final Setting<Integer> range;
    private final Setting<Integer> rangeY;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Integer> boxAlpha;
    private final Setting<Float> lineWidth;
    private final Setting<Integer> safeRed;
    private final Setting<Integer> safeGreen;
    private final Setting<Integer> safeBlue;
    private final Setting<Integer> safeAlpha;
    private final Setting<RenderMode> renderMode;
    public Setting<Boolean> doubleHoles;
    public Setting<Boolean> fov;
    public Setting<Boolean> renderOwn;
    public Setting<Boolean> box;
    public Setting<Boolean> outline;
    private final Setting<Integer> cRed;
    private final Setting<Integer> cGreen;
    private final Setting<Integer> cBlue;
    private final Setting<Integer> cAlpha;
    private final Setting<Integer> safecRed;
    private final Setting<Integer> safecGreen;
    private final Setting<Integer> safecBlue;
    private final Setting<Integer> safecAlpha;

    public HoleESP() {
        super("HoleESP", "Shows safe spots.", Module.Category.RENDER, false, false, false);
        this.range = (Setting<Integer>)this.register(new Setting("RangeX", 0, 0, 10));
        this.rangeY = (Setting<Integer>)this.register(new Setting("RangeY", 0, 0, 10));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 0, 0, 255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 0, 0, 255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 255,0, 255));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", 125, 0, 255));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", 1.0f, 0.1f, 5.0f));
        this.safeRed = (Setting<Integer>)this.register(new Setting("BedrockRed", 0, 0, 255));
        this.safeGreen = (Setting<Integer>)this.register(new Setting("BedrockGreen", 255, 0, 255));
        this.safeBlue = (Setting<Integer>)this.register(new Setting("BedrockBlue", 0, 0, 255));
        this.safeAlpha = (Setting<Integer>)this.register(new Setting("BedrockAlpha", 255, 0, 255));
        this.renderMode = (Setting<RenderMode>)this.register(new Setting("RenderMode", RenderMode.Crossed));
        this.doubleHoles = (Setting<Boolean>)this.register(new Setting("DoubleHoles", true));
        this.fov = (Setting<Boolean>)this.register(new Setting("InFov", true));
        this.renderOwn = (Setting<Boolean>)this.register(new Setting("RenderOwn", true));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", true));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline",true));
        this.cRed = (Setting<Integer>)this.register(new Setting("OL-Red", 0, 0, 255, v -> this.outline.getValue(true)));
        this.cGreen = (Setting<Integer>)this.register(new Setting("OL-Green", 0, 0, 255, v -> this.outline.getValue(true)));
        this.cBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", 255, 0, 255, v -> this.outline.getValue(true)));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", 255, 0, 255, v -> this.outline.getValue(true)));
        this.safecRed = (Setting<Integer>)this.register(new Setting("OL-BedrockRed", 0, 0,255, v -> this.outline.getValue(true)));
        this.safecGreen = (Setting<Integer>)this.register(new Setting("OL-BedrockGreen", 255, 0, 255, v -> this.outline.getValue(true)));
        this.safecBlue = (Setting<Integer>)this.register(new Setting("OL-BedrockBlue", 0, 0, 255, v -> this.outline.getValue(true)));
        this.safecAlpha = (Setting<Integer>)this.register(new Setting("OL-BedrockAlpha", 255, 0, 255, v -> this.outline.getValue(true)));
        this.setInstance();
    }

    public static HoleESP getInstance() {
        if (HoleESP.INSTANCE == null) {
            HoleESP.INSTANCE = new HoleESP();
        }
        return HoleESP.INSTANCE;
    }

    private void setInstance() {
        HoleESP.INSTANCE = this;
    }

    public void onRender3D(final Render3DEvent event) {
        assert HoleESP.mc.renderViewEntity != null;
        final Vec3i playerPos = new Vec3i(HoleESP.mc.renderViewEntity.posX, HoleESP.mc.renderViewEntity.posY, HoleESP.mc.renderViewEntity.posZ);
        for (int x = playerPos.getX() - this.range.getValue(true); x < playerPos.getX() + this.range.getValue(true); ++x) {
            for (int z = playerPos.getZ() - this.range.getValue(true); z < playerPos.getZ() + this.range.getValue(true); ++z) {
                for (int y = playerPos.getY() + this.rangeY.getValue(true); y > playerPos.getY() - this.rangeY.getValue(true); --y) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && (!pos.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)) || this.renderOwn.getValue(true))) {
                        if (BlockUtil.isPosInFov(pos) || !this.fov.getValue(true)) {
                            if (this.doubleHoles.getValue(true)) {
                                if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north(2)).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                                    if (this.renderMode.getValue(true) == RenderMode.Full) {
                                        RenderUtil.drawBoxESP(pos, new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.outline.getValue(true), new Color(this.safecRed.getValue(true), this.safecGreen.getValue(true), this.safecBlue.getValue(true), this.safecAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                        RenderUtil.drawBoxESP(pos.north(), new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.outline.getValue(true), new Color(this.safecRed.getValue(true), this.safecGreen.getValue(true), this.safecBlue.getValue(true), this.safecAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                    }
                                    if (this.renderMode.getValue(true) == RenderMode.FullOffset) {
                                        RenderUtil.drawBoxESP(pos.down(), new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.outline.getValue(true), new Color(this.safecRed.getValue(true), this.safecGreen.getValue(true), this.safecBlue.getValue(true), this.safecAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                        RenderUtil.drawBoxESP(pos.north().down(), new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.outline.getValue(true), new Color(this.safecRed.getValue(true), this.safecGreen.getValue(true), this.safecBlue.getValue(true), this.safecAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                    }
                                    if (this.renderMode.getValue(true) == RenderMode.Crossed) {
                                        RenderUtil.drawCrossESP(pos, new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.lineWidth.getValue(true), true);
                                        RenderUtil.drawCrossESP(pos.north(), new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.lineWidth.getValue(true), true);
                                    }
                                }
                                else if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR && (HoleESP.mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.north(2)).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.north(2)).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK)) {
                                    if (this.renderMode.getValue(true) == RenderMode.Full) {
                                        RenderUtil.drawBoxESP(pos, new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.outline.getValue(true), new Color(this.cRed.getValue(true), this.cGreen.getValue(true), this.cBlue.getValue(true), this.alpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                        RenderUtil.drawBoxESP(pos.north(), new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.outline.getValue(true), new Color(this.cRed.getValue(true), this.cGreen.getValue(true), this.cBlue.getValue(true), this.alpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                    }
                                    if (this.renderMode.getValue(true) == RenderMode.FullOffset) {
                                        RenderUtil.drawBoxESP(pos.down(), new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.outline.getValue(true), new Color(this.cRed.getValue(true), this.cGreen.getValue(true), this.cBlue.getValue(true), this.alpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                        RenderUtil.drawBoxESP(pos.north().down(), new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.outline.getValue(true), new Color(this.cRed.getValue(true), this.cGreen.getValue(true), this.cBlue.getValue(true), this.alpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                    }
                                    if (this.renderMode.getValue(true) == RenderMode.Crossed) {
                                        RenderUtil.drawCrossESP(pos, new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.lineWidth.getValue(true), true);
                                        RenderUtil.drawCrossESP(pos.north(), new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.lineWidth.getValue(true), true);
                                    }
                                }
                                if (HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.east().up()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east(2).down()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                                    if (this.renderMode.getValue(true) == RenderMode.Full) {
                                        RenderUtil.drawBoxESP(pos, new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.outline.getValue(true), new Color(this.safecRed.getValue(true), this.safecGreen.getValue(true), this.safecBlue.getValue(true), this.safecAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                        RenderUtil.drawBoxESP(pos.east(), new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.outline.getValue(true), new Color(this.safecRed.getValue(true), this.safecGreen.getValue(true), this.safecBlue.getValue(true), this.safecAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                    }
                                    if (this.renderMode.getValue(true) == RenderMode.FullOffset) {
                                        RenderUtil.drawBoxESP(pos.down(), new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.outline.getValue(true), new Color(this.safecRed.getValue(true), this.safecGreen.getValue(true), this.safecBlue.getValue(true), this.safecAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                        RenderUtil.drawBoxESP(pos.east().down(), new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.outline.getValue(true), new Color(this.safecRed.getValue(true), this.safecGreen.getValue(true), this.safecBlue.getValue(true), this.safecAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                    }
                                    if (this.renderMode.getValue(true) == RenderMode.Crossed) {
                                        RenderUtil.drawCrossESP(pos, new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.lineWidth.getValue(true), true);
                                        RenderUtil.drawCrossESP(pos.east(), new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.lineWidth.getValue(true), true);
                                    }
                                }
                                else if (HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.east().up()).getBlock() == Blocks.AIR && (HoleESP.mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN)) {
                                    if (this.renderMode.getValue(true) == RenderMode.Full) {
                                        RenderUtil.drawBoxESP(pos, new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.outline.getValue(true), new Color(this.cRed.getValue(true), this.cGreen.getValue(true), this.cBlue.getValue(true), this.cAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                        RenderUtil.drawBoxESP(pos.east(), new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.outline.getValue(true), new Color(this.cRed.getValue(true), this.cGreen.getValue(true), this.cBlue.getValue(true), this.cAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                    }
                                    if (this.renderMode.getValue(true) == RenderMode.FullOffset) {
                                        RenderUtil.drawBoxESP(pos.down(), new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.outline.getValue(true), new Color(this.cRed.getValue(true), this.cGreen.getValue(true), this.cBlue.getValue(true), this.cAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                        RenderUtil.drawBoxESP(pos.east().down(), new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.outline.getValue(true), new Color(this.cRed.getValue(true), this.cGreen.getValue(true), this.cBlue.getValue(true), this.cAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                    }
                                    if (this.renderMode.getValue(true) == RenderMode.Crossed) {
                                        RenderUtil.drawCrossESP(pos, new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.lineWidth.getValue(true), true);
                                        RenderUtil.drawCrossESP(pos.east(), new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.lineWidth.getValue(true), true);
                                    }
                                }
                            }
                            if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                                if (this.renderMode.getValue(true) == RenderMode.Full) {
                                    RenderUtil.drawBoxESP(pos, new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.outline.getValue(true), new Color(this.safecRed.getValue(true), this.safecGreen.getValue(true), this.safecBlue.getValue(true), this.safecAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                }
                                if (this.renderMode.getValue(true) == RenderMode.FullOffset) {
                                    RenderUtil.drawBoxESP(pos.down(), new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.outline.getValue(true), new Color(this.safecRed.getValue(true), this.safecGreen.getValue(true), this.safecBlue.getValue(true), this.safecAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                }
                                if (this.renderMode.getValue(true) == RenderMode.Crossed) {
                                    RenderUtil.drawCrossESP(pos, new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.lineWidth.getValue(true), true);
                                }
                                if (this.renderMode.getValue(true) == RenderMode.Fluctuate) {
                                    RenderUtil.drawFlucESP(pos, new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.lineWidth.getValue(true), true);
                                }
                            }
                            else if (BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.down()).getBlock()) && BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.east()).getBlock()) && BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.west()).getBlock()) && BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.south()).getBlock())) {
                                if (BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.north()).getBlock())) {
                                    if (this.renderMode.getValue(true) == RenderMode.Full) {
                                        RenderUtil.drawBoxESP(pos, new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.outline.getValue(true), new Color(this.cRed.getValue(true), this.cGreen.getValue(true), this.cBlue.getValue(true), this.cAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                    }
                                    if (this.renderMode.getValue(true) == RenderMode.FullOffset) {
                                        RenderUtil.drawBoxESP(pos.down(), new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.outline.getValue(true), new Color(this.cRed.getValue(true), this.cGreen.getValue(true), this.cBlue.getValue(true), this.cAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true);
                                    }
                                    if (this.renderMode.getValue(true) == RenderMode.Crossed) {
                                        RenderUtil.drawCrossESP(pos, new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.safeAlpha.getValue(true)), this.lineWidth.getValue(true), true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static {
        HoleESP.INSTANCE = new HoleESP();
    }

    public enum RenderMode
    {
        Full,
        FullOffset,
        Crossed,
        CrossedFull,
        Fluctuate;
    }
}
