package ru.memman.holders;

import ru.memman.enums.Localisation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Setter
@Getter
public class HolderSelector {
    final private TextHolderRu textHolderRu;
    final private TextHolderEn textHolderEn;

    public HolderSelector(TextHolderRu textHolderRu, TextHolderEn textHolderEn) {
        this.textHolderRu = textHolderRu;
        this.textHolderEn = textHolderEn;
    }
    public AbstractTextHolder chooseLocalisation(String localisation){
        Localisation loc = Localisation.RU;
        for (Localisation local :
                Localisation.values()) {
            if (Objects.equals(localisation.toUpperCase(), local.name())) loc = local;
        }
        return (loc == Localisation.RU)? getTextHolderRu():getTextHolderEn();

    }
}
