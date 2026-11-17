package br.com.condominio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Main {
    private final CondominioService service = new CondominioService();
    private final JFrame frame = new JFrame("Sistema de Controle de Condomínio");
    private final DefaultTableModel unidadesModel = model("Código", "Tipo", "Responsável", "Metragem", "Fração ideal", "Regra", "Taxa mensal");
    private final DefaultTableModel moradoresModel = model("Nome", "Documento", "Telefone", "Unidade", "Responsável?");
    private final DefaultTableModel areasModel = model("Nome", "Tipo", "Capacidade", "Cobra reserva?", "Valor");
    private final DefaultTableModel reservasModel = model("Data", "Horário", "Área", "Unidade", "Valor");
    private final DefaultTableModel cobrancasModel = model("Competência", "Unidade", "Itens/Total", "Status");
    private JTable unidadesTable, moradoresTable, areasTable, reservasTable, cobrancasTable;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().iniciar());
    }
    private void iniciar() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1050, 650); frame.setLocationRelativeTo(null);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Unidades", painelTabela(unidadesModel, this::menuUnidades));
        tabs.addTab("Moradores", painelTabela(moradoresModel, this::cadastrarMorador));
        tabs.addTab("Áreas comuns", painelTabela(areasModel, this::cadastrarArea));
        tabs.addTab("Reservas", painelTabela(reservasModel, this::cadastrarReserva));
        tabs.addTab("Cobranças", painelTabela(cobrancasModel, this::menuCobrancas));
        JPanel relatorios = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 18));
        JButton inad = new JButton("Relatório de inadimplência");
        inad.addActionListener(e -> mostrarRelatorio(service.relatorioInadimplencia()));
        JButton uso = new JButton("Relatório de uso das áreas");
        uso.addActionListener(e -> mostrarRelatorio(service.relatorioUsoAreas()));
        relatorios.add(inad); relatorios.add(uso);
        tabs.addTab("Relatórios", relatorios);
        frame.add(tabs); frame.setVisible(true);
        atualizarTudo();
    }
    private interface Acao { void executar(); }
    private JPanel painelTabela(DefaultTableModel model, Acao acao) {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        JTable tabela = new JTable(model); tabela.setAutoCreateRowSorter(true);
        if (model == unidadesModel) unidadesTable = tabela;
        if (model == moradoresModel) moradoresTable = tabela;
        if (model == areasModel) areasTable = tabela;
        if (model == reservasModel) reservasTable = tabela;
        if (model == cobrancasModel) cobrancasTable = tabela;
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton novo = new JButton(model == unidadesModel ? "Cadastrar / editar / remover" : "Cadastrar");
        novo.addActionListener(e -> acao.executar()); botoes.add(novo);
        if (model == cobrancasModel) {
            JButton gerar = new JButton("Gerar cobrança mensal");
            gerar.addActionListener(e -> gerarCobrancas()); botoes.add(gerar);
            JButton pagar = new JButton("Marcar selecionada como paga");
            pagar.addActionListener(e -> marcarPaga()); botoes.add(pagar);
        }
        p.add(botoes, BorderLayout.SOUTH); return p;
    }
    private static DefaultTableModel model(String... cols) {
        return new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    }
    private String pedir(String msg) { return JOptionPane.showInputDialog(frame, msg); }
    private double pedirDouble(String msg) { return Double.parseDouble(pedir(msg).trim().replace(",", ".")); }
    private int pedirInt(String msg) { return Integer.parseInt(pedir(msg).trim()); }
    private boolean confirmar(String msg) { return JOptionPane.showConfirmDialog(frame, msg, "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION; }
    private void aviso(String msg) { JOptionPane.showMessageDialog(frame, msg); }
    private void tratarErro(Exception ex) { aviso("Não foi possível concluir: " + ex.getMessage()); }

    private void menuUnidades() {
        String[] opcoes = {"Cadastrar unidade", "Editar responsável", "Remover unidade", "Cancelar"};
        int op = JOptionPane.showOptionDialog(frame, "Escolha uma ação", "Unidades", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        try {
            if (op == 0) cadastrarUnidade();
            else if (op == 1) {
                String codigo = pedir("Código da unidade:");
                Unidade u = service.buscarUnidade(codigo);
                if (u == null) throw new IllegalArgumentException("Unidade não encontrada.");
                u.setProprietarioResponsavel(pedir("Novo responsável:")); atualizarTudo();
            } else if (op == 2) {
                String codigo = pedir("Código da unidade para remover:");
                if (confirmar("Remover a unidade " + codigo + "?")) service.removerUnidade(codigo);
                atualizarTudo();
            }
        } catch (Exception ex) { tratarErro(ex); }
    }
    private void cadastrarUnidade() {
        try {
            String[] tipos = {"Apartamento", "Casa", "Sala comercial"};
            String tipo = (String) JOptionPane.showInputDialog(frame, "Tipo:", "Nova unidade", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
            if (tipo == null) return;
            String codigo = pedir("Código (ex.: A101):");
            String resp = pedir("Nome do responsável/proprietário:");
            double metragem = pedirDouble("Metragem em m²:");
            double fracao = pedirDouble("Fração ideal (ex.: 0,75):");
            Unidade u;
            if (tipo.equals("Apartamento")) u = new Apartamento(codigo, resp, metragem, fracao, pedirDouble("Valor por m² (R$):"));
            else if (tipo.equals("Casa")) u = new Casa(codigo, resp, metragem, fracao, pedirDouble("Valor por fração ideal (R$):"));
            else u = new SalaComercial(codigo, resp, metragem, fracao, pedirDouble("Valor fixo mensal (R$):"));
            service.adicionarUnidade(u); atualizarTudo();
        } catch (Exception ex) { tratarErro(ex); }
    }
    private void cadastrarMorador() {
        try {
            if (service.getUnidades().isEmpty()) throw new IllegalArgumentException("Cadastre uma unidade primeiro.");
            String nome = pedir("Nome do morador:"); String doc = pedir("CPF/documento:"); String tel = pedir("Telefone:");
            Unidade u = escolher("Unidade:", service.getUnidades());
            if (u == null) return;
            boolean resp = confirmar("Este morador é responsável pela unidade?");
            service.adicionarMorador(new Morador(nome, doc, tel, u, resp)); atualizarTudo();
        } catch (Exception ex) { tratarErro(ex); }
    }
    private void cadastrarArea() {
        try {
            String[] tipos = {"Salão de festas", "Churrasqueira", "Quadra esportiva"};
            String tipo = (String) JOptionPane.showInputDialog(frame, "Tipo:", "Área comum", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
            if (tipo == null) return;
            String nome = pedir("Nome/identificação da área:");
            int cap = pedirInt("Capacidade máxima de pessoas:");
            boolean cobra = confirmar("A reserva dessa área cobra taxa extra?");
            double valor = cobra ? pedirDouble("Valor da reserva (R$):") : 0;
            AreaComum a = switch (tipo) {
                case "Salão de festas" -> new SalaoFestas(nome, cap, cobra, valor);
                case "Churrasqueira" -> new Churrasqueira(nome, cap, cobra, valor);
                default -> new QuadraEsportiva(nome, cap, cobra, valor);
            };
            service.adicionarArea(a); atualizarTudo();
        } catch (Exception ex) { tratarErro(ex); }
    }
    private void cadastrarReserva() {
        try {
            if (service.getUnidades().isEmpty() || service.getAreas().isEmpty()) throw new IllegalArgumentException("Cadastre ao menos uma unidade e uma área.");
            AreaComum area = escolher("Área comum:", service.getAreas());
            Unidade unidade = escolher("Unidade solicitante:", service.getUnidades());
            if (area == null || unidade == null) return;
            LocalDate data = LocalDate.parse(pedir("Data (AAAA-MM-DD):"));
            LocalTime ini = LocalTime.parse(pedir("Horário inicial (HH:MM):"));
            LocalTime fim = LocalTime.parse(pedir("Horário final (HH:MM):"));
            service.reservar(new Reserva(area, unidade, data, ini, fim)); atualizarTudo();
        } catch (DateTimeParseException ex) { tratarErro(new IllegalArgumentException("Use data AAAA-MM-DD e horário HH:MM.")); }
        catch (Exception ex) { tratarErro(ex); }
    }
    private void menuCobrancas() { gerarCobrancas(); }
    private void gerarCobrancas() {
        try {
            YearMonth mes = YearMonth.parse(pedir("Competência (AAAA-MM), por exemplo 2026-10:"));
            List<CobrancaMensal> lista = service.gerarCobrancas(mes);
            atualizarTudo(); aviso(lista.size() + " cobrança(s) gerada(s) para " + mes + ".");
        } catch (Exception ex) { tratarErro(ex); }
    }
    private void marcarPaga() {
        int row = cobrancasTable.getSelectedRow();
        if (row < 0) { aviso("Selecione uma cobrança."); return; }
        int modelRow = cobrancasTable.convertRowIndexToModel(row);
        String competencia = (String) cobrancasModel.getValueAt(modelRow, 0);
        String codigo = (String) cobrancasModel.getValueAt(modelRow, 1);
        service.getCobrancas().stream().filter(c -> c.getCompetencia().toString().equals(competencia) && c.getUnidade().getCodigo().equals(codigo)).findFirst().ifPresent(c -> c.setPaga(true));
        atualizarTudo();
    }
    private <T> T escolher(String mensagem, List<T> itens) {
        return (T) JOptionPane.showInputDialog(frame, mensagem, "Seleção", JOptionPane.QUESTION_MESSAGE, null, itens.toArray(), itens.get(0));
    }
    private void mostrarRelatorio(String texto) {
        JTextArea area = new JTextArea(texto, 20, 60); area.setEditable(false);
        JOptionPane.showMessageDialog(frame, new JScrollPane(area), "Relatório", JOptionPane.INFORMATION_MESSAGE);
    }
    private void atualizarTudo() {
        unidadesModel.setRowCount(0);
        for (Unidade u : service.getUnidades()) unidadesModel.addRow(new Object[]{u.getCodigo(), u.getTipo(), u.getProprietarioResponsavel(), u.getMetragem(), u.getFracaoIdeal(), u.getRegraTaxa(), String.format("R$ %.2f", u.calcularValor())});
        moradoresModel.setRowCount(0);
        for (Morador m : service.getMoradores()) moradoresModel.addRow(new Object[]{m.getNome(), m.getDocumento(), m.getTelefone(), m.getUnidade().getCodigo(), m.isResponsavel() ? "Sim" : "Não"});
        areasModel.setRowCount(0);
        for (AreaComum a : service.getAreas()) areasModel.addRow(new Object[]{a.getNome(), a.getTipo(), a.getCapacidade(), a.isCobraReserva() ? "Sim" : "Não", String.format("R$ %.2f", a.getValorReserva())});
        reservasModel.setRowCount(0);
        for (Reserva r : service.getReservas()) reservasModel.addRow(new Object[]{r.getData(), r.getInicio() + "–" + r.getFim(), r.getArea().getNome(), r.getUnidade().getCodigo(), String.format("R$ %.2f", r.calcularValor())});
        cobrancasModel.setRowCount(0);
        for (CobrancaMensal c : service.getCobrancas()) cobrancasModel.addRow(new Object[]{c.getCompetencia(), c.getUnidade().getCodigo(), String.format("R$ %.2f", c.calcularTotal()), c.isPaga() ? "PAGA" : "EM ABERTO"});
    }
}
