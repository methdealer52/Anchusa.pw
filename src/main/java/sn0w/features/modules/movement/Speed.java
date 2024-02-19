package sn0w.features.modules.movement;

import sn0w.features.modules.Module;

public class Speed extends Module {
    public Speed() {
        super("Speed", "Speed.", Module.Category.MOVEMENT);
    }

    @Override
    public String getDisplayInfo() {
        return "Strafe";
    }
}

