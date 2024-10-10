package com.example.ttlts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "keyferformanceindicators")
public class KeyPerformanceIndicators {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int employeeId;
    String indicatorName;
    int target;
    int actuallyAchieved;
    Period period;
    Boolean achieveKPI;

    @ManyToOne
    @JoinColumn(name = "employeeId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    User user;
}
