package com.cat;

import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.bukkit.Bukkit;

import java.io.Serializable;

public class LogAppender extends AbstractAppender {

    private TextChannel channel;
    protected LogAppender(TextChannel channel) {
        super("MyLogAppender", null,null);
        this.channel = channel;
        start();
    }


    @Override
    public void append(LogEvent logEvent) {
        LogEvent log = logEvent.toImmutable();
        String msg = log.getMessage().getFormattedMessage().replace("\u001B[m", "");
        channel.sendMessage(msg).queue();
    }
}
