package com.example.models;

import com.example.dtos.AloneNewDto;
import com.example.enums.NewsCategory;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Log4j2
public class StandardInfo {
    private int[] counts = new int[]{0, 0, 0, 0, 0};
    private List<AloneNewDto> systemNews = new LinkedList<>();
    private List<AloneNewDto> familyNews = new LinkedList<>();
    private List<AloneNewDto> individualNews = new LinkedList<>();

    private List<Integer> systemGlobalRead = new ArrayList<>();
    private List<Integer> commonGlobalRead = new ArrayList<>();

    public void addNewMessageToPerson(AloneNewDto aloneNewDto) {
        switch (aloneNewDto.getCategory()) {
            case SYSTEM -> {
                this.counts[1] += 1;
                systemNews.add(aloneNewDto);
            }
            case FAMILY -> {
                this.counts[3] += 1;
                familyNews.add(aloneNewDto);
            }
            case PRIVATE -> {
                this.counts[4] += 1;
                individualNews.add(aloneNewDto);
            }
            default -> log.warn("try to receive unknown letter");
        }
        this.counts[0] += 1;
    }

    public void removeNew(AloneNewDto aloneNewDto) {
        switch (aloneNewDto.getCategory()) {
            case SYSTEM -> {
                this.counts[1] -= 1;
                systemNews.remove(aloneNewDto);
            }
            case FAMILY -> {
                this.counts[3] -= 1;
                familyNews.remove(aloneNewDto);
            }
            case PRIVATE -> {
                this.counts[4] -= 1;
                individualNews.remove(aloneNewDto);
            }
            default -> log.warn("try to remove unknown letter");
        }
        this.counts[0] -= 1;
    }
    public int viewGlobalMessage(List<Integer> mask, List<Integer> read){
        int countGlobal=0;
        for (int i = 0; i < mask.size(); i++) {
            if (i==read.size()) read.add(0);
            if (mask.get(i)+read.get(i)==0) countGlobal+=1;
        }
        return countGlobal;
    }
}

