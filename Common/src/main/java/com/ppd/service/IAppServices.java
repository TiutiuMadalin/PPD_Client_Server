package com.ppd.service;

import com.ppd.domain.Sale;
import com.ppd.domain.ShowRoom;

import java.util.Set;

public interface IAppServices {
    boolean login(IAppObserver client);

    void logout(IAppObserver client);

    Sale buyTickets(Long showId, Set<Integer> seats) throws Exception;

    ShowRoom getShowRoom();
}
