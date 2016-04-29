package com.bls.patronage.helpers;

import com.bls.patronage.Message;

import java.net.URL;

public class CVMessage implements Message {

    private final URL location;
    private final String action;

    public CVMessage(URL location, String action) {
        this.location = location;
        this.action = action;
    }
}