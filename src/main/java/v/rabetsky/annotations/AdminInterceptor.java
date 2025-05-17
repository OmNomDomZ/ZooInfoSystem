package v.rabetsky.annotations;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import v.rabetsky.config.Role;
import v.rabetsky.config.RoleContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse resp,
                             Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            if (hm.getMethodAnnotation(AdminOnly.class) != null) {
                if (RoleContext.get() != Role.ADMIN) {
                    throw new DataRetrievalFailureException(
                            "У выбранной роли нет прав на изменение данных."
                    );
                }
            }
        }
        return true;
    }
}