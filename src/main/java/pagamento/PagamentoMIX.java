package pagamento;

import interfaces.FormaPagamento;

public class PagamentoMIX implements FormaPagamento{
    
    private FormaPagamento pagamento1;
    private FormaPagamento pagamento2;

    public PagamentoMIX(FormaPagamento pagamento1, FormaPagamento pagamento2){
        this.pagamento1 = pagamento1;
        this.pagamento2 = pagamento2;
    }

    public FormaPagamento getPagamento1() {
        return pagamento1;
    }

    public void setPagamento1(FormaPagamento pagamento1) {
        this.pagamento1 = pagamento1;
    }

    public FormaPagamento getPagamento2() {
        return pagamento2;
    }

    public void setPagamento2(FormaPagamento pagamento2) {
        this.pagamento2 = pagamento2;
    }

    @Override 
    public void processarPagamento(double valor){
        System.out.println("Pagamento MIX: R$ " + valor);
    }

}
