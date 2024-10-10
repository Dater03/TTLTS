package com.example.ttlts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.awt.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "resourceforprintjob")
public class ResourceForPrintJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int resourcePropertyDetailId;
    int printJobId;

    @ManyToOne
    @JoinColumn(name = "resourcePropertyDetailId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    ResourcePropertyDetail resourcepropertydetail;

    @ManyToOne
    @JoinColumn(name = "printJobId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    PrintJobs printjobs;
}
