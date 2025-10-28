package com.msoftwares.demo.service;

import com.msoftwares.demo.dto.AdminDTO;
import com.msoftwares.demo.dto.EnderecoDTO;
import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.DadosPessoais;
import com.msoftwares.demo.model.Endereco;
import com.msoftwares.demo.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private AdminDTO criarDTO() {
        AdminDTO dto = new AdminDTO();
        dto.setNome("Admin Teste");
        dto.setCpf("12345678900");
        dto.setEmail("admin@teste.com");
        dto.setSenha("123");
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setRua("Rua A");
        enderecoDTO.setBairro("Bairro B");
        enderecoDTO.setCidade("Cidade C");
        enderecoDTO.setComplemento("Ap 101");
        dto.setEnderecoDTO(enderecoDTO);
        return dto;
    }

    @Test
    void cadastrar_DeveSalvarAdministrador_QuandoDadosValidos() throws Exception {
        AdminDTO dto = criarDTO();

        when(adminRepository.findByDadosPessoaisEmail(dto.getEmail())).thenReturn(null);
        when(adminRepository.findByDadosPessoaisDocumento(dto.getCpf())).thenReturn(null);

        adminService.cadastrar(dto);

        ArgumentCaptor<Administrador> captor = ArgumentCaptor.forClass(Administrador.class);
        verify(adminRepository).save(captor.capture());

        Administrador salvo = captor.getValue();
        assertEquals(dto.getEmail(), salvo.getDadosPessoais().getEmail());
    }

    @Test
    void cadastrar_DeveLancarExcecao_QuandoEmailExistente() {
        AdminDTO dto = criarDTO();
        when(adminRepository.findByDadosPessoaisEmail(dto.getEmail())).thenReturn(new Administrador());

        Exception e = assertThrows(Exception.class, () -> adminService.cadastrar(dto));
        assertEquals("Email já existente", e.getMessage());
    }

    @Test
    void cadastrar_DeveLancarExcecao_QuandoCPFExistente() {
        AdminDTO dto = criarDTO();
        when(adminRepository.findByDadosPessoaisEmail(dto.getEmail())).thenReturn(null);
        when(adminRepository.findByDadosPessoaisDocumento(dto.getCpf())).thenReturn(new Administrador());

        Exception e = assertThrows(Exception.class, () -> adminService.cadastrar(dto));
        assertEquals("CPF já existente", e.getMessage());
    }

    @Test
    void cadastrar_DeveLancarExcecao_QuandoSenhaExistente() {
        AdminDTO dto = criarDTO();
        when(adminRepository.findByDadosPessoaisEmail(dto.getEmail())).thenReturn(null);
        when(adminRepository.findByDadosPessoaisDocumento(dto.getCpf())).thenReturn(null);
        when(adminRepository.findByDadosPessoaisEmail(dto.getSenha())).thenReturn(new Administrador()); // errado, mas é o que tá no código

        Exception e = assertThrows(Exception.class, () -> adminService.cadastrar(dto));
        assertEquals("Senha já existente", e.getMessage());
    }

    @Test
    void login_DeveRetornarAdministrador_QuandoCredenciaisValidas() {
        DadosPessoais dados = new DadosPessoais();
        dados.setEmail("admin@teste.com");
        dados.setSenha("123");

        Administrador admin = new Administrador();
        admin.setDadosPessoais(dados);

        when(adminRepository.findByDadosPessoaisEmail("admin@teste.com")).thenReturn(admin);

        Administrador resultado = adminService.login("admin@teste.com", "123");

        assertNotNull(resultado);
        assertEquals("123", resultado.getDadosPessoais().getSenha());
    }

    @Test
    void login_DeveRetornarNull_QuandoSenhaIncorreta() {
        DadosPessoais dados = new DadosPessoais();
        dados.setEmail("admin@teste.com");
        dados.setSenha("senhaCorreta");

        Administrador admin = new Administrador();
        admin.setDadosPessoais(dados);

        when(adminRepository.findByDadosPessoaisEmail("admin@teste.com")).thenReturn(admin);

        Administrador resultado = adminService.login("admin@teste.com", "senhaErrada");

        assertNull(resultado);
    }

    @Test
    void verificarEmailExistente_DeveRetornarTrue_SeEmailExistir() {
        when(adminRepository.findByDadosPessoaisEmail("admin@teste.com")).thenReturn(new Administrador());
        assertTrue(adminService.verificarEmailExistente("admin@teste.com"));
    }

    @Test
    void verificarEmailExistente_DeveRetornarFalse_SeNaoExistir() {
        when(adminRepository.findByDadosPessoaisEmail("admin@teste.com")).thenReturn(null);
        assertFalse(adminService.verificarEmailExistente("admin@teste.com"));
    }

    @Test
    void verificarDocumentoExistente_DeveRetornarTrue_SeDocumentoExistir() {
        when(adminRepository.findByDadosPessoaisDocumento("12345678900")).thenReturn(new Administrador());
        assertTrue(adminService.verificarDocumentoExistente("12345678900"));
    }

    @Test
    void verificarSenhaExistente_DeveRetornarTrue_SeSenhaExistir() {
        when(adminRepository.findByDadosPessoaisEmail("123")).thenReturn(new Administrador());
        assertTrue(adminService.verificarSenhaExistente("123"));
    }
}
