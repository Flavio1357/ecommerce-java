package service;

import java.util.List;

import exception.FuncionarioNaoEncontradoException;
import model.Funcionario;

public class FuncionarioService {
    
    public Funcionario buscarPorId(List<Funcionario> funcionarios, int id){

        for(Funcionario funcionario : funcionarios){
            if(funcionario.getId() == id){
                return funcionario;
            }
        }

        throw new FuncionarioNaoEncontradoException("Funcionario com ID " + id + " não encontrado");
    }

    public boolean autenticar(Funcionario funcionario, String senha){
        return funcionario.autenticar(senha);
    }
}
