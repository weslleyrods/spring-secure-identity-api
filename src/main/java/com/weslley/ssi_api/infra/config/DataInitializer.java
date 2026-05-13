package com.weslley.ssi_api.infra.config;

import com.weslley.ssi_api.model.UserModel;
import com.weslley.ssi_api.model.enums.UserRole;
import com.weslley.ssi_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.initial.email}")
    private String adminEmail;
    @Value("${admin.initial.password}")
    private String adminPassword;
    @Value("${admin.initial.create}")
    private boolean createAdmin;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!createAdmin) return;
        if(userRepository.findByEmail(adminEmail) == null){
            UserModel user = new UserModel();
            user.setName("admin");
            user.setEmail(adminEmail);
            user.setPassword(passwordEncoder.encode(adminPassword));
            user.setRole(UserRole.ADMIN);
            userRepository.save(user);
            System.out.println("Initial admin created successfully: " + adminEmail);

        }
    }
}
