package br.com.condominio;
public class SalaoFestas extends AreaComum {
    public SalaoFestas(String nome, int capacidade, boolean cobra, double valor) { super(nome, capacidade, cobra, valor); }
    @Override public String getTipo() { return "Salão de festas"; }
}
