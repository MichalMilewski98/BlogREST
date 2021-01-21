package com.example.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private static final String USERS_SQL_QUERY = "select username,password,active from users where username = ?";
    private static final String AUTHORITIES_SQL_QUERY = "select users.username, roles.role\n" +
            "from users\n" +
            "inner join user_roles on (users.id = user_roles.user_id)\n" +
            "inner join roles on (user_roles.role_id = roles.id)\n" +
            "where users.username = ?;";

    @Autowired
    public SpringSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public BCryptPasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()   // if enabled must use post method to logout and additional configuration needed
                .headers().frameOptions().disable() // fix H2 console access
                .and()
                .authorizeRequests()
                .antMatchers("/**").hasRole("ADMIN")
                .antMatchers("/new_post/**", "/edit_post/**", "/post/**", "/delete_post/**", "/my_posts/**", "/comment/**", "/new_comment/**").hasRole("USER")
//                .antMatchers("/new_post/**", "/edit_post/**", "/post/**").hasRole("USER")
//                .antMatchers("/deletePost/**").hasRole("USER")
//                .antMatchers("/deletePost/**").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").loginProcessingUrl("/login") // same as default implicit configuration
                .usernameParameter("username").passwordParameter("password") // same as default implicit configuration
                .defaultSuccessUrl("/").failureUrl("/login?error") // same as default implicit configuration
                .permitAll()
                .and()
                .rememberMe().rememberMeParameter("remember-me")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout") // same as default implicit configuration
                .permitAll()
                .and()
                .sessionManagement().maximumSessions(1);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        authenticationManagerBuilder
                .jdbcAuthentication()
                .usersByUsernameQuery(USERS_SQL_QUERY) // not really necessary, as users table follows default Spring Security User schema
                .authoritiesByUsernameQuery(AUTHORITIES_SQL_QUERY)  // a must as using customized authorities table, many to many variation
                .dataSource(dataSource)
                .passwordEncoder(bcryptEncoder());

//        authenticationManagerBuilder
//                .inMemoryAuthentication()
//                .withUser("user").password(bcryptEncoder().encode("password")).roles("USER").and()
//                .withUser("admin").password(bcryptEncoder().encode("password")).roles("USER", "ADMIN");
    }
}
