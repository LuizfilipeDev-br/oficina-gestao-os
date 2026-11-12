package br.com.oficina.backend.controller;

import br.com.oficina.backend.dto.LoginRequestDTO;
import br.com.oficina.backend.dto.LoginResponseDTO;
import br.com.oficina.backend.model.Usuario;
import br.com.oficina.backend.repository.UsuarioRepository;
import br.com.oficina.backend.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos."));

        if (!usuario.getAtivo() || !passwordEncoder.matches(dto.senha(), usuario.getSenhaHash())) {
            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }

        String token = jwtService.gerarToken(usuario);
        return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getNome(), usuario.getPerfil().name()));
    }
}
