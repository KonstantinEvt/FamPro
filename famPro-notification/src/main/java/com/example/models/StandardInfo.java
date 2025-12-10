package com.example.models;

import com.example.dtos.AloneNewDto;
import com.example.enums.Localisation;
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
    private Localisation localisation;

    public void addNewMessage(AloneNewDto aloneNewDto) {
        boolean success = false;
        switch (aloneNewDto.getCategory()) {
            case SYSTEM -> {
                success = systemNews.add(aloneNewDto);
                this.counts[1] = systemNews.size();
            }
            case FAMILY -> {
                success = familyNews.add(aloneNewDto);
                this.counts[3] = familyNews.size();
            }
            case PRIVATE -> {
                success = individualNews.add(aloneNewDto);
                this.counts[4] = individualNews.size();
            }
            default -> log.warn("try to receive unknown letter");
        }
        if (success) this.counts[0] = this.counts[1] + this.counts[3] + this.counts[4];
    }

    public void removeNewMessage(AloneNewDto aloneNewDto) {
        boolean success = false;
        switch (aloneNewDto.getCategory()) {
            case SYSTEM -> {
                success = systemNews.remove(aloneNewDto);
                this.counts[1] = systemNews.size();
            }
            case FAMILY -> {
                success = familyNews.remove(aloneNewDto);
                this.counts[3] = familyNews.size();
            }
            case PRIVATE -> {
                success = individualNews.remove(aloneNewDto);
                this.counts[4] = individualNews.size();
            }
            default -> log.warn("try to remove unknown letter");
        }
        if (success) this.counts[0] = this.counts[1] + this.counts[3] + this.counts[4];
    }

    public int viewNewGlobalMessage(List<Integer> mask, List<Integer> read) {
        int countGlobal = 0;
        for (int i = 0; i < mask.size(); i++) {
            if (i == read.size()) read.add(0);
            if (mask.get(i) + read.get(i) == 0) countGlobal += 1;
        }
        return countGlobal;
    }
    public void setLocalisation(Localisation localisation){
        this.localisation=localisation;
    }
    public Localisation getLocalisation(){
        return localisation;
    }
}

