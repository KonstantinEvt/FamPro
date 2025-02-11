package com.example.holders;

import com.example.dtos.AloneNewDto;
import com.example.models.StandardInfo;
import com.example.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
@Getter
@Setter
public class StandardInfoHolder {
    private TokenService tokenService;
    private Map<String, StandardInfo> onlineInfo;

    public void addNew(AloneNewDto aloneNewDto) {
        String onlineUser = aloneNewDto.getSendingTo();
        if (!onlineInfo.containsKey(onlineUser))
            onlineInfo.put(onlineUser, new StandardInfo());
        onlineInfo.get(onlineUser).addNew(aloneNewDto);
        System.out.println(onlineInfo);
    }
}
