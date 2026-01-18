package ru.yandex.practicum.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
    UUID address_id;
    String country;
    String city;
    String street;
    String house;
    String flat;
}