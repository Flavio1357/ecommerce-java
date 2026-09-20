import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import enums.CategoriaProduto;
import exception.ProdutoNaoEncontradoException;
import model.Produto;
import model.ProdutoInternacional;
import model.ProdutoNacional;
import service.ProdutoService;

public class ProdutoTest {

	@Test
	public void deveCalcularFreteProdutoNacional() {

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
	public void deveCalcularFreteEImpostoProdutoInternacional() {

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

	@Test
	public void deveReduzirEstoqueCorretamente() {

		ProdutoNacional produto = new ProdutoNacional(
			1,
			"Notebook",
			"Notebook nacional",
			3000.00,
			10,
			100,
			CategoriaProduto.ELETRONICO
		);

		produto.reduzirEstoque(3);

		assertEquals(7, produto.getEstoque());
	}

	@Test
	public void deveBuscarProdutoPorId() {
    	Produto produto1 = new ProdutoNacional(
        	1,
        	"Notebook",
        	"Notebook para estudo",
        	3000.00,
        	10,
        	100,
        	CategoriaProduto.ELETRONICO
    	);

    	Produto produto2 = new ProdutoNacional(
        	2,
        	"Cadeira",
        	"Cadeira para escritorio",
        	500.00,
        	5,
        	50,
        	CategoriaProduto.MOVEIS
    	);

    	List<Produto> produtos = new ArrayList<>();
    	produtos.add(produto1);
    	produtos.add(produto2);

    	ProdutoService service = new ProdutoService();

    	Produto resultado = service.buscarPorId(produtos, 2);

    	assertEquals("Cadeira", resultado.getNome());
	}

	@Test
	public void deveLancarExcecaoQuandoProdutoNaoForEncontrado() {

    	Produto produto = new ProdutoNacional(
        	1,
        	"Notebook",
        	"Notebook para estudo",
        	3000.00,
        	10,
        	100,
        	CategoriaProduto.ELETRONICO
    	);

    	List<Produto> produtos = new ArrayList<>();
    	produtos.add(produto);

    	ProdutoService service = new ProdutoService();

    	assertThrows(
        	ProdutoNaoEncontradoException.class,
        	() -> service.buscarPorId(produtos, 99)
    	);
	}

	@Test
	public void deveVerificarEstoquePeloService() {

    	Produto produto = new ProdutoNacional(
        	1,
        	"Notebook",
        	"Notebook para estudo",
        	3000.00,
        	10,
        	100,
        	CategoriaProduto.ELETRONICO
    	);

    	ProdutoService service = new ProdutoService();

    	service.verificarEstoque(produto, 5);

    	assertEquals(10, produto.getEstoque());
	}

	@Test
	public void deveReduzirEstoquePeloService() {

    	Produto produto = new ProdutoNacional(
        	1,
        	"Notebook",
        	"Notebook para estudo",
        	3000.00,
        	10,
        	100,
        	CategoriaProduto.ELETRONICO
    	);

    	ProdutoService service = new ProdutoService();

    	service.reduzirEstoque(produto, 3);

    	assertEquals(7, produto.getEstoque());
	}
}