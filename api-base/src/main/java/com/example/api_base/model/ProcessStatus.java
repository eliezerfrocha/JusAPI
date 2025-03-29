package com.example.api_base.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "process_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessStatus {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Email email;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false)
    private String date;
}