package model;

public class ProdutoNacional extends Produto{
    
    public ProdutoNacional(int id, String nome, String descricao, double  preco, int estoque, double  distancia){
        super(id, nome, descricao, preco, estoque, distancia);
    }

    @Override 
    public double calcularFrete(){
        return getDistancia() * 0.50;
    }
}
