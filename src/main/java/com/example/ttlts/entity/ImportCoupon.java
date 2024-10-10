package com.example.ttlts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "importcoupon")
public class ImportCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    Double totalMoney;
    int resourcePropertyDetailId;
    int employeeId;
    String tradingCode;
    LocalDateTime createTime;
    LocalDateTime updateTime;

    @ManyToOne
    @JoinColumn(name = "resourcePropertyDetailId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    ResourcePropertyDetail resourcepropertydetail;

    @ManyToOne
    @JoinColumn(name = "employeeId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    User user;

}
