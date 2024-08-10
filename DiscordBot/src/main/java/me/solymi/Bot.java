package me.solymi;

import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.LavalinkNode;
import dev.arbjerg.lavalink.client.NodeOptions;
import dev.arbjerg.lavalink.client.event.EmittedEvent;
import dev.arbjerg.lavalink.client.event.StatsEvent;
import dev.arbjerg.lavalink.client.event.TrackStartEvent;
import dev.arbjerg.lavalink.client.loadbalancing.RegionGroup;
import dev.arbjerg.lavalink.libraries.jda.JDAVoiceUpdateListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Bot {
    private final LavalinkClient client;
    public Bot() throws Exception {
        final var TOKEN = loadTokenFromEnvFile();
        //System.out.println(TOKEN);

        client = new LavalinkClient(Helpers.getUserIdFromToken(TOKEN));


//        JDABuilder builder = JDABuilder.createDefault(TOKEN,
//                GatewayIntent.DIRECT_MESSAGES,
//                GatewayIntent.SCHEDULED_EVENTS,
//                GatewayIntent.GUILD_MESSAGES,
//                GatewayIntent.GUILD_MEMBERS,
//                GatewayIntent.GUILD_PRESENCES,
//                GatewayIntent.GUILD_MESSAGE_REACTIONS,
//                GatewayIntent.GUILD_VOICE_STATES,
//                GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
//                GatewayIntent.MESSAGE_CONTENT);

        JDABuilder builder = JDABuilder.createDefault(TOKEN);

        builder.setVoiceDispatchInterceptor(new JDAVoiceUpdateListener(client));
        builder.enableIntents(GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.MESSAGE_CONTENT);
        builder.enableCache(CacheFlag.VOICE_STATE);

        registerLavalinkListeners();
        registerLavalinkNodes();

        builder.addEventListeners(new MyListener());
        builder.addEventListeners(new MyAudioSender());

        builder.build().awaitReady();
    }

    private void registerLavalinkNodes() {
        List.of(
            /*client.addNode(
                "Testnode",
                URI.create("ws://localhost:2333"),
                "youshallnotpass",
                RegionGroup.EUROPE
            ),*/

                client.addNode(new NodeOptions.Builder().setName("Testnode")
                        .setServerUri(URI.create("ws://192.168.0.94:2333"))
                        .setPassword("youshallnotpass")
                        .setRegionFilter(RegionGroup.EUROPE)
                        .setHttpTimeout(5000L)
                        .build()
                )
        ).forEach((node) -> {
            node.on(TrackStartEvent.class).subscribe((event) -> {
                final LavalinkNode node1 = event.getNode();

                System.out.printf(
                        "%s: track started: %s%n",
                        node1.getName(),
                        event.getTrack().getInfo()
                );
            });
        });
    }

    private void registerLavalinkListeners() {
        client.on(dev.arbjerg.lavalink.client.event.ReadyEvent.class).subscribe((event) -> {
            final LavalinkNode node = event.getNode();

            System.out.printf(
                    "Node '%s' is ready, session id is '%s'!%n",
                    node.getName(),
                    event.getSessionId()
            );
        });

        client.on(StatsEvent.class).subscribe((event) -> {
            final LavalinkNode node = event.getNode();

            System.out.printf(
                    "Node '%s' has stats, current players: %d/%d%n",
                    node.getName(),
                    event.getPlayingPlayers(),
                    event.getPlayers()
            );
        });

        client.on(EmittedEvent.class).subscribe((event) -> {
            if (event instanceof TrackStartEvent) {
                System.out.println("Is a track start event!");
            }

            final var node = event.getNode();

            System.out.printf(
                    "Node '%s' emitted event: %s%n",
                    node.getName(),
                    event
            );
        });
    }

    public static String loadTokenFromEnvFile() throws Exception {
        try {
            var lines = Files.readAllLines(Path.of("token.env"));

            return lines.getFirst().split("=")[1];
        } catch (FileNotFoundException e) {
            System.out.println("token.env file not found!");
            throw e;
        }
    }
}
