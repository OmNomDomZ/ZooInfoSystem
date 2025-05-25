package v.rabetsky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private int id;
    private String fullName;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private Integer positionId;
    private String positionName;
    private Integer salary;
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