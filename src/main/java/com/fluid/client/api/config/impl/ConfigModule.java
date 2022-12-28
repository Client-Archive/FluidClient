package com.fluid.client.api.config.impl;

import com.fluid.client.api.setting.Setting;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class ConfigModule implements Serializable {

    @Expose
    @SerializedName("name")
    private final String name;

    @Expose
    @SerializedName("enabled")
    private final boolean enabled;

    @Expose
    @SerializedName("key")
    private final int key;

    @Expose
    @SerializedName("settings")
    private final List<Setting> settings;

}
