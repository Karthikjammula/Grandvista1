package com.grandvista.room;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.*;
@RestController @RequestMapping("/api/rooms")
public class RoomController {
 private final RoomRepository repo;
 public RoomController(RoomRepository r){repo=r;}
 @GetMapping public List<Room> all(){return repo.findAll();}
 @GetMapping("/available") public List<Room> available(){return repo.findAll().stream().filter(r->r.available).toList();}
 @GetMapping("/{id}") public ResponseEntity<Room> get(@PathVariable Long id){return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());}
 @PostMapping public ResponseEntity<?> add(@RequestBody Room r,@RequestAttribute(value="role",required=false) String role){
  if(!"ADMIN".equals(role))return ResponseEntity.status(403).body(Map.of("message","Admin only"));
  r.id=null; return ResponseEntity.ok(repo.save(r));
 }
 @PutMapping("/{id}") public ResponseEntity<?> update(@PathVariable Long id,@RequestBody Room x,@RequestAttribute(value="role",required=false) String role){
  if(!"ADMIN".equals(role))return ResponseEntity.status(403).body(Map.of("message","Admin only"));
  return repo.findById(id).map(r->{r.roomNumber=x.roomNumber;r.type=x.type;r.price=x.price;r.available=x.available;return ResponseEntity.ok(repo.save(r));}).orElse(ResponseEntity.notFound().build());
 }
 @PostMapping("/{id}/reserve") public ResponseEntity<?> reserve(@PathVariable Long id){
  return repo.findById(id).map(r->{if(!r.available)return ResponseEntity.status(409).body(Map.of("message","Room already reserved"));
   r.available=false; repo.save(r); return ResponseEntity.ok(Map.of("message","Room reserved"));}).orElse(ResponseEntity.notFound().build());
 }
 @PostMapping("/{id}/release") public ResponseEntity<?> release(@PathVariable Long id){
  return repo.findById(id).map(r->{r.available=true;repo.save(r);return ResponseEntity.ok(Map.of("message","Room released"));}).orElse(ResponseEntity.notFound().build());
 }
 @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable Long id,@RequestAttribute(value="role",required=false) String role){
  if(!"ADMIN".equals(role))return ResponseEntity.status(403).body(Map.of("message","Admin only"));
  repo.deleteById(id);return ResponseEntity.ok(Map.of("message","Deleted"));
 }
}
