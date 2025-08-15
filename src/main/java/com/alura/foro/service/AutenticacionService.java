package com.alura.foro.service;

import com.alura.foro.model.Usuario;
import com.alura.foro.repository.UsuarioRepository;
import com.alura.foro.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    public String generarToken(Authentication authentication) {
        com.alura.foro.model.Usuario usuario =
                (com.alura.foro.model.Usuario) authentication.getPrincipal();
        return tokenService.generarToken(usuario);  // antes: getUsername()
    }
}