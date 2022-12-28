package com.fluid.client.mixin.impl;

import com.fluid.client.Fluid;
import com.fluid.client.bridge.BridgeManager;
import com.fluid.client.bridge.bridges.MinecraftBridge;
import com.fluid.client.api.event.EventManager;
import com.fluid.client.api.event.impl.EventKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements MinecraftBridge {

    @Shadow public int displayWidth;
    @Shadow public int displayHeight;
    @Shadow public Session session;
    @Shadow public Timer timer;

    @Shadow
    public abstract boolean isFullScreen();

    @Shadow public GuiIngame ingameGUI;

    @Shadow @Final public File mcDataDir;

    // set the MinecraftBridge implementation in the BridgeManager.
    @Inject(method = "run", at = @At("HEAD"))
    public void impl$run(CallbackInfo ci) {
        BridgeManager.setMinecraftBridge(this);
    }

    @Inject(method = "startGame", at = @At("TAIL"))
    public void startGame$start(CallbackInfo ci) {
        Fluid.getInstance().start();
    }

    @Inject(method = "shutdownMinecraftApplet", at = @At("HEAD"))
    public void shutdownMinecraftApplet(CallbackInfo ci) {
        Fluid.getInstance().stop();
    }

    @Inject(method = "dispatchKeypresses", at = @At("HEAD"))
    public void dispatchKeypresses(CallbackInfo ci) {
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiControls) || ((GuiControls) Minecraft.getMinecraft().currentScreen).time <= Minecraft.getSystemTime() - 20L) {
            int key = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() : Keyboard.getEventKey();

            if (!Keyboard.isRepeatEvent() && key != 0) {
                if (Keyboard.getEventKeyState()) {
                    EventManager.getPubSub().publish(new EventKey(key));
                }
            }
        }
    }

    @Override
    public boolean bridge$isFullScreen() {
        return isFullScreen();
    }

    @Override
    public void bridge$setSession(Session session) {
        this.session = session;
    }

    @Override
    public Timer bridge$getTimer() { return this.timer; }

    @Override
    public Session bridge$getSession() { return this.session; }

    @Overwrite
    public int getLimitFramerate() {
        return Minecraft.getMinecraft().theWorld == null && Minecraft.getMinecraft().currentScreen == null ? 120 : Minecraft.getMinecraft().gameSettings.limitFramerate;
    }

}
