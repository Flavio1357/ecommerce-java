import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import enums.CategoriaProduto;
import enums.StatusPedido;
import exception.CarrinhoVazioException;
import exception.PagamentoNaoDefinidoException;
import exception.PedidoJaPagoException;
import exception.ProdutoIndisponivelException;
import model.Carrinho;
import model.Cliente;
import model.ItemCarrinho;
import model.Pedido;
import model.Produto;
import model.ProdutoNacional;
import pagamento.PagamentoCredito;
import pagamento.PagamentoDebito;
import pagamento.PagamentoMIX;
import service.PedidoService;

public class PedidoTest {

	@Test
	public void deveCriarPedidoCorretamente() {

		Cliente cliente = new Cliente(
			"Rua A",
			1,
			"Flavio",
			"flavio@email.com",
			"123456",
			"12345678900"
		);

		ProdutoNacional produto = new ProdutoNacional(
			1,
			"Notebook",
			"Notebook nacional",
			3000.00,
			10,
			100,
			CategoriaProduto.ELETRONICO
		);

		Carrinho carrinho = new Carrinho();
		carrinho.adicionarItem(new ItemCarrinho(produto, 2));

		Pedido pedido = new Pedido(1, cliente, carrinho);

		assertEquals(1, pedido.getId());
		assertEquals(cliente, pedido.getCliente());
		assertEquals(carrinho, pedido.getCarrinho());
		assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, pedido.getStatus());
		assertEquals(6000.00, pedido.calcularTotal());
	}

    @Test
    public void deveImpedirPedidoComCarrinhoVazio() {

	    Cliente cliente = new Cliente(
		    "Rua A",
		    1,
		    "Flavio",
		    "flavio@email.com",
		    "123456",
		    "12345678900"
	    );

	    Carrinho carrinho = new Carrinho();

	    assertThrows(
		    CarrinhoVazioException.class,
		    () -> new Pedido(1, cliente, carrinho)
	    );
    }

	@Test
	public void deveAdicionarFormaPagamentoAoPedido() {

		Cliente cliente = new Cliente(
			"Rua A",
			1,
			"Flavio",
			"flavio@email.com",
			"123456",
			"12345678900"
		);

		ProdutoNacional produto = new ProdutoNacional(
			1,
			"Notebook",
			"Notebook nacional",
			3000.00,
			10,
			100,
			CategoriaProduto.ELETRONICO
		);

		Carrinho carrinho = new Carrinho();
		carrinho.adicionarItem(new ItemCarrinho(produto, 2));

		Pedido pedido = new Pedido(1, cliente, carrinho);

		PagamentoDebito pagamento = new PagamentoDebito();

		pedido.setFormaPagamento(pagamento);

		assertEquals(pagamento, pedido.getFormaPagamento());
	}

	@Test
	public void deveRealizarPagamentoDoPedido() {

		Cliente cliente = new Cliente(
			"Rua A",
			1,
			"Flavio",
			"flavio@email.com",
			"123456",
			"12345678900"
		);

		ProdutoNacional produto = new ProdutoNacional(
			1,
			"Notebook",
			"Notebook nacional",
			3000.00,
			10,
			100,
			CategoriaProduto.ELETRONICO
		);

		Carrinho carrinho = new Carrinho();
		carrinho.adicionarItem(new ItemCarrinho(produto, 2));

		Pedido pedido = new Pedido(1, cliente, carrinho);

		pedido.setFormaPagamento(new PagamentoDebito());

		pedido.pagar();

		assertEquals(StatusPedido.PAGO, pedido.getStatus());
	}

	@Test
	public void deveImpedirPagamentoSemFormaPagamento() {

		Cliente cliente = new Cliente(
			"Rua A",
			1,
			"Flavio",
			"flavio@email.com",
			"123456",
			"12345678900"
		);

		ProdutoNacional produto = new ProdutoNacional(
			1,
			"Notebook",
			"Notebook nacional",
			3000.00,
			10,
			100,
			CategoriaProduto.ELETRONICO
		);

		Carrinho carrinho = new Carrinho();
		carrinho.adicionarItem(new ItemCarrinho(produto, 2));

		Pedido pedido = new Pedido(1, cliente, carrinho);

		assertThrows(
			PagamentoNaoDefinidoException.class,
			() -> pedido.pagar()
		);
	}

	@Test
	public void deveRealizarPagamentoMIXDoPedido() {

		Cliente cliente = new Cliente(
			"Rua A",
			1,
			"Flavio",
			"flavio@email.com",
			"123456",
			"12345678900"
		);

		ProdutoNacional produto = new ProdutoNacional(
			1,
			"Notebook",
			"Notebook nacional",
			3000.00,
			10,
			100,
			CategoriaProduto.ELETRONICO
		);

		Carrinho carrinho = new Carrinho();
		carrinho.adicionarItem(new ItemCarrinho(produto, 2));

		Pedido pedido = new Pedido(1, cliente, carrinho);

		PagamentoMIX pagamento = new PagamentoMIX(
			new PagamentoDebito(),
			new PagamentoCredito(),
			3000.00,
			3000.00
		);

		pedido.setFormaPagamento(pagamento);

		pedido.pagar();

		assertEquals(StatusPedido.PAGO, pedido.getStatus());
	}

	@Test
	public void deveImpedirPagamentoDuplicado() {

		Cliente cliente = new Cliente(
			"Rua A",
			1,
			"Flavio",
			"flavio@email.com",
			"123456",
			"12345678900"
		);

		ProdutoNacional produto = new ProdutoNacional(
			1,
			"Notebook",
			"Notebook nacional",
			3000.00,
			10,
			100,
			CategoriaProduto.ELETRONICO
		);

		Carrinho carrinho = new Carrinho();
		carrinho.adicionarItem(new ItemCarrinho(produto, 2));

		Pedido pedido = new Pedido(1, cliente, carrinho);

		pedido.setFormaPagamento(new PagamentoDebito());

		pedido.pagar();

		assertThrows(
			PedidoJaPagoException.class,
			() -> pedido.pagar()
		);
	}

	@Test
	public void deveReduzirEstoqueAoPagarPedido() {

		Cliente cliente = new Cliente(
			"Rua A",
			1,
			"Flavio",
			"flavio@email.com",
			"123456",
			"12345678900"
		);

		ProdutoNacional produto = new ProdutoNacional(
			1,
			"Notebook",
			"Notebook nacional",
			3000.00,
			10,
			100,
			CategoriaProduto.ELETRONICO
		);

		Carrinho carrinho = new Carrinho();
		carrinho.adicionarItem(new ItemCarrinho(produto, 2));

		Pedido pedido = new Pedido(1, cliente, carrinho);

		pedido.setFormaPagamento(new PagamentoDebito());

		pedido.pagar();

		assertEquals(8, produto.getEstoque());
	}

	@Test
	public void deveImpedirPagamentoQuandoEstoqueForInsuficiente() {

		Cliente cliente = new Cliente(
			"Rua A",
			1,
			"Flavio",
			"flavio@email.com",
			"123456",
			"12345678900"
		);

		ProdutoNacional produto = new ProdutoNacional(
			1,
			"Notebook",
			"Notebook nacional",
			3000.00,
			2,
			100,
			CategoriaProduto.ELETRONICO
		);

		Carrinho carrinho = new Carrinho();

		carrinho.adicionarItem(new ItemCarrinho(produto, 2));

		Pedido pedido = new Pedido(1, cliente, carrinho);

		produto.reduzirEstoque(1);

		pedido.setFormaPagamento(new PagamentoDebito());

		assertThrows(
			ProdutoIndisponivelException.class,
			() -> pedido.pagar()
		);

		assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, pedido.getStatus());
	}

	@Test
	public void deveRealizarPagamentoPeloService() {
    	Cliente cliente = new Cliente(
        	"Rua A",
       		1,
        	"Flavio",
        	"flavio@email.com",
        	"123",
        	"11111111111"
    	);

    	Produto produto = new ProdutoNacional(
        	1,
        	"Notebook",
       		"Notebook para estudo",
        	3000.00,
        	10,
        	100,
        	CategoriaProduto.ELETRONICO
    	);

    	Carrinho carrinho = new Carrinho();
    	carrinho.adicionarItem(new ItemCarrinho(produto, 1));

    	Pedido pedido = new Pedido(1, cliente, carrinho);
    	pedido.setFormaPagamento(new PagamentoDebito());

    	PedidoService service = new PedidoService();

    	service.realizarPagamento(pedido);

    	assertEquals(StatusPedido.PAGO, pedido.getStatus());
	}
}