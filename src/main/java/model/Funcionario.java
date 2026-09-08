package model;

import interfaces.Autenticavel;

public class Funcionario extends Usuario implements Autenticavel{
    private String cargo;

    public Funcionario(String cargo, int id, String nome, String email, String senha, String cpf){
        super(id, nome, email, senha, cpf);
        this.cargo = cargo;
    }

    public String getCargo(){
        return cargo;
    }

    public void setCargo(String cargo){
        this.cargo = cargo;
    }

    @Override 
    public void exibirDados(){
        System.out.println("Funcionario: " + getNome());
        System.out.println("CPF: " + getCpf());
        System.out.println("Email: " + getEmail());
        System.out.println("Cargo: "+ cargo);
    }

    @Override 
    public boolean autenticar(String senha){
        return getSenha().equals(senha);
    }

}
