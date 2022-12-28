package com.fluid.client.api.setting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Setting {

    @Expose
    @SerializedName("name")
    private final String name;
    private final String description;

    public abstract void render(double x, double y, double width, double mouseX, double mouseY);
    public abstract void mouseClicked(double x, double y, double width, double mouseX, double mouseY, int button);
    public abstract double getHeight();

}
