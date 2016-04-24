package com.bls.patronage;

import javax.ws.rs.core.Response;

public interface HTTPListenerInformer {
    Response inform(Listener listener, Object content);
}
