package com.example.BachelorProefBackend.UserManagement.User;
import com.example.BachelorProefBackend.SubjectManagement.Subject.Subject;
import com.example.BachelorProefBackend.SubjectManagement.TargetAudience.TargetAudience;
import com.example.BachelorProefBackend.UserManagement.Role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Table
@Entity
@NoArgsConstructor
public class User_entity {

    @Id
    @SequenceGenerator(name="user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String telNr;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER) //load all roles every time we load a user
    private Collection<Role> roles = new ArrayList<>();
    @ManyToMany
    @JsonIgnore //No recursion between user en subject, showing data over and over again
    @JoinTable(name="subject_preference")
    private Collection<Subject> preferredSubjects = new ArrayList<>();
    @ManyToOne //TwoToOne
    private Subject finalSubject; // Also valid for promotor
    @ManyToOne
    private TargetAudience targetAudience;


    public User_entity(String firstName, String lastName, String email, String telNr, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telNr = telNr;
        this.password = password;
    }

    public void addPreferredSubject(Subject subject){
        preferredSubjects.add(subject);
    }


    @Override
    public String toString() {
        return "User_entity{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", telNr='" + telNr + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", preferredSubjects=" + preferredSubjects +
                ", finalSubject=" + finalSubject +
                ", targetAudience=" + targetAudience +
                '}';
    }


}