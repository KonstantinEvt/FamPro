package ru.memman.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.memman.config.FeignRequestIntercepter;

@FeignClient(name = "MEMMAN-CONTACT", configuration = FeignRequestIntercepter.class, fallbackFactory = ContactConnectionClient.checkRightsFallbackFactory.class)
public interface ContactConnectionClient extends ConnectionClient{
    @Override
    @GetMapping("/contact/contact/access/{uuid}")
    boolean checkRights(@PathVariable("uuid") String uuid);

    @Component
    class checkRightsFallbackFactory implements FallbackFactory<FallContactConnection> {

        @Override
        public FallContactConnection create(Throwable cause) {
            return new FallContactConnection(cause.getMessage());
        }
    }

    @Slf4j
    record FallContactConnection(String reason) implements ContactConnectionClient {
        @Override
        public boolean checkRights(String uuid) {
            log.info("oops");return false;
        }
    }
}