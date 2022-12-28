package com.fluid.client.util.render;

import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.Gui;

@UtilityClass
public class RenderUtil {

    public void drawRect(float x, float y, float width, float height, int color) {
        Gui.drawRect((int) x, (int) y, (int) (x + width), (int) (y + height), color);
    }

}
