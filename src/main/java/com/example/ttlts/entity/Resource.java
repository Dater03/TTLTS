package com.example.ttlts.entity;

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
@Table(name = "resource")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String resourceName;
    String image;
    ResourceType resourceType;
    int availableQuantity;
    ResourceStatus resourceStatus;

    @OneToMany(mappedBy = "resource")
    @JsonManagedReference
    List<ResourceProperty> resourceproperty;
}
