package service;

import java.util.List;

import exception.ProdutoNaoEncontradoException;
import model.Produto;

public class ProdutoService {

    public Produto buscarPorId(List<Produto> produtos, int id){
        for(Produto produto : produtos){
            if(produto.getId() == id){
                return produto;
            }
        }
        
        throw new ProdutoNaoEncontradoException("Produto com ID " + id + " não encontrado");
    }
    
    public void verificarEstoque(Produto produto, int qtd){
        produto.verificarEstoque(qtd);
    }

    public void reduzirEstoque(Produto produto, int qtd){
        produto.reduzirEstoque(qtd);
    }
}
