//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\jedav\Documents\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package sn0w.features.modules.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sn0w.event.events.PacketEvent;
import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import sn0w.util.TextUtil;
import sn0w.util.Timer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatModifier extends Module
{
    public Setting<Suffix> suffix;
    public Setting<Boolean> clean;
    public Setting<Boolean> infinite;
    public Setting<Boolean> autoQMain;
    public Setting<TextUtil.Color> timeStamps;
    public Setting<Boolean> rainbowTimeStamps;
    public Setting<TextUtil.Color> bracket;
    public Setting<Boolean> space;
    public Setting<Boolean> all;
    public Setting<Boolean> qNotification;
    public Setting<Integer> qDelay;
    private final Timer timer;
    private static ChatModifier INSTANCE;

    public ChatModifier() {
        super("BetterChat", "Modifies your chat", Category.RENDER, true, false, false);
        this.suffix = (Setting<Suffix>)this.register(new Setting("Suffix", Suffix.NONE, "Your Suffix."));
        this.clean = (Setting<Boolean>)this.register(new Setting("Clean Chat", false, "Cleans your chat"));
        this.infinite = (Setting<Boolean>)this.register(new Setting("Infinite", false, "Makes your chat infinite."));
        this.autoQMain = (Setting<Boolean>)this.register(new Setting("Auto Q Main", false, "Spams AutoQMain"));
        this.timeStamps = (Setting<TextUtil.Color>)this.register(new Setting("Time", TextUtil.Color.NONE));
        this.rainbowTimeStamps = (Setting<Boolean>)this.register(new Setting("RainbowTimeStamps", false, v -> this.timeStamps.getValue(true) != TextUtil.Color.NONE));
        this.bracket = (Setting<TextUtil.Color>)this.register(new Setting("Bracket", TextUtil.Color.WHITE, v -> this.timeStamps.getValue(true) != TextUtil.Color.NONE));
        this.space = (Setting<Boolean>)this.register(new Setting("Space", true, v -> this.timeStamps.getValue(true) != TextUtil.Color.NONE));
        this.all = (Setting<Boolean>)this.register(new Setting("All", false, v -> this.timeStamps.getValue(true) != TextUtil.Color.NONE));
        this.qNotification = (Setting<Boolean>)this.register(new Setting("Q Notification", false, v -> this.autoQMain.getValue(true)));
        this.qDelay = (Setting<Integer>)this.register(new Setting("Q Delay", 9, 1, 90, v -> this.autoQMain.getValue(true)));
        this.timer = new Timer();
        this.setInstance();
    }

    private void setInstance() {
        ChatModifier.INSTANCE = this;
    }

    public static ChatModifier getInstance() {
        if (ChatModifier.INSTANCE == null) {
            ChatModifier.INSTANCE = new ChatModifier();
        }
        return ChatModifier.INSTANCE;
    }


    public String getTimeString(final String message) {
        final String date = new SimpleDateFormat("k:mm").format(new Date());
        if (this.rainbowTimeStamps.getValue(true)) {
            final String timeString = "<" + date + ">" + (this.space.getValue(true) ? " " : "");
            final StringBuilder builder = new StringBuilder(timeString);
            builder.insert(0, "�+");
            return builder.toString();
        }
        return ((this.bracket.getValue(true) == TextUtil.Color.NONE) ? "" : TextUtil.coloredString("<", this.bracket.getValue(true))) + TextUtil.coloredString(date, this.timeStamps.getValue(true)) + ((this.bracket.getValue(true) == TextUtil.Color.NONE) ? "" : TextUtil.coloredString(">", this.bracket.getValue(true))) + (this.space.getValue(true) ? " " : "") + "�r";
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            final CPacketChatMessage packet = (CPacketChatMessage)event.getPacket();
            String s = packet.getMessage();
            if (s.startsWith("/")) {
                return;
            }
            switch (this.suffix.getValue(true)) {
                case ANCHUSA: {
                    s += " | \\u200b\\ud83c\\udde6\\u200b\\u200b\\ud83c\\uddf3\\u200b\\u200b\\ud83c\\udde8\\u200b\\u200b\\ud83c\\udded\\u200b\\u200b\\ud83c\\uddfa\\u200b\\u200b\\ud83c\\uddf8\\u200b\\u200b\\ud83c\\udde6\\u200b";
                    break;

                }
            }
            if (s.length() >= 256) {
                s = s.substring(0, 256);
            }
            packet.message = s;
        }
    }

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && this.timeStamps.getValue(true) != TextUtil.Color.NONE && event.getPacket() instanceof SPacketChat) {
            if (!((SPacketChat)event.getPacket()).isSystem()) {
                return;
            }
            final String originalMessage = ((SPacketChat)event.getPacket()).chatComponent.getFormattedText();
            final String message = this.getTimeString(originalMessage) + originalMessage;
            ((SPacketChat)event.getPacket()).chatComponent = (ITextComponent)new TextComponentString(message);
        }
    }

    private boolean shouldSendMessage(final EntityPlayer player) {
        return player.dimension == 1 && this.timer.passedS(this.qDelay.getValue(true)) && player.getPosition().equals((Object)new Vec3i(0, 240, 0));
    }

    static {
        ChatModifier.INSTANCE = new ChatModifier();
    }

    public enum Suffix
    {
        NONE,
        ANCHUSA,

    }
}
