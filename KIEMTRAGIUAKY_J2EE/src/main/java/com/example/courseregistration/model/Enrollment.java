package com.example.courseregistration.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    private LocalDate enroll_date;

    public Enrollment() {}

    public Enrollment(Long id, Student student, Course course, LocalDate enroll_date) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.enroll_date = enroll_date;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public LocalDate getEnroll_date() { return enroll_date; }
    public void setEnroll_date(LocalDate enroll_date) { this.enroll_date = enroll_date; }
}
