package model;

public class ItemCarrinho {
    private Produto produto;
    private int qtd;

    public ItemCarrinho(Produto produto, int qtd){
        this.produto = produto;
        this.qtd = qtd;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
    
    public double calcularSubtotal(){
        return produto.getPreco() * qtd; 
    }
    
}
