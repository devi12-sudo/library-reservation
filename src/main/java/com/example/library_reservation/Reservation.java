
package com.example.library;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {

    private Long id;
    private String resourceId;
    private String userName;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;

    public Reservation(Long id, String resourceId, String userName,
                       LocalDate date, LocalTime start, LocalTime end) {
        this.id = id;
        this.resourceId = resourceId;
        this.userName = userName;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public Long getId() {
        return id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }
}
