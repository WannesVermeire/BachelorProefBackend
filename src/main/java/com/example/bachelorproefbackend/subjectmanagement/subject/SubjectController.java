package com.example.bachelorproefbackend.subjectmanagement.subject;

import com.example.bachelorproefbackend.subjectmanagement.tag.Tag;
import com.example.bachelorproefbackend.subjectmanagement.tag.TagService;
import com.example.bachelorproefbackend.usermanagement.company.Company;
import com.example.bachelorproefbackend.usermanagement.company.CompanyService;
import com.example.bachelorproefbackend.usermanagement.user.UserService;
import com.example.bachelorproefbackend.usermanagement.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="subjectManagement/subjects")
public class SubjectController {
    private final SubjectService subjectService;
    private final CompanyService companyService;
    private final UserService userService;
    private final TagService tagService;

    @Autowired
    public SubjectController(SubjectService subjectService, CompanyService companyService, UserService userService, TagService tagService) {
        this.subjectService = subjectService;
        this.companyService = companyService;
        this.userService = userService;
        this.tagService = tagService;
    }

    public UserEntity getUserObject(Authentication authentication){
        return userService.getUserByEmail(authentication.getName());
    }

    @GetMapping
    public List<Subject> getAllSubjects() {return subjectService.getAllSubjects();}

    @GetMapping(path="byTargetAudience")
    public List<Subject> getAllRelatedSubjects(Authentication authentication) {return subjectService.getAllRelatedSubjects(authentication);}

    @GetMapping(path="{subjectId}")
    public Subject getSubjectById(@PathVariable("subjectId") Long subject_id){
        return subjectService.getSubjectById(subject_id);
    }
//    @GetMapping(path="{subjectId}/students")
//    public List<User_entity> getAllStudents(@PathVariable("subjectId") long id){return subjectService.getAllUsers(id);}


    @PostMapping
    public void addNewSubject(@RequestParam String name, String description, int nrOfStudents, Authentication authentication) {
        subjectService.addNewSubject(new Subject(name, description, nrOfStudents), authentication);
    }

    @DeleteMapping(path="{subjectId}")
    public void deleteSubject(@PathVariable("subjectId") long id) {subjectService.deleteSubject(id);}

    @PutMapping(path="{subjectId}")
    public void updateSubject(@PathVariable("subjectId") long id,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false) int nrOfStudents) {
        subjectService.updateSubject(id, name, description, nrOfStudents);
    }

    @PutMapping(path="{subjectId}/addCompany")
    public void addCompany(@PathVariable("subjectId") long subjectId, @RequestParam long companyId, Authentication authentication){
        Company company = companyService.getCompanyById(companyId);
        subjectService.addCompany(subjectId, company, authentication);
    }

    @PutMapping(path="{subjectId}/addPromotor")
    public void addPromotor(@PathVariable("subjectId") long subjectId, @RequestParam long promotorId, Authentication authentication){
        UserEntity promotor = userService.getUserById(promotorId);
        subjectService.addPromotor(subjectId, promotor, authentication);
    }

    @PutMapping(path="{subjectId}/addTag")
    public void addTag(@PathVariable("subjectId") long subjectId, @RequestParam long tagId, Authentication authentication){
        Tag tag = tagService.getTagById(tagId);
        subjectService.addTag(subjectId, tag, getUserObject(authentication));
    }

    @PutMapping(path="{subjectId}/addTargetAudience")
    public void addTargetAudience(@PathVariable("subjectId") long subjectId, @RequestParam long facultyId, long educationId, long campusId){
        subjectService.addTargetAudience(subjectId, facultyId, educationId, campusId);
    }
}
