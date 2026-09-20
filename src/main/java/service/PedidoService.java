package service;

import model.Pedido;

public class PedidoService {
    
    public void realizarPagamento(Pedido pedido){
        pedido.pagar();
    }
}
