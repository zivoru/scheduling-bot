package ru.zivo.schedulingbot.service;

import ru.zivo.schedulingbot.model.Chat;

import java.util.List;

public interface ChatService {

    boolean save(Chat chat);

    List<Chat> findAll();

}
