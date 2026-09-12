import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import enums.CategoriaProduto;
import enums.StatusPedido;
import exception.CarrinhoVazioException;
import model.Carrinho;
import model.Cliente;
import model.ItemCarrinho;
import model.Pedido;
import model.ProdutoNacional;

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
}