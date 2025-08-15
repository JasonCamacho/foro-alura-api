package com.alura.foro.service;

import com.alura.foro.model.Usuario;
import com.alura.foro.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByLogin("jason").isEmpty()) {
                String passwordEncriptada = passwordEncoder.encode("123456");
                Usuario usuario = new Usuario("jason", passwordEncriptada);
                usuarioRepository.save(usuario);
                System.out.println("✅ Usuario 'jason' creado con clave encriptada.");
            }
        };
    }
}