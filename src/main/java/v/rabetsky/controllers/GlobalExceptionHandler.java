package v.rabetsky.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Ловим любые JDBC-исключения, которые означают «нет прав».
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DataAccessException.class,
            BadSqlGrammarException.class })
    public String insufficientRights(Exception ex, Model m) {
        log.warn("Database error intercepted: {}", ex.getMessage());
        if (log.isDebugEnabled()) {
            log.debug("Full stacktrace:", ex);
        }
        m.addAttribute("message",
                "У выбранной роли нет прав на изменение данных или введены некорректные значения.");
        return "errors/noRights";
    }
}
