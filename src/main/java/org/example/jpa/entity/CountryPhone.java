package org.example.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(name = "phone_country_uindex", columnNames = {"phone_prefix", "country_name"}))
public class CountryPhone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "phone_prefix", nullable = false)
    private String phonePrefix;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    public CountryPhone(String phonePrefix, String countryName) {
        this.phonePrefix = phonePrefix;
        this.countryName = countryName;
    }
}
