package com.bls.patronage.dao;


import com.bls.patronage.db.dao.TokenDAO;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

public class TokenDAOTest extends DAOTest {
    private TokenDAO dao;

    @Override
    @BeforeTest
    public void buildDatabase() {
        super.buildDatabase();
        dao = dbi.onDemand(TokenDAO.class);
    }

    @Override
    @BeforeMethod
    public void loadContent() throws Exception {
        super.loadContent();
    }


    @Override
    @AfterTest
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void createToken() {
        dao.create();
    }
}
