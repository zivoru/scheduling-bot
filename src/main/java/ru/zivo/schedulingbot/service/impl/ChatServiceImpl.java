package ru.zivo.schedulingbot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zivo.schedulingbot.model.Chat;
import ru.zivo.schedulingbot.repository.ChatRepository;
import ru.zivo.schedulingbot.service.ChatService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public boolean save(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

}
