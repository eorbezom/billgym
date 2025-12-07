package com.billgym.pe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.billgym.pe.entity.RolUsuario;
import com.billgym.pe.entity.Usuario;
import com.billgym.pe.exeption.DniDuplicadoExeption;
import com.billgym.pe.service.UsuarioService;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
	//CONSTRUCTOR
	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
		
	}
	
	//LISTAR USUARIOS
	@GetMapping("/usuarios")
	public String obtenerUsuario(Model model) {
		List<Usuario> usuarios = usuarioService.listarUsuario();
		model.addAttribute("usuarios",usuarios);
		return"usuarios";
	}
	
	
	//EDITAR USUSARIOS
	
	@GetMapping("/usuarios/edit/{id}")
	public String editarUsuario(@PathVariable("id")Integer id, Model model) {
		Usuario usuario = usuarioService.obtenerUsuario(id);
		model.addAttribute("usuario", usuario);
		return"editarUsuario";
	}
	
	//PARA ACTIALIZAR USUARIO
	@PostMapping("/usuarios/actualizar")
	public String actualizarUsuario(@ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes) {
		try {
			usuarioService.guardarUsuario(usuario);
			redirectAttributes.addFlashAttribute("mensaje", "Usuario Actualizado Correctamente :");
			return"redirect:/usuarios";
		} catch (DniDuplicadoExeption e) {
			redirectAttributes.addFlashAttribute("errorDni", e.getMessage());
			return"redirect:/usuarios/edit"+usuario.getId();
		}
	}
	
	//CREAR USUARIO
	@GetMapping("/usuarios/crear")
	public String crearUsuario(Model model) {
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("roles",RolUsuario.values());
		return"crearUsuario";
	}
	
	//GUARDAR USUARIO
	@PostMapping("/usuarios/guardar")
	public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
		try {
			usuarioService.guardarUsuario(usuario);
			return"redirect:/usuarios";
		} catch (DniDuplicadoExeption e) {
			model.addAttribute("errorDni",e.getMessage());
			model.addAttribute("usuario", usuario);
			return"crearUsuario";
		}
		
	}
	
	//ELIMINAR USUARIO
	@GetMapping("/usuarios/eliminar/{id}")
	public String eliminarUsuario(@PathVariable("id") Integer id) {
		usuarioService.eliminarUsuario(id);
		return"redirect:/usuarios";
	}
	
	//BUSCAR USUARIO POR SU DNI O NOMBRE
	@GetMapping("/usuarios/buscar")
	public String buscarUsuario(@RequestParam(value = "buscar", required = false) String terminoBusqueda, Model model) {
	    if (terminoBusqueda == null || terminoBusqueda.trim().isEmpty()) {
	        // Si no hay término de búsqueda, mostrar todos los usuarios
	        return "redirect:/usuarios";
	    }
	    List<Usuario> resultados = usuarioService.buscar(terminoBusqueda);
	    model.addAttribute("usuarios", resultados);
	    return "usuarios";
	}

}
