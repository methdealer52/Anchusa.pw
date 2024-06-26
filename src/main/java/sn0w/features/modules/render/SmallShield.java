package sn0w.features.modules.render;

import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;

public class SmallShield
        extends Module {
    private static SmallShield INSTANCE = new SmallShield();
    public Setting<Float> offX = this.register(new Setting<Float>("OffHandX", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public Setting<Float> offY = this.register(new Setting<Float>("OffHandY", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public Setting<Float> mainX = this.register(new Setting<Float>("MainHandX", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public Setting<Float> mainY = this.register(new Setting<Float>("MainHandY", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));

    public SmallShield() {
        super("SmallShield", "Makes you offhand lower.", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static SmallShield getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new SmallShield();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

