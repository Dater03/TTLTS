package com.example.ttlts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "design")
public class Design {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int projectId;
    int designerId;
    String filePath;
    LocalDateTime designTime;
    @Enumerated(EnumType.STRING)
    DesignStatus designStatus;
    int approverId;

    @OneToMany(mappedBy = "design")
    @JsonManagedReference
            @JsonIgnore
    List<PrintJobs> printjobs;

    @ManyToOne
    @JoinColumn(name = "projectId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
            @JsonIgnore
    Project project;

    @ManyToOne
    @JoinColumn(name = "designerId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
            @JsonIgnore
    User user;
}

