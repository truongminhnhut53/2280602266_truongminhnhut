package com.example.courseregistration;

import com.example.courseregistration.model.Category;
import com.example.courseregistration.model.Course;
import com.example.courseregistration.model.Role;
import com.example.courseregistration.model.Student;
import com.example.courseregistration.repository.CategoryRepository;
import com.example.courseregistration.repository.CourseRepository;
import com.example.courseregistration.repository.RoleRepository;
import com.example.courseregistration.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.findByName("ADMIN").orElse(null);
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole = roleRepository.save(adminRole);
        }

        Role studentRole = roleRepository.findByName("STUDENT").orElse(null);
        if (studentRole == null) {
            studentRole = new Role();
            studentRole.setName("STUDENT");
            studentRole = roleRepository.save(studentRole);
        }

        if (studentRepository.findByUsername("admin").isEmpty()) {
            Student admin = new Student();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@example.com");
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);
            studentRepository.save(admin);
        }

        if (studentRepository.findByUsername("student").isEmpty()) {
            Student student = new Student();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("student"));
            student.setEmail("student@example.com");
            Set<Role> studentRoles = new HashSet<>();
            studentRoles.add(studentRole);
            student.setRoles(studentRoles);
            studentRepository.save(student);
        }

        if (categoryRepository.count() == 0) {
            Category cntt = new Category();
            cntt.setName("Công nghệ thông tin");
            cntt = categoryRepository.save(cntt);

            Category kt = new Category();
            kt.setName("Kinh tế");
            kt = categoryRepository.save(kt);

            Course c1 = new Course();
            c1.setName("Lập trình Web với Spring Boot");
            c1.setCredits(3);
            c1.setLecturer("Nguyễn Văn A");
            c1.setImage("https://miro.medium.com/v2/resize:fit:1200/1*CqA3tQy9F94a6wJv66V51g.png");
            c1.setCategory(cntt);
            courseRepository.save(c1);

            Course c2 = new Course();
            c2.setName("Cơ sở dữ liệu nâng cao");
            c2.setCredits(4);
            c2.setLecturer("Trần Thị B");
            c2.setImage("https://www.mysql.com/common/logos/logo-mysql-170x115.png");
            c2.setCategory(cntt);
            courseRepository.save(c2);
            
            Course c3 = new Course();
            c3.setName("Lập trình Java căn bản");
            c3.setCredits(3);
            c3.setLecturer("Lê Hữu C");
            c3.setImage("https://upload.wikimedia.org/wikipedia/en/3/30/Java_programming_language_logo.svg");
            c3.setCategory(cntt);
            courseRepository.save(c3);

            Course c4 = new Course();
            c4.setName("Kinh tế vĩ mô");
            c4.setCredits(3);
            c4.setLecturer("Phạm Văn D");
            c4.setImage("https://www.investopedia.com/thmb/9zGkXG_E0v7V5oB2X_q2rT_C95A=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/macroeconomics-12f71f11cb2049daba62b083a30ab711.jpg");
            c4.setCategory(kt);
            courseRepository.save(c4);
        }
    }
}
