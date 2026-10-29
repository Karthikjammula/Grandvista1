package com.grandvista.room;
import jakarta.persistence.*;
@Entity
public class Room {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 public String roomNumber; public String type; public double price; public boolean available=true;
 public Room(){} public Room(String n,String t,double p){roomNumber=n;type=t;price=p;available=true;}
}
