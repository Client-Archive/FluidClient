package com.fluid.client.bridge.bridges;

import net.minecraft.util.Session;
import net.minecraft.util.Timer;

public interface MinecraftBridge {

    boolean bridge$isFullScreen();
    void bridge$setSession(Session session);
    Session bridge$getSession();
    Timer bridge$getTimer();

}
