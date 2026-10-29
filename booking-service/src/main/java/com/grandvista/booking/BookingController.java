package com.grandvista.booking;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestClient;
import java.time.LocalDateTime;
import java.util.*;
@RestController @RequestMapping("/api/bookings")
public class BookingController {
 private final BookingRepository repo; private final RestClient rest;
 public BookingController(BookingRepository r,RestClient.Builder b){repo=r;rest=b.baseUrl("http://ROOM-SERVICE").build();}

 @GetMapping("/my")
 public ResponseEntity<?> mine(@RequestAttribute(value="email",required=false) String email){
  if(email==null)return ResponseEntity.status(401).body(Map.of("message","Login required"));
  return ResponseEntity.ok(repo.findByCustomerEmail(email));
 }

 @GetMapping
 public ResponseEntity<?> all(@RequestAttribute(value="role",required=false) String role){
  if(!"ADMIN".equals(role))return ResponseEntity.status(403).body(Map.of("message","Admin only"));
  return ResponseEntity.ok(repo.findAll());
 }

 @PostMapping
 public ResponseEntity<?> create(@RequestBody Map<String,Object> b,@RequestAttribute(value="email",required=false) String email){
  if(email==null)return ResponseEntity.status(401).body(Map.of("message","Login required"));
  if(b.get("roomId")==null||b.get("amount")==null)return ResponseEntity.badRequest().body(Map.of("message","roomId and amount required"));
  Long roomId=((Number)b.get("roomId")).longValue(); double amount=((Number)b.get("amount")).doubleValue();
  try{
   rest.post().uri("/api/rooms/{id}/reserve",roomId).retrieve().toBodilessEntity();
   Booking x=new Booking(); x.roomId=roomId; x.customerEmail=email;
   x.checkIn=(String)b.get("checkIn"); x.checkOut=(String)b.get("checkOut");
   x.amount=amount; x.status="RESERVED"; x.createdAt=LocalDateTime.now();
   return ResponseEntity.ok(repo.save(x));
  }catch(Exception e){return ResponseEntity.status(409).body(Map.of("message","Room is not available"));}
 }

 @DeleteMapping("/{id}/cancel")
 public ResponseEntity<?> cancel(@PathVariable Long id,@RequestAttribute(value="email",required=false) String email){
  return repo.findById(id).map(b->{
   if(email==null||!b.customerEmail.equals(email))return ResponseEntity.status(403).body(Map.of("message","Not your booking"));
   try{rest.post().uri("/api/rooms/{id}/release",b.roomId).retrieve().toBodilessEntity();}catch(Exception ignored){}
   b.status="CANCELLED"; repo.save(b); return ResponseEntity.ok(b);
  }).orElse(ResponseEntity.notFound().build());
 }
}
