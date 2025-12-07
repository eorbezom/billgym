package com.billgym.pe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.billgym.pe.entity.Loguin;
import com.billgym.pe.repository.LoguinRepository;

@Service
public class LoguinService {

    private final LoguinRepository loguinRepository;

    public LoguinService(LoguinRepository loguinRepository) {
        this.loguinRepository = loguinRepository;
    }

    // LISTAR
    public List<Loguin> listarLoguin() {
        return loguinRepository.findAll();
    }

    // OBTENER POR ID
    public Loguin obtenerLoguin(Integer id_loguin) {
        return loguinRepository.findById(id_loguin)
                .orElseThrow(() -> new IllegalArgumentException("ID invalido: " + id_loguin));
    }

    // GUARDAR
    public void guardarLoguin(Loguin loguin) {
        loguinRepository.save(loguin);
    }

    // BUSCAR POR DNI
    public List<Loguin> buscar(String termino) {
        return loguinRepository.buscarPorDniUsuario(termino);
    }

    // ELIMINAR
    public void eliminar(Integer id_loguin) {
        loguinRepository.deleteById(id_loguin);
    }

    // 🔥 VALIDAR LOGIN
    public Loguin validarCredenciales(String usuario, String password) {
        Optional<Loguin> loguinOpt = loguinRepository.findByUsuario(usuario);

        if (loguinOpt.isEmpty()) {
            return null; // usuario no existe
        }

        Loguin loguin = loguinOpt.get();

        if (!loguin.getPassword().equals(password)) {
            return null; // contraseña incorrecta
        }

        return loguin; // éxito
    }
}
