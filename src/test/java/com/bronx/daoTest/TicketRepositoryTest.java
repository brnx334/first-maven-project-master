package com.bronx.daoTest;

import com.bronx.entity.Ticket;
import com.bronx.repository.TicketRepository;
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
public class TicketRepositoryTest {


    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private Session session;
    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void init() {
        TestDataImporter.importData(sessionFactory);
    }



    @Test
    void findAll() {
        session.beginTransaction();

        List<Ticket> results = ticketRepository.findAll();
        assertThat(results).hasSize(3);

        List<Integer> fullNames = results.stream().map(Ticket::getTicketNumber).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder(40, 35, 50);

        session.getTransaction().rollback();
    }

    @Test
    void findTicketById() {

        session.beginTransaction();

        Optional<Ticket> result = ticketRepository.findById(1L);
        assertEquals(result.get().getTicketNumber(), 40);

        session.getTransaction().rollback();
    }

    @Test
    void saveTicketAndReturnWithId() {

        session.beginTransaction();
        var ticket = GettersEntityUtil
                .getTicket(GettersEntityUtil.getUser(),
                        GettersEntityUtil.getEventFilm(GettersEntityUtil
                                        .getHall(GettersEntityUtil.getCinema()),
                                GettersEntityUtil.getFilm()));

        ticket = ticketRepository.save(ticket);

        assertThat(ticket.getId()).isEqualTo(4L);

        session.getTransaction().rollback();
    }

    @Test
    void deleteTicket() {
        session.beginTransaction();

        ticketRepository.delete(1L);
        assertNull(session.get(Ticket.class, 1L));

        session.getTransaction().rollback();
    }

    @Test
    void checkUpdateUsername() {
        session.beginTransaction();

        var ticket = session.get(Ticket.class, 1L);
        ticket.setTicketNumber(34);
        ticketRepository.update(ticket);

        assertEquals(session.get(Ticket.class, 1L).getTicketNumber(), 34);

        session.getTransaction().rollback();
    }
}
