//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\jedav\Documents\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

//Decompiled by Procyon!

package sn0w.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import sn0w.features.modules.render.PopChams;

import java.awt.*;

public class StatusChamsUtil
{
    private static final Minecraft mc;
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double alphaFill;
    double alphaLine;

    public StatusChamsUtil(final EntityOtherPlayerMP player, final ModelPlayer playerModel, final Long startTime, final double alphaFill, final double alphaLine) {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.player = player;
        this.playerModel = playerModel;
        this.startTime = startTime;
        this.alphaFill = alphaFill;
        this.alphaLine = alphaFill;
    }

    @SubscribeEvent
    public void onRenderWorld(final RenderWorldLastEvent event) {
        if (this.player == null || StatusChamsUtil.mc.world == null || StatusChamsUtil.mc.player == null) {
            return;
        }
        GL11.glLineWidth(1.0f);
        final Color lineColorS = new Color((int)PopChams.rL.getValue(true), (int)PopChams.gL.getValue(true), (int)PopChams.bL.getValue(true), (int)PopChams.aL.getValue(true));
        final Color fillColorS = new Color((int)PopChams.rF.getValue(true), (int)PopChams.gF.getValue(true), (int)PopChams.bF.getValue(true), (int)PopChams.aF.getValue(true));
        int lineA = lineColorS.getAlpha();
        int fillA = fillColorS.getAlpha();
        final long time = System.currentTimeMillis() - this.startTime - ((Number)PopChams.fadestart.getValue(true)).longValue();
        if (System.currentTimeMillis() - this.startTime > ((Number)PopChams.fadestart.getValue(true)).longValue()) {
            double normal = this.normalize((double)time, 0.0, ((Number)PopChams.fadetime.getValue(true)).doubleValue());
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
                this.alphaFill -= (float)PopChams.fadetime.getValue(true);
            }
            final Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int)this.alphaFill);
            if (this.alphaLine > 1.0) {
                this.alphaLine -= (float)PopChams.fadetime.getValue(true);
            }
            final Color outlineFinal = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int)this.alphaLine);
            glColor(fillFinal);
            GL11.glPolygonMode(1032, 6914);
            renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float)this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
            glColor(outlineFinal);
            GL11.glPolygonMode(1032, 6913);
            renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float)this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
            GL11.glPolygonMode(1032, 6914);
            GL11.glPopAttrib();
            RenderUtil.releaseGL();
        }
    }

    double normalize(final double value, final double min, final double max) {
        return (value - min) / (max - min);
    }

    public static void renderEntity(final EntityLivingBase entity, final ModelBase modelBase, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (StatusChamsUtil.mc.getRenderManager() == null) {
            return;
        }
        final float partialTicks = StatusChamsUtil.mc.getRenderPartialTicks();
        final double x = entity.posX - StatusChamsUtil.mc.getRenderManager().viewerPosX;
        double y = entity.posY - StatusChamsUtil.mc.getRenderManager().viewerPosY;
        final double z = entity.posZ - StatusChamsUtil.mc.getRenderManager().viewerPosZ;
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
        final float f9 = prepareScale(entity, scale);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.setRotationAngles(limbSwing, limbSwingAmount, f8, entity.rotationYawHead, entity.rotationPitch, f9, (Entity)entity);
        modelBase.render((Entity)entity, limbSwing, limbSwingAmount, f8, entity.rotationYawHead, entity.rotationPitch, f9);
        GlStateManager.popMatrix();
    }

    public static void prepareTranslate(final EntityLivingBase entityIn, final double x, final double y, final double z) {
        renderLivingAt(x - StatusChamsUtil.mc.getRenderManager().viewerPosX, y - StatusChamsUtil.mc.getRenderManager().viewerPosY, z - StatusChamsUtil.mc.getRenderManager().viewerPosZ);
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

    static {
        mc = Minecraft.getMinecraft();
    }
}
