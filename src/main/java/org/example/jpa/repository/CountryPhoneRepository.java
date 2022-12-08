package org.example.jpa.repository;

import org.example.jpa.entity.CountryPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CountryPhoneRepository extends JpaRepository<CountryPhone, Long> {

    @Query(nativeQuery = true, value = "with max_length as (\n" +
            "    select max(length(phone_prefix)) as val from country_phone\n" +
            "    where :phoneNumber like concat(country_phone.phone_prefix, '%')\n" +
            ")\n" +
            "select distinct country_name from country_phone, max_length\n" +
            "    where :phoneNumber like concat(country_phone.phone_prefix, '%')\n" +
            "    and length(phone_prefix) = max_length.val\n")
    Optional<List<String>> findCountryByPhone(String phoneNumber);
}
