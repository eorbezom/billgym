package com.billgym.pe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.billgym.pe.entity.Loguin;
import com.billgym.pe.repository.LoguinRepository;

@Service
public class DetalleUsuarioService implements UserDetailsService {

    @Autowired
    private LoguinRepository loguinRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Loguin loguin = loguinRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        if (loguin.getUsuarioDato() == null) {
            throw new UsernameNotFoundException("El login no tiene un usuario asignado");
        }

        String rolBD = loguin.getUsuarioDato().getRol().name();
       

        return User.withUsername(loguin.getUsuario())
                .password(loguin.getPassword())  
                .authorities(new SimpleGrantedAuthority("ROLE_"+rolBD))
                .build();
    }
}
