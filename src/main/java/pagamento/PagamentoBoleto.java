package pagamento;

import interfaces.FormaPagamento;

public class PagamentoBoleto implements FormaPagamento{
    
    @Override 
    public void processarPagamento(double valor){
        System.out.println("Pagamento no boleto: R$ " + valor);
    }
}
