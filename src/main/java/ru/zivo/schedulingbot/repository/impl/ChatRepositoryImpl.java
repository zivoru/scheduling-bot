package ru.zivo.schedulingbot.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ru.zivo.schedulingbot.model.Chat;
import ru.zivo.schedulingbot.repository.ChatRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS chats
            (
                chat_id BIGINT NOT NULL UNIQUE,
                language_tag TEXT NOT NULL
            )
            """;
    private static final String SAVE = "INSERT INTO chats(chat_id, language_tag) VALUES (?, ?)";
    private static final String FIND_ALL = "SELECT * FROM chats";

    private final JdbcOperations jdbcOperations;

    @PostConstruct
    private void init() {
        jdbcOperations.execute(CREATE_TABLE);
    }

    @Override
    public boolean save(Chat chat) {
        try {
            return jdbcOperations.update(SAVE, chat.getChatId(), chat.getLanguageTag()) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public List<Chat> findAll() {
        return jdbcOperations.query(FIND_ALL, (rs, rowNum) -> new Chat(rs.getLong(1), rs.getString(2)));
    }

}
