package model;

import enums.StatusPedido;
import exception.CarrinhoVazioException;
import exception.PagamentoNaoDefinidoException;
import exception.PedidoJaPagoException;
import interfaces.FormaPagamento;

public class Pedido {
    private int id;
    private Cliente cliente;
    private Carrinho carrinho;
    private StatusPedido status;
    private FormaPagamento formaPagamento;

    public Pedido(int id, Cliente cliente, Carrinho carrinho){

        if(carrinho.getItens().isEmpty()){
            throw new CarrinhoVazioException("Não é possível criar um pedido com o carrinho vazio");
        }

        this.id = id;
        this.cliente = cliente;
        this.carrinho = carrinho;
        this.status = StatusPedido.AGUARDANDO_PAGAMENTO;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public double calcularTotal(){
        return carrinho.calcularTotal();
    }

    public void pagar(){

        if(status == StatusPedido.PAGO){
            throw  new PedidoJaPagoException("Este pedido já foi pago");
        }

        if(formaPagamento == null){
            throw new PagamentoNaoDefinidoException("Ë necessario definir uma forma de pagamento"); 
        }

        for(ItemCarrinho item : carrinho.getItens()){
            item.getProduto().verificarEstoque(item.getQtd());
        }

        formaPagamento.processarPagamento(calcularTotal());

        for(ItemCarrinho item : carrinho.getItens()){
            item.getProduto().reduzirEstoque(item.getQtd());
        }
        
        status = StatusPedido.PAGO;
    }
}
