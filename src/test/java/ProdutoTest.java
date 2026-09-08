import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

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
			100
		);

		assertEquals(50.00, produto.calcularFrete());
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
		    20
	    );

	    assertEquals(80.00, produto.calcularFrete());
	    assertEquals(600.00, produto.calcularImpostoImportacao());
    }
}