package com.bls.patronage.dao;

import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;
import com.codahale.metrics.MetricRegistry;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DAOTest {
    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();
    private static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";

    protected DBI dbi;
    private Handle handle;

    private Deck deckExample1;
    private Deck deckExample2;


    @Before
    public void setUp() throws Exception {
        Environment environment = new Environment( "test-environment", Jackson.newObjectMapper(), null, new MetricRegistry(), null);
        dbi = new DBIFactory().build( environment, getDataSourceFactory(), "test");
        handle = dbi.open();
        setUpDatabaseContent(handle);

        deckExample1 = new Deck(UUID.fromString("020276e9-285c-4162-b585-32f272a947b1"),"math");
        deckExample2 = new Deck(UUID.fromString("b5e9aa75-64d6-4d21-87a6-d0a91c70f997"),"bio");
    }

    @After
    public void tearDown() throws Exception {
        handle.close();
    }

//    @Test
//    public void getAllDecks() throws Exception {
//        final DeckDAO dao = dbi.open(DeckDAO.class);
//        final List<Deck> decks = dao.getAllDecks();
//        assertThat(decks).containsSequence(deckExample1,deckExample2);
//    }
//
//    @Test
//    public void getDeckById() throws Exception {
//        final DeckDAO dao = dbi.open(DeckDAO.class);
//        final Deck foundDeck = dao.getDeckById(deckExample1.getId());
//        assertThat(foundDeck).isEqualTo(deckExample1);
//    }
//
//    @Test
//    public void getDeckByName() throws Exception {
//        final DeckDAO dao = dbi.open(DeckDAO.class);
//        final List<Deck> decks = dao.getDecksByName(deckExample1.getName());
//        assertThat(decks).containsOnly(deckExample1);
//    }
//
//    @Test
//    public void createDeck() throws Exception {
//        final Deck createdDeck = new Deck(UUID.fromString("a04692bc-4a70-4696-9815-24b8c0de5398"),"sport");
//        final DeckDAO dao = dbi.open(DeckDAO.class);
//        dao.createDeck(createdDeck);
//        assertThat(dao.getDeckById(createdDeck.getId())).isEqualTo(createdDeck);
//    }
//
//    @Test
//    public void deleteDeck() throws Exception {
//        final DeckDAO dao = dbi.open(DeckDAO.class);
//        dao.deleteDeck(deckExample2.getId());
//        assertThat(dao.getDeckById(deckExample2.getId())).isNull();
//    }
//
//    @Test
//    public void updateDeck() throws Exception {
//        final Deck updatedDeck = new Deck(deckExample1.getId(),"miscellanous");
//        final DeckDAO dao = dbi.open(DeckDAO.class);
//        dao.updateDeck(updatedDeck);
//        assertThat(dao.getDeckById(deckExample1.getId())).isEqualTo(updatedDeck);
//    }

    protected DataSourceFactory getDataSourceFactory()
    {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass(JDBC_DRIVER);
        dataSourceFactory.setUrl(JDBC_URL);
        dataSourceFactory.setUser(USER);
        dataSourceFactory.setPassword(PASSWORD);
        return dataSourceFactory;
    }

    private void setUpDatabaseContent(Handle handle) {
        handle.createCall("DROP TABLE decks IF EXISTS").invoke();
        handle.createCall(
                "CREATE TABLE decks (id uuid primary key, name varchar(50) not null)")
                .invoke();
        handle.createStatement("INSERT INTO decks VALUES (?, ?)")
                .bind(0, "020276e9-285c-4162-b585-32f272a947b1")
                .bind(1, "math")
                .execute();
        handle.createStatement("INSERT INTO decks VALUES (?, ?)")
                .bind(0, "b5e9aa75-64d6-4d21-87a6-d0a91c70f997")
                .bind(1, "bio")
                .execute();
    }
}