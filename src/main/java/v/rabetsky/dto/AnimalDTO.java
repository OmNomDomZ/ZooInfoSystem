package v.rabetsky.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;
    private boolean needsWarmHousing;
    private Integer animalTypeId;
    private String animalTypeName;
    private String animalDietType;
    private Integer cageId;
}
