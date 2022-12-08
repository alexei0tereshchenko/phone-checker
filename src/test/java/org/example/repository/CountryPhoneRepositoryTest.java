package org.example.repository;

import org.example.jpa.repository.CountryPhoneRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql(scripts = {"/sql/insert_phone_countries.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/truncate_all.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CountryPhoneRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private CountryPhoneRepository countryPhoneRepository;

    @Test
    void findCountryByPhone() {
        final Optional<List<String>> result = countryPhoneRepository
                .findCountryByPhone("12332121212");

        assertTrue(result.isPresent());
        assertFalse(result.get().isEmpty());
        assertEquals(2, result.get().size());
    }

}
