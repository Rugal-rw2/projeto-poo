package br.com.condominio;

/** Interface comum para objetos que participam de uma cobrança. */
public interface Cobravel {
    double calcularValor();
    String getDescricaoCobranca();
}
