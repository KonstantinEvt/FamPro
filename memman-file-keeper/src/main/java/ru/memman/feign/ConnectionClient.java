package ru.memman.feign;

import org.springframework.web.bind.annotation.PathVariable;

public interface ConnectionClient {
    boolean checkRights(@PathVariable("uuid") String uuid);
}
