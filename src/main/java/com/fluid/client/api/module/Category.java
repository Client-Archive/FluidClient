package com.fluid.client.api.module;

import lombok.Getter;

public enum Category {

    ALL("All"),
    HUD("HUD"),
    MECHANIC("Mechanic"),
    MISC("Misc");

    @Getter
    private final String name;

    Category(String name) {
        this.name = name;
    }

}
