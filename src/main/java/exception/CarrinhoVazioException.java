package exception;

public class CarrinhoVazioException extends RuntimeException{
    public CarrinhoVazioException(String mensagem){
        super(mensagem);
    }
}
