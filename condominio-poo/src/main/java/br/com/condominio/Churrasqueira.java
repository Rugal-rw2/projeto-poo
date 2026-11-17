package br.com.condominio;
public class Churrasqueira extends AreaComum {
    public Churrasqueira(String nome, int capacidade, boolean cobra, double valor) { super(nome, capacidade, cobra, valor); }
    @Override public String getTipo() { return "Churrasqueira"; }
}
