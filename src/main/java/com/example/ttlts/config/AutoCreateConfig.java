package com.example.ttlts.config;

import com.example.ttlts.entity.Permissions;
import com.example.ttlts.entity.Role;
import com.example.ttlts.entity.Team;
import com.example.ttlts.entity.User;
import com.example.ttlts.repository.PermissionsRepository;
import com.example.ttlts.repository.RoleRepository;
import com.example.ttlts.repository.TeamRepository;
import com.example.ttlts.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AutoCreateConfig {

    PasswordEncoder passwordEncoder;

    List<String> roles = Arrays.asList("Leader", "Designer", "Deliver", "Manager", "Employee", "Admin");
    List<String> permissions = Arrays.asList("Read", "Write", "Delete", "ReadWrite", "Admin");
    List<String> teams = Arrays.asList("Team Leader", "Team Designer", "Team Deliver", "Team Manager", "Team Employee", "Team Admin");

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, PermissionsRepository permissionsRepository, TeamRepository teamRepository,RoleRepository roleRepository) {
        return args -> {
            for (String roleName : roles) {
                if (roleRepository.findByRoleName(roleName).isEmpty()) {
                    Role role = Role.builder()
                            .roleCode(roleName)
                            .roleName(roleName)
                            .build();
                    roleRepository.save(role);
                    System.out.println("Inserted role: " + roleName);
                }
            }
            for (String teamName : teams) {
                if (teamRepository.findByName(teamName).isEmpty()) {
                    Team team = Team.builder()
                            .createTime(LocalDateTime.now())
                            .description(teamName)
                            .managerId(1)
                            .name(teamName)
                            .numberOfMember(1)
                            .updateTime(LocalDateTime.now())
                            .build();
                    teamRepository.save(team);
                    System.out.println("Inserted: " + teamName);
                }
            }

            if (userRepository.findByUsername("Admin").isEmpty()) {
                Role adminRole = roleRepository.findByRoleName("Admin")
                        .orElseGet(() -> roleRepository.save(
                                Role.builder().roleCode("Admin").roleName("Admin").build()
                        ));

                User admin = User.builder()
                        .username("Admin")
                        .password(passwordEncoder.encode("admin"))
                        .email("admin@gmail.com")
                        .fullName("Administrator")
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isActive(true)
                        .teamId(6)
                        .build();
                userRepository.save(admin);

                permissionsRepository.save(Permissions.builder()
                        .userId(admin.getId())
                        .roleId(adminRole.getId())
                        .build());
            }
        };
    }
}