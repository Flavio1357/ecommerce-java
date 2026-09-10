import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import pagamento.PagamentoDebito;

public class PagamentoTest {

	@Test
	void deveProcessarPagamentoDebito() {

		PagamentoDebito pagamento = new PagamentoDebito();

		assertDoesNotThrow(() -> pagamento.processarPagamento(100.00));
	}
}