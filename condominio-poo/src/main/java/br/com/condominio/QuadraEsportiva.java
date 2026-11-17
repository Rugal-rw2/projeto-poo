package br.com.condominio;
public class QuadraEsportiva extends AreaComum {
    public QuadraEsportiva(String nome, int capacidade, boolean cobra, double valor) { super(nome, capacidade, cobra, valor); }
    @Override public String getTipo() { return "Quadra esportiva"; }
}
