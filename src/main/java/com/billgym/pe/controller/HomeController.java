package com.billgym.pe.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
	// Ruta principal
				
		@GetMapping("/sobreNosotros")
		public String mostrarSobreNosotros(Model model) {
			model.addAttribute("mensaje", "desde controller sobre Nosotros");
			return"sobreNosotros";
		}
		
		@GetMapping("/soporte")
		public String mostrarSoporte(Model model) {
			model.addAttribute("mensaje"," Desde controller soporte tecnico");
			return"soporte";
		}
		

}
