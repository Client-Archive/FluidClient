package com.fluid.client.api.setting.impl;

import com.fluid.client.api.setting.Setting;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class BooleanSetting extends Setting {

    @Expose
    @SerializedName("enabled")
    private boolean enabled;

    public BooleanSetting(String name, String description, boolean enabled) {
        super(name, description);
        this.enabled = enabled;
    }

    @Override
    public void render(double x, double y, double width, double mouseX, double mouseY) {

    }

    @Override
    public void mouseClicked(double x, double y, double width, double mouseX, double mouseY, int button) {

    }

    @Override
    public double getHeight() {
        return 20;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }

}
