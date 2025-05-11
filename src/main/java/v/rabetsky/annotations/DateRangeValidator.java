package v.rabetsky.annotations;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {
    private String startField;
    private String endField;
    private String message;

    @Override
    public void initialize(DateRange constraint){
        this.startField = constraint.start();
        this.endField   = constraint.end();
        this.message    = constraint.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext ctx){
        try {
            BeanWrapper bw = new BeanWrapperImpl(value);
            LocalDate start = (LocalDate) bw.getPropertyValue(startField);
            LocalDate end   = (LocalDate) bw.getPropertyValue(endField);

            // если одно из полей null — пропускаем (будет отловлено @NotNull)
            if (start == null || end == null) return true;
            boolean ok = !start.isAfter(end);
            if (!ok) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(endField)
                        .addConstraintViolation();
            }
            return ok;
        } catch (Exception ex) {
            return true;
        }
    }
}

