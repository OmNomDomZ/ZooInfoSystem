package v.rabetsky.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CageForm {
    @NotNull(message="Выберите вид животного")
    private Integer animalTypeId;

    @NotNull(message="Укажите вместимость")
    @Min(value=1, message="Вместимость должна быть ≥ 1")
    private Integer capacity;

    @Getter
    @Setter
    public static class MoveForm {
        @NotNull(message="Нужно выбрать животное")
        private Integer animalId;

        @NotNull(message="Нужно выбрать новую клетку")
        private Integer newCageId;
    }
}
