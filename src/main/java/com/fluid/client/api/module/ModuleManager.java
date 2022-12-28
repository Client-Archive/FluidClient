package com.fluid.client.api.module;

import com.fluid.client.api.module.feature.Draggable;
import com.fluid.client.api.module.impl.hud.FPS;
import com.fluid.client.api.event.EventManager;
import com.fluid.client.api.event.bus.Listen;
import com.fluid.client.api.event.impl.EventRenderHUD;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        addModules(
                new FPS()
        );

        EventManager.getPubSub().subscribe(this);
    }

    private void addModules(Module... modules) {
        this.modules.addAll(Arrays.asList(modules));
    }

    public Module getModule(Class<? extends Module> module) {
        return getModules().stream().filter(m -> m.getClass() == module).findFirst().orElse(null);
    }

    public Module getModule(String name) {
        return getModules().stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Module> getModulesInCategory(Category category) {
        if (category == Category.ALL) return this.modules;

        return this.modules.stream().filter(m -> m.getCategory() == category).collect(Collectors.toList());
    }

    public List<Draggable> getDraggables() {
        return modules.stream().filter(m -> m instanceof Draggable).map(m -> (Draggable) m).collect(Collectors.toList());
    }

    public List<Draggable> getEnabledDraggables() {
        return getDraggables().stream().filter(Module::isEnabled).collect(Collectors.toList());
    }

    @Listen
    public void onRender(EventRenderHUD e) {
        getEnabledDraggables().forEach(Draggable::render);
    }

}
