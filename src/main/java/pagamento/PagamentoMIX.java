package pagamento;

import exception.PagamentoInvalidoException;
import interfaces.FormaPagamento;

public class PagamentoMIX implements FormaPagamento{
    
    private FormaPagamento pagamento1;
    private FormaPagamento pagamento2;
    private double valorPagamento1;
    private double valorPagamento2;

    public PagamentoMIX(FormaPagamento pagamento1, FormaPagamento pagamento2, double valorPagamento1, double valorPagamento2){
        this.pagamento1 = pagamento1;
        this.pagamento2 = pagamento2;
        this.valorPagamento1 = valorPagamento1;
        this.valorPagamento2 = valorPagamento2;
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

    public double getValorPagamento1() {
        return valorPagamento1;
    }

    public void setValorPagamento1(double valorPagamento1) {
        this.valorPagamento1 = valorPagamento1;
    }

    public double getValorPagamento2() {
        return valorPagamento2;
    }

    public void setValorPagamento2(double valorPagamento2) {
        this.valorPagamento2 = valorPagamento2;
    }


    @Override 
    public void processarPagamento(double valor){
        
        if(valorPagamento1 <= 0 || valorPagamento2 <= 0){
            throw new PagamentoInvalidoException("Os valores dos pagamentos devem ser maiores que zero");
        }

        if(valorPagamento1 + valorPagamento2 != valor){
            throw new PagamentoInvalidoException("Os valores dos pagamentos não correspondem ao total");
        }

        pagamento1.processarPagamento(valorPagamento1);
        pagamento2.processarPagamento(valorPagamento2);
    }

}
