package br.com.oficina.backend.config;

import br.com.oficina.backend.enums.PerfilUsuario;
import br.com.oficina.backend.model.Usuario;
import br.com.oficina.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByEmail("admin@oficina.com").isEmpty()) {
            Usuario admin = Usuario.builder()
                    .nome("Administrador da Oficina")
                    .email("admin@oficina.com")
                    .senhaHash(passwordEncoder.encode("admin123"))
                    .perfil(PerfilUsuario.ADMIN)
                    .ativo(true)
                    .build();
            usuarioRepository.save(admin);
        }
    }
}
