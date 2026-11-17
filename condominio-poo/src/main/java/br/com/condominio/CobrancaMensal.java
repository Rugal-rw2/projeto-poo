package br.com.condominio;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class CobrancaMensal {
    private final Unidade unidade;
    private final YearMonth competencia;
    private final List<Cobravel> itens = new ArrayList<>();
    private boolean paga;

    public CobrancaMensal(Unidade unidade, YearMonth competencia) {
        this.unidade = unidade; this.competencia = competencia;
    }
    public void adicionarItem(Cobravel item) { itens.add(item); }
    public Unidade getUnidade() { return unidade; }
    public YearMonth getCompetencia() { return competencia; }
    public List<Cobravel> getItens() { return List.copyOf(itens); }
    public boolean isPaga() { return paga; }
    public void setPaga(boolean paga) { this.paga = paga; }
    public double calcularTotal() { double total = 0; for (Cobravel item : itens) total += item.calcularValor(); return total; }
    @Override public String toString() { return competencia + " | Unidade " + unidade.getCodigo() + " | R$ " + String.format("%.2f", calcularTotal()) + " | " + (paga ? "PAGA" : "EM ABERTO"); }
}
