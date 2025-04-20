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
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private int id;

    @NotEmpty(message = "Полное имя не должно быть пустым")
    @Size(min = 5, max = 100, message = "Имя должно быть от 5 до 100 символов")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Имя должно содержать только буквы")
    private String fullName;

    @NotNull(message = "Пол не должен быть пустым")
    @Pattern(regexp = "мужской|женский", message = "Пол должен быть 'мужской' или 'женский'")
    private String gender;

    @NotNull(message = "Дата найма обязательна")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    @NotNull(message = "Дата рождения обязательна")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(message = "ID должности обязателен")
    private Integer positionId;

    @Min(value = 0, message = "Зарплата должна быть неотрицательной")
    private Integer salary = 0;

    private String contactInfo;

    private String specialAttributes;
}
