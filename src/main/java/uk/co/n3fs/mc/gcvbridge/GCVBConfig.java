package uk.co.n3fs.mc.gcvbridge;

import io.leangen.geantyref.TypeToken;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.spongepowered.configurate.ConfigurationNode;
import rocks.blackblock.fluxchat.api.FluxChatApi;
import rocks.blackblock.fluxchat.api.FluxChatFormat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GCVBConfig {

    private final ConfigurationNode root;

    private final String token;
    private final List<Long> inChannels;
    private final List<Long> outChannels;
    private final boolean playerlistEnabled;
    private final String playerlistFormat;
    private final String playerlistSeparator;
    private final int playerlistCommandRemoveDelay;
    private final int playerlistResponseRemoveDelay;

    private final String joinFormat;
    private final String quitFormat;
    private final boolean requireSeePerm;
    private final boolean requireSendPerm;
    private final String out_webhook;

    private final FluxChatFormat gchatInFormat;
    private final String neutronAlertFormat;

    public GCVBConfig(FluxChatApi gcApi, ConfigurationNode root) throws Exception {
        this.root = root;

        token = root.node("discord", "token").getString();
        out_webhook = root.node("discord", "out-webhook").getString();
        inChannels = root.node("discord", "in-channels").getList(TypeToken.get(Long.class));
        outChannels = root.node("discord", "out-channels").getList(TypeToken.get(Long.class));

        playerlistEnabled = root.node("discord", "playerlist", "enabled").getBoolean(true);
        playerlistFormat = root.node("discord", "playerlist", "format").getString("**{count} players online:** ```\n{players}\n```");
        playerlistSeparator = root.node("discord", "playerlist", "separator").getString(", ");
        playerlistCommandRemoveDelay = root.node("discord", "playerlist", "command-remove-delay").getInt(0);
        playerlistResponseRemoveDelay = root.node("discord", "playerlist", "response-remove-delay").getInt(10);

        if (token == null || token.isEmpty()) {
            throw new InvalidConfigException("You need to set a bot token in config.yml!");
        }

        joinFormat = root.node("velocity", "join-format").getString("**{player} joined the game**");
        quitFormat = root.node("velocity", "quit-format").getString("**{player} left the game**");
        requireSeePerm = root.node("velocity", "require-see-permission").getBoolean(false);
        requireSendPerm = root.node("velocity", "require-send-permission").getBoolean(false);

        String gchatFormatName = root.node("gchat", "in-format").getString("default");
        gchatInFormat = gcApi.getFormats().stream()
                .filter(format -> format.getId().equalsIgnoreCase(gchatFormatName))
                .findFirst()
                .orElseThrow(() -> new InvalidConfigException("The format specified by in-format does not exist in the gChat config!"));

        neutronAlertFormat = root.node("neutron", "alert-format").getString("**BROADCAST** {message}");
    }

    public class InvalidConfigException extends Exception {
        InvalidConfigException(String message) {
            super(message);
        }
    }

    public String getToken() {
        return token;
    }

    public String getOutWebhook() {
        return out_webhook;
    }

    public List<TextChannel> getInChannels(DiscordApi dApi) {
        return inChannels.stream()
            .map(dApi::getTextChannelById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    public List<TextChannel> getOutChannels(DiscordApi dApi) {
        return outChannels.stream()
            .map(dApi::getTextChannelById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    public boolean isPlayerlistEnabled() {
        return playerlistEnabled;
    }

    public String getPlayerlistFormat() {
        return playerlistFormat;
    }

    public String getPlayerlistSeparator() {
        return playerlistSeparator;
    }

    public int getPlayerlistCommandRemoveDelay() {
        return playerlistCommandRemoveDelay;
    }

    public int getPlayerlistResponseRemoveDelay() {
        return playerlistResponseRemoveDelay;
    }

    public String getJoinFormat() {
        return joinFormat;
    }

    public String getQuitFormat() {
        return quitFormat;
    }

    public boolean isRequireSeePerm() {
        return requireSeePerm;
    }

    public boolean isRequireSendPerm() {
        return requireSendPerm;
    }

    public FluxChatFormat getInFormat() {
        return gchatInFormat;
    }

    public String getNeutronAlertFormat() {
        return neutronAlertFormat;
    }
}
