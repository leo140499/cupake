package com.msoftwares.demo.controller;

import com.msoftwares.demo.model.*;
import com.msoftwares.demo.service.PedidoService;
import com.msoftwares.demo.service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PedidoControllerTest {

    @InjectMocks
    private PedidoController pedidoController;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Captor
    private ArgumentCaptor<Pedido> pedidoCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void adicionarAoCarrinho_deveAdicionarProdutoENavegarCorretamente() {
        Produto produto = new Produto();
        produto.setId(1L);
        when(produtoService.buscarPorId(1L)).thenReturn(produto);
        when(session.getAttribute("carrinho")).thenReturn(null);

        String view = pedidoController.adicionarAoCarrinho(1L, 2, session, redirectAttributes);

        verify(session).setAttribute(eq("carrinho"), any(Carrinho.class));
        verify(redirectAttributes).addFlashAttribute("mensagem", "Cupcake adicionado com sucesso!");
        assertEquals("redirect:/cupcakes/comprar/all", view);
    }

    @Test
    void removerDoCarrinho_deveRemoverProduto() {
        Carrinho carrinho = mock(Carrinho.class);
        when(session.getAttribute("carrinho")).thenReturn(carrinho);

        String view = pedidoController.removerDoCarrinho(1L, session, redirectAttributes);

        verify(carrinho).removerProduto(1L);
        verify(session).setAttribute("carrinho", carrinho);
        verify(redirectAttributes).addFlashAttribute("mensagem", "Produto removido do carrinho.");
        assertEquals("redirect:/cupcakes/comprar/all", view);
    }

    @Test
    void limparCarrinho_deveRemoverCarrinhoDaSessao() {
        String view = pedidoController.limparCarrinho(session, redirectAttributes);

        verify(session).removeAttribute("carrinho");
        verify(redirectAttributes).addFlashAttribute("mensagem", "Carrinho esvaziado com sucesso.");
        assertEquals("redirect:/cupcakes/comprar/all", view);
    }

    @Test
    void finalizarCompra_comCarrinhoVazio_deveRetornarErro() {
        when(session.getAttribute("carrinho")).thenReturn(null);

        String view = pedidoController.finalizarCompra(session, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("erro", "Seu carrinho está vazio.");
        assertEquals("redirect:/cupcakes/comprar/all", view);
    }

    @Test
    void finalizarCompra_semClienteLogado_deveRetornarErro() {
        Carrinho carrinho = new Carrinho();
        carrinho.adicionarItem(new Produto(), 1);
        when(session.getAttribute("carrinho")).thenReturn(carrinho);
        when(session.getAttribute("clienteLogado")).thenReturn(null);

        String view = pedidoController.finalizarCompra(session, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("erro", "Você precisa estar logado para finalizar a compra.");
        assertEquals("redirect:/login?tipo=cliente", view);
    }

    @Test
    void finalizarCompra_comDadosValidos_deveSalvarEPedirSucesso() {
        Carrinho carrinho = new Carrinho();
        Produto produto = new Produto();
        produto.setPreco(BigDecimal.valueOf(10));
        produto.setNome("Brigadeiro");

        carrinho.adicionarItem(produto, 2);
        when(session.getAttribute("carrinho")).thenReturn(carrinho);

        Cliente cliente = new Cliente();
        when(session.getAttribute("clienteLogado")).thenReturn(cliente);

        String view = pedidoController.finalizarCompra(session, redirectAttributes);

        verify(pedidoService).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();

        assertEquals(cliente, pedidoSalvo.getCliente());
        assertEquals(1, pedidoSalvo.getItens().size());
        assertEquals(BigDecimal.valueOf(20), pedidoSalvo.getValor());

        verify(session).removeAttribute("carrinho");
        verify(redirectAttributes).addFlashAttribute("mensagem", "Pedido realizado com sucesso!");
        assertEquals("redirect:/pedidos/sucesso", view);
    }

    @Test
    void mostrarDashboard_deveRetornarPaginaDeSucesso() {
        String view = pedidoController.mostrarDashboard();
        assertEquals("pages/sucesso", view);
    }
}
