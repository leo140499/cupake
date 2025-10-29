package com.msoftwares.demo.controller;

import com.msoftwares.demo.dto.AdminDTO;
import com.msoftwares.demo.dto.ClienteDTO;
import com.msoftwares.demo.dto.EnderecoDTO;
import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.Cliente;
import com.msoftwares.demo.service.AdminService;
import com.msoftwares.demo.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private ClienteService clienteService;

    @Mock
    private AdminService adminService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ========== CLIENTE ==========
    @Test
    void exibirFormularioCadastroCliente_deveAdicionarClienteDTO() {
        String view = loginController.exibirFormularioCadastroCliente(model);
        verify(model).addAttribute(eq("clienteDTO"), any(ClienteDTO.class));
        assertEquals("pages/cadastro-cliente", view);
    }

    @Test
    void processarCadastroCliente_sucesso() throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO();
        String view = loginController.processarCadastroCliente(clienteDTO, model);
        verify(clienteService).cadastrar(clienteDTO);
        assertEquals("redirect:/cliente/login", view);
    }

    @Test
    void processarCadastroCliente_erro() throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO();
        doThrow(new RuntimeException("Erro")).when(clienteService).cadastrar(clienteDTO);

        String view = loginController.processarCadastroCliente(clienteDTO, model);

        verify(model).addAttribute(eq("erro"), contains("Erro ao cadastrar cliente"));
        assertEquals("pages/cadastro-cliente", view);
    }

    @Test
    void processarLoginCliente_sucesso() {
        Cliente cliente = new Cliente();
        when(clienteService.login("email", "senha")).thenReturn(cliente);

        String view = loginController.processarLoginCliente("email", "senha", model, session, redirectAttributes);

        verify(session).setAttribute("clienteLogado", cliente);
        assertEquals("redirect:/cliente/dashboard", view);
    }

    @Test
    void processarLoginCliente_falha() {
        when(clienteService.login("email", "senha")).thenReturn(null);

        String view = loginController.processarLoginCliente("email", "senha", model, session, redirectAttributes);

        verify(model).addAttribute("erro", "Email ou senha inválidos.");
        assertEquals("pages/login-cliente", view);
    }

    // ========== ADMIN ==========
    @Test
    void exibirFormularioCadastroAdmin_deveAdicionarAdminDTO() {
        String view = loginController.exibirFormularioCadastroAdmin(model);
        verify(model).addAttribute(eq("adminDTO"), any(AdminDTO.class));
        assertEquals("pages/cadastro-admin", view);
    }

    @Test
    void processarCadastroAdmin_sucesso() throws Exception {
        AdminDTO adminDTO = new AdminDTO();
        String view = loginController.processarCadastroAdmin(adminDTO, model);
        verify(adminService).cadastrar(adminDTO);
        assertEquals("redirect:/admin/login", view);
    }

    @Test
    void processarCadastroAdmin_erro() throws Exception {
        AdminDTO adminDTO = new AdminDTO();
        doThrow(new RuntimeException("Erro")).when(adminService).cadastrar(adminDTO);

        String view = loginController.processarCadastroAdmin(adminDTO, model);

        verify(model).addAttribute(eq("erro"), contains("Erro ao cadastrar administrador"));
        assertEquals("pages/cadastro-admin", view);
    }

    @Test
    void processarLoginAdmin_sucesso() {
        Administrador admin = new Administrador();
        when(adminService.login("email", "senha")).thenReturn(admin);

        String view = loginController.processarLoginAdmin(session, redirectAttributes, "email", "senha", model);

        verify(session).setAttribute("adminLogado", admin);
        assertEquals("redirect:/admin/dashboard", view);
    }

    @Test
    void processarLoginAdmin_falha() {
        when(adminService.login("email", "senha")).thenReturn(null);

        String view = loginController.processarLoginAdmin(session, redirectAttributes, "email", "senha", model);

        verify(model).addAttribute("erro", "Email ou senha inválidos.");
        assertEquals("pages/login-admin", view);
    }
}
