package com.billgym.pe.service;

import java.util.List;
import com.billgym.pe.entity.Loguin;

public interface LoginService {

    List<Loguin> listarLoguin();

    Loguin obtenerLoguin(Integer id);

    Loguin guardarLoguin(Loguin loguin);

    void eliminar(Integer id);

    List<Loguin> buscar(String dni);

    Loguin validarCredenciales(String usuario, String password);
}
