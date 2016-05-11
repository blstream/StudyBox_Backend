package com.bls.patronage;

import javax.ws.rs.core.Response;

public interface HTTPInformer {
    Response inform(Object message);
}
