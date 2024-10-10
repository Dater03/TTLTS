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
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String billName;
    BillStatus billStatus;
    Double totalMoney;
    int projectId;
    int customerId;
    String tradingCode;
    LocalDateTime createTime;
    LocalDateTime updateTime;
    int employeeId;

    @ManyToOne
    @JoinColumn(name = "customerId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "employeeId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    User user;
}
