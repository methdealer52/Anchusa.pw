package sn0w.features.modules.render;

import sn0w.event.events.Render3DEvent;
import sn0w.features.modules.Module;
import sn0w.features.modules.client.ClickGui;
import sn0w.features.setting.Setting;
import sn0w.util.ColorUtil;
import sn0w.util.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

public class BlockHighlight
        extends Module {
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private final Setting<Integer> cAlpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));

    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block u look at.", Module.Category.RENDER, true, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();
            RenderUtil.drawBlockOutline(blockpos, ClickGui.getInstance().rainbow.getValue(true) != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue(true)) : new Color(ClickGui.getInstance().red.getValue(true), ClickGui.getInstance().green.getValue(true), ClickGui.getInstance().blue.getValue(true), this.cAlpha.getValue(true)), this.lineWidth.getValue(true).floatValue(), false);
        }
    }
}

