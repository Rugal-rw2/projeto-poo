package br.com.condominio;

public class Morador {
    private final String nome;
    private final String documento;
    private final String telefone;
    private final Unidade unidade;
    private final boolean responsavel;
    public Morador(String nome, String documento, String telefone, Unidade unidade, boolean responsavel) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome obrigatório.");
        if (unidade == null) throw new IllegalArgumentException("Selecione uma unidade.");
        this.nome = nome.trim(); this.documento = documento == null ? "" : documento.trim();
        this.telefone = telefone == null ? "" : telefone.trim(); this.unidade = unidade; this.responsavel = responsavel;
    }
    public String getNome() { return nome; }
    public String getDocumento() { return documento; }
    public String getTelefone() { return telefone; }
    public Unidade getUnidade() { return unidade; }
    public boolean isResponsavel() { return responsavel; }
    @Override public String toString() { return nome + " — " + unidade.getCodigo() + (responsavel ? " (responsável)" : ""); }
}
