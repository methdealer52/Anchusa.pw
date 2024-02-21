//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\jedav\Documents\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package sn0w.features.modules.render;

import sn0w.features.modules.*;
import sn0w.features.setting.*;
import java.awt.*;
import sn0w.event.events.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import sn0w.manager.*;
import sn0w.util.*;

public class HoleESP extends Module
{
    private final Setting<Boolean> renderOwn;
    private final Setting<Boolean> fov;
    private final Setting<Integer> range;
    private final Setting<Boolean> box;
    private final Setting<Boolean> gradientBox;
    private final Setting<Boolean> invertGradientBox;
    private final Setting<Boolean> outline;
    private final Setting<Boolean> gradientOutline;
    private final Setting<Boolean> invertGradientOutline;
    private final Setting<Boolean> separateHeight;
    private final Setting<Double> lineHeight;
    private final Setting<Boolean> wireframe;
    private final Setting<WireframeMode> wireframeMode;
    private final Setting<Double> height;
    private final Setting<Integer> boxAlpha;
    private final Setting<Float> lineWidth;
    private final Setting<Boolean> rainbow;
    private final Setting<Color> obbyColor;
    private final Setting<Color> brockColor;
    private final Setting<Boolean> customOutline;
    private final Setting<Color> obbyLineColor;
    private final Setting<Color> brockLineColor;

    public HoleESP() {
        super("HoleESP", "Shows safe spots near you.", Module.Category.RENDER, false, false, false);
        this.renderOwn = (Setting<Boolean>)this.register(new Setting("RenderOwn", true));
        this.fov = (Setting<Boolean>)this.register(new Setting("FovOnly", false));
        this.range = (Setting<Integer>)this.register(new Setting("Range", 5, 0, 25));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", true));
        this.gradientBox = (Setting<Boolean>)this.register(new Setting("FadeBox", false));
        this.invertGradientBox = (Setting<Boolean>)this.register(new Setting("InvertBoxFade", false));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", true));
        this.gradientOutline = (Setting<Boolean>)this.register(new Setting("FadeLine", false));
        this.invertGradientOutline = (Setting<Boolean>)this.register(new Setting("InvertLineFade", false));
        this.separateHeight = (Setting<Boolean>)this.register(new Setting("SeparateHeight", true));
        this.lineHeight = (Setting<Double>)this.register(new Setting("LineHeight", (-1.0), (-2.0), 2.0));
        this.wireframe = (Setting<Boolean>)this.register(new Setting("Wireframe", true));
        this.wireframeMode = (Setting<WireframeMode>)this.register(new Setting("Mode", WireframeMode.FLAT));
        this.height = (Setting<Double>)this.register(new Setting("Height", (-1.0), (-2.0), 5.0));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", 120, 0, 255));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", 1.0f, 0.1f, 5.0f, v -> this.outline.getValue(true) || this.wireframe.getValue(true)));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", false));
        this.obbyColor = (Setting<Color>)this.register(new Setting("ObbyColor", new Color(12721437)));
        this.brockColor = (Setting<Color>)this.register(new Setting("BedrockColor", new Color(917248)));
        this.customOutline = (Setting<Boolean>)this.register(new Setting("LineColor", false));
        this.obbyLineColor = (Setting<Color>)this.register(new Setting("ObbyLine", new Color(12721437)));
        this.brockLineColor = (Setting<Color>)this.register(new Setting("BedrockLine", new Color(917248)));
    }

    public void onRender3D(final Render3DEvent event) {
        assert HoleESP.mc.renderViewEntity != null;
        final Vec3i playerPos = new Vec3i(HoleESP.mc.renderViewEntity.posX, HoleESP.mc.renderViewEntity.posY, HoleESP.mc.renderViewEntity.posZ);
        for (int x = playerPos.getX() - this.range.getValue(true); x < playerPos.getX() + this.range.getValue(true); ++x) {
            for (int z = playerPos.getZ() - this.range.getValue(true); z < playerPos.getZ() + this.range.getValue(true); ++z) {
                for (int rangeY = 5, y = playerPos.getY() + rangeY; y > playerPos.getY() - rangeY; --y) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    final Color safeColor = this.rainbow.getValue(true) ? ColorManager.getRainbow() : this.brockColor.getValue(true);
                    final Color color = this.rainbow.getValue(true) ? ColorManager.getRainbow() : this.obbyColor.getValue(true);
                    final Color safecColor = this.brockLineColor.getValue(true);
                    final Color cColor = this.obbyLineColor.getValue(true);
                    if (HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && (!pos.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)) || this.renderOwn.getValue(true))) {
                        if (RotationManager.isInFov(pos) || !this.fov.getValue(true)) {
                            if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north(2)).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                                this.drawDoubles(true, pos, safeColor, this.customOutline.getValue(true), safecColor, this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true, this.height.getValue(true), ((boolean)this.separateHeight.getValue(true)) ? ((double)this.lineHeight.getValue(true)) : ((double)this.height.getValue(true)), this.gradientBox.getValue(true), this.gradientOutline.getValue(true), this.invertGradientBox.getValue(true), this.invertGradientOutline.getValue(true), 0, this.wireframe.getValue(true), this.wireframeMode.getValue(true) == WireframeMode.FLAT);
                            }
                            else if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR && (HoleESP.mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.north(2)).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.north(2)).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK)) {
                                this.drawDoubles(true, pos, color, this.customOutline.getValue(true), cColor, this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true, this.height.getValue(true), ((boolean)this.separateHeight.getValue(true)) ? ((double)this.lineHeight.getValue(true)) : ((double)this.height.getValue(true)), this.gradientBox.getValue(true), this.gradientOutline.getValue(true), this.invertGradientBox.getValue(true), this.invertGradientOutline.getValue(true), 0, this.wireframe.getValue(true), this.wireframeMode.getValue(true) == WireframeMode.FLAT);
                            }
                            if (HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.east().up()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east(2).down()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                                this.drawDoubles(false, pos, safeColor, this.customOutline.getValue(true), safecColor, this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true, this.height.getValue(true), ((boolean)this.separateHeight.getValue(true)) ? ((double)this.lineHeight.getValue(true)) : ((double)this.height.getValue(true)), this.gradientBox.getValue(true), this.gradientOutline.getValue(true), this.invertGradientBox.getValue(true), this.invertGradientOutline.getValue(true), 0, this.wireframe.getValue(true), this.wireframeMode.getValue(true) == WireframeMode.FLAT);
                            }
                            else if (HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.east().up()).getBlock() == Blocks.AIR && (HoleESP.mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN)) {
                                this.drawDoubles(false, pos, color, this.customOutline.getValue(true), cColor, this.lineWidth.getValue(true, this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true, this.height.getValue(true), ((boolean)this.separateHeight.getValue(true)) ? ((double)this.lineHeight.getValue(true)) : ((double)this.height.getValue(true)); this.gradientBox.getValue(true), this.gradientOutline.getValue(true), this.invertGradientBox.getValue(true), this.invertGradientOutline.getValue(true), 0, this.wireframe.getValue(true), this.wireframeMode.getValue(true) == WireframeMode.FLAT);
                            }
                            if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                                this.drawHoleESP(pos, safeColor, this.customOutline.getValue(true), safecColor, this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true, this.height.getValue(true), ((boolean)this.separateHeight.getValue(true)) ? ((double)this.lineHeight.getValue(true)) : ((double)this.height.getValue(true)), this.gradientBox.getValue(true), this.gradientOutline.getValue(true), this.invertGradientBox.getValue(true), this.invertGradientOutline.getValue(true), 0, this.wireframe.getValue(true), this.wireframeMode.getValue(true) == WireframeMode.FLAT);
                            }
                            else if (BlockUtil.isUnsafe(HoleESP.mc.world.getBlockState(pos.down()).getBlock()) && BlockUtil.isUnsafe(HoleESP.mc.world.getBlockState(pos.east()).getBlock()) && BlockUtil.isUnsafe(HoleESP.mc.world.getBlockState(pos.west()).getBlock()) && BlockUtil.isUnsafe(HoleESP.mc.world.getBlockState(pos.south()).getBlock())) {
                                if (BlockUtil.isUnsafe(HoleESP.mc.world.getBlockState(pos.north()).getBlock())) {
                                    this.drawHoleESP(pos, color, this.customOutline.getValue(true), cColor, this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true, this.height.getValue(true), ((boolean)this.separateHeight.getValue(true)) ? ((double)this.lineHeight.getValue(true)) : ((double)this.height.getValue(true)), this.gradientBox.getValue(true), this.gradientOutline.getValue(true), this.invertGradientBox.getValue(true), this.invertGradientOutline.getValue(true), 0, this.wireframe.getValue(true), this.wireframeMode.getValue(true) == WireframeMode.FLAT);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void drawDoubles(final boolean faceNorth, final BlockPos pos, final Color color, final boolean secondC, final Color secondColor, final float lineWidth, final boolean outline, final boolean box, final int boxAlpha, final boolean air, final double height, final double lineHeight, final boolean gradientBox, final boolean gradientOutline, final boolean invertGradientBox, final boolean invertGradientOutline, final int gradientAlpha, final boolean cross, final boolean flatCross) {
        this.drawHoleESP(pos, color, secondC, secondColor, lineWidth, outline, box, boxAlpha, air, height, lineHeight, gradientBox, gradientOutline, invertGradientBox, invertGradientOutline, gradientAlpha, cross, flatCross);
        this.drawHoleESP(faceNorth ? pos.north() : pos.east(), color, secondC, secondColor, lineWidth, outline, box, boxAlpha, air, height, lineHeight, gradientBox, gradientOutline, invertGradientBox, invertGradientOutline, gradientAlpha, cross, flatCross);
    }

    public void drawHoleESP(final BlockPos pos, final Color color, final boolean secondC, final Color secondColor, final float lineWidth, final boolean outline, final boolean box, final int boxAlpha, final boolean air, final double height, final double lineHeight, final boolean gradientBox, final boolean gradientOutline, final boolean invertGradientBox, final boolean invertGradientOutline, final int gradientAlpha, final boolean cross, final boolean flatCross) {
        if (box) {
            RenderUtil.drawBox(pos, ColorUtil.injectAlpha(color, boxAlpha), height, gradientBox, invertGradientBox, gradientAlpha);
        }
        if (outline) {
            RenderUtil.drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air, lineHeight, gradientOutline, invertGradientOutline, gradientAlpha, false);
        }
        if (cross) {
            RenderUtil.drawBlockWireframe(pos, secondC ? secondColor : color, lineWidth, flatCross);
        }
    }

    private enum WireframeMode
    {
        FLAT,
        FULL;
    }
}
