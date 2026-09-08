package model;

public class ProdutoInternacional extends Produto {

	private double percentualImportacao;

	public ProdutoInternacional(int id, String nome, String descricao, double preco, int estoque, double distancia,double percentualImportacao) {
		super(id, nome, descricao, preco, estoque, distancia);
		this.percentualImportacao = percentualImportacao;
	}

	public double getPercentualImportacao() {
		return percentualImportacao;
	}

	public void setPercentualImportacao(double percentualImportacao) {
		this.percentualImportacao = percentualImportacao;
	}

	public double calcularImpostoImportacao() {
		return getPreco() * percentualImportacao / 100;
	}

	@Override
	public double calcularFrete() {
		return getDistancia() * 0.80;
	}
}