package com.example.BachelorProefBackend.Authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration //recognised by Spring as settings
@EnableWebSecurity
@RequiredArgsConstructor //Automatically provides constructor for userDetailService
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Where Spring has to look for users
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter((authenticationManagerBean()));
        customAuthenticationFilter.setFilterProcessesUrl("/authentication/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        //http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()); //niet in gebruik (bean in main)
        http.cors().and(); //verwijzing naar bean in main
        http.addFilterBefore(new CustomAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);



        // Everyone has access
        http.authorizeRequests().antMatchers("/authentication/login/**").permitAll(); //login
        http.authorizeRequests().antMatchers("/authentication/token/refresh/**").permitAll(); //refresh
        http.authorizeRequests().antMatchers(POST, "/userManagement/users").permitAll(); //register
        http.authorizeRequests().antMatchers(GET, "/subjectManagement/subjects/**").authenticated();
        http.authorizeRequests().antMatchers(POST, "/subjectManagement/subjects/**").authenticated();
        http.authorizeRequests().antMatchers( "/userManagement/users/student/**").authenticated(); //service layer checks that student can only access his own data

        // Access restricted
        //http.authorizeRequests().antMatchers("/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/subjectManagement/subjects/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_COORDINATOR");
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/subjects/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_COORDINATOR");
        //http.authorizeRequests().antMatchers(DELETE, "/subjectManagement/subjects/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_COORDINATOR");

        http.authorizeRequests().antMatchers("/subjectManagement/subjectAssignment/**").hasAnyAuthority("ROLE_COORDINATOR");


//        http.authorizeRequests().anyRequest().authenticated();

        http.addFilter(customAuthenticationFilter);

    }









}
