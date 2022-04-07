package com.example.BachelorProefBackend.SubjectManagement.TargetAudience;

import com.example.BachelorProefBackend.SubjectManagement.Campus.Campus;
import com.example.BachelorProefBackend.SubjectManagement.Education.Education;
import com.example.BachelorProefBackend.SubjectManagement.Faculty.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TargetAudienceRepository extends JpaRepository<TargetAudience, Long> {
    List<TargetAudience> findAllByFaculty(Faculty faculty);

    List<TargetAudience> findAllByEducation(Education education);

    List<TargetAudience> findAllByCampus(Campus campus);

    List<TargetAudience> findAllByCampusId(long id);

}
