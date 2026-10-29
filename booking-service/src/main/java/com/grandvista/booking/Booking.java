package com.grandvista.booking;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Booking {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 public Long roomId; public String customerEmail; public String checkIn; public String checkOut;
 public double amount; public String status; public LocalDateTime createdAt;
 public Booking(){}
}
