package com.example.bachelorproefbackend.subjectmanagement.faculty;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Faculty findByName(String name);
}
