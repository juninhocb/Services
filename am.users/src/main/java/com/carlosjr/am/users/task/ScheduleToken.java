package com.carlosjr.am.users.task;

import com.carlosjr.am.users.user.User;
import com.carlosjr.am.users.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ScheduleToken {
    @Value("${com.carlosjr.config.access-token-expiration}")
    private Long accessTokenExpiresIn;
    @Value("${com.carlosjr.config.refresh-token-expiration}")
    private Long refreshTokenExpiresIn;
    private final UserService userService;
    @Scheduled(fixedDelay = 15000)
    public void handleToken(){

        Set<User> loggedInUsers = userService
                .getLoggedUsers();

        //Should retrieve from database directly instead this logic.
        loggedInUsers.forEach(user -> {

            LocalDateTime now = LocalDateTime.now();

            if (user
                    .getCreatedAccessToken()
                    .plusSeconds(accessTokenExpiresIn)
                    .isBefore(now)){
                if (user
                        .getCreatedRefreshToken()
                        .plusSeconds(refreshTokenExpiresIn)
                        .isBefore(now)){
                    user.setCurrentAccessToken(null);
                    user.setCurrentRefreshToken(null);

                } else {
                    UUID updatedAccessToken = UUID.randomUUID();
                    user.setCurrentAccessToken(updatedAccessToken.toString());
                    user.setCreatedAccessToken(now);
                }

                userService.updateUserDirectly(user);
            }
        });

    }

}
