package ru.memman.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ContentDto {
    private Map<Integer,String> globalMenu;
    private Map<String,String> langMatrix;
    private Map<Integer, String> localMenu;
    private Map<Integer, String> globalTexts;
}
