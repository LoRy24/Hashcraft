package com.github.lory24.hashcraft.api;

import com.github.lory24.hashcraft.api.util.ServerListPingResponse;
import com.github.lory24.hashcraft.chatcomponent.TextChatComponent;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class ServerListPingResponseTestGson {

    private final Gson gson = new Gson();

    @Test
    public void test1() {
        Assert.assertEquals(gson.toJson(new ServerListPingResponse.ServerListVersion("Hashcraft 1.0-SNAPSHOT", 47)), "{\"name\":\"Hashcraft 1.0-SNAPSHOT\",\"protocol\":47}");
    }

    @Test
    public void test2() {
        ServerListPingResponse.SamplePlayer samplePlayer = new ServerListPingResponse.SamplePlayer("LoRy24", UUID.randomUUID().toString());

        Assert.assertEquals(gson.toJson(samplePlayer), "{\"name\":\"" + samplePlayer.getName() + "\",\"uuid\":\"" + samplePlayer.getUuid() + "\"}");
    }

    @Test
    public void test3() {
        ServerListPingResponse.ServerListPlayers serverListPlayers = new ServerListPingResponse.ServerListPlayers(100, 1, new ServerListPingResponse.SamplePlayer[]{new ServerListPingResponse.SamplePlayer("LoRy24", UUID.randomUUID().toString())});
        Assert.assertEquals(gson.toJson(serverListPlayers), "{\"max\":" + serverListPlayers.getMax() + ",\"online\":" + serverListPlayers.getOnline() + ",\"sample\":[{\"name\":\"" +
                serverListPlayers.getSample()[0].getName() + "\",\"uuid\":\"" + serverListPlayers.getSample()[0].getUuid() + "\"}]}");
    }

    @Test
    public void test4() {
        System.out.println(gson.toJson(new ServerListPingResponse(new ServerListPingResponse.ServerListVersion("ciao", 47), new ServerListPingResponse.ServerListPlayers(100, 0, null), new TextChatComponent("ciao"), "ciao")));
    }
}
