package com.example.courseregistration.controller;

import com.example.courseregistration.model.Course;
import com.example.courseregistration.model.Enrollment;
import com.example.courseregistration.model.Student;
import com.example.courseregistration.repository.CourseRepository;
import com.example.courseregistration.repository.EnrollmentRepository;
import com.example.courseregistration.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/enroll/{courseId}")
    public String enroll(@PathVariable Long courseId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(userDetails == null || userDetails.getStudent() == null) return "redirect:/login";

        Student student = userDetails.getStudent();
        Course course = courseRepository.findById(courseId).orElseThrow();

        if (!enrollmentRepository.existsByStudentAndCourse(student, course)) {
            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollment.setEnroll_date(LocalDate.now());
            enrollmentRepository.save(enrollment);
        }
        return "redirect:/my-courses";
    }

    @GetMapping("/unenroll/{courseId}")
    public String unenroll(@PathVariable Long courseId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(userDetails == null || userDetails.getStudent() == null) return "redirect:/login";

        Student student = userDetails.getStudent();
        Course course = courseRepository.findById(courseId).orElseThrow();

        java.util.Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByStudentAndCourse(student, course);
        enrollmentOpt.ifPresent(enrollment -> enrollmentRepository.delete(enrollment));
        
        return "redirect:/my-courses";
    }

    @GetMapping("/my-courses")
    public String myCourses(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(userDetails == null || userDetails.getStudent() == null) return "redirect:/login";

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(userDetails.getStudent());
        List<Course> courses = enrollments.stream().map(Enrollment::getCourse).collect(Collectors.toList());
        
        model.addAttribute("courses", courses);
        return "my-courses";
    }
}
