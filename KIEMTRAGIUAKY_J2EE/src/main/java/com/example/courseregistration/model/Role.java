package com.example.courseregistration.model;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;
    private String name; // e.g. "ADMIN", "STUDENT"
    
    @ManyToMany(mappedBy = "roles")
    private Set<Student> students;

    public Role() {}

    public Role(Long role_id, String name, Set<Student> students) {
        this.role_id = role_id;
        this.name = name;
        this.students = students;
    }

    public Long getRole_id() { return role_id; }
    public void setRole_id(Long role_id) { this.role_id = role_id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Set<Student> getStudents() { return students; }
    public void setStudents(Set<Student> students) { this.students = students; }
}
