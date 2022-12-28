package com.fluid.client.bridge;

import com.fluid.client.bridge.bridges.MinecraftBridge;

public class BridgeReferences {

    public static MinecraftBridge getMinecraft() {
        return BridgeManager.getMinecraftBridge();
    }

}
