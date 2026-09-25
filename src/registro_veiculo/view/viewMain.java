/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro_veiculo.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import registro_veiculo.dao.InfracaoDAO;
import registro_veiculo.dao.InfracaoDAO.InfracaoCompleta;
import registro_veiculo.dao.IpvaDAO;
import registro_veiculo.dao.MotoristaDAO;
import registro_veiculo.model.CNH;
import registro_veiculo.model.EmailMotorista;
import registro_veiculo.model.Endereco;
import registro_veiculo.model.IPVA;
import registro_veiculo.model.Motorista;
import registro_veiculo.model.Veiculo;

/**
 *
 * @author molsousa
 */
public class viewMain extends JFrame {

    private final MotoristaDAO motoristaDAO = new MotoristaDAO();
    private final InfracaoDAO infracaoDAO = new InfracaoDAO();
    private final IpvaDAO ipvaDAO = new IpvaDAO();

    private final JComboBox<Motorista> comboMotoristas = new JComboBox<>();

    // Conteúdo das abas, sempre referente ao motorista selecionado
    private final JTextArea areaDados = new JTextArea();
    private final DefaultTableModel modeloVeiculos = criarModelo("Placa", "Marca", "Modelo", "Cor", "Categoria", "Situação IPVA");
    private final DefaultTableModel modeloInfracoes = criarModelo("Data", "Hora", "Tipo", "Placa", "Local");
    private final DefaultTableModel modeloIpva = criarModelo("Placa", "Veículo", "Ano fiscal", "Preço FIPE", "Alíquota", "Vencimento", "Pagamento", "Situação");

    // Filtro por veículo nas abas de Infrações e IPVA (null = todos os veículos)
    private final JComboBox<Veiculo> comboVeiculoInfracoes = criarComboVeiculos();
    private final JComboBox<Veiculo> comboVeiculoIpva = criarComboVeiculos();

    private List<Veiculo> veiculosDoMotorista = new ArrayList<>();
    private List<InfracaoCompleta> infracoesDoMotorista = new ArrayList<>();
    private List<Veiculo> ipvaDoMotorista = new ArrayList<>();

    public viewMain() {
        super("Registro de Veículos - Consulta por Motorista");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        add(criarBarraSelecao(), BorderLayout.NORTH);
        add(criarAbas(), BorderLayout.CENTER);

        carregarMotoristas();
    }

    // ---------------- Seleção do motorista ----------------

    private JPanel criarBarraSelecao() {
        comboMotoristas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Motorista) {
                    Motorista m = (Motorista) value;
                    String doc = m.getCpf() != null ? m.getCpf() : m.getCnpj();
                    setText(m.getNome() + " (" + doc + ")");
                } else {
                    setText("-- selecione um motorista --");
                }
                return this;
            }
        });
        comboMotoristas.setPreferredSize(new Dimension(400, comboMotoristas.getPreferredSize().height));
        comboMotoristas.addActionListener(e -> exibirMotorista((Motorista) comboMotoristas.getSelectedItem()));

        JButton btnRecarregar = new JButton("Recarregar");
        btnRecarregar.addActionListener(e -> carregarMotoristas());

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        barra.add(new JLabel("Motorista:"));
        barra.add(comboMotoristas);
        barra.add(btnRecarregar);
        return barra;
    }

    private void carregarMotoristas() {
        try {
            List<Motorista> lista = motoristaDAO.buscarTodos();
            comboMotoristas.removeAllItems();
            comboMotoristas.addItem(null);
            for (Motorista m : lista) {
                comboMotoristas.addItem(m);
            }
            comboMotoristas.setSelectedIndex(0);
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private void exibirMotorista(Motorista m) {
        limparAbas();
        if (m == null) {
            return;
        }

        areaDados.setText(textoDadosMotorista(m));
        areaDados.setCaretPosition(0);

        veiculosDoMotorista = m.getVeiculos() != null ? m.getVeiculos() : new ArrayList<>();
        for (Veiculo v : veiculosDoMotorista) {
            modeloVeiculos.addRow(new Object[]{
                v.getPlaca(), v.getMarca().getDescricaoMarca(), v.getModelo().getDescricaoModelo(),
                v.getCor(), v.getCategoriaVeiculo(), situacaoIpvaMaisRecente(v)
            });
        }

        try {
            infracoesDoMotorista = infracaoDAO.buscarPorMotorista(m.getIdMotorista());
            ipvaDoMotorista = ipvaDAO.buscarPorProprietario(m.getIdMotorista());
        } catch (Exception ex) {
            mostrarErro(ex);
        }

        preencherComboVeiculos(comboVeiculoInfracoes, veiculosDoMotorista);
        preencherComboVeiculos(comboVeiculoIpva, veiculosDoMotorista);
        preencherInfracoes();
        preencherIpva();
    }

    private void preencherInfracoes() {
        modeloInfracoes.setRowCount(0);
        Veiculo filtro = (Veiculo) comboVeiculoInfracoes.getSelectedItem();
        for (InfracaoCompleta ic : infracoesDoMotorista) {
            if (!mesmoVeiculo(filtro, ic.getVeiculo())) {
                continue;
            }
            Endereco local = ic.getInfracao().getLocalInfracao();
            modeloInfracoes.addRow(new Object[]{
                ic.getInfracao().getDataInfracao(), ic.getInfracao().getHoraInfracao(),
                ic.getInfracao().getTipoInfracao(), ic.getVeiculo().getPlaca(),
                local.getBairro() + ", " + local.getCidade() + "/" + local.getSiglaUF()
            });
        }
    }

    private void preencherIpva() {
        modeloIpva.setRowCount(0);
        Veiculo filtro = (Veiculo) comboVeiculoIpva.getSelectedItem();
        for (Veiculo v : ipvaDoMotorista) {
            if (!mesmoVeiculo(filtro, v) || v.getIpvas() == null) {
                continue;
            }
            String nomeVeiculo = v.getMarca().getDescricaoMarca() + " " + v.getModelo().getDescricaoModelo();
            for (IPVA ipva : v.getIpvas()) {
                modeloIpva.addRow(new Object[]{
                    v.getPlaca(), nomeVeiculo, ipva.getAnoFiscal(),
                    ipva.getPrecoTabelaFIPE(), ipva.getAliquota(),
                    ipva.getDataVencimento(), ipva.getDataPagamento(),
                    ipva.getStatusPagamento()
                });
            }
        }
    }

    // Filtro null = "Todos os veículos". Compara pela placa porque cada DAO cria seus próprios objetos
    private boolean mesmoVeiculo(Veiculo filtro, Veiculo v) {
        return filtro == null || filtro.getPlaca().equals(v.getPlaca());
    }

    private void limparAbas() {
        areaDados.setText("Selecione um motorista acima para ver as informações.");
        veiculosDoMotorista = new ArrayList<>();
        infracoesDoMotorista = new ArrayList<>();
        ipvaDoMotorista = new ArrayList<>();
        preencherComboVeiculos(comboVeiculoInfracoes, veiculosDoMotorista);
        preencherComboVeiculos(comboVeiculoIpva, veiculosDoMotorista);
        modeloVeiculos.setRowCount(0);
        modeloInfracoes.setRowCount(0);
        modeloIpva.setRowCount(0);
    }

    // ---------------- Abas ----------------

    private JTabbedPane criarAbas() {
        areaDados.setEditable(false);
        areaDados.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaDados.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTable tabelaVeiculos = new JTable(modeloVeiculos);
        tabelaVeiculos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int linha = tabelaVeiculos.getSelectedRow();
                if (evt.getClickCount() == 2 && linha >= 0 && linha < veiculosDoMotorista.size()) {
                    mostrarResumoVeiculo(veiculosDoMotorista.get(linha));
                }
            }
        });

        JPanel painelVeiculos = new JPanel(new BorderLayout());
        painelVeiculos.add(new JLabel("  Duplo clique numa linha = resumo do veículo"), BorderLayout.NORTH);
        painelVeiculos.add(new JScrollPane(tabelaVeiculos), BorderLayout.CENTER);

        comboVeiculoInfracoes.addActionListener(e -> preencherInfracoes());
        comboVeiculoIpva.addActionListener(e -> preencherIpva());

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Dados do motorista", new JScrollPane(areaDados));
        abas.addTab("Veículos", painelVeiculos);
        abas.addTab("Infrações", painelComFiltro(comboVeiculoInfracoes, modeloInfracoes));
        abas.addTab("IPVA", painelComFiltro(comboVeiculoIpva, modeloIpva));

        limparAbas();
        return abas;
    }

    private JPanel painelComFiltro(JComboBox<Veiculo> comboVeiculo, DefaultTableModel modelo) {
        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.add(new JLabel("Veículo:"));
        filtro.add(comboVeiculo);

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(filtro, BorderLayout.NORTH);
        painel.add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);
        return painel;
    }

    private static JComboBox<Veiculo> criarComboVeiculos() {
        JComboBox<Veiculo> combo = new JComboBox<>();
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Veiculo) {
                    Veiculo v = (Veiculo) value;
                    setText(v.getPlaca() + " - " + v.getMarca().getDescricaoMarca() + " " + v.getModelo().getDescricaoModelo());
                } else {
                    setText("Todos os veículos");
                }
                return this;
            }
        });
        combo.setPreferredSize(new Dimension(300, combo.getPreferredSize().height));
        return combo;
    }

    private void preencherComboVeiculos(JComboBox<Veiculo> combo, List<Veiculo> veiculos) {
        combo.removeAllItems();
        combo.addItem(null);
        for (Veiculo v : veiculos) {
            combo.addItem(v);
        }
        combo.setSelectedIndex(0);
    }

    private String textoDadosMotorista(Motorista m) {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(m.getNome()).append("\n");
        sb.append("CPF: ").append(m.getCpf() != null ? m.getCpf() : "-").append("\n");
        sb.append("CNPJ: ").append(m.getCnpj() != null ? m.getCnpj() : "-").append("\n");
        sb.append("Nascimento/Fundação: ").append(m.getDataNascimento_Fundacao()).append("\n");
        sb.append("Sexo: ").append(m.getSexo()).append("\n");

        Endereco end = m.getEndereco();
        sb.append("Endereço: ").append(end.getTipoLogradouro()).append(" ").append(end.getLogradouro())
          .append(", ").append(m.getNroEndereco());
        if (m.getCompEndereco() != null && !m.getCompEndereco().isEmpty()) {
            sb.append(" (").append(m.getCompEndereco()).append(")");
        }
        sb.append(" - ").append(end.getBairro()).append(", ").append(end.getCidade())
          .append("/").append(end.getSiglaUF()).append(" - CEP ").append(end.getCep()).append("\n");

        if (m.getCnh() != null) {
            CNH cnh = m.getCnh();
            sb.append("CNH: ").append(cnh.getNroRegistro())
              .append(" (emissão ").append(cnh.getDataEmissao())
              .append(", validade ").append(cnh.getDataValidade()).append(")\n");
        } else {
            sb.append("CNH: não cadastrada\n");
        }

        sb.append("Telefones: ");
        if (m.getFones() != null && !m.getFones().isEmpty()) {
            sb.append(m.getFones().stream()
                .map(f -> "(+" + f.getDDI() + " " + f.getDDD() + ") " + f.getNroFone())
                .collect(Collectors.joining(", ")));
        } else {
            sb.append("nenhum");
        }
        sb.append("\n");

        sb.append("E-mails: ");
        if (m.getEmails() != null && !m.getEmails().isEmpty()) {
            sb.append(m.getEmails().stream()
                .map(EmailMotorista::getEnderecoEmail)
                .collect(Collectors.joining(", ")));
        } else {
            sb.append("nenhum");
        }

        return sb.toString();
    }

    private String situacaoIpvaMaisRecente(Veiculo v) {
        if (v.getIpvas() == null || v.getIpvas().isEmpty()) {
            return "-";
        }
        // A consulta já traz os IPVAs ordenados do ano mais recente para o mais antigo
        IPVA ipva = v.getIpvas().get(0);
        return ipva.getStatusPagamento() + " (" + ipva.getAnoFiscal() + ")";
    }

    private void mostrarResumoVeiculo(Veiculo v) {
        StringBuilder sb = new StringBuilder();
        sb.append("Placa: ").append(v.getPlaca()).append("\n");
        sb.append("Chassi: ").append(v.getChassi()).append("\n");
        sb.append("RENAVAM: ").append(v.getRenavam()).append("\n");
        sb.append("Ano de fabricação: ").append(v.getAnoFabricacao()).append("\n");
        sb.append("Cor: ").append(v.getCor()).append("\n");
        sb.append("Categoria: ").append(v.getCategoriaVeiculo()).append("\n");
        sb.append("Marca: ").append(v.getMarca().getDescricaoMarca()).append("\n");
        sb.append("Modelo: ").append(v.getModelo().getDescricaoModelo())
          .append(" (lançamento ").append(v.getModelo().getDataLancamento()).append(")\n");

        if (v.getIpvas() != null && !v.getIpvas().isEmpty()) {
            sb.append("Situação do IPVA:\n");
            for (IPVA ipva : v.getIpvas()) {
                sb.append("  - ").append(ipva.getAnoFiscal()).append(": ")
                  .append(ipva.getStatusPagamento()).append("\n");
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450, 300));
        JOptionPane.showMessageDialog(this, scroll, "Resumo do veículo " + v.getPlaca(), JOptionPane.PLAIN_MESSAGE);
    }

    // ---------------- Utilitários ----------------

    private static DefaultTableModel criarModelo(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
    }

    private void mostrarErro(Exception ex) {
        JOptionPane.showMessageDialog(this,
            "Erro ao acessar o banco de dados:\n" + ex.getMessage(),
            "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new viewMain().setVisible(true));
    }
}
