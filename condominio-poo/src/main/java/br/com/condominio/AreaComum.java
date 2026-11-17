package br.com.condominio;

public abstract class AreaComum {
    private final String nome;
    private final int capacidade;
    private final boolean cobraReserva;
    private final double valorReserva;
    public AreaComum(String nome, int capacidade, boolean cobraReserva, double valorReserva) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome da área obrigatório.");
        if (capacidade <= 0) throw new IllegalArgumentException("Capacidade deve ser positiva.");
        if (valorReserva < 0) throw new IllegalArgumentException("Valor inválido.");
        this.nome = nome.trim(); this.capacidade = capacidade; this.cobraReserva = cobraReserva;
        this.valorReserva = cobraReserva ? valorReserva : 0;
    }
    public abstract String getTipo();
    public String getNome() { return nome; }
    public int getCapacidade() { return capacidade; }
    public boolean isCobraReserva() { return cobraReserva; }
    public double getValorReserva() { return valorReserva; }
    @Override public String toString() { return nome + " (" + getTipo() + ")"; }
}
