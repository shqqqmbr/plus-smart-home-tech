package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Builder
@Getter
@Setter
@EqualsAndHashCode(of = "address_id")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @UuidGenerator
    UUID address_id;

    @Column(name = "country")
    String country;

    @Column(name = "city")
    String city;

    @Column(name = "street")
    String street;

    @Column(name = "house")
    String house;

    @Column(name = "flat")
    String flat;
}