package v.rabetsky.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Ловим любые JDBC-исключения, которые означают «нет прав».
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DataAccessException.class,
            BadSqlGrammarException.class })
    public String insufficientRights(Model m) {
        m.addAttribute("message",
                "У выбранной роли нет прав на изменение данных.");
        return "errors/noRights";           // /WEB-INF/views/errors/noRights.html
    }
}
