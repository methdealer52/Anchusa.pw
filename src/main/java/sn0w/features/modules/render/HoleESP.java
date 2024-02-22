//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\jedav\Documents\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package sn0w.features.modules.render;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import sn0w.event.events.Render3DEvent;
import sn0w.features.modules.Module;
import sn0w.features.modules.client.ClickGui;
import sn0w.features.setting.Setting;
import sn0w.util.BlockUtil;
import sn0w.util.ColorUtil;
import sn0w.util.RenderUtil;

import java.awt.*;

public class HoleESP extends Module
{
    public Setting<Boolean> renderOwn;
    public Setting<Boolean> fov;
    public Setting<Boolean> rainbow;
    private final Setting<Integer> range;
    private final Setting<Integer> rangeY;
    public Setting<Boolean> box;
    public Setting<Boolean> gradientBox;
    public Setting<Boolean> invertGradientBox;
    public Setting<Boolean> outline;
    public Setting<Boolean> gradientOutline;
    public Setting<Boolean> invertGradientOutline;
    public Setting<Double> height;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha;
    private Setting<Integer> boxAlpha;
    private Setting<Float> lineWidth;
    public Setting<Boolean> safeColor;
    private Setting<Integer> safeRed;
    private Setting<Integer> safeGreen;
    private Setting<Integer> safeBlue;
    private Setting<Integer> safeAlpha;
    public Setting<Boolean> customOutline;
    private Setting<Integer> cRed;
    private Setting<Integer> cGreen;
    private Setting<Integer> cBlue;
    private Setting<Integer> cAlpha;
    private Setting<Integer> safecRed;
    private Setting<Integer> safecGreen;
    private Setting<Integer> safecBlue;
    private Setting<Integer> safecAlpha;
    private static HoleESP INSTANCE;
    private int currentAlpha;

    public HoleESP() {
        super("HoleESP", "Shows safe spots.", Module.Category.RENDER, false, false, false);
        this.renderOwn = (Setting<Boolean>)this.register(new Setting("RenderOwn", true));
        this.fov = (Setting<Boolean>)this.register(new Setting("InFov", true));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", false));
        this.range = (Setting<Integer>)this.register(new Setting("RangeX", 0, 0, 10));
        this.rangeY = (Setting<Integer>)this.register(new Setting("RangeY", 0, 0, 10));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", true));
        this.gradientBox = (Setting<Boolean>)this.register(new Setting("Gradient", false, v -> this.box.getValue(true)));
        this.invertGradientBox = (Setting<Boolean>)this.register(new Setting("ReverseGradient", false, v -> this.gradientBox.getValue(true)));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", true));
        this.gradientOutline = (Setting<Boolean>)this.register(new Setting("GradientOutline", false, v -> this.outline.getValue(true)));
        this.invertGradientOutline = (Setting<Boolean>)this.register(new Setting("ReverseOutline", false, v -> this.gradientOutline.getValue(true)));
        this.height = (Setting<Double>)this.register(new Setting("Height", 0.0, (-2.0), 2.0));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 0, 0, 255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 0, 0, 255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 255, 0, 255));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", 125, 0, 255, v -> this.box.getValue(true)));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", 1.0f, 0.1f, 5.0f, v -> this.outline.getValue(true)));
        this.safeColor = (Setting<Boolean>)this.register(new Setting("BedrockColor", false));
        this.safeRed = (Setting<Integer>)this.register(new Setting("BedrockRed", 0, 0, 255, v -> this.safeColor.getValue(true)));
        this.safeGreen = (Setting<Integer>)this.register(new Setting("BedrockGreen", 255, 0, 255, v -> this.safeColor.getValue(true)));
        this.safeBlue = (Setting<Integer>)this.register(new Setting("BedrockBlue", 0, 0, 255, v -> this.safeColor.getValue(true)));
        this.safeAlpha = (Setting<Integer>)this.register(new Setting("BedrockAlpha", 255, 0, 255, v -> this.safeColor.getValue(true)));
        this.customOutline = (Setting<Boolean>)this.register(new Setting("CustomLine", false, v -> this.outline.getValue(true)));
        this.cRed = (Setting<Integer>)this.register(new Setting("OL-Red", 0,0, 255, v -> this.customOutline.getValue(true) && this.outline.getValue(true)));
        this.cGreen = (Setting<Integer>)this.register(new Setting("OL-Green", 0, 0, 255, v -> this.customOutline.getValue(true) && this.outline.getValue(true)));
        this.cBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", 255, 0, 255, v -> this.customOutline.getValue(true) && this.outline.getValue(true)));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", 255, 0, 255, v -> this.customOutline.getValue(true) && this.outline.getValue(true)));
        this.safecRed = (Setting<Integer>)this.register(new Setting("OL-SafeRed", 0, 0, 255, v -> this.customOutline.getValue(true) && this.outline.getValue(true) && this.safeColor.getValue(true)));
        this.safecGreen = (Setting<Integer>)this.register(new Setting("OL-SafeGreen", 255, 0, 255, v -> this.customOutline.getValue(true) && this.outline.getValue(true) && this.safeColor.getValue(true)));
        this.safecBlue = (Setting<Integer>)this.register(new Setting("OL-SafeBlue", 0, 0, 255, v -> this.customOutline.getValue(true) && this.outline.getValue(true) && this.safeColor.getValue(true)));
        this.safecAlpha = (Setting<Integer>)this.register(new Setting("OL-SafeAlpha", 255, 0, 255, v -> this.customOutline.getValue(true) && this.outline.getValue(true) && this.safeColor.getValue(true)));
        this.currentAlpha = 0;
        this.setInstance();
    }

    private void setInstance() {
        HoleESP.INSTANCE = this;
    }

    public static HoleESP getInstance() {
        if (HoleESP.INSTANCE == null) {
            HoleESP.INSTANCE = new HoleESP();
        }
        return HoleESP.INSTANCE;
    }

    public void onRender3D(final Render3DEvent event) {
        assert HoleESP.mc.renderViewEntity != null;
        final Vec3i playerPos = new Vec3i(HoleESP.mc.renderViewEntity.posX, HoleESP.mc.renderViewEntity.posY, HoleESP.mc.renderViewEntity.posZ);
        for (int x = playerPos.getX() - this.range.getValue(true); x < playerPos.getX() + this.range.getValue(true); ++x) {
            for (int z = playerPos.getZ() - this.range.getValue(true); z < playerPos.getZ() + this.range.getValue(true); ++z) {
                for (int y = playerPos.getY() + this.rangeY.getValue(true); y > playerPos.getY() - this.rangeY.getValue(true); --y) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && (!pos.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)) || this.renderOwn.getValue(true)) && (BlockUtil.isPosInFov(pos) || !this.fov.getValue(true))) {
                        if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                            RenderUtil.drawBoxESP(pos, ((boolean)this.rainbow.getValue(true)) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue(true)) : new Color(this.safeRed.getValue(true), this.safeGreen.getValue(true), this.safeBlue.getValue(true), this.safeAlpha.getValue(true)), this.customOutline.getValue(true), new Color(this.safecRed.getValue(true), this.safecGreen.getValue(true), this.safecBlue.getValue(true), this.safecAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true, this.height.getValue(true), this.gradientBox.getValue(true), this.gradientOutline.getValue(true), this.invertGradientBox.getValue(true), this.invertGradientOutline.getValue(true), this.currentAlpha);
                        }
                        else if (BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.down()).getBlock()) && BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.east()).getBlock()) && BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.west()).getBlock()) && BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.south()).getBlock()) && BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.north()).getBlock())) {
                            RenderUtil.drawBoxESP(pos, ((boolean)this.rainbow.getValue(true)) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue(true)) : new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.alpha.getValue(true)), this.customOutline.getValue(true), new Color(this.cRed.getValue(true), this.cGreen.getValue(true), this.cBlue.getValue(true), this.cAlpha.getValue(true)), this.lineWidth.getValue(true), this.outline.getValue(true), this.box.getValue(true), this.boxAlpha.getValue(true), true, this.height.getValue(true), this.gradientBox.getValue(true), this.gradientOutline.getValue(true), this.invertGradientBox.getValue(true), this.invertGradientOutline.getValue(true), this.currentAlpha);
                        }
                    }
                }
            }
        }
    }

    static {
        HoleESP.INSTANCE = new HoleESP();
    }
}
