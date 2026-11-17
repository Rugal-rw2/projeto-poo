package br.com.condominio;

public class Apartamento extends Unidade {
    private double valorPorMetroQuadrado;
    public Apartamento(String codigo, String responsavel, double metragem, double fracaoIdeal, double valorPorMetroQuadrado) {
        super(codigo, responsavel, metragem, fracaoIdeal);
        if (valorPorMetroQuadrado < 0) throw new IllegalArgumentException("Valor por metro inválido.");
        this.valorPorMetroQuadrado = valorPorMetroQuadrado;
    }
    @Override public String getTipo() { return "Apartamento"; }
    @Override public String getRegraTaxa() { return "Por metragem"; }
    @Override public double calcularValor() { return getMetragem() * valorPorMetroQuadrado; }
    public double getValorPorMetroQuadrado() { return valorPorMetroQuadrado; }
}
