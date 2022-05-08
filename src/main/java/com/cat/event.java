package com.cat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class event extends ListenerAdapter {

    private boolean logger = false;
    public static Logger log;
    public static LogAppender appender;
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getAuthor().isBot())
            return;
        User user = e.getAuthor();
        TextChannel channel = e.getTextChannel();
        Message msg = e.getMessage();
        if (msg.getContentRaw().contains("-아바타")){
            if (user.getAvatarUrl() != null)
                channel.sendMessage(user.getAvatarUrl()).queue();
            else
                channel.sendMessage(user.getAsMention() + " 너는 아바타가 없다냥").queue();
        }
        else if (msg.getContentRaw().contains("-커맨드")){
            String[] arr = msg.getContentRaw().split(" ");
            if (arr.length >= 2){
                //-커맨드 /kill DR_Division
                String value = msg.getContentRaw().replace("-커맨드 ", "");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), value);
                channel.sendMessage(user.getAsMention() + " 명령어 실행 완료.").queue();
            }
            else{
                channel.sendMessage(user.getAsMention() + " 명령어를 띄어쓰기를 포함해서 작성해 주세요.").queue();
            }
        }
        else if (msg.getContentRaw().contains("-블럭설치")){
            String[] arr = msg.getContentRaw().split(" ");
            if (arr.length == 6){
                //-블럭설치 100 100 100 Material.SAND World;
                World world = Bukkit.getServer().getWorld(arr[5]);
                if (world == null) {
                    channel.sendMessage(user.getAsMention() + " 존재하지 않는 월드입니다.").queue();
                }
                else{
                    Material material = Material.getMaterial(arr[4]);
                    if (material == null){
                        channel.sendMessage(user.getAsMention() + " 존재하지 않는 블럭입니다.").queue();
                    }
                    else{
                        Bukkit.getServer().getScheduler().runTask(BotMain.instance, () -> {world.getBlockAt(new Location(world, Double.valueOf(arr[1]), Double.valueOf(arr[2]), Double.valueOf(arr[3]))).setType(material);});

                        channel.sendMessage(user.getAsMention() + " 블럭 설치완료").queue();
                    }
                }
            }
            else{
                channel.sendMessage(user.getAsMention() + " 명령어를 띄어쓰기를 포함해서 작성해 주세요.").queue();
            }
        }
        else if (msg.getContentRaw().contains("-로그")){
            if (logger){
                logger = false;
                log.removeAppender(appender);
                channel.sendMessage(user.getAsMention() + " 로거가 비활성화 되었습니다.").queue();

            }
            else{
                logger = true;
                log = (Logger) LogManager.getRootLogger();
                appender = new LogAppender(channel);
                log.removeAppender(appender);
                log.addAppender(appender);
                channel.sendMessage(user.getAsMention() + " 로거가 활성화 되었습니다.").queue();
            }
        }
        else if (msg.getContentRaw().contains("-종료")){
            channel.sendMessage(user.getAsMention() + " 서버를 종료하였습니다.").queue();
            Bukkit.getServer().shutdown();
        }
        else if (msg.getContentRaw().contains("-삭제")){
            String[] val = msg.getContentRaw().split(" ");
            if (val.length == 1)
                channel.sendMessage(user.getAsMention() + " 삭제할 메시지 양을 적어달라냥").queue();
            else{
                try{
                    int value = Integer.parseInt(val[1]);
                    if (value <= 0)
                        channel.sendMessage(user.getAsMention() + " 제대로 적으라냥").queue();
                    else{
                        channel.sendMessage(user.getAsMention()  + value + "개의 메시지가 삭제중이다냥").queueAfter(1, TimeUnit.SECONDS);
                        MessageHistory history = new MessageHistory(channel);
                        List<Message> hismsg = history.retrievePast(value).complete();
                        for (Message delmsg : hismsg)
                            delmsg.delete().queue();
                    }
                }
                catch (Exception ex){
                    channel.sendMessage(user.getAsMention() + " 숫자만 입력하라냥").queue();
                }
            }

        }
    }
}
