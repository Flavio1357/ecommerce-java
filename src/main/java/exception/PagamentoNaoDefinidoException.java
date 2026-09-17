package exception;

public class PagamentoNaoDefinidoException extends RuntimeException{
    public PagamentoNaoDefinidoException(String mensagem){
        super(mensagem);
    }
}
