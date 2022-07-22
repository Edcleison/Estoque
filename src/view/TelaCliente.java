/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author alyne.morais
 */
import java.sql.*;
import connection.ModuloConexao;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class TelaCliente extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaCliente
     */
    public TelaCliente() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void limpar_campos() {
        txtCliCodigo.setText(null);
        txtCliCpf.setText(null);
        txtCliNome.setText(null);
        txtCliCelular.setText(null);
        txtCliEmail.setText(null);
        // a linha abaixo habilita o  campo código
        txtCliCodigo.setEnabled(true);
        // a linha abaixo habilita o botão adicionar
        btnCliAdicionar.setEnabled(true);
    }

    // método para consultar fornecedores
    private void pesquisar_cliente() {
        // a linha abaixo desabilita o campo código
        txtCliCodigo.setEnabled(false);
        String sql = "select * from Cliente where nome like?";
        try {
            // passando o conteúdo da caixa de pesquisa para o ?
            //atenção ao "%" que é a continuação da String sql
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliPesquisar.getText() + "%");
            // a linha abaixo usa a biblioteca rs2xml.jar par preencher a tabela
            rs = pst.executeQuery();
            tblCliente.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    // método para setar os campos do formulário com o conteúdo da tabela
    public void setar_campos() {
        int setar = tblCliente.getSelectedRow();
        txtCliCodigo.setText(tblCliente.getModel().getValueAt(setar, 0).toString());
        txtCliCpf.setText(tblCliente.getModel().getValueAt(setar, 1).toString());
        txtCliNome.setText(tblCliente.getModel().getValueAt(setar, 2).toString());
        txtCliCelular.setText(tblCliente.getModel().getValueAt(setar, 3).toString());
        txtCliEmail.setText(tblCliente.getModel().getValueAt(setar, 4).toString());

        // a linha abaixo desabilitra o botão adicionar
        btnCliAdicionar.setEnabled(false);

    }
    // método para consultar clientes

    private void consultar() {
        String sql = "select * from cliente where Codigo =?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliCodigo.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtCliCpf.setText(rs.getString(2));
                txtCliNome.setText(rs.getString(3));
                txtCliCelular.setText(rs.getString(4));
                txtCliEmail.setText(rs.getNString(5));

            } else {
                JOptionPane.showMessageDialog(null, "Cliente não cadastrado");
                // as linhas abaixo "limpam os campos"
                txtCliCpf.setText(null);
                txtCliCodigo.setText(null);
                txtCliNome.setText(null);
                txtCliCelular.setText(null);
                txtCliEmail.setText(null);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    // método para adicionar clientes
    private void adicionar() {
        String sql = "insert into cliente(codigo,cpf,nome,celular,email) values(?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliCodigo.getText());
            pst.setString(2, txtCliCpf.getText());
            pst.setString(3, txtCliNome.getText());
            pst.setString(4, txtCliCelular.getText());
            pst.setString(5, txtCliEmail.getText());
// validação dos campos obrigatórios
            if ((txtCliCodigo.getText().isEmpty()) || (txtCliNome.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

// a linha abaixo atualiza a tabela fornecedores com os dados do formulário
                // a estrutura abaixo é usada para confirmar a inserção dos dados na tabela abaixo
                int adicionado = pst.executeUpdate();

                // a linha abaixo serve de apoio ao entendimento da lógica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso");
                    txtCliCodigo.setText(null);
                    txtCliCpf.setText(null);
                    txtCliNome.setText(null);
                    txtCliCelular.setText(null);
                    txtCliEmail.setText(null);
                }
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Código já cadastrado!");
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }
    }
    // criando o método para alterar dados do fornecedor

    private void alterar() {
        String sql = "update cliente set cpf=? ,nome=?, celular=?, email=? where codigo=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliCpf.getText());
            pst.setString(2, txtCliNome.getText());
            pst.setString(3, txtCliCelular.getText());
            pst.setString(4, txtCliEmail.getText());
            pst.setString(5, txtCliCodigo.getText());
            if ((txtCliNome.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                // a linha abaixo atualiza a tabela fornecedor com os dados do formulário
                // a estrutura abaixo é usada para confirmar a alteração dos dados do fornecedor na tabela abaixo
                int alterado = pst.executeUpdate();

                // a linha abaixo serve de apoio ao entendimento da lógica
                //System.out.println(adicionado);
                if (alterado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do Cliente alterados com sucesso");
                    txtCliCodigo.setText(null);
                    txtCliCpf.setText(null);
                    txtCliNome.setText(null);
                    txtCliCelular.setText(null);
                    txtCliEmail.setText(null);
                    // a linha abaixo habilita o botão adicionar
                    btnCliAdicionar.setEnabled(true);
                    // a linha abaixo habilita o o campo código
                    txtCliCodigo.setEnabled(true);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    // método responsável pela remoção de Fornecedores

    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este cliente?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from cliente where codigo =?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCliCodigo.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso");
                    txtCliCodigo.setText(null);
                    txtCliCpf.setText(null);
                    txtCliNome.setText(null);
                    txtCliCelular.setText(null);
                    txtCliEmail.setText(null);

                }

            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(null, "Exclusão não permitida,existe(m) saída(s) vinculadas ao cliente");
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null, e2);
            }
        }
        // a linha abaixo habilita o botão adicionar
        btnCliAdicionar.setEnabled(true);
        // a linha abaixo habilitra  o campo código
        txtCliCodigo.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtCliCodigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCliNome = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCliEmail = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnCliAdicionar = new javax.swing.JButton();
        btnCliPesquisar = new javax.swing.JButton();
        btnCliAlterar = new javax.swing.JButton();
        btnCliExcluir = new javax.swing.JButton();
        btnFornLimparCampos = new javax.swing.JButton();
        txtCliPesquisar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCliente = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCliCpf = new javax.swing.JFormattedTextField();
        txtCliCelular = new javax.swing.JFormattedTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cliente");
        setPreferredSize(new java.awt.Dimension(670, 480));

        jLabel1.setText("*Código:");

        txtCliCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliCodigoActionPerformed(evt);
            }
        });

        jLabel2.setText("CPF:");

        jLabel3.setText("* Nome:");

        txtCliNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliNomeActionPerformed(evt);
            }
        });

        jLabel4.setText("Email:");

        txtCliEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliEmailActionPerformed(evt);
            }
        });

        jLabel5.setText("Celular:");

        btnCliAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/create.png"))); // NOI18N
        btnCliAdicionar.setToolTipText("Adicionar");
        btnCliAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCliAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliAdicionarActionPerformed(evt);
            }
        });

        btnCliPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/read.png"))); // NOI18N
        btnCliPesquisar.setToolTipText("Pesquisar");
        btnCliPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliPesquisar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCliPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliPesquisarActionPerformed(evt);
            }
        });

        btnCliAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/update.png"))); // NOI18N
        btnCliAlterar.setToolTipText("Alterar");
        btnCliAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliAlterar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCliAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliAlterarActionPerformed(evt);
            }
        });

        btnCliExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/delete.png"))); // NOI18N
        btnCliExcluir.setToolTipText("Excluir");
        btnCliExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliExcluir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCliExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliExcluirActionPerformed(evt);
            }
        });

        btnFornLimparCampos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnFornLimparCampos.setText("Limpar Campos");
        btnFornLimparCampos.setToolTipText("Limpar Campos");
        btnFornLimparCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFornLimparCamposActionPerformed(evt);
            }
        });

        txtCliPesquisar.setToolTipText("Pesquisar Nome");
        txtCliPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliPesquisarActionPerformed(evt);
            }
        });
        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 200));

        tblCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "CPF", "Nome", "Celular"
            }
        ));
        tblCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCliente);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pesquisar.png"))); // NOI18N

        jLabel7.setText("* Campos Obrigatórios");

        try {
            txtCliCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtCliCelular.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCliCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnFornLimparCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCliCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCliCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(btnCliAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCliAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCliExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(68, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(42, 42, 42))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCliCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCliCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(btnFornLimparCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCliAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCliAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCliExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        setBounds(0, 0, 670, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCliCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliCodigoActionPerformed

    private void txtCliNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliNomeActionPerformed

    private void txtCliEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliEmailActionPerformed

    private void btnCliAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliAdicionarActionPerformed
        // chamando o método adicionar cliente
        adicionar();
    }//GEN-LAST:event_btnCliAdicionarActionPerformed

    private void btnCliPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliPesquisarActionPerformed
        // chamando o método consultar cliente
        consultar();

    }//GEN-LAST:event_btnCliPesquisarActionPerformed

    private void btnCliAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliAlterarActionPerformed
        // chamando o método alterar cliente
        alterar();


    }//GEN-LAST:event_btnCliAlterarActionPerformed

    private void btnCliExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliExcluirActionPerformed
        // chamando o método para excluir cliente
        remover();
    }//GEN-LAST:event_btnCliExcluirActionPerformed

    private void btnFornLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFornLimparCamposActionPerformed
        //método para limpar os campos manualmente
        limpar_campos();
    }//GEN-LAST:event_btnFornLimparCamposActionPerformed

    private void txtCliPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliPesquisarActionPerformed

    }//GEN-LAST:event_txtCliPesquisarActionPerformed

    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        // chamar o método pesquisar fornecedor
        pesquisar_cliente();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased

    private void tblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClienteMouseClicked
        // chamando o mpetodo para setar os campos
        setar_campos();
    }//GEN-LAST:event_tblClienteMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCliAdicionar;
    private javax.swing.JButton btnCliAlterar;
    private javax.swing.JButton btnCliExcluir;
    private javax.swing.JButton btnCliPesquisar;
    private javax.swing.JButton btnFornLimparCampos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCliente;
    private javax.swing.JFormattedTextField txtCliCelular;
    private javax.swing.JTextField txtCliCodigo;
    private javax.swing.JFormattedTextField txtCliCpf;
    private javax.swing.JTextField txtCliEmail;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtCliPesquisar;
    // End of variables declaration//GEN-END:variables
}
