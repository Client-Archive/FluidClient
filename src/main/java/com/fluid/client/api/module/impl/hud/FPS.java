package com.fluid.client.api.module.impl.hud;

import com.fluid.client.api.module.Category;
import com.fluid.client.api.module.ModuleData;
import com.fluid.client.api.module.feature.Draggable;
import net.minecraft.client.Minecraft;

@ModuleData(name = "FPS", description = "Displays your FPS on the HUD", category = Category.HUD, enabled = true)
public class FPS extends Draggable {

    @Override
    public String getText() {
        return Minecraft.getDebugFPS() + " FPS";
    }

}
