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

public class TelaFornecedor extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaFornecedor
     */
    public TelaFornecedor() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void limpar_campos() {
        txtFornCodigo.setText(null);
        txtFornCnpj.setText(null);
        txtFornNome.setText(null);
        txtFornFone.setText(null);
        txtFornEmail.setText(null);
        //a linha abaixo habilita o campo código
        txtFornCodigo.setEnabled(true);
        //a linha abaixo habilita o botão adicionar
        btnFornAdicionar.setEnabled(true);
    }

    private void consultar() {
        // a linha abaixo desabilita o campo código
        txtFornCodigo.setEnabled(false);
        String sql = "select * from fornecedor where codigo =?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtFornCodigo.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtFornCnpj.setText(rs.getString(2));
                txtFornNome.setText(rs.getString(3));
                txtFornFone.setText(rs.getString(4));
                txtFornEmail.setText(rs.getString(5));

            } else {
                JOptionPane.showMessageDialog(null, "Fornecedor não cadastrado");
                //a linha abaixo habilita o campo código
                txtFornCodigo.setEnabled(true);
                // as linhas abaixo "limpam os campos"
                txtFornCodigo.setText(null);
                txtFornCnpj.setText(null);
                txtFornNome.setText(null);
                txtFornFone.setText(null);
                txtFornEmail.setText(null);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para consultar fornecedores
    private void pesquisar_fornecedor() {
        String sql = "select * from Fornecedor where nome like?";
        try {
            // passando o conteúdo da caixa de pesquisa para o ?
            //atenção ao "%" que é a continuação da String sql
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtFornPesquisar.getText() + "%");
            // a linha abaixo usa a biblioteca rs2xml.jar par preencher a tabela
            rs = pst.executeQuery();
            tblFornecedor.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para setar os campos do formulário com o conteúdo da tabela
    public void setar_campos() {
        // a linha abaixo desabilita o campo código
        txtFornCodigo.setEnabled(false);
        int setar = tblFornecedor.getSelectedRow();
        txtFornCodigo.setText(tblFornecedor.getModel().getValueAt(setar, 0).toString());
        txtFornCnpj.setText(tblFornecedor.getModel().getValueAt(setar, 1).toString());
        txtFornNome.setText(tblFornecedor.getModel().getValueAt(setar, 2).toString());
        txtFornFone.setText(tblFornecedor.getModel().getValueAt(setar, 3).toString());
        txtFornEmail.setText(tblFornecedor.getModel().getValueAt(setar, 4).toString());

        // a linha abaixo desabilitra o botão adicionar
        btnFornAdicionar.setEnabled(false);
    }

    // método para adicionar fornecedores
    private void adicionar() {
        String sql = "insert into fornecedor(codigo,cnpj,nome,telefone,email) values(?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtFornCodigo.getText());
            pst.setString(2, txtFornCnpj.getText());
            pst.setString(3, txtFornNome.getText());
            pst.setString(4, txtFornFone.getText());
            pst.setString(5, txtFornEmail.getText());
// validação dos campos obrigatórios
            if ((txtFornCodigo.getText().isEmpty()) || (txtFornNome.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

// a linha abaixo atualiza a tabela fornecedores com os dados do formulário
                // a estrutura abaixo é usada para confirmar a inserção dos dados na tabela abaixo
                int adicionado = pst.executeUpdate();

                // a linha abaixo serve de apoio ao entendimento da lógica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Fornecedor adicionado com sucesso");
                    txtFornCodigo.setText(null);
                    txtFornCnpj.setText(null);
                    txtFornNome.setText(null);
                    txtFornFone.setText(null);
                    txtFornEmail.setText(null);
                }
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Código já cadastrado");
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }
    }
    // criando o método para alterar dados do fornecedor

    private void alterar() {
        String sql = "update fornecedor set  cnpj=? ,nome=?, telefone=?, email=? where codigo=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtFornCnpj.getText());
            pst.setString(2, txtFornNome.getText());
            pst.setString(3, txtFornFone.getText());
            pst.setString(4, txtFornEmail.getText());
            pst.setString(5, txtFornCodigo.getText());
            if (txtFornNome.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                // a linha abaixo atualiza a tabela fornecedor com os dados do formulário
                // a estrutura abaixo é usada para confirmar a alteração dos dados do fornecedor na tabela abaixo
                int alterado = pst.executeUpdate();

                if (alterado> 0) {
                    JOptionPane.showMessageDialog(null, "Dados do fornecedor alterados com sucesso");
                    txtFornCodigo.setText(null);
                    txtFornCnpj.setText(null);
                    txtFornNome.setText(null);
                    txtFornFone.setText(null);
                    txtFornEmail.setText(null);
                    // a linha abaixo habilita o botão adicionar
                    btnFornAdicionar.setEnabled(true);
                    //a linha abaixo hanilita o campo código
                    txtFornCodigo.setEnabled(true);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    // método responsável pela remoção de Fornecedores

    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este fornecedor?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from fornecedor where codigo =?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtFornCodigo.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Fornecedor removido com sucesso");
                    txtFornCodigo.setText(null);
                    txtFornCnpj.setText(null);
                    txtFornNome.setText(null);
                    txtFornFone.setText(null);
                    txtFornEmail.setText(null);

                }

            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(null, "Exclusão não permitida,existe(m) entrada(s) vinculadas ao fornecedor");
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null, e2);
            }

        }
        //a linha abaixo hanilita o botão adicionar
        btnFornAdicionar.setEnabled(true);
        //a linha abaixo hanilita o campo código
        txtFornCodigo.setEnabled(true);
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtFornNome = new javax.swing.JTextField();
        txtFornEmail = new javax.swing.JTextField();
        btnFornAdicionar = new javax.swing.JButton();
        btnFornAlterar = new javax.swing.JButton();
        btnFornExcluir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFornecedor = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtFornPesquisar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnFornPesquisar = new javax.swing.JButton();
        btnFornLimparCampos = new javax.swing.JButton();
        txtFornCodigo = new javax.swing.JTextField();
        txtFornCnpj = new javax.swing.JFormattedTextField();
        txtFornFone = new javax.swing.JFormattedTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Fornecedor");
        setPreferredSize(new java.awt.Dimension(670, 480));

        jLabel1.setText("* Código");

        jLabel2.setText("CNPJ");

        jLabel3.setText("Nome");

        jLabel4.setText("Telefone");

        jLabel5.setText("Email");

        txtFornEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFornEmailActionPerformed(evt);
            }
        });

        btnFornAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/create.png"))); // NOI18N
        btnFornAdicionar.setToolTipText("Adicionar");
        btnFornAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFornAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnFornAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFornAdicionarActionPerformed(evt);
            }
        });

        btnFornAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/update.png"))); // NOI18N
        btnFornAlterar.setToolTipText("Alterar");
        btnFornAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFornAlterar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnFornAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFornAlterarActionPerformed(evt);
            }
        });

        btnFornExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/delete.png"))); // NOI18N
        btnFornExcluir.setToolTipText("Excluir");
        btnFornExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFornExcluir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnFornExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFornExcluirActionPerformed(evt);
            }
        });

        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 200));

        tblFornecedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "CNPJ", "Telefone", "Email"
            }
        ));
        tblFornecedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFornecedorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblFornecedor);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pesquisar.png"))); // NOI18N

        txtFornPesquisar.setToolTipText("Pesquisar Nome");
        txtFornPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFornPesquisarActionPerformed(evt);
            }
        });
        txtFornPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFornPesquisarKeyReleased(evt);
            }
        });

        jLabel7.setText("* Campos Obrigatórios");

        btnFornPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/read.png"))); // NOI18N
        btnFornPesquisar.setToolTipText("Pesquisar");
        btnFornPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFornPesquisar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnFornPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFornPesquisarActionPerformed(evt);
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

        try {
            txtFornCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtFornFone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtFornPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(170, 170, 170)
                .addComponent(jLabel7)
                .addGap(133, 133, 133))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel5))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnFornAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnFornPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnFornAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addComponent(btnFornExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtFornCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFornFone, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFornCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtFornNome, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtFornEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnFornLimparCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(58, 58, 58))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtFornPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtFornCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtFornCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFornNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel4))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFornFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtFornEmail)))
                    .addComponent(btnFornLimparCampos, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFornAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFornAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFornExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFornPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53))
        );

        setBounds(0, 0, 670, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void txtFornEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFornEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFornEmailActionPerformed

    private void btnFornAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFornAdicionarActionPerformed
        // chamando o mpetodo adicionar Fornecedor
        adicionar();
    }//GEN-LAST:event_btnFornAdicionarActionPerformed

    private void btnFornAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFornAlterarActionPerformed
        // chamando o método alterara fornecedor
        alterar();

    }//GEN-LAST:event_btnFornAlterarActionPerformed

    private void btnFornExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFornExcluirActionPerformed
        // chamando o método remover
        remover();

    }//GEN-LAST:event_btnFornExcluirActionPerformed

    private void txtFornPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFornPesquisarActionPerformed

    }//GEN-LAST:event_txtFornPesquisarActionPerformed

    private void tblFornecedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFornecedorMouseClicked
        // chamando o mpetodo para setar os campos
        setar_campos();
    }//GEN-LAST:event_tblFornecedorMouseClicked

    private void txtFornPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFornPesquisarKeyReleased
        // chamar o método pesquisar fornecedor
        pesquisar_fornecedor();
    }//GEN-LAST:event_txtFornPesquisarKeyReleased

    private void btnFornPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFornPesquisarActionPerformed
        //chamando o método consultar
        consultar();

    }//GEN-LAST:event_btnFornPesquisarActionPerformed

    private void btnFornLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFornLimparCamposActionPerformed
        //método para limpar os campos manualmente
        limpar_campos();
    }//GEN-LAST:event_btnFornLimparCamposActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFornAdicionar;
    private javax.swing.JButton btnFornAlterar;
    private javax.swing.JButton btnFornExcluir;
    private javax.swing.JButton btnFornLimparCampos;
    private javax.swing.JButton btnFornPesquisar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblFornecedor;
    private javax.swing.JFormattedTextField txtFornCnpj;
    private javax.swing.JTextField txtFornCodigo;
    private javax.swing.JTextField txtFornEmail;
    private javax.swing.JFormattedTextField txtFornFone;
    private javax.swing.JTextField txtFornNome;
    private javax.swing.JTextField txtFornPesquisar;
    // End of variables declaration//GEN-END:variables
}
