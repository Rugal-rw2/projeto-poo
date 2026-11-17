package br.com.condominio;

public class SalaComercial extends Unidade {
    private double valorFixoMensal;
    public SalaComercial(String codigo, String responsavel, double metragem, double fracaoIdeal, double valorFixoMensal) {
        super(codigo, responsavel, metragem, fracaoIdeal);
        if (valorFixoMensal < 0) throw new IllegalArgumentException("Valor fixo inválido.");
        this.valorFixoMensal = valorFixoMensal;
    }
    @Override public String getTipo() { return "Sala comercial"; }
    @Override public String getRegraTaxa() { return "Valor fixo comercial"; }
    @Override public double calcularValor() { return valorFixoMensal; }
    public double getValorFixoMensal() { return valorFixoMensal; }
}
