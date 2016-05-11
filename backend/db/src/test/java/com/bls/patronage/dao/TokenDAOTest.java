package com.bls.patronage.dao;

import com.bls.patronage.db.dao.TokenDAO;
import com.bls.patronage.db.model.ResetPasswordToken;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class TokenDAOTest extends DAOTest {
    private TokenDAO dao;
    private String defaultUserEmail;

    @Override
    @BeforeTest
    public void buildDatabase() {
        super.buildDatabase();
        dao = dbi.onDemand(TokenDAO.class);
        defaultUserEmail = "test@mail.com";
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
        final ResetPasswordToken token = new ResetPasswordToken(UUID.randomUUID(), defaultUserEmail, new Date(), true);
        dao.createToken(token);
        assertThat(dao.findByEmail(defaultUserEmail)).isEqualTo(token);
    }
}
