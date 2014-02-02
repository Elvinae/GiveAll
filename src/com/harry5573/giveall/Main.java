package com.harry5573.giveall;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    
    public String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_RED + "GiveAll" + ChatColor.WHITE + "] ";
    public static Main plugin;
    
    @Override
    public void onEnable() {
        plugin = this;
        
        this.getCommand("giveall").setExecutor(new Commands(this));

        log("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        log("Plugin disabled.");
    }

    public void log(String msg) {
        this.getLogger().info(msg);
    }
}
