package v.rabetsky.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import v.rabetsky.annotations.AdminInterceptor;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@ComponentScan("v.rabetsky")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {

    private final ApplicationContext ctx;

    @Autowired
    public SpringConfig(ApplicationContext ctx) { this.ctx = ctx; }

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/zoo/**");
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver r = new SpringResourceTemplateResolver();
        r.setApplicationContext(ctx);
        r.setCharacterEncoding("UTF-8");
        r.setPrefix("/WEB-INF/views/");
        r.setSuffix(".html");
        r.setTemplateMode("HTML");
        return r;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine e = new SpringTemplateEngine();
        e.setTemplateResolver(templateResolver());
        e.setEnableSpringELCompiler(true);
        return e;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry reg) {
        ThymeleafViewResolver v = new ThymeleafViewResolver();
        v.setTemplateEngine(templateEngine());
        v.setCharacterEncoding("UTF-8");
        v.setContentType("text/html; charset=UTF-8");
        reg.viewResolver(v);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer c) {
        c.enable();
    }


    /** Администратор. */
    @Bean("adminDs")
    public DataSource adminDs() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/zoo_db");
        ds.setUsername("zoo_admin");
        ds.setPassword("zoo_password");
        return ds;
    }

    /** Читатель. */
    @Bean("readerDs")
    public DataSource readerDs() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/zoo_db");
        ds.setUsername("zoo_reader");
        ds.setPassword("reader_pass");
        return ds;
    }

    @Primary
    @Bean
    public DataSource routingDs(@Qualifier("adminDs")  DataSource admin,
                                @Qualifier("readerDs") DataSource reader) {

        RoleRoutingDataSource rds = new RoleRoutingDataSource();
        rds.setTargetDataSources(Map.of(Role.ADMIN, admin, Role.READER, reader));
        rds.setDefaultTargetDataSource(reader);
        return rds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource routingDs) {
        return new JdbcTemplate(routingDs);
    }
}
