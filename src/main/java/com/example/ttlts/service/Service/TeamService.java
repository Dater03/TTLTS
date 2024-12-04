package com.example.ttlts.service.Service;

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
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class TeamService {
    TeamRepository teamRepository;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PermissionsRepository permissionsRepository;

    public Team createTeam(Team team) {
        team.setNumberOfMember(0);
        team.setCreateTime(LocalDateTime.now());
        return teamRepository.save(team);
    }

    public Team updateTeam(int id, Team updatedTeam) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        team.setName(updatedTeam.getName());
        team.setDescription(updatedTeam.getDescription());
        team.setUpdateTime(LocalDateTime.now());
        return teamRepository.save(team);
    }

    public void deleteTeam(int teamId) {
        Team deleteTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        teamRepository.delete(deleteTeam);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll().stream().toList();
    }

    public Team setManager(int teamId, int userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        team.setManagerId(userId);

        User user = userRepository.findById(userId);
        if (user != null) {
            Role leaderRole = roleRepository.findByRoleName("Leader")
                    .orElseThrow(() -> new RuntimeException("Role 'Leader' not found"));

            Permissions permissions = Permissions.builder()
                    .userId(user.getId())
                    .roleId(leaderRole.getId())
                    .build();
            permissionsRepository.save(permissions);
        }
        return teamRepository.save(team);
    }

    // Thay đổi trưởng phòng
    public Team changeManager(int teamId, int newManagerId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        team.setManagerId(newManagerId);
        team.setUpdateTime(LocalDateTime.now());
        return teamRepository.save(team);
    }

}
