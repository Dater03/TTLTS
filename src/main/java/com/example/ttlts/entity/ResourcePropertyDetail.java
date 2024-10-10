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
@Table(name = "resourcepropertydetail")
public class ResourcePropertyDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int propertyId;
    String propertyDetailName;
    String image;
    Double price;
    int quantity;

    @ManyToOne
    @JoinColumn(name = "propertyId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    ResourceProperty resourceproperty;

    @OneToMany(mappedBy = "resourcepropertydetail")
    @JsonManagedReference
    List<ResourceForPrintJob> resourceforprintjob;

    @OneToMany(mappedBy = "resourcepropertydetail")
    @JsonManagedReference
    List<ImportCoupon> importcoupon;
}
