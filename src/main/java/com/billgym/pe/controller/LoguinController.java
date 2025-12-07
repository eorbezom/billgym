package com.billgym.pe.controller;

import java.util.List;
import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.billgym.pe.entity.Loguin;
import com.billgym.pe.repository.LoguinRepository;
import com.billgym.pe.service.LoguinService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoguinController {
    
    private final LoguinService loguinService;
    private final LoguinRepository loguinRepository;

    public LoguinController(LoguinService loguinService, LoguinRepository loguinRepository) {
        this.loguinService = loguinService;
        this.loguinRepository = loguinRepository;
    }

    // ================== GESTIÓN DE USUARIOS LOGUIN ==================
    
    @GetMapping("/loguinTabla")
    public String obtenerLoguin(Model model) {
        List<Loguin> loguin = loguinService.listarLoguin();
        model.addAttribute("loguins", loguin);
        return "loginTabla";
    }

    @GetMapping("/loguinTabla/edit/{id_loguin}")
    public String editarLoguin(@PathVariable("id_loguin") Integer id_loguin, Model model) {
        Loguin loguin = loguinService.obtenerLoguin(id_loguin);
        model.addAttribute("loguin", loguin);
        return "editarLogin";
    }

    @PostMapping("/loguinTabla/actualizar")
    public String actualizarLoguin(@ModelAttribute("loguin") Loguin loguin, RedirectAttributes redirectAttributes) {
        loguinService.guardarLoguin(loguin);
        redirectAttributes.addFlashAttribute("mensaje", "Login actualizado correctamente");
        return "redirect:/loguinTabla";
    }

    @GetMapping("/loguinTabla/crear")
    public String crearLoguin(Model model) {
        model.addAttribute("loguin", new Loguin());
        return "crearLoguin";
    }

    @PostMapping("/loguinTabla/guardar")
    public String guardarLoguin(@ModelAttribute("loguin") Loguin loguin, RedirectAttributes redirectAttributes) {
        loguinService.guardarLoguin(loguin);
        redirectAttributes.addFlashAttribute("mensaje", "Login guardado correctamente");
        return "redirect:/loguinTabla";
    }

    @GetMapping("/loguinTabla/eliminar/{id_loguin}")
    public String eliminarLoguin(@PathVariable("id_loguin") Integer id_loguin) {
        loguinService.eliminar(id_loguin);
        return "redirect:/loguinTabla";
    }

    @GetMapping("/loguinTabla/buscar")
    public String buscarUsuarioPorDni(@RequestParam("buscar") String terminoBusqueda, Model model) {
        List<Loguin> resultados = loguinService.buscar(terminoBusqueda);
        model.addAttribute("loguins", resultados);
        return "loguinTabla";
    }

    // ================== LOGIN Y HOME ==================
    // ✅ Spring Security maneja el login automáticamente
    
    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        // ✅ Spring Security maneja error y logout automáticamente con param.error y param.logout
        return "formularioLogin";
    }

    // ✅ CORREGIDO: Spring Security valida automáticamente
    @GetMapping("/home")
    public String mostrarHome(Model model, HttpServletResponse response) {
        // Prevenir caché
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Obtener usuario autenticado por Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Buscar datos del usuario
        Loguin loguin = loguinRepository.findByUsuario(username).orElse(null);
        String nombreCompleto = (loguin != null && loguin.getUsuarioDato() != null)
                ? loguin.getUsuarioDato().getNombres()
                : username;
        
        model.addAttribute("nombreUsuario", nombreCompleto);
        model.addAttribute("rol", auth.getAuthorities().toString());
        
        return "home";
    }

    @GetMapping("/inicio")
    public String mostrarInicio(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Loguin loguin = loguinRepository.findByUsuario(username).orElse(null);
        String nombreCompleto = (loguin != null && loguin.getUsuarioDato() != null)
                ? loguin.getUsuarioDato().getNombres()
                : username;
        
        model.addAttribute("nombreUsuario", nombreCompleto);
        return "inicio"; // o redirige a "redirect:/home" si es la misma página
    }
}