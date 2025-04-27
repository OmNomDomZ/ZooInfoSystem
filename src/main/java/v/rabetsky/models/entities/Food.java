package v.rabetsky.models.entities;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class Food {
    private Integer id;

    @NotNull(message = "Название не должен быть пустым")
    @Size(min = 5, max = 100, message = "Название должно быть от 5 до 100 символов")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Название должно содержать только буквы")
    private String name;

    private String foodTypeId;
    private Boolean isProducedInternally;
}
