package com.example.api_base.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "metadata_mail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetadataMail {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Email email;

    @Column(nullable = false, length = 200)
    private String subject;

    @Column(name = "sender", nullable = false, length = 100)
    private String from;

    @Column(name = "recipient", nullable = false, length = 100)
    private String to;

    @Column(nullable = false)
    private String date;
}