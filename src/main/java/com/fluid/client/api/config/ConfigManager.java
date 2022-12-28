package com.fluid.client.api.config;

import com.fluid.client.Fluid;
import com.fluid.client.api.config.impl.ConfigModule;
import com.fluid.client.api.module.Module;
import com.fluid.client.api.setting.Setting;
import com.google.gson.*;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final File config = new File("fluid/configs", "default.json");
    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    @SneakyThrows
    public ConfigManager() {
        config.getParentFile().mkdirs();
    }

    @SneakyThrows
    public void save() {
        Files.write(config.toPath(), serialize());
    }

    @SneakyThrows
    public void load() {
        if (config.exists()) {
            final JsonParser parser = new JsonParser();
            try (final FileReader reader = new FileReader(config)) {
                final Object obj = parser.parse(reader);
                final JsonArray modules = (JsonArray) obj;
                modules.forEach(m -> parse(m.getAsJsonObject()));
            }
        }
    }

    private byte[] serialize() {
        final List<ConfigModule> configModules = new ArrayList<>();

        for (Module m : Fluid.getInstance().getModuleManager().getModules()) {
            configModules.add(new ConfigModule(m.getName(), m.isEnabled(), m.getKey(), m.getSettings()));
        }

        return gson.toJson(configModules).getBytes(StandardCharsets.UTF_8);
    }

    private void parse(JsonObject module) {
        final Module m = Fluid.getInstance().getModuleManager().getModule(module.get("name").getAsString());

        m.setEnabled(module.get("enabled").getAsBoolean());
        m.setKey(module.get("key").getAsInt());

        for (JsonElement s : module.getAsJsonArray("settings")) {
            try {
                final JsonObject settingObj = s.getAsJsonObject();
                final Setting setting = m.getSetting(settingObj.get("name").getAsString());


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
