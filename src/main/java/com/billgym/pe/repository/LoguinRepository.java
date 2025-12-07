package com.billgym.pe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.billgym.pe.entity.Loguin;

public interface LoguinRepository extends JpaRepository<Loguin, Integer> {
	//PARA BUSCAR USUARIO POR DNI
	@Query("SELECT l FROM Loguin l JOIN l.usuarioDato u WHERE u.dni = :dni")
	List<Loguin> buscarPorDniUsuario(@Param("dni")String usuario);
	
	//PARA VALIDAR CONTRASEÑA
	// Loguin findByUsuarioAndPassword(String usuario, String password);
	 
	// Buscar por nombre de usuario
	   Optional<Loguin> findByUsuario(String usuario);

}
