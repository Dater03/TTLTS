package com.example.ttlts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "resourceproperty")
public class ResourceProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String resourcePropertyName;
    int resourceId;
    int quantity;

    @ManyToOne
    @JoinColumn(name = "resource",insertable = false, updatable = false,nullable = false)
    @JsonIgnore
    @JsonBackReference
    Resource resource;

    @OneToMany(mappedBy = "resourceproperty")
    @JsonManagedReference
    List<ResourcePropertyDetail> resourcepropertydetail;
}
