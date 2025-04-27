package v.rabetsky.models.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    private int id;
    private int animalId;
    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;
    @NotNull @DecimalMin("0.1")
    private BigDecimal weight;
    @NotNull @DecimalMin("0.1")
    private BigDecimal height;
    private String vaccinations;
    private String illnesses;
    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate checkupDate;
}