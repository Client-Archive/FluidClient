package com.fluid.client.mixin.impl.gui;

import com.fluid.client.api.event.EventManager;
import com.fluid.client.api.event.impl.EventRenderHUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiInGame {

    @Inject(method = "renderGameOverlay", at = @At("TAIL"))
    public void renderGameOverlay(CallbackInfo ci) {
        if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
            EventManager.getPubSub().publish(new EventRenderHUD());
        }
    }

}
