import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import exception.PagamentoInvalidoException;
import pagamento.PagamentoBoleto;
import pagamento.PagamentoCredito;
import pagamento.PagamentoDebito;
import pagamento.PagamentoMIX;

public class PagamentoTest {

	@Test
	public void deveProcessarPagamentoDebito() {

		PagamentoDebito pagamento = new PagamentoDebito();

		assertDoesNotThrow(() -> pagamento.processarPagamento(100.00));
	}

    @Test
    public void deveProcessarPagamentoCredito() {

	    PagamentoCredito pagamento = new PagamentoCredito();

	    assertDoesNotThrow(() -> pagamento.processarPagamento(200.00));
    }

    @Test
    public void deveProcessarPagamentoBoleto() {

	    PagamentoBoleto pagamento = new PagamentoBoleto();

	    assertDoesNotThrow(() -> pagamento.processarPagamento(300.00));
    }

    @Test
	public void deveCriarPagamentoMix() {

		PagamentoMIX pagamento = new PagamentoMIX(
			new PagamentoDebito(),
			new PagamentoCredito(),
			700.00,
			300.00
		);

		assertEquals(PagamentoDebito.class, pagamento.getPagamento1().getClass());
		assertEquals(PagamentoCredito.class, pagamento.getPagamento2().getClass());
	}

	@Test
	public void deveProcessarPagamentoMixComValoresValidos() {

		PagamentoMIX pagamento = new PagamentoMIX(
			new PagamentoDebito(),
			new PagamentoCredito(),
			700.00,
			300.00
		);

		assertDoesNotThrow(() -> pagamento.processarPagamento(1000.00));
	}

	@Test
	public void deveRecusarPagamentoMixComValoresIncorretos() {

		PagamentoMIX pagamento = new PagamentoMIX(
			new PagamentoDebito(),
			new PagamentoCredito(),
			700.00,
			200.00
		);

		assertThrows(
			PagamentoInvalidoException.class,
			() -> pagamento.processarPagamento(1000.00)
		);
	}
}