package v.rabetsky.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SupplierFood {
    private int id;

    private int supplierId;

    @NotNull(message = "Нужно выбрать продукт")
    private Integer foodId;

    @NotNull(message = "Дата доставки обязательна")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;

    @NotNull @DecimalMin("0.01")
    private BigDecimal quantity;

    @NotNull @DecimalMin("0.00")
    private BigDecimal price;
}
