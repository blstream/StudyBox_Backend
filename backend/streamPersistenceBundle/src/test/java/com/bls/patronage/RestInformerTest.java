package com.bls.patronage;


import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class RestInformerTest {
    static RestInformer informer;

    @BeforeClass
    public static void setUp() throws Exception {
        informer = new RestInformer(new URI("https://www.example.org"));
    }

    @Test
    public void informTest() {
        Response response = informer.inform("");

        assertThat(response.getStatus()).isEqualTo(200);
    }
}
