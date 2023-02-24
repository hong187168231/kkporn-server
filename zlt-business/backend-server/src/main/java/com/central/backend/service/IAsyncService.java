package com.central.backend.service;

import java.util.Date;

public interface IAsyncService {

    void setVipExpire(Date newVipExpire, Long userId);
}
