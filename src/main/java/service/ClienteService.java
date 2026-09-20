package service;

import java.util.List;

import exception.ClienteNaoEncontradoException;
import model.Cliente;

public class ClienteService {

    public Cliente buscarPorId(List<Cliente> clientes, int id){
        for(Cliente cliente : clientes){
            if(cliente.getId() == id){
                return cliente;
            }
        }

        throw new ClienteNaoEncontradoException("Cliente de ID " + id + " não encontrado");
    }

    public boolean autenticar(Cliente cliente, String senha){
        return cliente.autenticar(senha);
    }
}
