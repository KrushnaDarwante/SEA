package com.sea.repository;

import com.sea.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRollNo(String rollNo);
    List<Student> findByDepartment(String department);
    List<Student> findByYear(Integer year);
    List<Student> findByDepartmentAndYear(String department, Integer year);
    Boolean existsByRollNo(String rollNo);
}
