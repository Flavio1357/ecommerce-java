package pagamento;

import  interfaces.FormaPagamento;

public class PagamentoDebito implements FormaPagamento{
    
    @Override 
    public void processarPagamento(double valor){
        System.out.println("Pagamento no débito: R$ " + valor);
    }
}
