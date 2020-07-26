package com.cat;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public class BotMain extends JavaPlugin {

    public static BotMain instance;
    public static JDA jda;

    @Override
    public void onEnable() {
        instance = this;
        JDABuilder build = JDABuilder.createDefault("THIS IS TOKEN");
        build.setAutoReconnect(true);
        build.setStatus(OnlineStatus.ONLINE);
        build.setActivity(Activity.of(Activity.ActivityType.DEFAULT, "냥냥봇 온라인! -help"));
        build.addEventListeners(new event());
        try {
            jda = build.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable(){
        if (event.log != null)
            event.log.removeAppender(event.appender);
        jda.cancelRequests();
        jda.shutdownNow();
    }
}
