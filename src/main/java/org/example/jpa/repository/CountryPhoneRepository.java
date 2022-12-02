package org.example.jpa.repository;

import org.example.jpa.entity.CountryPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CountryPhoneRepository extends JpaRepository<CountryPhone, Long> {

    @Query(nativeQuery = true, value = "with max_length as (\n" +
            "    select max(length(phone_prefix)) as val from country_phone\n" +
            "    where :phoneNumber like country_phone.phone_prefix || '%'\n" +
            ")\n" +
            "select distinct country_name from country_phone, max_length\n" +
            "    where :phoneNumber like country_phone.phone_prefix || '%'\n" +
            "    and length(phone_prefix) = max_length.val\n")
    Set<String> findCountryByPhone(String phoneNumber);
}
