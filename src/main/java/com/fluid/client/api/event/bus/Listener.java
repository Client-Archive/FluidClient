package com.fluid.client.api.event.bus;

@FunctionalInterface
public interface Listener<Event> {

    void invoke(Event event);

}
