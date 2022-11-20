package ru.zivo.schedulingbot.repository;

import ru.zivo.schedulingbot.model.Chat;

import java.util.List;

public interface ChatRepository {

    boolean save(Chat chat);

    List<Chat> findAll();

}
