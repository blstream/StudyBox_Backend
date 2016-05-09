package com.bls.patronage.resources;

import com.bls.patronage.db.dao.TokenDAO;
import com.bls.patronage.db.dao.UserDAO;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

@RunWith(MockitoJUnitRunner.class)
public class ResetPasswordResourceTest {

    private static final UserDAO userDAO = mock(UserDAO.class);
    private static final TokenDAO tokenDAO = mock(TokenDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ResetPasswordResource(userDAO, tokenDAO))
            .build();

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        reset(userDAO);
        reset(tokenDAO);
    }

    @Test
    public void passwordRecovery(){
        
    }

    @Test
    public void passwordChange(){

    }
}
