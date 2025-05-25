package v.rabetsky.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import v.rabetsky.annotations.DateRange;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DateRange(start="birthDate", end="hireDate",
        message="Дата рождения не должна быть позже даты найма")
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
    @PastOrPresent(message = "Дата найма не может быть в будущем")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    @NotNull(message = "Дата рождения обязательна")
    @Past(message = "Дата рождения должна быть в прошлом")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(message = "ID должности обязателен")
    private Integer positionId;

    @Min(value = 0, message = "Зарплата должна быть неотрицательной")
    private Integer salary = 0;

    private String contactInfo;

    private String specialAttributes;

    // ветеринар
    private Long   licenseNumber;
    private String specialization;

    // смотритель
    private String section;

    // уборщик
    private String cleaningShift;
    private String area;
    private String equipment;

    // администратор
    private String department;
    private String phone;
}
