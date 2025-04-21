package v.rabetsky.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalFilter {
    private String nickname;
    private Integer[] animalTypeIds;
    private String[] genders;
    private Integer[] cageIds;
    private Boolean needsWarnHousing;
}
