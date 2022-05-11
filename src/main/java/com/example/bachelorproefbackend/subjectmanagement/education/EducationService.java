package com.example.bachelorproefbackend.subjectmanagement.education;


import com.example.bachelorproefbackend.subjectmanagement.faculty.Faculty;
import com.example.bachelorproefbackend.subjectmanagement.targetaudience.TargetAudience;
import com.example.bachelorproefbackend.subjectmanagement.targetaudience.TargetAudienceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class EducationService {

    private final EducationRepository educationRepository;
    private final TargetAudienceService targetAudienceService;

    @Autowired
    public EducationService(EducationRepository educationRepository, TargetAudienceService targetAudienceService){
        this.educationRepository = educationRepository;
        this.targetAudienceService = targetAudienceService;
    }

    public List<Education> getAllEducations() {return educationRepository.findAll();}

    public Set<Education> getAllEducationsByFaculties(Faculty [] faculties){
        Set<Education> result = new HashSet<>();
        for (Faculty f : faculties){
            List<TargetAudience> targets = targetAudienceService.getAllByFaculty(f);
            for (TargetAudience t : targets) {
                result.add(t.getEducation());
            }
        }
        return result;
    }

    public void addNewEducation(Education education) {
        educationRepository.save(education);
    }

    public void deleteEducation(long id){
        educationRepository.deleteById(id);
    }

    public void updateEducation(long id, String name){
        if(!educationRepository.existsById(id)) throw new IllegalStateException("Education does not exist (id: "+id+")");
        Education education = educationRepository.getById(id);
        if(name != null && name.length()>0) education.setName(name);

    }

}
