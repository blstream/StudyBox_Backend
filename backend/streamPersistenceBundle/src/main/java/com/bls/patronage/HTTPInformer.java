package com.bls.patronage;

import javax.ws.rs.core.Response;

public interface HTTPInformer {
    <T> Response inform(T message);
}
