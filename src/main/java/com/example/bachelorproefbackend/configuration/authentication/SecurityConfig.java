package com.example.bachelorproefbackend.configuration.authentication;

import com.example.bachelorproefbackend.usermanagement.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.parameters.P;
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
    private final UserRepository userRepository;


    public static final String ADMIN = "ROLE_ADMIN";
    public static final String COORDINATOR = "ROLE_COORDINATOR";
    public static final String PROMOTOR = "ROLE_PROMOTOR";
    public static final String STUDENT = "ROLE_STUDENT";
    public static final String CONTACT = "ROLE_CONTACT";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Where Spring has to look for users
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter((authenticationManagerBean()), userRepository);
        customAuthenticationFilter.setFilterProcessesUrl("/authentication/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.cors().and(); //Reference to bean in main
        http.addFilterBefore(new CustomAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests().antMatchers(POST,"/authentication/login").permitAll();
        http.authorizeRequests().antMatchers(GET, "/authentication/token/refresh").permitAll();
        http.authorizeRequests().antMatchers(GET, "/authentication/isRole/{role}").permitAll();

        http.authorizeRequests().antMatchers(GET, "/timing").authenticated();
        http.authorizeRequests().antMatchers(PUT, "/timing").hasAuthority(ADMIN);


        http.authorizeRequests().antMatchers(GET, "/subjectManagement/subjects").authenticated();
        http.authorizeRequests().antMatchers(POST, "/subjectManagement/subjects/preferredStudents").authenticated();
        http.authorizeRequests().antMatchers(GET, "/subjectManagement/subjects/nonApproved").hasAnyAuthority(ADMIN, COORDINATOR);
        http.authorizeRequests().antMatchers(GET, "/subjectManagement/subjects/byTargetAudience").authenticated();
        http.authorizeRequests().antMatchers(POST, "/subjectManagement/subjects").authenticated();
        http.authorizeRequests().antMatchers(GET, "/subjectManagement/subjects/{subjectId}").authenticated();
        http.authorizeRequests().antMatchers(DELETE, "/subjectManagement/subjects/{subjectId}").hasAnyAuthority(ADMIN, COORDINATOR);
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/subjects/{subjectId}").hasAnyAuthority(ADMIN, COORDINATOR);
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/subjects/{subjectId}/addPromotor").hasAnyAuthority(ADMIN, COORDINATOR, PROMOTOR, CONTACT);
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/subjects/{subjectId}/addTag").authenticated();
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/subjects/{subjectId}/addTargetAudience").authenticated();
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/subjects/{subjectId}/addCompany").authenticated();
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/subjects/{subjectId}/setApproved").hasAnyAuthority(ADMIN, COORDINATOR);
        http.authorizeRequests().antMatchers(GET, "/subjectManagement/subjects/stats").hasAnyAuthority(ADMIN, COORDINATOR);

        http.authorizeRequests().antMatchers(GET, "/subjectManagement/campus").authenticated();
        http.authorizeRequests().antMatchers(POST, "/subjectManagement/campus").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(DELETE, "/subjectManagement/campus/{campusId}").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/campus/{campusId}").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(POST, "/subjectManagement/campus/byFaculties").authenticated();
        http.authorizeRequests().antMatchers(POST, "/subjectManagement/campus/byEducations").authenticated();

        http.authorizeRequests().antMatchers(GET, "/subjectManagement/tag").authenticated();
        http.authorizeRequests().antMatchers(POST, "/subjectManagement/tag").authenticated();
        http.authorizeRequests().antMatchers(DELETE, "/subjectManagement/tag/{tagId}").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/tag/{tagId}").hasAuthority(ADMIN);

        http.authorizeRequests().antMatchers(GET, "/subjectManagement/faculty").authenticated();
        http.authorizeRequests().antMatchers(POST, "/subjectManagement/faculty").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(DELETE, "/subjectManagement/faculty/{facultyId}").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/faculty/{facultyId}").hasAuthority(ADMIN);

        http.authorizeRequests().antMatchers(GET, "/subjectManagement/education").authenticated();
        http.authorizeRequests().antMatchers(POST, "/subjectManagement/education").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(DELETE, "/subjectManagement/education/{educationId}").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/education/{educationId}").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/education/byFaculties").authenticated();

        http.authorizeRequests().antMatchers(GET, "/subjectManagement/targetAudience").authenticated();
        http.authorizeRequests().antMatchers(POST, "/subjectManagement/targetAudience").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(DELETE, "/subjectManagement/targetAudience/{targetAudienceId}").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(PUT, "/subjectManagement/targetAudience/{targetAudienceId}").hasAuthority(ADMIN);

        http.authorizeRequests().antMatchers(GET, "/userManagement/users").hasAnyAuthority(ADMIN, COORDINATOR, PROMOTOR);
        http.authorizeRequests().antMatchers(POST, "/userManagement/users").permitAll();
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/ownId").authenticated();
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/{userId}").authenticated();
        http.authorizeRequests().antMatchers(DELETE, "/userManagement/users/{userId}").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(PUT, "/userManagement/users/{userId}").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/{userId}/favouriteSubjects").authenticated();
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/{userId}/preferredSubjects").authenticated();
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/{userId}/boostedSubjects").authenticated();
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/{userId}/finalSubject").authenticated();
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/student").authenticated();
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/administrator").hasAnyAuthority(ADMIN, COORDINATOR, PROMOTOR);
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/promotor").hasAnyAuthority(ADMIN, COORDINATOR, PROMOTOR);
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/coordinator").hasAnyAuthority(ADMIN, COORDINATOR, PROMOTOR);
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/contact").hasAnyAuthority(ADMIN, COORDINATOR, PROMOTOR);
        http.authorizeRequests().antMatchers(PUT, "/userManagement/users/student/addTargetAudience").hasAnyAuthority(ADMIN, STUDENT);
        http.authorizeRequests().antMatchers(POST, "/userManagement/users/student/addPreferredSubject").hasAnyAuthority(ADMIN, STUDENT);
        http.authorizeRequests().antMatchers(POST, "/userManagement/users/student/addFavouriteSubject").hasAuthority(STUDENT);
        http.authorizeRequests().antMatchers(DELETE, "/userManagement/users/student/favouriteSubject").hasAnyAuthority(ADMIN, STUDENT);
        http.authorizeRequests().antMatchers(POST, "/userManagement/users/student/addFinalSubject").hasAnyAuthority(ADMIN, COORDINATOR);
        http.authorizeRequests().antMatchers(POST, "/userManagement/users/student/addRoleToUser").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(POST, "/userManagement/users/student/batch").hasAuthority(ADMIN);
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/stats").hasAnyAuthority(ADMIN, COORDINATOR);
        http.authorizeRequests().antMatchers(GET, "/userManagement/users/mySubjects").hasAnyAuthority(ADMIN, COORDINATOR, PROMOTOR, CONTACT);
        http.authorizeRequests().antMatchers(POST, "/userManagement/users/student/boost").hasAnyAuthority(ADMIN, COORDINATOR, PROMOTOR);

        http.authorizeRequests().antMatchers(GET, "/userManagement/company").authenticated();
        http.authorizeRequests().antMatchers(GET, "/userManagement/company/nonApproved").hasAnyAuthority(ADMIN, COORDINATOR);
        http.authorizeRequests().antMatchers(POST, "/userManagement/company").authenticated();
        http.authorizeRequests().antMatchers(GET, "/userManagement/company/{companyId}").authenticated();
        http.authorizeRequests().antMatchers(DELETE, "/userManagement/company/{companyId}").hasAnyAuthority(ADMIN, CONTACT);
        http.authorizeRequests().antMatchers(PUT, "/userManagement/company/{companyId}").hasAnyAuthority(ADMIN, CONTACT);
        http.authorizeRequests().antMatchers(PUT, "/userManagement/company/{companyId}/setApproved").hasAnyAuthority(ADMIN, COORDINATOR);
        http.authorizeRequests().antMatchers(POST, "/userManagement/company/{companyId}/addContact").hasAnyAuthority(ADMIN, CONTACT);
        http.authorizeRequests().antMatchers(GET, "/userManagement/company/{companyId}/subjects").authenticated();


        http.addFilter(customAuthenticationFilter);

    }









}
