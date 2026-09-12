import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import enums.CategoriaProduto;
import exception.QuantidadeInvalidaException;
import model.Carrinho;
import model.ItemCarrinho;
import model.ProdutoNacional;

public class CarrinhoTest {

	@Test
	public void deveCalcularTotalDoCarrinho() {

		ProdutoNacional produto = new ProdutoNacional(
			1,
			"Notebook",
			"Notebook nacional",
			3000.00,
			10,
			100,
			CategoriaProduto.ELETRONICO
		);

		ItemCarrinho item = new ItemCarrinho(produto, 2);

		Carrinho carrinho = new Carrinho();
		carrinho.adicionarItem(item);

		assertEquals(6000.00, carrinho.calcularTotal());
	}

	@Test
	public void deveLancarExcecaoParaQuantidadeInvalida() {

		ProdutoNacional produto = new ProdutoNacional(
			1,
			"Notebook",
			"Notebook nacional",
			3000.00,
			10,
			100,
			CategoriaProduto.ELETRONICO
		);

		ItemCarrinho item = new ItemCarrinho(produto, 0);

		Carrinho carrinho = new Carrinho();

		assertThrows(
			QuantidadeInvalidaException.class,
			() -> carrinho.adicionarItem(item)
		);
	}
}