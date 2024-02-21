package sn0w.features.modules.movement;

import sn0w.features.modules.Module;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class NoVoid extends Module {
    public NoVoid() {
        super("NoVoid", "Glitches you up from void.", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) return 0;
        if (!mc.player.noClip && mc.player.posY <= 0.0) {
            RayTraceResult trace = mc.world.rayTraceBlocks(mc.player.getPositionVector(), new Vec3d(mc.player.posX, 0.0, mc.player.posZ), false, false, false);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) return 0;
            mc.player.setVelocity(0.0, 0.0, 0.0);
            if (mc.player.getRidingEntity() != null) mc.player.getRidingEntity().setVelocity(0.0, 0.0, 0.0);
        }
        return 0;
    }
}

