package me.advancedmical.cfoodrel;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class CFoodRel extends JavaPlugin implements Listener {
    public static String prefix = "§6§l食物系统§1§l>>";
    @Override
    public void onLoad(){}
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
    }
    @Override
    public void onDisable() {}
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        for (String key : getConfig().getKeys(false)) {
            try {
                if (e.getItem().getItemMeta().getDisplayName().equals(getConfig().getString(String.valueOf(key) + ".name").replace("&", "§"))) {
                    for (String cmd : getConfig().getStringList(String.valueOf(key) + ".cmd")) {
                        if (player.hasPermission(getConfig().getString(String.valueOf(key) + ".Permission")) || getConfig().getString(String.valueOf(key) + ".Permission").equalsIgnoreCase("none")) {
                            if (cmd != null) {
                                boolean isOp = player.isOp();
                                if (getConfig().getBoolean(String.valueOf(key) + ".op")) {
                                    try {
                                        player.setOp(true);
                                        getServer().dispatchCommand((CommandSender) player, cmd.replace("%player%", e.getPlayer().getName()).replace("&", "§"));
                                        player.setOp(isOp);
                                    } catch (Exception exception) {
                                        continue;
                                    } finally {
                                        player.setOp(isOp);
                                    }
                                    continue;
                                }
                                getServer().dispatchCommand((CommandSender) player, cmd.replace("%player%", e.getPlayer().getName()).replace("&", "§"));
                            }
                            continue;
                        }
                        player.sendMessage(prefix + " §c您没有权限这样做");
                    }
                }
            } catch (NullPointerException npe){}
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("cfoodreload")){
                if (sender.hasPermission("cfood.use.reload")){
                    this.reloadConfig();
                    sender.sendMessage(prefix+" §a插件已重载.");
                }
            return true;
        }
        return false;
    }

    public void helpMsg(CommandSender sender) {
        sender.sendMessage(prefix+" §a/cfood reload §9=== §b重载插件.");
    }
}

