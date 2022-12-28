package com.fluid.client.bridge;

import com.fluid.client.bridge.bridges.MinecraftBridge;

public class BridgeManager {

    private static IBridge INSTANCE;
    private static MinecraftBridge MINECRAFT_BRIDGE;

    public static void setImplementation(IBridge bridge) {
        INSTANCE = bridge;
        INSTANCE.enable();
    }

    public static IBridge getImplementation() {
        return INSTANCE;
    }

    public static MinecraftBridge getMinecraftBridge() {
        return MINECRAFT_BRIDGE;
    }

    public static void setMinecraftBridge(MinecraftBridge bridge) {
        MINECRAFT_BRIDGE = bridge;
    }

}