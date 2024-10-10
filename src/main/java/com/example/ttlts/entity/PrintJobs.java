package com.example.ttlts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "printjobs")
public class PrintJobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int designId;
    PrintJobStatus printJobStatus;

    @OneToMany(mappedBy = "printjobs")
    @JsonManagedReference
    List<ResourceForPrintJob> resourceforprintjob;

    @ManyToOne
    @JoinColumn(name = "designId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    Design design;
}
