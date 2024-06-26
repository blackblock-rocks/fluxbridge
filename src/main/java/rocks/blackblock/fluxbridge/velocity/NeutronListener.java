package rocks.blackblock.fluxbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import me.crypnotic.neutron.api.event.AlertBroadcastEvent;
import me.crypnotic.neutron.api.user.User;
import net.kyori.adventure.text.Component;
import rocks.blackblock.fluxbridge.FluxBridge;
import rocks.blackblock.fluxbridge.util.TextUtil;

public class NeutronListener {
    private final FluxBridge plugin;

    public NeutronListener(FluxBridge plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onNeutronBroadcast(AlertBroadcastEvent event) {
        Component component = event.getMessage();
        String message = plugin.getConfig().getNeutronAlertFormat()
                .replace("{message}", TextUtil.toMarkdown(component))
                .replace("{author}", event.getAuthor().map(User::getName).orElse("CONSOLE"));

        if (!message.isEmpty()) {
            plugin.getConfig().getOutChannels(plugin.getDiscordApi())
                    .forEach(channel -> channel.sendMessage(message));
        }
    }

}
