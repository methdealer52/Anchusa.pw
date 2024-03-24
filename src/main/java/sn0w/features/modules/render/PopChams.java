//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\jedav\Documents\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package sn0w.features.modules.render;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import sn0w.event.events.PacketEvent;
import sn0w.features.modules.Module;
import sn0w.features.setting.Setting;
import sn0w.util.RenderUtil;
import sn0w.util.StatusChamsUtil;

import java.awt.*;

public class PopChams extends Module
{
    public static Setting<Boolean> self;
    public static Setting<Boolean> travel;
    public static Setting<Integer> rL;
    public static Setting<Integer> gL;
    public static Setting<Integer> bL;
    public static Setting<Integer> aL;
    public static Setting<Integer> rF;
    public static Setting<Integer> gF;
    public static Setting<Integer> bF;
    public static Setting<Integer> aF;
    public static Setting<Integer> fadestart;
    public static Setting<Float> fadetime;
    public static Setting<Boolean> onlyOneEsp;
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double alphaFill;
    double alphaLine;

    public PopChams() {
        super("PopChams", "Renders a Glowing fakeplayer in the exact location where your enemy popped", Module.Category.RENDER, true, false, false);
        PopChams.self = (Setting<Boolean>)this.register(new Setting("Render Own Pops", true));
        PopChams.travel = (Setting<Boolean>)this.register(new Setting("Travel", true));
        PopChams.rL = (Setting<Integer>)this.register(new Setting("Outline Red", 30, 0, 255));
        PopChams.gL = (Setting<Integer>)this.register(new Setting("Outline Green", 167, 0, 255));
        PopChams.bL = (Setting<Integer>)this.register(new Setting("Outline Blue", 255, 0, 255));
        PopChams.aL = (Setting<Integer>)this.register(new Setting("Outline Alpha", 255, 0, 255));
        PopChams.rF = (Setting<Integer>)this.register(new Setting("Fill Red", 30, 0, 255));
        PopChams.gF = (Setting<Integer>)this.register(new Setting("Fill Green", 167, 0, 255));
        PopChams.bF = (Setting<Integer>)this.register(new Setting("Fill Blue", 255, 0, 255));
        PopChams.aF = (Setting<Integer>)this.register(new Setting("Fill Alpha", 140, 0, 255));
        PopChams.fadestart = (Setting<Integer>)this.register(new Setting("Fade Start", 0, 0, 255));
        PopChams.fadetime = (Setting<Float>)this.register(new Setting("Fade Time", 0.5f, 0.0f, 2.0f));
        PopChams.onlyOneEsp = (Setting<Boolean>)this.register(new Setting("Only Render One", true));
    }

    @SubscribeEvent
    public void onUpdate(final PacketEvent.Receive event) {
        final SPacketEntityStatus packet;
        if (event.getPacket() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus)event.getPacket()).getOpCode() == 35 && packet.getEntity((World)PopChams.mc.world) != null && (PopChams.self.getValue(true) || packet.getEntity((World)PopChams.mc.world).getEntityId() != PopChams.mc.player.getEntityId())) {
            final GameProfile profile = new GameProfile(PopChams.mc.player.getUniqueID(), "");
            (this.player = new EntityOtherPlayerMP((World)PopChams.mc.world, profile)).copyLocationAndAnglesFrom(packet.getEntity((World)PopChams.mc.world));
            this.playerModel = new ModelPlayer(0.0f, false);
            this.startTime = System.currentTimeMillis();
            this.playerModel.bipedHead.showModel = false;
            this.playerModel.bipedBody.showModel = false;
            this.playerModel.bipedLeftArmwear.showModel = false;
            this.playerModel.bipedLeftLegwear.showModel = false;
            this.playerModel.bipedRightArmwear.showModel = false;
            this.playerModel.bipedRightLegwear.showModel = false;
            this.alphaFill = PopChams.aF.getValue(true);
            this.alphaLine = PopChams.aL.getValue(true);
            if (!PopChams.onlyOneEsp.getValue(true)) {
                final StatusChamsUtil statusChamsUtil = new StatusChamsUtil(this.player, this.playerModel, this.startTime, this.alphaFill, this.alphaLine);
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(final RenderWorldLastEvent event) {
        if (PopChams.onlyOneEsp.getValue(true)) {
            if (this.player == null || PopChams.mc.world == null || PopChams.mc.player == null) {
                return;
            }
            GL11.glLineWidth(1.0f);
            final Color lineColorS = new Color(PopChams.rL.getValue(true), PopChams.gL.getValue(true), PopChams.bL.getValue(true), PopChams.aL.getValue(true));
            final Color fillColorS = new Color(PopChams.rF.getValue(true), PopChams.gF.getValue(true), PopChams.bF.getValue(true), PopChams.aF.getValue(true));
            int lineA = lineColorS.getAlpha();
            int fillA = fillColorS.getAlpha();
            final long time = System.currentTimeMillis() - this.startTime - PopChams.fadestart.getValue(true).longValue();
            if (System.currentTimeMillis() - this.startTime > PopChams.fadestart.getValue(true).longValue()) {
                double normal = this.normalize((double)time, 0.0, PopChams.fadetime.getValue(true).doubleValue());
                normal = MathHelper.clamp(normal, 0.0, 1.0);
                normal = -normal + 1.0;
                lineA *= (int)normal;
                fillA *= (int)normal;
            }
            final Color lineColor = newAlpha(lineColorS, lineA);
            final Color fillColor = newAlpha(fillColorS, fillA);
            if (this.player != null && this.playerModel != null) {
                RenderUtil.prepareGL();
                GL11.glPushAttrib(1048575);
                GL11.glEnable(2881);
                GL11.glEnable(2848);
                if (this.alphaFill > 1.0) {
                    this.alphaFill -= PopChams.fadetime.getValue(true);
                }
                final Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int)this.alphaFill);
                if (this.alphaLine > 1.0) {
                    this.alphaLine -= PopChams.fadetime.getValue(true);
                }
                final Color outlineFinal = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int)this.alphaLine);
                glColor(fillFinal);
                GL11.glPolygonMode(1032, 6914);
                renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float)this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1);
                glColor(outlineFinal);
                GL11.glPolygonMode(1032, 6913);
                renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float)this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1);
                GL11.glPolygonMode(1032, 6914);
                GL11.glPopAttrib();
                RenderUtil.releaseGL();
            }
        }
    }

    double normalize(final double value, final double min, final double max) {
        return (value - min) / (max - min);
    }

    public static void renderEntity(final EntityLivingBase entity, final ModelBase modelBase, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final int scale) {
        if (PopChams.mc.getRenderManager() == null) {
            return;
        }
        final float partialTicks = PopChams.mc.getRenderPartialTicks();
        final double x = entity.posX - PopChams.mc.getRenderManager().viewerPosX;
        double y = entity.posY - PopChams.mc.getRenderManager().viewerPosY;
        final double z = entity.posZ - PopChams.mc.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        if (entity.isSneaking()) {
            y -= 0.125;
        }
        final float interpolateRotation = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        final float interpolateRotation2 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        final float rotationInterp = interpolateRotation2 - interpolateRotation;
        final float renderPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        renderLivingAt(x, y, z);
        final float f8 = handleRotationFloat(entity, partialTicks);
        prepareRotations(entity);
        final float f9 = prepareScale(entity, (float)scale);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.setRotationAngles(limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9, (Entity)entity);
        modelBase.render((Entity)entity, limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9);
        GlStateManager.popMatrix();
    }

    public static void prepareTranslate(final EntityLivingBase entityIn, final double x, final double y, final double z) {
        renderLivingAt(x - PopChams.mc.getRenderManager().viewerPosX, y - PopChams.mc.getRenderManager().viewerPosY, z - PopChams.mc.getRenderManager().viewerPosZ);
    }

    public static void renderLivingAt(final double x, final double y, final double z) {
        GlStateManager.translate((float)x, (float)y, (float)z);
    }

    public static float prepareScale(final EntityLivingBase entity, final float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        final double widthX = entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX;
        final double widthZ = entity.getRenderBoundingBox().maxZ - entity.getRenderBoundingBox().minZ;
        GlStateManager.scale(scale + widthX, (double)(scale * entity.height), scale + widthZ);
        final float f = 0.0625f;
        GlStateManager.translate(0.0f, -1.501f, 0.0f);
        return 0.0625f;
    }

    public static void prepareRotations(final EntityLivingBase entityLivingBase) {
        GlStateManager.rotate(180.0f - entityLivingBase.rotationYaw, 0.0f, 1.0f, 0.0f);
    }

    public static float interpolateRotation(final float prevYawOffset, final float yawOffset, final float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0f; f += 360.0f) {}
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return prevYawOffset + partialTicks * f;
    }

    public static Color newAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }

    public static float handleRotationFloat(final EntityLivingBase livingBase, final float partialTicks) {
        return 0.0f;
    }
}
