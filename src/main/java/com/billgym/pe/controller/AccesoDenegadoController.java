package com.billgym.pe.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AccesoDenegadoController {

    @GetMapping("/acceso-denegado")
    public String accesoDenegado(HttpServletRequest request, Model model) {
        
        // Obtener información del usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String rol = auth.getAuthorities().toString();
        
        // Obtener la URL que intentó acceder
        String referer = request.getHeader("Referer");
        String urlIntentada = (referer != null) ? referer : "desconocida";
        
        // Pasar datos al modelo
        model.addAttribute("username", username);
        model.addAttribute("rol", rol);
        model.addAttribute("urlIntentada", urlIntentada);
        
        // Log para debug (opcional)
        System.out.println("⚠️ Acceso denegado para: " + username + " | Rol: " + rol + " | URL: " + urlIntentada);
        
        return "acceso-denegado";
    }
}