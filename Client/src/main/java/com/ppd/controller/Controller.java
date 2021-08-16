package com.ppd.controller;

import com.ppd.domain.ShowRoom;
import com.ppd.service.IAppObserver;
import com.ppd.service.IAppServices;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Controller extends UnicastRemoteObject implements IAppObserver, Serializable {
    private ShowRoom showRoom;
    private IAppServices service;
    private final Random random;
    private ScheduledFuture<?> taskScheduler;

    public Controller() throws RemoteException {
        this.random = new Random();
    }

    public void setService(IAppServices service) {
        this.service = service;
        System.out.println("Started");
        boolean isRunning = service.login(this);
        if (isRunning) {
            this.showRoom = service.getShowRoom();
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            this.taskScheduler = executor.scheduleAtFixedRate(this::makeRequest, 0, 2, TimeUnit.SECONDS);
        } else
            System.out.println("Finished");
    }

    //nrSeats <= max - min, [min,max]
    private Set<Integer> getRandomSeats(int nrSeats, int min, int max) {
        if (nrSeats <= (max + 1) - min) {
            return random.ints(min, max + 1)
                    .distinct()
                    .limit(nrSeats)
                    .boxed()
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    private void makeRequest() {
        try {
            Long showId = showRoom.getShows().get(random.nextInt(showRoom.getShows().size())).getId();
            System.out.println(service.buyTickets(showId, getRandomSeats(random.nextInt(5) + 1, 1, (int) showRoom.getSeatCount())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeSoldSeats(Long showId, Set<Integer> soldSeats) {
        System.out.println(soldSeats);
    }

    @Override
    public void serverStop() throws RemoteException {
        this.taskScheduler.cancel(true);
        System.out.println("Finished");
    }
}
