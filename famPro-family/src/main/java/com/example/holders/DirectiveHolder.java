package com.example.holders;

import com.example.dtos.FamilyDirective;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Setter
@Getter
public class DirectiveHolder {
    private Map<String, LinkedList<FamilyDirective>> directiveMap = new ConcurrentHashMap<>();
}
