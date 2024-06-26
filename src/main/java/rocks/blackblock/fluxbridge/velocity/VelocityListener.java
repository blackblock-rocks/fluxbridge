package rocks.blackblock.fluxbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import rocks.blackblock.fluxbridge.FluxBridge;

public class VelocityListener {

    private final FluxBridge plugin;

    public VelocityListener(FluxBridge plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        if (event.getPlayer().hasPermission("fluxbridge.silentjoin")) return;

        String message = plugin.getConfig().getJoinFormat()
            .replace("{player}", event.getPlayer().getUsername());

        plugin.getConfig().getOutChannels(plugin.getDiscordApi()).forEach(chan -> chan.sendMessage(message));
    }

    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        if (event.getPlayer().hasPermission("fluxbridge.silentquit")) return;

        String message = plugin.getConfig().getQuitFormat()
            .replace("{player}", event.getPlayer().getUsername());

        plugin.getConfig().getOutChannels(plugin.getDiscordApi()).forEach(chan -> chan.sendMessage(message));
    }

}
