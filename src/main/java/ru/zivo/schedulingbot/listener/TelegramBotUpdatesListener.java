package ru.zivo.schedulingbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zivo.schedulingbot.model.Chat;
import ru.zivo.schedulingbot.service.ChatService;
import ru.zivo.schedulingbot.service.SchedulingService;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final String START = "/start";

    private final TelegramBot telegramBot;

    private final ChatService chatService;
    private final SchedulingService schedulingService;

    private static final Map<String, ResourceBundle> bundles = Map.of(
            "en", ResourceBundle.getBundle("messages", Locale.ENGLISH),
            "ru", ResourceBundle.getBundle("messages", Locale.forLanguageTag("ru-RU"))
    );

    @PostConstruct
    private void init() {
        telegramBot.setUpdatesListener(this);

        List<Chat> chats = chatService.findAll();

        for (Chat chat : chats) {
            schedulingService.schedule(() -> sendMessage(chat.getChatId(),
                    bundles.get(chat.getLanguageTag()).getString("reminder")));
        }
    }

    @Override
    public int process(List<Update> updates) {

        String languageTag = "en";

        for (Update update : updates) {

            if (update == null || update.message() == null || update.message().chat() == null) {
                continue;
            }

            Message message = update.message();
            User from = message.from();

            if (from != null && from.languageCode() != null && !languageTag.equals(from.languageCode())) {
                languageTag = from.languageCode();
            }

            if (message.chat().id() != null) {
                processingMessage(languageTag, message, message.chat().id());
            }
        }

        return CONFIRMED_UPDATES_ALL;
    }

    private void processingMessage(String languageTag, Message message, Long chatId) {
        ResourceBundle messages = bundles.get(languageTag);

        if (Objects.equals(message.text(), START)) {
            boolean saved = chatService.save(new Chat(chatId, languageTag));

            if (saved) {
                sendMessage(chatId, messages.getString("started"));
                schedulingService.schedule(() -> sendMessage(chatId, messages.getString("reminder")));
            } else {
                sendMessage(chatId, messages.getString("already_running"));
            }

        } else {
            sendMessage(chatId, messages.getString("not_found"));
        }
    }

    private void sendMessage(Long chatId, String reminder) {
        telegramBot.execute(new SendMessage(chatId, reminder));
    }

}
