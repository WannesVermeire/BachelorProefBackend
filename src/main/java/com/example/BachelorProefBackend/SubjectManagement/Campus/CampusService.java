package com.example.BachelorProefBackend.SubjectManagement.Campus;

import com.example.BachelorProefBackend.SubjectManagement.Education.Education;
import com.example.BachelorProefBackend.SubjectManagement.Education.EducationRepository;
import com.example.BachelorProefBackend.SubjectManagement.Faculty.Faculty;
import com.example.BachelorProefBackend.SubjectManagement.TargetAudience.TargetAudience;
import com.example.BachelorProefBackend.SubjectManagement.TargetAudience.TargetAudienceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class CampusService {

    private final CampusRepository campusRepository;
    private final TargetAudienceService targetAudienceService;
    
    @Autowired
    public CampusService(CampusRepository campusRepository, TargetAudienceService targetAudienceService){
        this.campusRepository = campusRepository;
        this.targetAudienceService = targetAudienceService;
    }

    @GetMapping
    public List<Campus> getAllCampuses() {return campusRepository.findAll();}

    @GetMapping
    public List<Campus> getAllCampusesByFaculty(Faculty faculty){
        List<TargetAudience> targets = targetAudienceService.getAllByFaculty(faculty);
        List<Campus> result = new ArrayList<>();
        for (TargetAudience t : targets) {
            result.add(t.getCampus());
        }
        return result;
    }

    @GetMapping
    public List<Campus> getAllCampusesByEducation(Education education){
        List<TargetAudience> targets = targetAudienceService.getAllByEducation(education);
        List<Campus> result = new ArrayList<>();
        for (TargetAudience t : targets) {
            result.add(t.getCampus());
        }
        return result;
    }
    
    @PostMapping
    public void addNewCampus(Campus campus) {
        campusRepository.save(campus);
    }

    @DeleteMapping
    public void deleteCampus(long id){
        campusRepository.deleteById(id);
    }

    @PutMapping
    public void updateCampus(long id, String name, String address){
        if(!campusRepository.existsById(id)) throw new IllegalStateException("Campus does not exist (id: "+id+")");
        Campus campus = campusRepository.getById(id);
        if(name != null && name.length()>0) campus.setName(name);
        if(address != null && address.length()>0) campus.setAddress(address);
    }

}
