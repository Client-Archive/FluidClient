package com.fluid.client.api.event.impl;

import com.fluid.client.api.event.Event;
import lombok.Getter;

@Getter
public class EventKey extends Event {

    private final int key;

    public EventKey(int key) {
        this.key = key;
    }

}
