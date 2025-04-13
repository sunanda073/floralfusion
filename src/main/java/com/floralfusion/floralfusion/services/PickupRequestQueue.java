package com.floralfusion.floralfusion.services;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

import com.floralfusion.floralfusion.entities.PickupRequest;

@Component
public class PickupRequestQueue {
    private final BlockingQueue<PickupRequest> queue = new LinkedBlockingQueue<>();

    public void addRequest(PickupRequest request) {
        try {
            queue.put(request); // Adds the request to the queue, waiting if necessary
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Handle interruption appropriately
        }
    }

    public PickupRequest takeRequest() {
        try {
            return queue.take(); // Retrieves and removes the head of the queue, waiting if necessary
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Handle interruption appropriately
            return null;
        }
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

