// TelaInclusaoApolice.java
package br.edu.cs.poo.ac.seguro.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.mediators.ApoliceMediator;
import br.edu.cs.poo.ac.seguro.mediators.DadosVeiculo;
import br.edu.cs.poo.ac.seguro.mediators.RetornoInclusaoApolice;

public class TelaInclusaoApolice extends JFrame {

    private ApoliceMediator mediator;
    private JTextField txtCpfOuCnpj;
    private JTextField txtPlaca;
    private JTextField txtAno;
    private JTextField txtValorMaximoSegurado;
    private JComboBox<String> cmbCategoriaVeiculo;

    private JButton btnIncluir;
    private JButton btnLimpar;

    public TelaInclusaoApolice() {
        mediator = ApoliceMediator.getInstancia(); // [cite: 26]
        setTitle("Inclusão de Apólice");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close if it's a secondary window
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;


        // CPF ou CNPJ
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("CPF/CNPJ do Segurado:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        txtCpfOuCnpj = new JTextField(20); // [cite: 27]
        add(txtCpfOuCnpj, gbc);

        // Placa
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Placa do Veículo:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        txtPlaca = new JTextField(20); // [cite: 27]
        add(txtPlaca, gbc);

        // Ano
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Ano do Veículo:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        txtAno = new JTextField(20); // [cite: 27]
        add(txtAno, gbc);

        // Valor Máximo Segurado
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Valor Máximo Segurado (R$):"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        txtValorMaximoSegurado = new JTextField(20); // [cite: 27]
        add(txtValorMaximoSegurado, gbc);

        // Categoria do Veículo
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Categoria do Veículo:"), gbc); // [cite: 29]
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        cmbCategoriaVeiculo = new JComboBox<>();
        popularComboCategoriaVeiculo();
        add(cmbCategoriaVeiculo, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnIncluir = new JButton("Incluir"); // [cite: 30]
        btnLimpar = new JButton("Limpar"); // [cite: 30]
        buttonPanel.add(btnIncluir);
        buttonPanel.add(btnLimpar);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Event Handlers
        btnIncluir.addActionListener(e -> {
            try {
                String cpfOuCnpj = txtCpfOuCnpj.getText().trim();
                String placa = txtPlaca.getText().trim();
                int ano = Integer.parseInt(txtAno.getText().trim()); // [cite: 28]
                BigDecimal valorMaxSegurado = new BigDecimal(txtValorMaximoSegurado.getText().trim()); // [cite: 28]

                int selectedIndex = cmbCategoriaVeiculo.getSelectedIndex();
                if (selectedIndex < 0) {
                    JOptionPane.showMessageDialog(this, "Selecione uma categoria de veículo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String selectedCategoriaName = (String) cmbCategoriaVeiculo.getSelectedItem();
                CategoriaVeiculo selectedCategoria = null;
                for (CategoriaVeiculo cat : CategoriaVeiculo.values()) {
                    if (cat.getNome().equals(selectedCategoriaName)) {
                        selectedCategoria = cat;
                        break;
                    }
                }
                if (selectedCategoria == null) {
                    JOptionPane.showMessageDialog(this, "Categoria de veículo inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int codigoCategoria = selectedCategoria.getCodigo();

                DadosVeiculo dados = new DadosVeiculo(cpfOuCnpj, placa, ano, valorMaxSegurado, codigoCategoria);
                RetornoInclusaoApolice retorno = mediator.incluirApolice(dados);

                if (retorno.getMensagemErro() != null) {
                    JOptionPane.showMessageDialog(this, retorno.getMensagemErro(), "Erro de Validação", JOptionPane.ERROR_MESSAGE); // [cite: 34]
                } else {
                    JOptionPane.showMessageDialog(this, "Apólice incluída com sucesso! Anote o número da apólice: " + retorno.getNumeroApolice(), "Sucesso", JOptionPane.INFORMATION_MESSAGE); // [cite: 32, 33]
                    limparCampos();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ano e Valor Máximo Segurado devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao incluir apólice: " + ex.getMessage(), "Erro Inesperado", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        btnLimpar.addActionListener(e -> limparCampos());

        pack(); // Adjusts frame size to components
        if (getHeight() < 300) setSize(getWidth(), 300); // Ensure minimum height
        if (getWidth() < 450) setSize(450, getHeight()); // Ensure minimum width
        setVisible(true);
    }

    private void popularComboCategoriaVeiculo() {
        CategoriaVeiculo[] categorias = CategoriaVeiculo.values();
        String[] nomesCategorias = new String[categorias.length];
        for (int i = 0; i < categorias.length; i++) {
            nomesCategorias[i] = categorias[i].getNome();
        }
        Arrays.sort(nomesCategorias, Comparator.naturalOrder()); // Sort alphabetically [cite: 29]

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(nomesCategorias);
        cmbCategoriaVeiculo.setModel(model);

        if (cmbCategoriaVeiculo.getItemCount() > 0) {
            cmbCategoriaVeiculo.setSelectedIndex(0); // [cite: 31]
        }
    }

    private void limparCampos() {
        txtCpfOuCnpj.setText("");
        txtPlaca.setText("");
        txtAno.setText("");
        txtValorMaximoSegurado.setText("");
        if (cmbCategoriaVeiculo.getItemCount() > 0) {
            cmbCategoriaVeiculo.setSelectedIndex(0); // [cite: 31]
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaInclusaoApolice());
    }
}