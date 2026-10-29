package com.grandvista.payment;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.time.LocalDateTime;
import java.util.*;
@RestController @RequestMapping("/api/payments")
public class PaymentController {
 private final PaymentRepository repo;
 public PaymentController(PaymentRepository r){repo=r;}
 @PostMapping("/{bookingId}")
 public ResponseEntity<?> pay(@PathVariable Long bookingId,@RequestBody Map<String,Object> b,@RequestAttribute(value="email",required=false) String email){
  if(email==null)return ResponseEntity.status(401).body(Map.of("message","Login required"));
  Payment p=new Payment(); p.bookingId=bookingId;
  p.amount=((Number)b.getOrDefault("amount",0)).doubleValue();
  p.method=(String)b.getOrDefault("method","CARD"); p.status="SUCCESS"; p.paidAt=LocalDateTime.now();
  return ResponseEntity.ok(repo.save(p));
 }
 @GetMapping
 public ResponseEntity<?> all(@RequestAttribute(value="role",required=false) String role){
  if(!"ADMIN".equals(role))return ResponseEntity.status(403).body(Map.of("message","Admin only"));
  return ResponseEntity.ok(repo.findAll());
 }
}
