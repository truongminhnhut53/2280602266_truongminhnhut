package com.example.courseregistration.controller;

import com.example.courseregistration.model.Role;
import com.example.courseregistration.model.Student;
import com.example.courseregistration.repository.RoleRepository;
import com.example.courseregistration.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AuthController {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        Role defaultRole = roleRepository.findByName("STUDENT").orElseGet(() -> {
            Role role = new Role();
            role.setName("STUDENT");
            return roleRepository.save(role);
        });
        
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        student.setRoles(roles);
        
        studentRepository.save(student);
        return "redirect:/login";
    }
}
