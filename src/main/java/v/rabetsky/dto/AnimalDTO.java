package v.rabetsky.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDTO {
    private int id;
    private String nickname;
    private String gender;
    private LocalDate arrivalDate;
    private boolean needsWarmHousing;
    private Integer animalTypeId;
    private String animalTypeName;
    private String animalDietType;
    private Integer cageId;
}
