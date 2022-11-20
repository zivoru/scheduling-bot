package ru.zivo.schedulingbot.config;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.zivo.schedulingbot.config.properties.TelegramBotProperties;

@Configuration
@RequiredArgsConstructor
public class TelegramBotConfiguration {

    private final TelegramBotProperties properties;

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(properties.token());
    }

}
