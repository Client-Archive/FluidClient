package com.fluid.client.api.module.feature;

import com.fluid.client.api.module.Module;
import com.fluid.client.util.render.RenderUtil;
import com.fluid.client.util.render.font.FontManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Draggable extends Module {

    private float x = 4, y = 4;
    private float dragX, dragY;
    private float padding = 6;
    private boolean dragging;
    private int backgroundColor = 0x70000000;

    public String getText() {
        return "";
    }

    public float getWidth() {
        return FontManager.getFont("regular 20").getWidth(getText()) + padding;
    }

    public float getHeight() {
        return 12 + padding;
    }

    public void render() {
        RenderUtil.drawRect(getX(), getY(), getWidth(), getHeight(), getBackgroundColor());
        FontManager.getFont("regular 20").drawCenteredString(getText(), getX() + getWidth() / 2, getY() + getHeight() / 2 - FontManager.getFont("bold 20").getHeight(getText()) / 2 + 1, -1);
    }

}
