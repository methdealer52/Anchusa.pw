package sn0w.features.modules.client;

import sn0w.OyVey;
import sn0w.event.events.ClientEvent;
import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import sn0w.util.ColorUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Colors
        extends Module {
    public static Colors INSTANCE;
    public Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", Boolean.valueOf(false), "Rainbow colors."));
    public Setting<Integer> rainbowSpeed = this.register(new Setting<Object>("Speed", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbow.getValue(true)));
    public Setting<Integer> rainbowSaturation = this.register(new Setting<Object>("Saturation", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue(true)));
    public Setting<Integer> rainbowBrightness = this.register(new Setting<Object>("Brightness", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue(true)));
    public Setting<Integer> rainbowHue = this.register(new Setting<Object>("Delay", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue(true)));
    public Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue(true) == false));
    public Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(200), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue(true) == false));
    public Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue(true) == false));
    public Setting<Integer> hoverAlpha = this.register(new Setting<Integer>("Alpha", 180, 0, 255, v -> this.rainbow.getValue(true) == false));
    public float hue;
    public Map<Integer, Integer> colorHeightMap = new HashMap<Integer, Integer>();

    public Colors() {
        super("Colors", "Universal colors.", Category.CLIENT, true, false, true);
        this.setInstance();
    }

    public static Colors getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Colors();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            OyVey.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }

    @Override
    public void onLoad() {
        OyVey.colorManager.setColor(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.hoverAlpha.getValue(true));
    }

    @Override
    public void onTick ( ) {
        int colorSpeed = 101 - this.rainbowSpeed.getValue (true);
        float tempHue = this.hue = (float) ( System.currentTimeMillis ( ) % ( 360L * colorSpeed ) ) / ( 360.0f * (float) colorSpeed );
        for (int i = 0; i <= 510; ++ i) {
            this.colorHeightMap.put ( i , Color.HSBtoRGB ( tempHue , (float) this.rainbowSaturation.getValue (true) / 255.0f , (float) this.rainbowBrightness.getValue (true) / 255.0f ) );
            tempHue += 0.0013071896f;
        }
    }


    public int getCurrentColorHex() {
        if (this.rainbow.getValue(true).booleanValue()) {
            return Color.HSBtoRGB(this.hue, (float) this.rainbowSaturation.getValue(true).intValue() / 255.0f, (float) this.rainbowBrightness.getValue(true).intValue() / 255.0f);
        }
        return ColorUtil.toARGB(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.hoverAlpha.getValue(true));
    }

    public Color getCurrentColor() {
        if (this.rainbow.getValue(true).booleanValue()) {
            return Color.getHSBColor(this.hue, (float) this.rainbowSaturation.getValue(true).intValue() / 255.0f, (float) this.rainbowBrightness.getValue(true).intValue() / 255.0f);
        }
        return new Color(this.red.getValue(true), this.green.getValue(true), this.blue.getValue(true), this.hoverAlpha.getValue(true));
    }
}

