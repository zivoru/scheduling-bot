package ru.zivo.schedulingbot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import ru.zivo.schedulingbot.service.SchedulingService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {

    private final TaskScheduler taskScheduler;

    @Override
    public void schedule(Runnable runnable) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hour9 = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 9, 0);

        for (int i = 0; i < 4; i++) {

            if (now.getDayOfMonth() == hour9.getDayOfMonth()) {
                if (now.getHour() >= hour9.getHour()) {
                    hour9 = hour9.plusDays(1);
                }
            } else {
                if (now.getHour() < hour9.getHour()) {
                    hour9 = hour9.minusDays(1);
                }
            }

            taskScheduler.scheduleWithFixedDelay(runnable, hour9.toInstant(ZoneOffset.ofHours(5)), Duration.ofDays(1));

            hour9 = hour9.plusHours(4);
        }
    }

}
