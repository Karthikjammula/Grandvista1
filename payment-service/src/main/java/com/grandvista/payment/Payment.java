package com.grandvista.payment;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Payment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 public Long bookingId; public double amount; public String method; public String status; public LocalDateTime paidAt;
 public Payment(){}
}
