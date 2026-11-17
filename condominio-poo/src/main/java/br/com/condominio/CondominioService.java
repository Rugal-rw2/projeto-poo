package br.com.condominio;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class CondominioService {
    private final List<Unidade> unidades = new ArrayList<>();
    private final List<Morador> moradores = new ArrayList<>();
    private final List<AreaComum> areas = new ArrayList<>();
    private final List<Reserva> reservas = new ArrayList<>();
    private final List<CobrancaMensal> cobrancas = new ArrayList<>();

    public List<Unidade> getUnidades() { return List.copyOf(unidades); }
    public List<Morador> getMoradores() { return List.copyOf(moradores); }
    public List<AreaComum> getAreas() { return List.copyOf(areas); }
    public List<Reserva> getReservas() { return List.copyOf(reservas); }
    public List<CobrancaMensal> getCobrancas() { return List.copyOf(cobrancas); }

    public void adicionarUnidade(Unidade u) {
        if (buscarUnidade(u.getCodigo()) != null) throw new IllegalArgumentException("Já existe unidade com esse código.");
        unidades.add(u);
    }
    public Unidade buscarUnidade(String codigo) {
        return unidades.stream().filter(u -> u.getCodigo().equalsIgnoreCase(codigo.trim())).findFirst().orElse(null);
    }
    public void removerUnidade(String codigo) {
        Unidade u = buscarUnidade(codigo);
        if (u == null) throw new IllegalArgumentException("Unidade não encontrada.");
        if (moradores.stream().anyMatch(m -> m.getUnidade().equals(u)) || reservas.stream().anyMatch(r -> r.getUnidade().equals(u)))
            throw new IllegalArgumentException("Não é possível remover: há moradores ou reservas vinculados.");
        unidades.remove(u);
    }
    public void adicionarMorador(Morador m) { moradores.add(m); }
    public void adicionarArea(AreaComum a) {
        if (areas.stream().anyMatch(x -> x.getNome().equalsIgnoreCase(a.getNome()))) throw new IllegalArgumentException("Já existe uma área com esse nome.");
        areas.add(a);
    }
    public void reservar(Reserva nova) {
        if (!unidades.contains(nova.getUnidade())) throw new IllegalArgumentException("Unidade não cadastrada.");
        if (!areas.contains(nova.getArea())) throw new IllegalArgumentException("Área não cadastrada.");
        if (reservas.stream().anyMatch(nova::conflitaCom)) throw new IllegalArgumentException("Conflito: essa área já está reservada nesse período.");
        reservas.add(nova);
    }
    public List<CobrancaMensal> gerarCobrancas(YearMonth competencia) {
        List<CobrancaMensal> geradas = new ArrayList<>();
        for (Unidade unidade : unidades) {
            CobrancaMensal c = new CobrancaMensal(unidade, competencia);
            c.adicionarItem(unidade); // Polimorfismo: cada tipo calcula sua própria taxa.
            for (Reserva r : reservas) {
                if (r.getUnidade().equals(unidade) && YearMonth.from(r.getData()).equals(competencia) && r.isConfirmada())
                    c.adicionarItem(r); // Unidades e reservas compartilham a interface Cobravel.
            }
            CobrancaMensal existente = cobrancas.stream().filter(x -> x.getUnidade().equals(unidade) && x.getCompetencia().equals(competencia)).findFirst().orElse(null);
            if (existente != null) cobrancas.remove(existente);
            cobrancas.add(c); geradas.add(c);
        }
        return geradas;
    }
    public String relatorioInadimplencia() {
        StringBuilder sb = new StringBuilder("RELATÓRIO DE INADIMPLÊNCIA\n\n");
        boolean encontrou = false;
        for (CobrancaMensal c : cobrancas) if (!c.isPaga()) { sb.append(c).append("\n"); encontrou = true; }
        return encontrou ? sb.toString() : sb.append("Nenhuma cobrança em aberto.").toString();
    }
    public String relatorioUsoAreas() {
        StringBuilder sb = new StringBuilder("RELATÓRIO DE USO DAS ÁREAS COMUNS\n\n");
        for (AreaComum a : areas) {
            long quantidade = reservas.stream().filter(r -> r.getArea().equals(a) && r.isConfirmada()).count();
            sb.append(a.getNome()).append(" (").append(a.getTipo()).append("): ").append(quantidade).append(" reserva(s)\n");
        }
        if (areas.isEmpty()) sb.append("Nenhuma área cadastrada.");
        return sb.toString();
    }
}
