package model;

import enums.CategoriaProduto;

public class ProdutoNacional extends Produto{
    
    public ProdutoNacional(int id, String nome, String descricao, double  preco, int estoque, double  distancia, CategoriaProduto categoria){
        super(id, nome, descricao, preco, estoque, distancia, categoria);
    }

    @Override 
    public double calcularFrete(){
        return getDistancia() * 0.50;
    }
}
