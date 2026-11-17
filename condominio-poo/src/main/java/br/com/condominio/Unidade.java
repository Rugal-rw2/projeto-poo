package br.com.condominio;

public abstract class Unidade implements Cobravel {
    private String codigo;
    private String proprietarioResponsavel;
    private double metragem;
    private double fracaoIdeal;
    private boolean ativa = true;

    protected Unidade(String codigo, String proprietarioResponsavel, double metragem, double fracaoIdeal) {
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("Código da unidade obrigatório.");
        if (metragem <= 0) throw new IllegalArgumentException("A metragem deve ser maior que zero.");
        if (fracaoIdeal < 0) throw new IllegalArgumentException("A fração ideal não pode ser negativa.");
        this.codigo = codigo.trim();
        this.proprietarioResponsavel = proprietarioResponsavel == null ? "" : proprietarioResponsavel.trim();
        this.metragem = metragem;
        this.fracaoIdeal = fracaoIdeal;
    }
    public abstract String getTipo();
    public abstract String getRegraTaxa();
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("Código obrigatório."); this.codigo = codigo.trim(); }
    public String getProprietarioResponsavel() { return proprietarioResponsavel; }
    public void setProprietarioResponsavel(String nome) { proprietarioResponsavel = nome == null ? "" : nome.trim(); }
    public double getMetragem() { return metragem; }
    public void setMetragem(double metragem) { if (metragem <= 0) throw new IllegalArgumentException("Metragem inválida."); this.metragem = metragem; }
    public double getFracaoIdeal() { return fracaoIdeal; }
    public void setFracaoIdeal(double fracaoIdeal) { if (fracaoIdeal < 0) throw new IllegalArgumentException("Fração inválida."); this.fracaoIdeal = fracaoIdeal; }
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
    @Override public String getDescricaoCobranca() { return getTipo() + " " + codigo; }
    @Override public String toString() { return codigo + " (" + getTipo() + ")"; }
}
