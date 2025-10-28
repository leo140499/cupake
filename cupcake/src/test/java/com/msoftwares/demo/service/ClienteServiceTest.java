package com.msoftwares.demo.service;

import com.msoftwares.demo.dto.ClienteDTO;
import com.msoftwares.demo.dto.EnderecoDTO;
import com.msoftwares.demo.model.Cliente;
import com.msoftwares.demo.model.DadosPessoais;
import com.msoftwares.demo.model.Endereco;
import com.msoftwares.demo.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ClienteDTO criarDTO() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("Cliente Teste");
        dto.setCpf("12345678900");
        dto.setEmail("cliente@teste.com");
        dto.setSenha("abc123");
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setRua("Rua XPTO");
        enderecoDTO.setBairro("Centro");
        enderecoDTO.setCidade("Cidadezinha");
        enderecoDTO.setComplemento("Casa 1");
        dto.setEnderecoDTO(enderecoDTO);
        return dto;
    }

    @Test
    void cadastrar_DeveSalvarCliente_QuandoDadosValidos() throws Exception {
        ClienteDTO dto = criarDTO();

        when(clienteRepository.findByDadosPessoaisEmail(dto.getEmail())).thenReturn(null);
        when(clienteRepository.findByDadosPessoaisDocumento(dto.getCpf())).thenReturn(null);

        clienteService.cadastrar(dto);

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteRepository).save(captor.capture());

        Cliente salvo = captor.getValue();
        assertEquals(dto.getEmail(), salvo.getDadosPessoais().getEmail());
    }

    @Test
    void cadastrar_DeveLancarExcecao_SeEmailExistir() {
        ClienteDTO dto = criarDTO();
        when(clienteRepository.findByDadosPessoaisEmail(dto.getEmail())).thenReturn(new Cliente());

        Exception e = assertThrows(Exception.class, () -> clienteService.cadastrar(dto));
        assertEquals("Email já existente", e.getMessage());
    }

    @Test
    void cadastrar_DeveLancarExcecao_SeCPFExistir() {
        ClienteDTO dto = criarDTO();
        when(clienteRepository.findByDadosPessoaisEmail(dto.getEmail())).thenReturn(null);
        when(clienteRepository.findByDadosPessoaisDocumento(dto.getCpf())).thenReturn(new Cliente());

        Exception e = assertThrows(Exception.class, () -> clienteService.cadastrar(dto));
        assertEquals("CPF já existente", e.getMessage());
    }

    @Test
    void cadastrar_DeveLancarExcecao_SeSenhaExistir() {
        ClienteDTO dto = criarDTO();
        when(clienteRepository.findByDadosPessoaisEmail(dto.getEmail())).thenReturn(null);
        when(clienteRepository.findByDadosPessoaisDocumento(dto.getCpf())).thenReturn(null);
        when(clienteRepository.findByDadosPessoaisEmail(dto.getSenha())).thenReturn(new Cliente()); // mesmo problema do AdminService

        Exception e = assertThrows(Exception.class, () -> clienteService.cadastrar(dto));
        assertEquals("Senha já existente", e.getMessage());
    }

    @Test
    void login_DeveRetornarCliente_SeCredenciaisValidas() {
        DadosPessoais dados = new DadosPessoais();
        dados.setEmail("cliente@teste.com");
        dados.setSenha("abc123");

        Cliente cliente = new Cliente();
        cliente.setDadosPessoais(dados);

        when(clienteRepository.findByDadosPessoaisEmail("cliente@teste.com")).thenReturn(cliente);

        Cliente resultado = clienteService.login("cliente@teste.com", "abc123");

        assertNotNull(resultado);
        assertEquals("abc123", resultado.getDadosPessoais().getSenha());
    }

    @Test
    void login_DeveRetornarNull_SeSenhaIncorreta() {
        DadosPessoais dados = new DadosPessoais();
        dados.setEmail("cliente@teste.com");
        dados.setSenha("senhaCorreta");

        Cliente cliente = new Cliente();
        cliente.setDadosPessoais(dados);

        when(clienteRepository.findByDadosPessoaisEmail("cliente@teste.com")).thenReturn(cliente);

        Cliente resultado = clienteService.login("cliente@teste.com", "senhaErrada");

        assertNull(resultado);
    }

    @Test
    void verificarEmailExistente_DeveRetornarTrue_SeEmailExistir() {
        when(clienteRepository.findByDadosPessoaisEmail("cliente@teste.com")).thenReturn(new Cliente());
        assertTrue(clienteService.verificarEmailExistente("cliente@teste.com"));
    }

    @Test
    void verificarEmailExistente_DeveRetornarFalse_SeNaoExistir() {
        when(clienteRepository.findByDadosPessoaisEmail("cliente@teste.com")).thenReturn(null);
        assertFalse(clienteService.verificarEmailExistente("cliente@teste.com"));
    }

    @Test
    void verificarDocumentoExistente_DeveRetornarTrue_SeDocumentoExistir() {
        when(clienteRepository.findByDadosPessoaisDocumento("12345678900")).thenReturn(new Cliente());
        assertTrue(clienteService.verificarDocumentoExistente("12345678900"));
    }

    @Test
    void verificarSenhaExistente_DeveRetornarTrue_SeSenhaExistir() {
        when(clienteRepository.findByDadosPessoaisEmail("abc123")).thenReturn(new Cliente()); // problema estrutural
        assertTrue(clienteService.verificarSenhaExistente("abc123"));
    }
}
