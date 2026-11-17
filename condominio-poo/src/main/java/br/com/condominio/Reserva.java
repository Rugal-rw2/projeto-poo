package br.com.condominio;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva implements Cobravel {
    private final AreaComum area;
    private final Unidade unidade;
    private final LocalDate data;
    private final LocalTime inicio;
    private final LocalTime fim;
    private boolean confirmada = true;
    private boolean paga;

    public Reserva(AreaComum area, Unidade unidade, LocalDate data, LocalTime inicio, LocalTime fim) {
        if (area == null || unidade == null || data == null || inicio == null || fim == null)
            throw new IllegalArgumentException("Preencha todos os dados da reserva.");
        if (!fim.isAfter(inicio)) throw new IllegalArgumentException("O horário final deve ser depois do inicial.");
        this.area = area; this.unidade = unidade; this.data = data; this.inicio = inicio; this.fim = fim;
    }
    public AreaComum getArea() { return area; }
    public Unidade getUnidade() { return unidade; }
    public LocalDate getData() { return data; }
    public LocalTime getInicio() { return inicio; }
    public LocalTime getFim() { return fim; }
    public boolean isConfirmada() { return confirmada; }
    public void setConfirmada(boolean confirmada) { this.confirmada = confirmada; }
    public boolean isPaga() { return paga; }
    public void setPaga(boolean paga) { this.paga = paga; }
    public boolean conflitaCom(Reserva outra) {
        return confirmada && outra.confirmada && area.equals(outra.area) && data.equals(outra.data)
            && inicio.isBefore(outra.fim) && outra.inicio.isBefore(fim);
    }
    @Override public double calcularValor() { return area.getValorReserva(); }
    @Override public String getDescricaoCobranca() { return "Reserva " + area.getNome() + " — " + data; }
    @Override public String toString() { return data + " " + inicio + "-" + fim + " | " + area.getNome() + " | unidade " + unidade.getCodigo(); }
}
