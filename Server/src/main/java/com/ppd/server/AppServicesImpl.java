package com.ppd.server;

import com.ppd.domain.Currency;
import com.ppd.domain.LogSale;
import com.ppd.domain.LogShow;
import com.ppd.domain.Sale;
import com.ppd.domain.ServerState;
import com.ppd.domain.Show;
import com.ppd.domain.ShowRoom;
import com.ppd.repository.ServerStateRepository;
import com.ppd.repository.ShowRepository;
import com.ppd.repository.ShowRoomRepository;
import com.ppd.service.IAppObserver;
import com.ppd.service.IAppServices;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class AppServicesImpl implements IAppServices {
    private final Set<IAppObserver> loggedUsers;
    private final ShowRepository showRepository;
    private final ShowRoomRepository showRoomRepository;
    private final ServerStateRepository serverStateRepository;
    private volatile boolean isRunning;
    private final ExecutorService executor;
    private ShowRoom showRoom;

    private Map<Long, LogShow> logShows;
    private Map<Long, Show> mapShows;
    private final ReentrantLock lock;
    private final ReentrantLock logShowsLock;

    public AppServicesImpl(ShowRepository showRepository, ShowRoomRepository showRoomRepository, ServerStateRepository serverStateRepository) {
        this.showRepository = showRepository;
        this.showRoomRepository = showRoomRepository;
        this.serverStateRepository = serverStateRepository;
        this.lock = new ReentrantLock();
        this.logShowsLock = new ReentrantLock();
        this.loggedUsers = new HashSet<>();
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        addData();
        this.isRunning = true;
        AtomicInteger i = new AtomicInteger();
        ScheduledFuture<?> verifyScheduler = Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            logShowsLock.lock();
            List<LogShow> shows = logShows.values().stream().map(LogShow::new).collect(Collectors.toList());
            System.out.println(serverStateRepository.save(new ServerState(shows, LocalDateTime.now())));
            logShowsLock.unlock();
        }, 0, 10, TimeUnit.SECONDS);

        Executors.newScheduledThreadPool(1).schedule(() -> {
            this.isRunning = false;
            verifyScheduler.cancel(true);
            notifyServerStopped();
            System.out.println("Finished");
        }, 150, TimeUnit.SECONDS);
    }

    private void addData() {
        showRoom = new ShowRoom(0L, 100, new ArrayList<>());
        logShows = new HashMap<>();
        mapShows = new HashMap<>();
        showRoom.getShows().add(new Show(0, LocalDateTime.now().withHour(10).withMinute(0).withSecond(0).withNano(0).plusDays(0),
                                         "Show 1", new Currency(100, "RON"), new ArrayList<>(), showRoom));
        showRoom.getShows().add(new Show(0, LocalDateTime.now().withHour(14).withMinute(0).withSecond(0).withNano(0).plusDays(1),
                                         "Show 2", new Currency(200, "RON"), new ArrayList<>(), showRoom));
        showRoom.getShows().add(new Show(0, LocalDateTime.now().withHour(18).withMinute(0).withSecond(0).withNano(0).plusDays(2),
                                         "Show 3", new Currency(150, "RON"), new ArrayList<>(), showRoom));
        showRoom = showRoomRepository.save(showRoom);
        logShows.put(showRoom.getShows().get(0).getId(), new LogShow(showRoom.getShows().get(0).getId(), 0, 0, new ArrayList<>(),
                                                                     new Currency(0, "RON")));
        logShows.put(showRoom.getShows().get(1).getId(), new LogShow(showRoom.getShows().get(1).getId(), 0, 0, new ArrayList<>(),
                                                                     new Currency(0, "RON")));
        logShows.put(showRoom.getShows().get(2).getId(), new LogShow(showRoom.getShows().get(2).getId(), 0, 0, new ArrayList<>(),
                                                                     new Currency(0, "RON")));
        mapShows.put(showRoom.getShows().get(0).getId(), showRoom.getShows().get(0));
        mapShows.put(showRoom.getShows().get(1).getId(), showRoom.getShows().get(1));
        mapShows.put(showRoom.getShows().get(2).getId(), showRoom.getShows().get(2));
    }

    @Override
    public synchronized boolean login(IAppObserver client) {
        loggedUsers.add(client);
        return isRunning;
    }

    @Override
    public synchronized void logout(IAppObserver client) {
        loggedUsers.remove(client);
    }

    @Override
    public Sale buyTickets(Long showId, Set<Integer> seats) throws Exception {
        Future<Sale> result = executor.submit(() -> {
            Show show = mapShows.get(showId);
            if (show == null)
                throw new Exception("No show with the given id.");
            Sale sale = new Sale(0, show, seats, LocalDateTime.now());
            lock.lock();
            if (show.getSales().stream().allMatch(s -> Collections.disjoint(s.getSoldSeats(), seats))) {
                show.getSales().add(sale);
                lock.unlock();
                logShowsLock.lock();
                logShows.get(showId).getSales().add(new LogSale(sale, true));
                logShowsLock.unlock();
                logShows.get(showId).incCorrect();
                logShows.get(showId).addToTotalSoldValue(show.getTicketPrice().multiply(seats.size()));
                notifySoldSeatsChanged(show);
                return sale;
            } else {
                lock.unlock();
                logShowsLock.lock();
                logShows.get(showId).getSales().add(new LogSale(sale, false));
                logShowsLock.unlock();
                logShows.get(showId).incIncorrect();
                return null;
            }
        });
        return result.get();
    }

    private void notifySoldSeatsChanged(Show show) {
        for (var loggedUser : loggedUsers) {
            executor.submit(() -> {
                try {
                    loggedUser.changeSoldSeats(show.getId(), show.getSales().stream()
                            .map(Sale::getSoldSeats).flatMap(Collection::stream).collect(Collectors.toSet()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void notifyServerStopped() {
        for (var loggedUser : loggedUsers) {
            executor.submit(() -> {
                try {
                    loggedUser.serverStop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public ShowRoom getShowRoom() {
        return showRoom;
    }
}