package v.rabetsky.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Animal {
    private int id;

    @NotEmpty(message = "Кличка не должна быть пустой")
    @Size(min = 2, max = 50, message = "Кличка должна быть от 2 до 50 символов")
    private String nickname;

    @NotNull(message = "Пол обязателен")
    @Pattern(regexp = "мужской|женский", message = "Пол должен быть 'мужской' или 'женский'")
    private String gender;

    @NotNull(message = "Дата прибытия обязательна")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;

    private boolean needs_warm_housing;

    @NotNull(message = "ID типа животного обязателен")
    private Integer animalTypeId;

    @NotNull(message = "ID клетки обязателен")
    private Integer cageId;
}
