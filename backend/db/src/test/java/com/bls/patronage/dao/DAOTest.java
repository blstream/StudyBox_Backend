package com.bls.patronage.dao;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.util.List;
import java.util.Map;

abstract public class DAOTest {
    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();
    private static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";

    protected DBI dbi;
    private Handle handle;
    private Liquibase liquibase;

    public void setUp() throws Exception {
        Environment environment = new Environment("test-environment", Jackson.newObjectMapper(), null, new MetricRegistry(), null);
        dbi = new DBIFactory().build(environment, getDataSourceFactory(), "test");
        handle = dbi.open();
        migrateDatabase();
    }

    public void tearDown() throws Exception {
        handle.close();
    }

    private void migrateDatabase() throws LiquibaseException {
        liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(handle.getConnection()));
        liquibase.update("");
    }

    public  <Type> List<Type> getObjectsFromDatabase(Class<Type> typeClass, Class mapperClass, String tableName) throws Exception {
        Query<Map<String, Object>> query = handle.createQuery("SELECT * from " + tableName);
        query.registerMapper((ResultSetMapper) mapperClass.newInstance());
        return query.mapTo(typeClass).list();
    }

    protected DataSourceFactory getDataSourceFactory() {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass(JDBC_DRIVER);
        dataSourceFactory.setUrl(JDBC_URL);
        dataSourceFactory.setUser(USER);
        dataSourceFactory.setPassword(PASSWORD);
        return dataSourceFactory;
    }
}
