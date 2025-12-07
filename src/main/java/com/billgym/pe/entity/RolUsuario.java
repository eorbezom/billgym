package com.billgym.pe.entity;

public enum RolUsuario {
	
	PRESIDENTE_FUNDADOR("ROLE_PRESIDENTE_FUNDADOR"),
    GERENTE_GENERAL("ROLE_GERENTE_GENERAL"),
    GERENTE_OPERACIONES("ROLE_GERENTE_OPERACIONES"),
    OPERARIO("ROLE_OPERARIO"),
    ENCARGADO("ROLE_ENCARGADO"),
    AYUDANTE("ROLE_AYUDANTE"),
    SEGURIDAD("ROLE_SEGURIDAD"),
    LIMPIEZA("ROLE_LIMPIEZA"),
    SUPERVISOR("ROLE_SUPERVISOR");
	

	private final String nombreRol;
	
	 RolUsuario(String nombreRol) {
	        this.nombreRol = nombreRol;
	    }

	public String getNombreRol() {
		return nombreRol;
	}

	public static RolUsuario fromNombreRol(String nombreRol) {
        for (RolUsuario rol : RolUsuario.values()) {
            if (rol.nombreRol.equalsIgnoreCase(nombreRol)) {
                return rol;
            }
        }
        throw new IllegalArgumentException("No se encontró un RolUsuario para: " + nombreRol);
    }

}
