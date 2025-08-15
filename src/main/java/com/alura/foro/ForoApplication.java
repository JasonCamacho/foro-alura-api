package com.alura.foro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class ForoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForoApplication.class, args);
    }

    // TEMPORAL: genera e imprime un BCrypt para '123456'
    @Bean
    CommandLineRunner printBcrypt(PasswordEncoder encoder) {
        return args -> {
            String raw = "123456"; // aquí puedes poner otra si quieres
            System.out.println("BCrypt para '" + raw + "': " + encoder.encode(raw));
        };
    }
}