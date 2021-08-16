package com.ppd.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface IAppObserver extends Remote {
    void changeSoldSeats(Long showId, Set<Integer> soldSeats) throws RemoteException;

    void serverStop() throws RemoteException;
}
