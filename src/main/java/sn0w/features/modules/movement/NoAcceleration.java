package sn0w.features.modules.movement;

import sn0w.event.events.MoveEvent;
import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import sn0w.util.EntityUtil;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class NoAcceleration extends Module {
    private Mode currentMode = Mode.Normal;
    public Setting<Mode> modeSetting = this.register(new Setting<>("Mode", Mode.Max));

    public NoAcceleration() {
        super("NoAcceleration", "Disables player acceleration", Category.MOVEMENT, true, false, false);
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        double[] speed = EntityUtil.forward(getSpeed(currentMode));
        event.setX(speed[0]);
        event.setZ(speed[1]);
    }

    private double getSpeed(Mode mode) {
        double defaultSpeed = 0.2873;
        switch (mode) {
            case Max:
            case Strict:
            case Normal:
            default:
                if (mc.player.isPotionActive(MobEffects.SPEED)) {
                    int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
                    defaultSpeed *= 1.0 + 0.2 * (amplifier + 1);
                }
                if (mc.player.isPotionActive(MobEffects.SLOWNESS)) {
                    int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SLOWNESS)).getAmplifier();
                    defaultSpeed /= 1.0 + 0.2 * (amplifier + 1);
                }
        }
        return defaultSpeed;
    }

    @Override
    public int onUpdate() {
        this.currentMode = modeSetting.getValue(true);
        return 0;
    }

    @Override
    public String getDisplayInfo() {
        return currentMode.toString();
    }

    public enum Mode {
        Max,
        Strict,
        Normal
    }
}
