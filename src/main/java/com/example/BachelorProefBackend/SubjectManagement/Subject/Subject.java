package com.example.BachelorProefBackend.SubjectManagement.Subject;

import com.example.BachelorProefBackend.UserManagement.Company.Company;
import com.example.BachelorProefBackend.UserManagement.Role.Role;
import com.example.BachelorProefBackend.UserManagement.User.User_entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table
public class Subject {
    @Id
    @SequenceGenerator(name="subject_sequence", sequenceName = "subject_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subject_sequence")
    private long id;
    private String name;
    private String description;
    private int nrOfStudents;
    private long targetAudienceId;
    private long tagId;
    @ManyToOne
    private Company company;
    @ManyToMany(mappedBy = "preferredSubjects")
    private Collection<User_entity> students = new ArrayList<>();
    @OneToMany(mappedBy = "finalSubject")
    private Collection<User_entity> finalStudents;


    public Subject() { }

    public Subject(String name, String description, int nrOfStudents) {
        this.name = name;
        this.description = description;
        this.nrOfStudents = nrOfStudents;
    }

    public long getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {
        return description;
    }
    public int getNrOfStudents() {return nrOfStudents;}
    public long getTargetAudienceId() {
        return targetAudienceId;
    }
    public long getTagId() {
        return tagId;
    }
    public Company getCompany() {return company;}
    public Collection<User_entity> getStudents() {return students;}

    public void setId(long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setDescription(String description) {this.description = description;}
    public void setNrOfStudents(int nrOfStudents) {this.nrOfStudents = nrOfStudents;}
    public void setTargetAudienceId(long targetAudienceId) {
        this.targetAudienceId = targetAudienceId;
    }
    public void setTagId(long tagId) {
        this.tagId = tagId;
    }
    public void setCompany(Company company){this.company = company;}
    public void setStudents(Collection<Role> roles) {this.students = students;}

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", nrOfStudents=" + nrOfStudents +
                ", targetAudienceId=" + targetAudienceId +
                ", tagId=" + tagId +
                ", company=" + company +
                ", students=" + students +
                ", finalStudents=" + finalStudents +
                '}';
    }
}
