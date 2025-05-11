package v.rabetsky.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * На каждый HTTP-запрос кладёт выбранную пользователем роль в RoleContext,
 * чтобы DataSource мог понять, к какому пользователю БД обращаться.
 */
public class RoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpSession session = ((HttpServletRequest) req).getSession(false);
        RoleContext.set(
                session != null && "ADMIN".equals(session.getAttribute("ROLE"))
                        ? Role.ADMIN
                        : Role.READER);

        try {
            chain.doFilter(req, res);
        } finally {
            RoleContext.clear();
        }
    }
}
