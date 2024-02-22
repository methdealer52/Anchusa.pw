package sn0w.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;
import sn0w.OyVey;
import sn0w.event.events.Render2DEvent;
import sn0w.event.events.Render3DEvent;
import sn0w.features.Feature;
import sn0w.features.gui.OyVeyGui;
import sn0w.features.modules.Module;
import sn0w.features.modules.client.ClickGui;
import sn0w.features.modules.client.FontMod;
import sn0w.features.modules.client.HUD;
import sn0w.features.modules.combat.Burrow;
import sn0w.features.modules.exploit.Clip;
import sn0w.features.modules.movement.NoAcceleration;
import sn0w.features.modules.player.ChestSwap;
import sn0w.features.modules.player.PigSpammer;
import sn0w.features.modules.player.SilentXP;
import sn0w.features.modules.render.CrystalModifier;
import sn0w.features.modules.render.HoleESP;
import sn0w.features.modules.render.KillEffects;
import sn0w.util.Util;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ModuleManager
        extends Feature {
    public ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<String> sortedModulesABC = new ArrayList<String>();
    public Animation animationThread;

    public void init() {
        this.modules.add(new ClickGui());
        this.modules.add(new FontMod());
        this.modules.add(new HUD());
        this.modules.add(new KillEffects());
        this.modules.add(new Clip());
        this.modules.add(new PigSpammer());
        this.modules.add(new ChestSwap());
        this.modules.add(new SilentXP());
        this.modules.add(new CrystalModifier());
        this.modules.add(new NoAcceleration());
        this.modules.add(new Burrow());
        this.modules.add(new HoleESP());
    }

    public Module getModuleByName(String name) {
        Optional<Module> optionalModule = modules.stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findFirst();

        return optionalModule.orElse(null);
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        Optional<T> optionalModule = this.modules.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst();

        return optionalModule.orElse(null);
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) module.enable();
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) module.disable();
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) module.disable();
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        Optional<Module> optionalModule = modules.stream()
                .filter(module -> module.getDisplayName().equalsIgnoreCase(displayName))
                .findFirst();

        return optionalModule.orElse(null);
    }

    public ArrayList<Module> getEnabledModules() {
        return modules.stream()
                .filter(Feature::isEnabled)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<String> getEnabledModulesName() {
        return this.modules.stream()
                .filter(module -> module.isEnabled() && module.isDrawn())
                .map(Module::getFullArrayString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        return this.modules.stream()
                .filter(module -> module.getCategory() == category)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream()
                .filter(Module::listening)
                .forEach(((EventBus) MinecraftForge.EVENT_BUS)::register);

        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream()
                .filter(Feature::isEnabled)
                .forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream()
                .filter(Feature::isEnabled)
                .forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().
                filter(Feature::isEnabled)
                .forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream()
                .filter(Feature::isEnabled)
                .forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream()
                .filter(Module::isDrawn)
                .sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1)))
                .collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        this.modules.forEach(module -> module.enabled.setValue(false));
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof OyVeyGui) return;
        this.modules.stream()
                .filter(module -> module.getBind().getKey() == eventKey)
                .forEach(Module::toggle);
    }

    private class Animation
            extends Thread {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;

        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue(true) == HUD.RenderingMode.Length) {
                for (Module module : ModuleManager.this.sortedModules) {
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue(true).floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue(true).floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue(true) != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue(true) == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            } else {
                for (String e : ModuleManager.this.sortedModulesABC) {
                    Module module = OyVey.moduleManager.getModuleByName(e);
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue(true).floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue(true).floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue(true) != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue(true) == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            }
        }

        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}

