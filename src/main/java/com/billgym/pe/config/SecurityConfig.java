package com.billgym.pe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.billgym.pe.service.DetalleUsuarioService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DetalleUsuarioService detalleUsuarioService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }
            @Override
            public boolean matches(CharSequence rawPassword, String storedPassword) {
                return rawPassword.toString().equals(storedPassword);
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(detalleUsuarioService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authenticationProvider(authenticationProvider())
            .csrf(cs -> cs.disable())

            .authorizeHttpRequests(auth -> auth
                // ========== RECURSOS PÚBLICOS ==========
                .requestMatchers("/css/**", "/js/**", "/img/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/login", "/logout").permitAll()

                // ========== SOBRE NOSOTROS Y SOPORTE (TODOS) ==========
                .requestMatchers("/sobreNosotros", "/soporte")
                    .authenticated() // Todos los roles autenticados

                // ========== INICIO Y HOME (TODOS) ==========
                .requestMatchers("/inicio", "/home", "/")
                    .authenticated()

                // ========== CREAR (SOLO PRESIDENTE_FUNDADOR Y GERENTE_GENERAL) ==========
                .requestMatchers("/crearCliente.html", "/crearCompra.html", "/crearLoguin.html", 
                                 "/crearProducto.html", "/crearUsuario.html", "/crear/**")
                    .hasAnyRole("PRESIDENTE_FUNDADOR", "GERENTE_GENERAL")

                // ========== EDITAR (SOLO PRESIDENTE_FUNDADOR Y GERENTE_GENERAL) ==========
                .requestMatchers("/editarCliente.html", "/editarCompra.html", "/editarLogin.html",
                                 "/editarProducto.html", "/editarUsuario.html", "/editar/**")
                    .hasAnyRole("PRESIDENTE_FUNDADOR", "GERENTE_GENERAL")

                // ========== CLIENTES ==========
                // PRESIDENTE_FUNDADOR, GERENTE_GENERAL, SEGURIDAD
                .requestMatchers("/clientes/**", "/clientes.html")
                    .hasAnyRole("PRESIDENTE_FUNDADOR", "GERENTE_GENERAL", "SEGURIDAD")

                // ========== PRODUCTOS ==========
                // PRESIDENTE_FUNDADOR, GERENTE_GENERAL, SUPERVISOR, ENCARGADO
                .requestMatchers("/productos/**", "/productos.html")
                    .hasAnyRole("PRESIDENTE_FUNDADOR", "GERENTE_GENERAL", "SUPERVISOR", "ENCARGADO")

                // ========== COMPRAS ==========
                // PRESIDENTE_FUNDADOR, GERENTE_GENERAL, SUPERVISOR, ENCARGADO
                .requestMatchers("/compras/**", "/compras.html")
                    .hasAnyRole("PRESIDENTE_FUNDADOR", "GERENTE_GENERAL", "SUPERVISOR", "ENCARGADO")

                // ========== USUARIOS ==========
                // PRESIDENTE_FUNDADOR, GERENTE_GENERAL, GERENTE_OPERACIONES, SEGURIDAD
                .requestMatchers("/usuarios/**", "/usuarios.html")
                    .hasAnyRole("PRESIDENTE_FUNDADOR", "GERENTE_GENERAL", "GERENTE_OPERACIONES", "SEGURIDAD")

                // ========== LOGIN TABLA ==========
                // PRESIDENTE_FUNDADOR, GERENTE_GENERAL, GERENTE_OPERACIONES
                .requestMatchers("/loguinTabla/**", "/loginTabla.html")
                    .hasAnyRole("PRESIDENTE_FUNDADOR", "GERENTE_GENERAL", "GERENTE_OPERACIONES")

                // ========== CUALQUIER OTRA RUTA ==========
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .usernameParameter("usuario")
                .passwordParameter("password")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            .exceptionHandling(ex -> ex
                .accessDeniedPage("/acceso-denegado")
            );

        return http.build();
    }
}