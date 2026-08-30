package ru.memman.holders;

import ru.memman.dtos.Directive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Setter
@Getter
public class DirectiveHolder {
    private Map<String, Directive> directiveMap = new ConcurrentHashMap<>();
}
