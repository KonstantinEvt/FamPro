package ru.memman.feign;

import ru.memman.config.FeignRequestIntercepter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MEMMAN-NOTIFICATION", configuration = FeignRequestIntercepter.class, fallbackFactory = NotificationConnectionClient.checkRightsFallbackFactory.class)
public interface NotificationConnectionClient extends ConnectionClient{
    @Override
    @GetMapping("/notify/news/rights/{uuid}")
    boolean checkRights(@PathVariable("uuid") String uuid);

    @Component
    class checkRightsFallbackFactory implements FallbackFactory<FallNotificationConnection> {

        @Override
        public FallNotificationConnection create(Throwable cause) {
            return new FallNotificationConnection(cause.getMessage());
        }
    }

    @Slf4j
    record FallNotificationConnection(String reason) implements NotificationConnectionClient {
        @Override
        public boolean checkRights(String uuid) {
            return false;
        }
    }
}