// TelaCrudSeguradoEmpresa.java
package br.edu.cs.poo.ac.seguro.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;

public class TelaCrudSeguradoEmpresa extends JFrame {

    private SeguradoEmpresaMediator mediator = SeguradoEmpresaMediator.getInstancia();
    private JTextField txtCnpj;
    private JTextField txtNome;
    private JTextField txtDataAbertura;
    private JTextField txtFaturamento;
    private JTextField txtBonus;
    private JCheckBox chkEhLocadora; // Checkbox

    // Endereco fields
    private JTextField txtLogradouro;
    private JTextField txtCep;
    private JTextField txtNumeroEndereco;
    private JTextField txtComplemento;
    private JTextField txtCidade;
    private JTextField txtEstado;
    private JTextField txtPais;

    private JButton btnNovo;
    private JButton btnBuscar;
    private JButton btnIncluirAlterar;
    private JButton btnExcluir;
    private JButton btnCancelar;
    private JButton btnLimpar;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaCrudSeguradoEmpresa() {
        setTitle("CRUD de Segurado Empresa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 550); // Adjusted size
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // CNPJ
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("CNPJ:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCnpj = new JTextField(20);
        add(txtCnpj, gbc);
        gbc.weightx = 0.0;

        gbc.gridx = 2;
        btnNovo = new JButton("Novo");
        add(btnNovo, gbc);

        gbc.gridx = 3;
        btnBuscar = new JButton("Buscar");
        add(btnBuscar, gbc);

        // Nome
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtNome = new JTextField(20);
        add(txtNome, gbc);
        gbc.gridwidth = 1;

        // Data de Abertura
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Data Abertura (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtDataAbertura = new JTextField(20);
        add(txtDataAbertura, gbc);
        gbc.gridwidth = 1;

        // Faturamento
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Faturamento:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtFaturamento = new JTextField(20);
        add(txtFaturamento, gbc);
        gbc.gridwidth = 1;

        // Bonus
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Bônus:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtBonus = new JTextField(20);
        add(txtBonus, gbc);
        gbc.gridwidth = 1;

        // Eh Locadora
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("É Locadora de Veículos?"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        chkEhLocadora = new JCheckBox();
        add(chkEhLocadora, gbc);
        gbc.gridwidth = 1;

        // Endereço Fields (Similar to SeguradoPessoa)
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Logradouro:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtLogradouro = new JTextField(20);
        add(txtLogradouro, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel("CEP:"), gbc);
        gbc.gridx = 1;
        txtCep = new JTextField(10);
        add(txtCep, gbc);

        gbc.gridx = 2;
        add(new JLabel("Número:"), gbc);
        gbc.gridx = 3;
        txtNumeroEndereco = new JTextField(5);
        add(txtNumeroEndereco, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        add(new JLabel("Complemento:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtComplemento = new JTextField(20);
        add(txtComplemento, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 9;
        add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1;
        txtCidade = new JTextField(15);
        add(txtCidade, gbc);

        gbc.gridx = 2;
        add(new JLabel("Estado (UF):"), gbc);
        gbc.gridx = 3;
        txtEstado = new JTextField(2);
        add(txtEstado, gbc);

        gbc.gridx = 0; gbc.gridy = 10;
        add(new JLabel("País:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtPais = new JTextField(15);
        add(txtPais, gbc);
        gbc.gridwidth = 1;

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnIncluirAlterar = new JButton("Incluir");
        btnExcluir = new JButton("Excluir");
        btnCancelar = new JButton("Cancelar");
        btnLimpar = new JButton("Limpar");

        buttonPanel.add(btnIncluirAlterar);
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Event Handlers
        btnNovo.addActionListener(e -> {
            String cnpj = txtCnpj.getText().trim();
            if (cnpj.isEmpty()) {
                JOptionPane.showMessageDialog(this, "CNPJ deve ser informado para verificar existência.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            SeguradoEmpresa seg = mediator.buscarSeguradoEmpresa(cnpj);
            if (seg != null) {
                JOptionPane.showMessageDialog(this, "Segurado Empresa já existente!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                habilitarCamposEdicao(true);
                btnIncluirAlterar.setText("Incluir");
                btnNovo.setEnabled(false);
                btnBuscar.setEnabled(false);
                txtCnpj.setEditable(false);
            }
        });

        btnBuscar.addActionListener(e -> {
            String cnpj = txtCnpj.getText().trim();
            if (cnpj.isEmpty()) {
                JOptionPane.showMessageDialog(this, "CNPJ deve ser informado para busca.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            SeguradoEmpresa seg = mediator.buscarSeguradoEmpresa(cnpj);
            if (seg == null) {
                JOptionPane.showMessageDialog(this, "Segurado Empresa não existente!", "Informação", JOptionPane.INFORMATION_MESSAGE);
            } else {
                preencherCamposComDados(seg);
                habilitarCamposEdicao(true);
                btnIncluirAlterar.setText("Alterar");
                btnExcluir.setEnabled(true);
                btnNovo.setEnabled(false);
                btnBuscar.setEnabled(false);
                txtCnpj.setEditable(false);
            }
        });

        btnIncluirAlterar.addActionListener(e -> {
            try {
                String cnpj = txtCnpj.getText().trim();
                String nome = txtNome.getText().trim();
                LocalDate dataAbertura = LocalDate.parse(txtDataAbertura.getText().trim(), DATE_FORMATTER);
                double faturamento = Double.parseDouble(txtFaturamento.getText().trim());
                BigDecimal bonus = new BigDecimal(txtBonus.getText().trim());
                boolean ehLocadora = chkEhLocadora.isSelected();

                Endereco endereco = new Endereco(
                        txtLogradouro.getText().trim(),
                        txtCep.getText().trim(),
                        txtNumeroEndereco.getText().trim(),
                        txtComplemento.getText().trim(),
                        txtPais.getText().trim(),
                        txtEstado.getText().trim(),
                        txtCidade.getText().trim()
                );

                SeguradoEmpresa seg = new SeguradoEmpresa(nome, endereco, dataAbertura, bonus, cnpj, faturamento, ehLocadora);
                String msg;
                String msgOk;

                if (btnIncluirAlterar.getText().equals("Incluir")) {
                    msg = mediator.incluirSeguradoEmpresa(seg);
                    msgOk = "Inclusão realizada com sucesso!";
                } else {
                    msg = mediator.alterarSeguradoEmpresa(seg);
                    msgOk = "Alteração realizada com sucesso!";
                }

                if (msg != null) {
                    JOptionPane.showMessageDialog(this, msg, "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, msgOk, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    resetarTelaParaEstadoInicial();
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Data de Abertura inválida. Use o formato dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Faturamento e/ou Bônus devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro Inesperado", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnExcluir.addActionListener(e -> {
            String cnpj = txtCnpj.getText().trim();
            if (cnpj.isEmpty()) {
                JOptionPane.showMessageDialog(this, "CNPJ não carregado para exclusão.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Confirma a exclusão do segurado empresa?", "Excluir Segurado Empresa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String msg = mediator.excluirSeguradoEmpresa(cnpj);
                if (msg != null) {
                    JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Exclusão realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    resetarTelaParaEstadoInicial();
                }
            }
        });

        btnCancelar.addActionListener(e -> resetarTelaParaEstadoInicial());

        btnLimpar.addActionListener(e -> {
            limparCamposTexto();
            if (txtCnpj.isEditable()) {
                txtCnpj.setText("");
            }
        });

        resetarTelaParaEstadoInicial();
        setVisible(true);
    }

    private void habilitarCamposEdicao(boolean habilitar) {
        txtNome.setEditable(habilitar);
        txtDataAbertura.setEditable(habilitar);
        txtFaturamento.setEditable(habilitar);
        txtBonus.setEditable(habilitar);
        chkEhLocadora.setEnabled(habilitar);
        txtLogradouro.setEditable(habilitar);
        txtCep.setEditable(habilitar);
        txtNumeroEndereco.setEditable(habilitar);
        txtComplemento.setEditable(habilitar);
        txtCidade.setEditable(habilitar);
        txtEstado.setEditable(habilitar);
        txtPais.setEditable(habilitar);

        btnIncluirAlterar.setEnabled(habilitar);
        btnCancelar.setEnabled(habilitar);
    }

    private void limparCamposTexto() {
        txtNome.setText("");
        txtDataAbertura.setText("");
        txtFaturamento.setText("");
        txtBonus.setText("");
        chkEhLocadora.setSelected(false);
        txtLogradouro.setText("");
        txtCep.setText("");
        txtNumeroEndereco.setText("");
        txtComplemento.setText("");
        txtCidade.setText("");
        txtEstado.setText("");
        txtPais.setText("");
    }

    private void preencherCamposComDados(SeguradoEmpresa seg) {
        txtCnpj.setText(seg.getCnpj());
        txtNome.setText(seg.getNome());
        txtDataAbertura.setText(seg.getDataAbertura() != null ? seg.getDataAbertura().format(DATE_FORMATTER) : "");
        txtFaturamento.setText(String.valueOf(seg.getFaturamento()));
        txtBonus.setText(seg.getBonus() != null ? seg.getBonus().toPlainString() : "0.00");
        chkEhLocadora.setSelected(seg.isEhLocadoraDeVeiculos());

        if (seg.getEndereco() != null) {
            Endereco end = seg.getEndereco();
            txtLogradouro.setText(end.getLogradouro() != null ? end.getLogradouro() : "");
            txtCep.setText(end.getCep() != null ? end.getCep() : "");
            txtNumeroEndereco.setText(end.getNumero() != null ? end.getNumero() : "");
            txtComplemento.setText(end.getComplemento() != null ? end.getComplemento() : "");
            txtCidade.setText(end.getCidade() != null ? end.getCidade() : "");
            txtEstado.setText(end.getEstado() != null ? end.getEstado() : "");
            txtPais.setText(end.getPais() != null ? end.getPais() : "");
        } else {
            limparCamposEndereco();
        }
    }

    private void limparCamposEndereco() {
        txtLogradouro.setText("");
        txtCep.setText("");
        txtNumeroEndereco.setText("");
        txtComplemento.setText("");
        txtCidade.setText("");
        txtEstado.setText("");
        txtPais.setText("");
    }

    private void resetarTelaParaEstadoInicial() {
        limparCamposTexto();
        txtCnpj.setText("");
        txtCnpj.setEditable(true);
        habilitarCamposEdicao(false);
        btnNovo.setEnabled(true);
        btnBuscar.setEnabled(true);
        btnExcluir.setEnabled(false);
        btnIncluirAlterar.setText("Incluir");
        txtCnpj.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaCrudSeguradoEmpresa());
    }
}