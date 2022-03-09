package loader.security;

import loader.entity.User;
import loader.custom.ConfigProperties;
import static loader.entity.UserRole.*;
import loader.repository.UserRepository;

import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfigProperties configProperties;
    private final UserDetailsService userDetailsService;

    public ApplicationSecurityConfig(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ConfigProperties configProperties,
            UserDetailsService userDetailsService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.configProperties = configProperties;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                //.requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .authorizeRequests()
                .antMatchers("/recovery", "/registration", "/css/*").permitAll()

                .antMatchers("/admin").hasRole(ADMIN.name())
                .antMatchers("/teacher", "/testVariants").hasRole(TEACHER.name())
                .antMatchers("/variant", "/speaking").hasRole(EXAM.name())
                .anyRequest().authenticated()

                .and()
                .formLogin().permitAll()
                .loginPage("/login").permitAll()
                .failureUrl("/login?error=true")
                .successHandler(myAuthenticationSuccessHandler());
    }

    @PostConstruct
    private void generateUsers(){
        User admin = new User(
                configProperties.getAdminEmail(),
                configProperties.getAdminUsername(),
                passwordEncoder.encode(configProperties.getAdminPassword()),
                true,
                ADMIN);
        userRepository.save(admin);
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MyAuthenticationSuccessHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

}