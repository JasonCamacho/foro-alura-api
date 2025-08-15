package com.alura.foro.controller;

import com.alura.foro.dto.DatosAutenticacionUsuario;
import com.alura.foro.infra.security.DatosTokenJWT;
import com.alura.foro.infra.security.TokenService;
import com.alura.foro.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DatosTokenJWT> autenticar(
            @RequestBody @Valid DatosAutenticacionUsuario datos) {

        Authentication authToken = new UsernamePasswordAuthenticationToken(
                datos.login(), datos.clave());

        Authentication autenticado = authenticationManager.authenticate(authToken);

        String tokenJWT = tokenService.generarToken((Usuario) autenticado.getPrincipal());

        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }
}