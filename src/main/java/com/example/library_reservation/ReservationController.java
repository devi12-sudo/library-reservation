
package com.example.library;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private final ConcurrentHashMap<Long, Reservation> reservations =
            new ConcurrentHashMap<>();

    private final AtomicLong idCounter = new AtomicLong(1);

    private final List<Resource> resources = List.of(
            new Resource("A1", "Seat A1", "SEAT", 1),
            new Resource("A2", "Seat A2", "SEAT", 1),
            new Resource("A3", "Seat A3", "SEAT", 1),
            new Resource("A4", "Seat A4", "SEAT", 1),
            new Resource("ROOM1", "Study Room 1", "ROOM", 4),
            new Resource("ROOM2", "Study Room 2", "ROOM", 4)
    );

    // Display all available resources
    @GetMapping("/resources")
    public List<Resource> getResources() {
        return resources;
    }

    // Display all reservations
    @GetMapping("/reservations")
    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations.values());
    }

    // Create a new reservation
    @PostMapping("/reservations")
    public synchronized ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservation) {

        if (reservation.getResourceId() == null ||
                reservation.getUserName() == null ||
                reservation.getUserName().isBlank() ||
                reservation.getDate() == null ||
                reservation.getStart() == null ||
                reservation.getEnd() == null ||
                !reservation.getStart().isBefore(reservation.getEnd())) {

            return ResponseEntity.badRequest().build();
        }

        boolean resourceExists = resources.stream()
                .anyMatch(r -> r.getId().equals(reservation.getResourceId()));

        if (!resourceExists) {
            return ResponseEntity.badRequest().build();
        }

        // Check for overlapping reservations
        boolean overlapping = reservations.values().stream()
                .anyMatch(existing ->
                        existing.getResourceId().equals(reservation.getResourceId())
                        && existing.getDate().equals(reservation.getDate())
                        && reservation.getStart().isBefore(existing.getEnd())
                        && existing.getStart().isBefore(reservation.getEnd())
                );

        if (overlapping) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Reservation newReservation = new Reservation(
                idCounter.getAndIncrement(),
                reservation.getResourceId(),
                reservation.getUserName(),
                reservation.getDate(),
                reservation.getStart(),
                reservation.getEnd()
        );

        reservations.put(newReservation.getId(), newReservation);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newReservation);
    }

    // Cancel a reservation
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {

        Reservation removed = reservations.remove(id);

        if (removed == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
