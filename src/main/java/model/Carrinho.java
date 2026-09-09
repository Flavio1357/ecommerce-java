package model;

import java.util.ArrayList;
import java.util.List;

import exception.QuantidadeInvalidaException;
import interfaces.Calculavel;

public class Carrinho implements Calculavel{
    private  List<ItemCarrinho> itens;

    public Carrinho(){
        itens = new ArrayList<>();
    }

    public List<ItemCarrinho> getItens(){
        return  itens;
    }

    public void setItens(List<ItemCarrinho> itens) {
        this.itens = itens;
    }

    public void adicionarItem(ItemCarrinho item){
        if(item.getQtd() <= 0){
            throw new QuantidadeInvalidaException("A quantidade deve ser maior que zero.");
        }
        itens.add(item);
    }

    public void removerItem(ItemCarrinho item){
        itens.remove(item);
    }

    @Override 
    public double calcularTotal(){
        double total = 0;

        for (ItemCarrinho item : itens){
            total += item.calcularSubtotal();
        }

        return total;
    }

}
