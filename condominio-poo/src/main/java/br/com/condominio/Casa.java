package br.com.condominio;

public class Casa extends Unidade {
    private double valorPorFracao;
    public Casa(String codigo, String responsavel, double metragem, double fracaoIdeal, double valorPorFracao) {
        super(codigo, responsavel, metragem, fracaoIdeal);
        if (valorPorFracao < 0) throw new IllegalArgumentException("Valor por fração inválido.");
        this.valorPorFracao = valorPorFracao;
    }
    @Override public String getTipo() { return "Casa"; }
    @Override public String getRegraTaxa() { return "Por fração ideal"; }
    @Override public double calcularValor() { return getFracaoIdeal() * valorPorFracao; }
    public double getValorPorFracao() { return valorPorFracao; }
}
