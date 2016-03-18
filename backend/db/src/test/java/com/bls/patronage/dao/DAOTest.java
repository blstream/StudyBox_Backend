package com.bls.patronage.dao;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.junit.After;
import org.junit.Before;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

abstract public class DAOTest {
    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();
    private static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";

    protected DBI dbi;
    private Handle handle;

    @Before
    public void setUp() throws Exception {
        Environment environment = new Environment("test-environment", Jackson.newObjectMapper(), null, new MetricRegistry(), null);
        dbi = new DBIFactory().build(environment, getDataSourceFactory(), "test");
        handle = dbi.open();
        setUpDatabaseContent(handle);
    }

    @After
    public void tearDown() throws Exception {
        handle.close();
    }

    protected DataSourceFactory getDataSourceFactory() {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass(JDBC_DRIVER);
        dataSourceFactory.setUrl(JDBC_URL);
        dataSourceFactory.setUser(USER);
        dataSourceFactory.setPassword(PASSWORD);
        return dataSourceFactory;
    }

    abstract protected void setUpDatabaseContent(Handle handle);
}
