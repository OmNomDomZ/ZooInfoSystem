package v.rabetsky.models.entities;

import lombok.*;
import v.rabetsky.annotations.DateRange;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DateRange(start="startDate", end="endDate",
        message="Дата начала не может быть позже даты окончания")
public class AnimalCageHistory {
    private int id;

    @Positive(message = "ID животного должно быть положительным")
    private int animalId;

    @Positive(message = "ID клетки должно быть положительным")
    private int cageId;

    @NotNull(message = "Дата начала обязательна")
    @PastOrPresent(message = "Дата начала не может быть в будущем")
    private LocalDate startDate;

    @PastOrPresent(message = "Дата окончания не может быть в будущем")
    private LocalDate endDate;  
}