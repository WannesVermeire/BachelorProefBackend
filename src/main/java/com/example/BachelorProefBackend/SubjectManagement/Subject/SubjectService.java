package com.example.BachelorProefBackend.SubjectManagement.Subject;

import com.example.BachelorProefBackend.SubjectManagement.Tag.Tag;
import com.example.BachelorProefBackend.UserManagement.Company.Company;
import com.example.BachelorProefBackend.UserManagement.Role.Role;
import com.example.BachelorProefBackend.UserManagement.Role.RoleRepository;
import com.example.BachelorProefBackend.UserManagement.User.UserService;
import com.example.BachelorProefBackend.UserManagement.User.User_entity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;


    @Autowired
    public SubjectService(SubjectRepository subjectRepository, UserService userService, RoleRepository roleRepository) {
        this.subjectRepository = subjectRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }




    @GetMapping
    public List<Subject> getAllSubjects() {return subjectRepository.findAll();}

    @GetMapping
    public Subject getSubjectById(long subject_id) {
        return subjectRepository.findById(subject_id);
    }

    @DeleteMapping
    public void deleteSubject(long id){
        if(!subjectRepository.existsById(id)) throw new IllegalStateException("Subject does not exist (id: "+id+")");
        subjectRepository.deleteById(id);
    }

    @PostMapping
    public void addNewSubject(Subject subject, Authentication authentication){
        User_entity activeUser = userService.getUserByEmail(authentication.getName());
        Role contact = roleRepository.findByName("ROLE_CONTACT");
        if(activeUser.getRoles().contains(contact)){
            //TODO Find a better solution for this
            subject.setCompany(activeUser.getFinalSubject().getCompany());
        }
        subjectRepository.save(subject);
    }

    @PutMapping
    public void updateSubject(long id, String name, String description, int nrOfStudents) {
        if (!subjectRepository.existsById(id)) throw new IllegalStateException("Subject does not exist (id: " + id + ")");
        Subject subject = subjectRepository.getById(id);
        if(name != null && name.length()>0 && !Objects.equals(subject.getName(), name)) subject.setName(name);
        if(description != null && description.length()>0 && !Objects.equals(subject.getDescription(), description)) subject.setDescription(description);
        if(nrOfStudents==0) throw new RuntimeException("Number of students can not be equal to zero");
        else if (nrOfStudents>2) throw new RuntimeException("Number of students can not be over 2");
        else subject.setNrOfStudents(nrOfStudents);
    }

    @PutMapping
    public void addCompany(long subjectId, Company company, Authentication authentication){
        User_entity activeUser = userService.getUserByEmail(authentication.getName());
        Role admin = roleRepository.findByName("ROLE_ADMIN");
        Subject subject = subjectRepository.findById(subjectId);
        if(activeUser.getRoles().contains(admin) || company.getContacts().contains(activeUser)){
            log.info("Adding company {} to subject {}", company.getName(), subject.getName());
            if(subject.getCompany() != null){
                throw new RuntimeException("Subject already has a company: "+subject.getCompany().getName());
            }
            else{
                subject.setCompany(company);
            }
        }
    }

    @PutMapping
    public void addPromotor(long subjectId, User_entity promotor, Authentication authentication){
        User_entity activeUser = userService.getUserByEmail(authentication.getName());
        Role admin = roleRepository.findByName("ROLE_ADMIN");
        Role promotorROLE = roleRepository.findByName("ROLE_PROMOTOR");
        Role coordinator = roleRepository.findByName("ROLE_COORDINATOR");
        Subject subject = subjectRepository.findById(subjectId);
        if(activeUser.getRoles().contains(admin) || activeUser.getRoles().contains(coordinator) || activeUser.getFinalSubject().equals(subject) || activeUser.equals(promotor)){
            if(promotor.getRoles().contains(promotorROLE)){
                if(subject.getPromotor() != null){
                    throw new RuntimeException("Subject already has a promotor: "+subject.getPromotor().getFirstName());
                }
                else{
                    log.info("Adding promotor {} to subject {}", promotor.getFirstName(), subject.getName());
                    subject.setPromotor(promotor);
                }
            }
            else{throw new RuntimeException("Only users with role: promotor can be added");}
        }
        else{throw new RuntimeException("Student can only add to his own final subject, Promotor can only add himself");}
    }

    @PutMapping
    public void addTag(long subjectId, Tag tag, User_entity activeUser){
        Subject subject = subjectRepository.findById(subjectId);
        Role student = roleRepository.findByName("ROLE_STUDENT");
        Role contact = roleRepository.findByName("ROLE_CONTACT");
        if(activeUser.getRoles().contains(student) || activeUser.getRoles().contains(contact)){
            if(!subject.equals(activeUser.getFinalSubject()))
                throw new RuntimeException("Student and Contact can only add to their finalSubject.");
        }
        log.info("Adding tag {} to subject {}", tag.getName(), subject.getName());
        subject.addTag(tag);
    }

}
