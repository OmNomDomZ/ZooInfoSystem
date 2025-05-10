package v.rabetsky.models.filters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalFilter {
    private String nickname;
    private Integer[] animalTypeIds;
    private String[] genders;
    private Integer[] cageIds;
    private Boolean needsWarmHousing;
}
