package com.example.service;


import com.example.models.Bot;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@AllArgsConstructor
@Log4j2
public class TelegaBot extends TelegramLongPollingBot {

    private Bot bot;


    /**
     * Метод получения сообщений телеграм-Ботом
     *
     * @param update cодержит message от бота
     */
    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        sendMsg(update.getMessage().getChatId().toString(), message);
        System.out.println("=================Message================");
        System.out.println(message);
    }

    /**
     * Создание и отправка сообщения
     *
     * @param chatId chat id
     * @param s      Строка сообщения.
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
     * Имя бота
     *
     * @return bot name
     */
    @Override
    public String getBotUsername() {
        return "FamPro_v1_bot";
    }

    /**
     * Токен бота
     *
     * @return token бота
     */
    @Override
    public String getBotToken() {
        return bot.getToken();
    }
}

