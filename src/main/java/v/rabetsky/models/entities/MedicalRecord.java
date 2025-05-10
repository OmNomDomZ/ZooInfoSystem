package v.rabetsky.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    private int id;

    @NotNull
    @Positive(message = "ID животного должен быть положительным")
    private Integer animalId;

    @NotNull(message = "Дата рождения обязательна")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(message = "Вес обязателен")
    @DecimalMin(value = "0.1", message = "Вес должен быть больше 0")
    private BigDecimal weight;

    @NotNull(message = "Рост обязателен")
    @DecimalMin(value = "0.1", message = "Рост должен быть больше 0")
    private BigDecimal height;

    @Size(max = 255, message = "Список прививок не должен превышать 255 символов")
    private String vaccinations;

    @Size(max = 255, message = "Список болезней не должен превышать 255 символов")
    private String illnesses;

    @NotNull(message = "Дата осмотра обязательна")
    @PastOrPresent(message = "Дата осмотра не может быть в будущем")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkupDate;
}
