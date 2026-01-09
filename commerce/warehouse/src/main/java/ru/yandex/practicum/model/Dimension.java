package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(of = "dimensionId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dimensions")
public class Dimension {

    @Id
    @UuidGenerator
    @Column(name = "dimension_id", nullable = false)
    private UUID dimensionId;

    @Column(name = "width", nullable = false)
    private double width;

    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "depth", nullable = false)
    private double depth;

}