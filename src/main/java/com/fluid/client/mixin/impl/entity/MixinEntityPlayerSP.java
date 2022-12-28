package com.fluid.client.mixin.impl.entity;

import com.fluid.client.api.event.EventManager;
import com.fluid.client.api.event.impl.EventUpdate;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci) {
        EventManager.getPubSub().publish(new EventUpdate());
    }

}
