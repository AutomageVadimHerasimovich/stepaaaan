package com.github.vadimher.library.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BusyBooksService {
    private final Set<Long> busyBookIds = new HashSet<>();

    public void updateBusyBooks(List<Long> ids) {
        busyBookIds.clear();
        busyBookIds.addAll(ids);
    }

    public Set<Long> getBusyBookIds() {
        return busyBookIds;
    }
}