package com.example.ttlts.entity;

public enum ProjectStatus {
    DESIGNING,                // Đang trong quá trình thiết kế
    DESIGN_APPROVED,          // Thiết kế được duyệt
    DESIGN_REJECTED,
    PRINTING_CONFIRMED,       // Đã xác nhận in ấn
    PRINTING,                 // Đang in ấn
    PRINTED,                  // Đã in xong
    DELIVERING,               // Đang giao hàng
    DELIVERED,                // Đã giao hàng
    COMPLETED;                // Hoàn thành
}
