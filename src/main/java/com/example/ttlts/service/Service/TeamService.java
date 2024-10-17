package com.example.ttlts.service.Service;

import com.example.ttlts.entity.Team;
import com.example.ttlts.repository.TeamRepository;
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

    // Thay đổi trưởng phòng
    public Team changeManager(int teamId, int newManagerId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        team.setManagerId(newManagerId);
        team.setUpdateTime(LocalDateTime.now());
        return teamRepository.save(team);
    }

}
