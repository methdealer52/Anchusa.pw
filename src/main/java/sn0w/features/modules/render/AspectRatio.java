package sn0w.features.modules.render;

import sn0w.features.modules.*;
import sn0w.features.setting.*;
import sn0w.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AspectRatio extends Module
{
    public Setting<Double> aspect;

    public AspectRatio() {
        super("AspectRatio", "Allows you to change your aspect ratio", Module.Category.RENDER, true, false, false);
        this.aspect = (Setting<Double>)this.register(new Setting("Ratio", (AspectRatio.mc.displayWidth / AspectRatio.mc.displayHeight + 0.0), 0.0, 3.0));
    }

    @SubscribeEvent
    public void onPerspectiveEvent(final PerspectiveEvent event) {
        event.setAspect(this.aspect.getValue(true).floatValue());
    }
}