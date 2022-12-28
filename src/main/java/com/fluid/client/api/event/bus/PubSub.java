package com.fluid.client.api.event.bus;

import java.util.function.Consumer;

public interface PubSub<Event> {

    static <Event> PubSub<Event> newInstance(Consumer<String> errorLogger) {
        return new PubSubImpl<>(errorLogger);
    }

    void subscribe(Object subscriber);

    void unsubscribe(Object subscriber);

    <T extends Event> void subscribe(
            Class<T> event,
            Listener<T> listener
    );

    void publish(Event event);

    void clear();
}