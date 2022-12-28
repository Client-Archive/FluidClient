package com.fluid.client.api.event;

import com.fluid.client.api.event.bus.PubSub;
import lombok.Getter;

public class EventManager {

    @Getter
    private static final PubSub<Event> pubSub = PubSub.newInstance(System.err::println);

}
