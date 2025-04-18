package org.empik.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "complaints", uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "complainant"}))
@Data
@NoArgsConstructor
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String complainant;

    @Column(nullable = false)
    private String country;

    @Column(name = "counter", nullable = false)
    private Integer counter;

}