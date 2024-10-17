package com.example.ttlts.controller;

import com.example.ttlts.entity.Team;
import com.example.ttlts.entity.User;
import com.example.ttlts.service.Service.TeamService;
import com.example.ttlts.service.Service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/team")
public class TeamController {
    TeamService teamService;
    UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        Team createdTeam = teamService.createTeam(team);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Team> updateTeam(@PathVariable int id, @RequestBody Team team) {
        Team updatedTeam = teamService.updateTeam(id, team);
        return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteTeam(@PathVariable int id) {
        teamService.deleteTeam(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    // Thay đổi trưởng phòng
    @PutMapping("/change-manager/{teamId}/{newManagerId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Team> changeManager(@PathVariable int teamId, @PathVariable int newManagerId) {
        Team updatedTeam = teamService.changeManager(teamId, newManagerId);
        return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
    }

    @PutMapping("/change-team/{userId}/{oldTeamId}/{newTeamId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<User> changeUserTeam(@PathVariable int userId,@PathVariable int oldTeamId, @PathVariable int newTeamId) {
        User updatedUser = userService.changeUserTeam(userId,oldTeamId, newTeamId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


}
