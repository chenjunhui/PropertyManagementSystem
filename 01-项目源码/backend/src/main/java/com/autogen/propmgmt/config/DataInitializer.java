package com.autogen.propmgmt.config;

import com.autogen.propmgmt.entity.User;
import com.autogen.propmgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        List<User> users = userRepository.findAll();
        boolean updated = false;
        for (User user : users) {
            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                updated = true;
            }
        }
        if (updated) {
            userRepository.saveAll(users);
            log.info("Migrated plain-text passwords to BCrypt");
        }
    }
}
