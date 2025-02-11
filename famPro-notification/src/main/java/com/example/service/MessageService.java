package com.example.service;

import com.example.holders.StandardInfoHolder;
import com.example.dtos.AloneNewDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Random;

@Service
@AllArgsConstructor
public class MessageService {
    private StandardInfoHolder infoHolder;

    public void sendMessage(String from, AloneNewDto aloneNewDto) {
        aloneNewDto.setCreationDate(new Timestamp(System.currentTimeMillis()));
        aloneNewDto.setSendingFrom(from);
        aloneNewDto.setId(new Random().nextLong());
        infoHolder.addNew(aloneNewDto);
        System.out.println("letter is done");
        System.out.println(aloneNewDto);
    }
}
