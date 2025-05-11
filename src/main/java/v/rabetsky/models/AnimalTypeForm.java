package v.rabetsky.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AnimalTypeForm {
    @NotBlank(message = "Введите название вида")
    private String type;

    @NotNull(message = "Выберите тип питания")
    private Integer dietTypeId;
}