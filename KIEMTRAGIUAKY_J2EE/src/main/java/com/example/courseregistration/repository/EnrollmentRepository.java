package com.example.courseregistration.repository;
import com.example.courseregistration.model.Enrollment;
import com.example.courseregistration.model.Student;
import com.example.courseregistration.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);
    boolean existsByStudentAndCourse(Student student, Course course);
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
}
