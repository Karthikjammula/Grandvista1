package com.grandvista.booking;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BookingRepository extends JpaRepository<Booking,Long>{
 List<Booking> findByCustomerEmail(String email);
}
