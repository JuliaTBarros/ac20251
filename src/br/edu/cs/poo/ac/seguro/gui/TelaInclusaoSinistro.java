// TelaInclusaoSinistro.java
package br.edu.cs.poo.ac.seguro.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;
import br.edu.cs.poo.ac.seguro.mediators.DadosSinistro;
import br.edu.cs.poo.ac.seguro.mediators.SinistroMediator;

public class TelaInclusaoSinistro extends JFrame {

    private SinistroMediator mediator; // [cite: 14]
    private JTextField txtPlaca;
    private JTextField txtDataHoraSinistro; // Format: dd/MM/yyyy HH:mm
    private JTextField txtUsuarioRegistro;
    private JTextField txtValorSinistro;
    private JComboBox<String> cmbTipoSinistro;

    private JButton btnIncluir;
    private JButton btnLimpar;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TelaInclusaoSinistro() {
        mediator = SinistroMediator.getInstancia();
        setTitle("Inclusão de Sinistro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(480, 280);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Placa do Veículo
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Placa do Veículo:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        txtPlaca = new JTextField(20); // [cite: 14]
        add(txtPlaca, gbc);

        // Data e Hora do Sinistro
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Data/Hora Sinistro (dd/MM/yyyy HH:mm):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        txtDataHoraSinistro = new JTextField(20); // [cite: 14]
        add(txtDataHoraSinistro, gbc);

        // Usuário do Registro
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Usuário do Registro:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        txtUsuarioRegistro = new JTextField(20); // [cite: 14]
        add(txtUsuarioRegistro, gbc);

        // Valor do Sinistro
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Valor do Sinistro (R$):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        txtValorSinistro = new JTextField(20); // [cite: 14]
        add(txtValorSinistro, gbc);

        // Tipo de Sinistro
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Tipo de Sinistro:"), gbc); // [cite: 16]
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        cmbTipoSinistro = new JComboBox<>();
        popularComboTipoSinistro();
        add(cmbTipoSinistro, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnIncluir = new JButton("Incluir"); // [cite: 17]
        btnLimpar = new JButton("Limpar"); // [cite: 17]
        buttonPanel.add(btnIncluir);
        buttonPanel.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);


        // Event Handlers
        btnIncluir.addActionListener(e -> {
            try {
                String placa = txtPlaca.getText().trim();
                LocalDateTime dataHoraSinistroVal = LocalDateTime.parse(txtDataHoraSinistro.getText().trim(), DATETIME_FORMATTER); // [cite: 15]
                String usuarioRegistro = txtUsuarioRegistro.getText().trim();
                double valorSinistroVal = Double.parseDouble(txtValorSinistro.getText().trim()); // [cite: 15]

                int selectedIndex = cmbTipoSinistro.getSelectedIndex();
                if (selectedIndex < 0) {
                    JOptionPane.showMessageDialog(this, "Selecione um tipo de sinistro.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String selectedTipoNome = (String) cmbTipoSinistro.getSelectedItem();
                TipoSinistro selectedTipo = null;
                for(TipoSinistro tipo : TipoSinistro.values()){
                    if(tipo.getNome().equals(selectedTipoNome)){
                        selectedTipo = tipo;
                        break;
                    }
                }
                if(selectedTipo == null) {
                    JOptionPane.showMessageDialog(this, "Tipo de sinistro inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int codigoTipoSinistro = selectedTipo.getCodigo();

                DadosSinistro dados = new DadosSinistro(placa, dataHoraSinistroVal, usuarioRegistro, valorSinistroVal, codigoTipoSinistro);

                String numeroSinistro = mediator.incluirSinistro(dados, LocalDateTime.now());

                JOptionPane.showMessageDialog(this, "Sinistro incluído com sucesso! Anote o número do sinistro: " + numeroSinistro, "Sucesso", JOptionPane.INFORMATION_MESSAGE); // [cite: 19, 20]
                limparCampos();

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Data/Hora do Sinistro inválida. Use o formato dd/MM/yyyy HH:mm.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor do Sinistro deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (ExcecaoValidacaoDados ex) {
                List<String> mensagensErro = ex.getMensagens();
                String errosConcatenados = mensagensErro.stream().collect(Collectors.joining("\n"));
                JOptionPane.showMessageDialog(this, "Erro ao incluir sinistro:\n" + errosConcatenados, "Erro de Validação", JOptionPane.ERROR_MESSAGE); // [cite: 21]
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro inesperado ao incluir sinistro: " + ex.getMessage(), "Erro Inesperado", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        btnLimpar.addActionListener(e -> limparCampos());

        pack(); // Adjusts frame size to components
        if (getHeight() < 280) setSize(getWidth(), 280); // Ensure minimum height
        if (getWidth() < 480) setSize(480, getHeight()); // Ensure minimum width
        setVisible(true);
    }

    private void popularComboTipoSinistro() {
        TipoSinistro[] tipos = TipoSinistro.values();
        String[] nomesTipos = new String[tipos.length];
        for (int i = 0; i < tipos.length; i++) {
            nomesTipos[i] = tipos[i].getNome();
        }
        Arrays.sort(nomesTipos, Comparator.naturalOrder()); // Sort alphabetically [cite: 16]

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(nomesTipos);
        cmbTipoSinistro.setModel(model);

        if (cmbTipoSinistro.getItemCount() > 0) {
            cmbTipoSinistro.setSelectedIndex(0); // [cite: 18]
        }
    }

    private void limparCampos() {
        txtPlaca.setText("");
        txtDataHoraSinistro.setText("");
        txtUsuarioRegistro.setText("");
        txtValorSinistro.setText("");
        if (cmbTipoSinistro.getItemCount() > 0) {
            cmbTipoSinistro.setSelectedIndex(0); // [cite: 18]
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaInclusaoSinistro());
    }
}