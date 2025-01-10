package com.example.holders;

import com.example.dtos.Directive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
@Setter
@Getter
public class DirectiveHolder {
    private Map<String, Directive> directiveMap = new WeakHashMap<>();
}
