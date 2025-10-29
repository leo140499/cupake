package com.msoftwares.demo.controller;

import com.msoftwares.demo.dto.AdminDTO;
import com.msoftwares.demo.dto.ClienteDTO;
import com.msoftwares.demo.dto.EnderecoDTO;
import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.Cliente;
import com.msoftwares.demo.service.AdminService;
import com.msoftwares.demo.service.ClienteService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private AdminService adminService;


    @GetMapping("/")
    public String landingPage() {
        return "pages/landpage";
    }


    // ========== CLIENTE ==========
    @GetMapping("/cliente/cadastro")
    public String exibirFormularioCadastroCliente(Model model) {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setEnderecoDTO(new EnderecoDTO());
        model.addAttribute("clienteDTO", clienteDTO);
        return "pages/cadastro-cliente";
    }

    @PostMapping("/cliente/cadastro")
    public String processarCadastroCliente(@ModelAttribute("clienteDTO") ClienteDTO clienteDTO, Model model) {
        try {
            clienteService.cadastrar(clienteDTO);
            return "redirect:/cliente/login";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao cadastrar cliente: " + e.getMessage());
            return "pages/cadastro-cliente";
        }
    }

    @GetMapping("/cliente/login")
    public String exibirLoginCliente() {
        return "pages/login-cliente";
    }

    @PostMapping("/cliente/login")
    public String processarLoginCliente(@RequestParam String email, @RequestParam String senha, Model model, HttpSession session,
                                        RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteService.login(email, senha);
        if (cliente != null) {
            session.setAttribute("clienteLogado", cliente);
            return "redirect:/cliente/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("erro", "Email ou senha inválidos");
            model.addAttribute("erro", "Email ou senha inválidos.");
            return "pages/login-cliente";
        }
    }

    // ========== ADMIN ==========
    @GetMapping("/admin/cadastro")
    public String exibirFormularioCadastroAdmin(Model model) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setEnderecoDTO(new EnderecoDTO());
        model.addAttribute("adminDTO", adminDTO);
        return "pages/cadastro-admin";
    }

    @PostMapping("/admin/cadastro")
    public String processarCadastroAdmin(@ModelAttribute("adminDTO") AdminDTO adminDTO, Model model) {
        try {
            adminService.cadastrar(adminDTO);
            return "redirect:/admin/login";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao cadastrar administrador: " + e.getMessage());
            return "pages/cadastro-admin";
        }
    }

    @GetMapping("/admin/login")
    public String exibirLoginAdmin() {
        return "pages/login-admin";
    }

    @PostMapping("/admin/login")
    public String processarLoginAdmin( HttpSession session,
                                       RedirectAttributes redirectAttributes,
                                       @RequestParam String email, @RequestParam String senha, Model model) {
        Administrador admin = adminService.login(email, senha);
        if (admin != null) {
            session.setAttribute("adminLogado", admin);
            return "redirect:/admin/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("erro", "Email ou senha inválidos");
            model.addAttribute("erro", "Email ou senha inválidos.");
            return "pages/login-admin";
        }
    }
}
