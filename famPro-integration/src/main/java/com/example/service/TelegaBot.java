package com.example.service;


import com.example.models.Bot;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@AllArgsConstructor
@Log4j2
public class TelegaBot extends TelegramLongPollingBot {


    Bot bot;


    /**
     * Method for receiving messages.
     *
     * @param update Contains a message from the user.
     */
    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        sendMsg(update.getMessage().getChatId().toString(), message);
        System.out.println("=================Message================");
        System.out.println(message);
    }

    /**
     * Method for creating a message and sending it.
     *
     * @param chatId chat id
     * @param s      The String that you want to send as a message.
     */
    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("Exception: ", e.toString());
        }
    }

    /**
     * This method returns the bot's name, which was specified during registration.
     *
     * @return bot name
     */
    @Override
    public String getBotUsername() {
        return "FamPro_v1_bot";
    }

    /**
     * This method returns the bot's token for communicating with the Telegram server
     *
     * @return the bot's token
     */
    @Override
    public String getBotToken() {
        return bot.getToken();
    }
}

