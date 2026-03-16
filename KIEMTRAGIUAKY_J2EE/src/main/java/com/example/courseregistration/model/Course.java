package com.example.courseregistration.model;
import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String image;
    private Integer credits;
    private String lecturer;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Course() {}

    public Course(Long id, String name, String image, Integer credits, String lecturer, Category category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.credits = credits;
        this.lecturer = lecturer;
        this.category = category;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
    public String getLecturer() { return lecturer; }
    public void setLecturer(String lecturer) { this.lecturer = lecturer; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
