package com.grandvista.room;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
@Configuration
public class DataSeeder {
 @Bean CommandLineRunner seed(RoomRepository r){
  return args->{if(r.count()==0){
   r.save(new Room("101","Deluxe",4500)); r.save(new Room("102","Suite",7000));
   r.save(new Room("201","Standard",3000)); r.save(new Room("202","Executive",6000));
  }};
 }
}
