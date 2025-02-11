package com.example.models;

import com.example.dtos.AloneNewDto;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter

public class StandardInfo {
    private int[] counts = new int[]{0, 0, 0, 0, 0};
    private List<AloneNewDto> systemNews = new LinkedList<>();
    private List<AloneNewDto> commonNews = new LinkedList<>();
    private List<AloneNewDto> familyNews = new LinkedList<>();
    private List<AloneNewDto> individualNews = new LinkedList<>();

    public void addNew(AloneNewDto aloneNewDto) {
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
            default -> {
                this.counts[2] += 1;
                commonNews.add(aloneNewDto);
            }
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
            default -> {
                this.counts[2] -= 1;
                commonNews.remove(aloneNewDto);
            }
        }
        this.counts[0] -= 1;
    }
}

