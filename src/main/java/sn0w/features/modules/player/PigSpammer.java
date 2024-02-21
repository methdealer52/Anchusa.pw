package sn0w.features.modules.player;

import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import sn0w.util.Timer;

import java.util.Random;

public class PigSpammer extends Module {

    private final String[] messages = {
            "LOL FUTURE BETA LOL LOL IM ON BETA",
            "LOL NAUGHTY 864 IS GETTING ON HIS MAIN LOL HES GETTING ON HIS MAIN",
            "THIS IS A FULL PK ENCOUNTER LOL!!",
            "LOL I JUST WATCHED THE HIDDEN TAPES LOL",
            "WHERES UR NEON OYVEY I DONT SEE UR NEON OYVEY LOL",
            "WHATS UR FUTURE UID LOL LETS COMPARE FUTURE UID",
            "LOL!!! HE CANNOT COMPARE FUTURE UID HE CANNOT AFFORD THE 20$ LOL",
            "LOL BITCOIN LOL I WON ALL THE BITCOIN",
            "LOL 1x1 LOL WINNER GETS THE ETH",
            "LOL WHY ARE U DODGING 1x1 WITH EMPERIUM LOL",
            "LOL MISTA IS HOPPING ON LOL THE HORSE IS HOPPING ON",
            "WANNA GO BAND 4 BAND SHITTER? LOL!",
            "I just flew 130 blocks like a butterfly thanks to DotGod.CC!",
            "XDD THIS IS AN DOTGOD.CC VICTORY LOL",
            "XD WANNA COMPARE Muffin UID LOL?"
    };

    private Setting<Integer> delay = register(new Setting<>("Seconds Delay", 7, 1, 20));
    private Timer timer = new Timer();

    public PigSpammer() {
        super("PigSpammer", "Automatically makes you spam shit bait", Category.PLAYER, true, false, false);
    }

    @Override
    public int onUpdate() {
        if (timer.passedS(delay.getValue(true))) {
            Random random = new Random();
            mc.player.sendChatMessage(messages[(random.nextInt(messages.length))]);
            timer.reset();
        }
        return 0;

    }

}