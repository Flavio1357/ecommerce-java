package model;

import interfaces.Autenticavel;

public class Cliente extends  Usuario implements Autenticavel{

    private String endereco;

    public Cliente(String endereco, int id, String nome, String email, String senha, String cpf){
        super(id, nome, email, senha, cpf);
        this.endereco = endereco;
    }

    public String getEndereco(){
        return endereco;
    }

    public void setEndereco(String endereco){
        this.endereco = endereco;
    }

    @Override
    public  void exibirDados(){
        System.out.println("Cliente: " + getNome());
        System.out.println("Email: " + getEmail());
        System.out.println("CPF: " + getCpf());
        System.out.println("Endereco: " + endereco);

    }

    @Override 
    public boolean autenticar(String senha){
        return getSenha().equals(senha);
    }
}