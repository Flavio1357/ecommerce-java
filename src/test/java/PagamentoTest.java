import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import pagamento.PagamentoBoleto;
import pagamento.PagamentoCredito;
import pagamento.PagamentoDebito;

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
}