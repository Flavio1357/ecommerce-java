import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import enums.CategoriaProduto;
import model.ProdutoInternacional;
import model.ProdutoNacional;

public class ProdutoTest {

	@Test
	void deveCalcularFreteProdutoNacional() {

		ProdutoNacional produto = new ProdutoNacional(
			1,
			"Notebook",
			"Notebook nacional",
			3000.00,
			10,
			100,
			CategoriaProduto.ELETRONICO
		);

		assertEquals(50.00, produto.calcularFrete());
		assertEquals(CategoriaProduto.ELETRONICO, produto.getCategoria());
	}

	@Test
	void deveCalcularFreteEImpostoProdutoInternacional() {

		ProdutoInternacional produto = new ProdutoInternacional(
	        2,
	        "Notebook",
	        "Notebook importado",
	        3000.00,
	        10,
	        100,
	        20,
	        CategoriaProduto.ELETRONICO
        );

		assertEquals(80.00, produto.calcularFrete());
		assertEquals(600.00, produto.calcularImpostoImportacao());
		assertEquals(CategoriaProduto.ELETRONICO, produto.getCategoria());
	}
}