import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import pagamento.PagamentoBoleto;
import pagamento.PagamentoCredito;
import pagamento.PagamentoDebito;
import pagamento.PagamentoMIX;

public class PagamentoTest {

	@Test
	void deveProcessarPagamentoDebito() {

		PagamentoDebito pagamento = new PagamentoDebito();

		assertDoesNotThrow(() -> pagamento.processarPagamento(100.00));
	}

    @Test
    void deveProcessarPagamentoCredito() {

	    PagamentoCredito pagamento = new PagamentoCredito();

	    assertDoesNotThrow(() -> pagamento.processarPagamento(200.00));
    }

    @Test
    void deveProcessarPagamentoBoleto() {

	    PagamentoBoleto pagamento = new PagamentoBoleto();

	    assertDoesNotThrow(() -> pagamento.processarPagamento(300.00));
    }

    @Test
    void deveCriarPagamentoMix() {

	    PagamentoMIX pagamento = new PagamentoMIX(
		    new PagamentoDebito(),
		    new PagamentoCredito()
	    );

	    assertEquals(PagamentoDebito.class, pagamento.getPagamento1().getClass());
	    assertEquals(PagamentoCredito.class, pagamento.getPagamento2().getClass());
    }
}