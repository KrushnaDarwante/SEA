package com.sea.repository;

import com.sea.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmployeeId(String employeeId);
    List<Teacher> findByDepartment(String department);
    List<Teacher> findByIsSupervisorTrue();
    Boolean existsByEmployeeId(String employeeId);
}
