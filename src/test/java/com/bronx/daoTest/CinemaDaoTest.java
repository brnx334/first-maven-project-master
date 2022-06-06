package com.bronx.daoTest;

import com.bronx.entity.Cinema;
import com.bronx.repositoryOldVersion.CinemaRepository;
import com.bronx.testUtil.GettersEntityUtil;
import com.bronx.testUtil.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class CinemaDaoTest {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private Session session;
    @Autowired
    static private CinemaRepository cinemaRepository;

    @BeforeEach
    void init() {
        TestDataImporter.importData(sessionFactory);
    }

    @Test
    void findAll() {
        session.beginTransaction();

        List<Cinema> results = cinemaRepository.findAll();
        assertThat(results).hasSize(2);

        List<String> fullNames = results.stream().map(Cinema::getName).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder("Galaxy", "Helios");

        session.getTransaction().rollback();
    }

    @Test
    void findCinemaById() {
        session.beginTransaction();

        Optional<Cinema> result = cinemaRepository.findById(1L);
        assertEquals(result.get().getName(), "Galaxy");

        session.getTransaction().rollback();
        session.close();

    }

    @Test
    void saveCinemaAndReturnWithId() {
        session.beginTransaction();
        var cinema = GettersEntityUtil.getCinema();

        cinema = cinemaRepository.save(cinema);

        assertThat(cinema.getId()).isEqualTo(3L);

        session.getTransaction().rollback();
        session.close();

    }

    @Test
    void deleteCinema() {
        session.beginTransaction();

        cinemaRepository.delete(1L);
        assertNull(session.get(Cinema.class, 1L));

        session.getTransaction().rollback();
        session.close();
    }

    @Test
    void checkUpdateUsername() {
        session.beginTransaction();

        var cinema = session.get(Cinema.class, 1L);
        cinema.setName("GGalaxy");
        cinemaRepository.update(cinema);

        assertEquals(session.get(Cinema.class, 1L).getName(), "GGalaxy");

        session.getTransaction().rollback();
        session.close();

    }
}
