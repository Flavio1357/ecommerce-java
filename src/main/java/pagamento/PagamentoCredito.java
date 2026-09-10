package pagamento;

import interfaces.FormaPagamento;

public class PagamentoCredito implements FormaPagamento{
    
    @Override 
    public void processarPagamento(double valor){
        System.out.println("Pagamento no credito: R$ " + valor);

    }
}
