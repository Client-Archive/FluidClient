package com.fluid.client.discord;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import org.json.JSONObject;

import java.time.OffsetDateTime;

public class DiscordIPC implements IPCListener
{
    public static final DiscordIPC INSTANCE;
    private IPCClient client;

    public void init() {
        (this.client = new IPCClient(964939727620939796L)).setListener(this);
        try {
            this.client.connect();
        }
        catch (NoDiscordClientException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void shutdown() {
        if (this.client != null && this.client.getStatus() == PipeStatus.CONNECTED) {
            this.client.close();
        }
    }

    public void onReady(final IPCClient client) {
        final RichPresence.Builder builder = new RichPresence.Builder().setState("Idle").setDetails("Minecraft 1.8.9").setLargeImage("logo", "Fluid Client 1.8.9").setStartTimestamp(OffsetDateTime.now());
        client.sendRichPresence(builder.build());
    }

    public void onClose(final IPCClient client, final JSONObject json) {
    }

    public void onDisconnect(final IPCClient client, final Throwable t) {
    }

    static {
        INSTANCE = new DiscordIPC();
    }
}
