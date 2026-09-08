package model;

import java.util.ArrayList;
import java.util.List;

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
