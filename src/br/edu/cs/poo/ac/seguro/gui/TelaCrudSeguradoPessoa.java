// TelaCrudSeguradoPessoa.java
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
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;

public class TelaCrudSeguradoPessoa extends JFrame {

    private SeguradoPessoaMediator mediator = SeguradoPessoaMediator.getInstancia();
    private JTextField txtCpf;
    private JTextField txtNome;
    private JTextField txtDataNascimento;
    private JTextField txtRenda;
    private JTextField txtBonus;

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

    public TelaCrudSeguradoPessoa
            () {
        setTitle("CRUD de Segurado Pessoa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 500); // Adjusted size
        setLocationRelativeTo(null); // Center the window
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // CPF
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtCpf = new JTextField(20);
        add(txtCpf, gbc);
        gbc.weightx = 0.0; // Reset weight

        gbc.gridx = 2;
        btnNovo = new JButton("Novo");
        add(btnNovo, gbc);

        gbc.gridx = 3;
        btnBuscar = new JButton("Buscar");
        add(btnBuscar, gbc);

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtNome = new JTextField(20);
        add(txtNome, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        // Data de Nascimento
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Data Nasc. (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtDataNascimento = new JTextField(20);
        add(txtDataNascimento, gbc);
        gbc.gridwidth = 1;

        // Renda
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Renda:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtRenda = new JTextField(20);
        add(txtRenda, gbc);
        gbc.gridwidth = 1;

        // Bonus
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Bônus:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtBonus = new JTextField(20);
        add(txtBonus, gbc);
        gbc.gridwidth = 1;

        // Endereço Fields
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Logradouro:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtLogradouro = new JTextField(20);
        add(txtLogradouro, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("CEP:"), gbc);
        gbc.gridx = 1;
        txtCep = new JTextField(10);
        add(txtCep, gbc);

        gbc.gridx = 2;
        add(new JLabel("Número:"), gbc);
        gbc.gridx = 3;
        txtNumeroEndereco = new JTextField(5);
        add(txtNumeroEndereco, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Complemento:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtComplemento = new JTextField(20);
        add(txtComplemento, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 8;
        add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1;
        txtCidade = new JTextField(15);
        add(txtCidade, gbc);

        gbc.gridx = 2;
        add(new JLabel("Estado (UF):"), gbc);
        gbc.gridx = 3;
        txtEstado = new JTextField(2);
        add(txtEstado, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        add(new JLabel("País:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
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

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Event Handlers
        btnNovo.addActionListener(e -> {
            String cpf = txtCpf.getText().trim();
            if (cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "CPF deve ser informado para verificar existência.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            SeguradoPessoa seg = mediator.buscarSeguradoPessoa(cpf);
            if (seg != null) {
                JOptionPane.showMessageDialog(this, "Segurado já existente!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                habilitarCamposEdicao(true);
                btnIncluirAlterar.setText("Incluir");
                btnNovo.setEnabled(false);
                btnBuscar.setEnabled(false);
                txtCpf.setEditable(false); // CPF não editável após "Novo" e verificação
            }
        });

        btnBuscar.addActionListener(e -> {
            String cpf = txtCpf.getText().trim();
            if (cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "CPF deve ser informado para busca.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            SeguradoPessoa seg = mediator.buscarSeguradoPessoa(cpf);
            if (seg == null) {
                JOptionPane.showMessageDialog(this, "Segurado não existente!", "Informação", JOptionPane.INFORMATION_MESSAGE);
            } else {
                preencherCamposComDados(seg);
                habilitarCamposEdicao(true);
                btnIncluirAlterar.setText("Alterar");
                btnExcluir.setEnabled(true);
                btnNovo.setEnabled(false);
                btnBuscar.setEnabled(false);
                txtCpf.setEditable(false);
            }
        });

        btnIncluirAlterar.addActionListener(e -> {
            try {
                String cpf = txtCpf.getText().trim();
                String nome = txtNome.getText().trim();
                LocalDate dataNascimento = LocalDate.parse(txtDataNascimento.getText().trim(), DATE_FORMATTER);
                double renda = Double.parseDouble(txtRenda.getText().trim());
                BigDecimal bonus = new BigDecimal(txtBonus.getText().trim());

                Endereco endereco = new Endereco(
                        txtLogradouro.getText().trim(),
                        txtCep.getText().trim(),
                        txtNumeroEndereco.getText().trim(),
                        txtComplemento.getText().trim(),
                        txtPais.getText().trim(),
                        txtEstado.getText().trim(),
                        txtCidade.getText().trim()
                );

                SeguradoPessoa seg = new SeguradoPessoa(nome, endereco, dataNascimento, bonus, cpf, renda);
                String msg;
                String msgOk;

                if (btnIncluirAlterar.getText().equals("Incluir")) {
                    msg = mediator.incluirSeguradoPessoa(seg);
                    msgOk = "Inclusão realizada com sucesso!";
                } else {
                    msg = mediator.alterarSeguradoPessoa(seg);
                    msgOk = "Alteração realizada com sucesso!";
                }

                if (msg != null) {
                    JOptionPane.showMessageDialog(this, msg, "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, msgOk, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    resetarTelaParaEstadoInicial();
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Data de Nascimento inválida. Use o formato dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Renda e/ou Bônus devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro Inesperado", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnExcluir.addActionListener(e -> {
            String cpf = txtCpf.getText().trim();
            if (cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "CPF não carregado para exclusão.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Confirma a exclusão do segurado?", "Excluir Segurado", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String msg = mediator.excluirSeguradoPessoa(cpf);
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
            if (txtCpf.isEditable()) {
                txtCpf.setText("");
            }
        });

        resetarTelaParaEstadoInicial();
        setVisible(true);
    }

    private void habilitarCamposEdicao(boolean habilitar) {
        txtNome.setEditable(habilitar);
        txtDataNascimento.setEditable(habilitar);
        txtRenda.setEditable(habilitar);
        txtBonus.setEditable(habilitar);
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
        txtDataNascimento.setText("");
        txtRenda.setText("");
        txtBonus.setText("");
        txtLogradouro.setText("");
        txtCep.setText("");
        txtNumeroEndereco.setText("");
        txtComplemento.setText("");
        txtCidade.setText("");
        txtEstado.setText("");
        txtPais.setText("");
    }

    private void preencherCamposComDados(SeguradoPessoa seg) {
        txtCpf.setText(seg.getCpf());
        txtNome.setText(seg.getNome());
        txtDataNascimento.setText(seg.getDataNascimento() != null ? seg.getDataNascimento().format(DATE_FORMATTER) : "");
        txtRenda.setText(String.valueOf(seg.getRenda()));
        txtBonus.setText(seg.getBonus() != null ? seg.getBonus().toPlainString() : "0.00");

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
        txtCpf.setText("");
        txtCpf.setEditable(true);
        habilitarCamposEdicao(false);
        btnNovo.setEnabled(true);
        btnBuscar.setEnabled(true);
        btnExcluir.setEnabled(false);
        btnIncluirAlterar.setText("Incluir");
        txtCpf.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaCrudSeguradoPessoa
                ());
    }
}