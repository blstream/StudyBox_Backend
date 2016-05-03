package com.bls.patronage;


import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

public class RestInformerTest {
    static RestInformer informer;

    @BeforeClass
    public static void setUp() {
        informer = new RestInformer(any(URI.class));
    }

    @Test
    public void informTest() {
        Response response = informer.inform(any(Message.class));

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isNull();
    }
}
