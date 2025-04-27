package v.rabetsky.models.entities;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalCageHistory {
    private int id;
    private int animalId;
    private int cageId;
    private LocalDate startDate;
    private LocalDate endDate;  
}