package com.example.courseregistration.security;

import com.example.courseregistration.model.Role;
import com.example.courseregistration.model.Student;
import com.example.courseregistration.repository.RoleRepository;
import com.example.courseregistration.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        
        if (email == null) {
            email = "user_" + System.currentTimeMillis() + "@no-email.com";
        }

        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        Student student;
        if (studentOpt.isEmpty()) {
            student = new Student();
            student.setEmail(email);
            student.setUsername(email.split("@")[0]);
            student.setPassword("oauth2-user");

            Role userRole = roleRepository.findByName("STUDENT").orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("STUDENT");
                return roleRepository.save(newRole);
            });
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            student.setRoles(roles);
            student = studentRepository.save(student);
        } else {
            student = studentOpt.get();
        }
        
        return new CustomUserDetails(student, oAuth2User.getAttributes());
    }
}
