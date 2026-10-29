package com.grandvista.auth;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final UserRepository repo; private final PasswordEncoder encoder; private final JwtUtil jwt;
 public AuthController(UserRepository r,PasswordEncoder e,JwtUtil j){repo=r;encoder=e;jwt=j;}
 @PostMapping("/register") public ResponseEntity<?> register(@RequestBody Map<String,String> b){
  String email=b.get("email"), pass=b.get("password");
  if(email==null||pass==null)return ResponseEntity.badRequest().body(Map.of("message","Email and password required"));
  if(repo.findByEmail(email).isPresent())return ResponseEntity.status(409).body(Map.of("message","Email already registered"));
  User u=new User(email,encoder.encode(pass),"USER"); repo.save(u);
  return ResponseEntity.ok(Map.of("message","Registered successfully"));
 }
 @PostMapping("/login") public ResponseEntity<?> login(@RequestBody Map<String,String> b){
  return repo.findByEmail(b.get("email")).filter(u->encoder.matches(b.get("password"),u.password))
   .<ResponseEntity<?>>map(u->ResponseEntity.ok(Map.of("token",jwt.generate(u.email,u.role),"email",u.email,"role",u.role)))
   .orElseGet(()->ResponseEntity.status(401).body(Map.of("message","Invalid email or password")));
 }
}
