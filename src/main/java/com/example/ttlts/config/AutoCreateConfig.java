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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AutoCreateConfig {

    PasswordEncoder passwordEncoder;

    List<String> roles = Arrays.asList("Leader", "Designer", "Deliver", "Manager", "Employee", "Admin");
    List<String> teams = Arrays.asList("Team Leader", "Team Designer", "Team Deliver", "Team Manager", "Team Employee", "Team Admin");

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, PermissionsRepository permissionsRepository, TeamRepository teamRepository, RoleRepository roleRepository) {
        return args -> {
            createRoles(roleRepository);
            Team adminTeam = createTeam(teamRepository, "Team Admin");

            User adminUser = createUser(userRepository, adminTeam);
            createPermissions(permissionsRepository, adminUser, roleRepository);
        };
    }

    private void createRoles(RoleRepository roleRepository) {
        roles.forEach(roleName -> {
            roleRepository.findByRoleName(roleName).orElseGet(() -> {
                Role newRole = Role.builder()
                        .roleCode("ROLE_" + roleName.toUpperCase())
                        .roleName(roleName)
                        .build();
                roleRepository.save(newRole);
                System.out.println("Inserted role: " + roleName);
                return newRole;
            });
        });
    }

    private Team createTeam(TeamRepository teamRepository, String teamName) {
        return teamRepository.findByName(teamName).orElseGet(() -> {
            Team team = Team.builder()
                    .name(teamName)
                    .description(teamName)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .managerId(1)
                    .numberOfMember(1)
                    .build();
            teamRepository.save(team);
            return team;
        });
    }

    private User createUser(UserRepository userRepository, Team adminTeam) {
        return userRepository.findByUsername("Admin").orElseGet(() -> {
            User newAdmin = User.builder()
                    .username("Admin")
                    .password(passwordEncoder.encode("admin"))
                    .fullName("Administrator")
                    .email("admin@gmail.com")
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .teamId(adminTeam.getId())
                    .isActive(true)
                    .build();
            userRepository.save(newAdmin);
            return newAdmin;
        });
    }

    private void createPermissions(PermissionsRepository permissionsRepository, User adminUser, RoleRepository roleRepository) {
        Role adminRole = roleRepository.findByRoleName("Admin").orElseThrow();
        permissionsRepository.findByUserIdAndRoleId(adminUser.getId(), adminRole.getId()).orElseGet(() -> {
            Permissions permissions = Permissions.builder()
                    .userId(adminUser.getId())
                    .roleId(adminRole.getId())
                    .build();
            permissionsRepository.save(permissions);
            return permissions;
        });
    }
}
