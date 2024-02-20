package sn0w.features.modules.render;


import sn0w.features.modules.Module;

import sn0w.*;
import sn0w.features.setting.Setting;

public class CrystalModifier extends Module
{
    public static Setting<Float> spin;
    public static Setting<Float> bounce;
    public static Setting<Float> scale;

    public CrystalModifier() {
        super("CrystalModifier", "modifies crystals", Module.Category.RENDER, true, false, false);
        this.register((Setting)CrystalModifier.spin);
        this.register((Setting)CrystalModifier.scale);
        this.register((Setting)CrystalModifier.bounce);
    }

    public static float[] getSpeed() {
        return OyVey.moduleManager.isModuleEnabled("CrystalModifier") ? new float[] { CrystalModifier.spin.getValue(true), CrystalModifier.bounce.getValue(true) } : new float[] { 1.0f, 1.0f };
    }

    static {
        CrystalModifier.spin = new Setting<Float>("Spin", 1.0f, 0.0f, 10.0f);
        CrystalModifier.bounce = new Setting<Float>("Bounce", 1.0f, 0.0f, 10.0f);
        CrystalModifier.scale = new Setting<Float>("Scale", 1.0f, 0.0f, 1.0f);
    }
}