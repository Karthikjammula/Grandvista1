package com.grandvista.auth;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class DataSeeder {
 @Bean CommandLineRunner seed(UserRepository repo, PasswordEncoder enc){
  return args -> {
   if(repo.findByEmail("admin@grandvista.com").isEmpty()) repo.save(new User("admin@grandvista.com",enc.encode("Admin@123"),"ADMIN"));
   if(repo.findByEmail("user@grandvista.com").isEmpty()) repo.save(new User("user@grandvista.com",enc.encode("User@123"),"USER"));
  };
 }
}
