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

public class TelaProduto extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaProduto
     */
    public TelaProduto() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void limpar_campos() {
        txtProdCodigo.setText(null);
        txtProdDescricao.setText(null);
        txtProdQtde.setText(null);
        txtProdCustoMedio.setText(null);
        // a linha abaixo habilita o campo código
        txtProdCodigo.setEnabled(true);
        // a linha abaixo habilita o botão adicionar
        btnProdAdicionar.setEnabled(true);

    }

    private void consultar() {
        // a linha abaixo desabilita o campo código
        txtProdCodigo.setEnabled(false);
        String sql = "select * from produto where codigo =?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdCodigo.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtProdDescricao.setText(rs.getString(2));
                txtProdQtde.setText(rs.getString(3));
                txtProdCustoMedio.setText(rs.getString(4));

            } else {
                JOptionPane.showMessageDialog(null, "Produto não cadastrado");
                // as linhas abaixo "limpam os campos"
                // a linha abaixo desabilita o campo código
                txtProdCodigo.setEnabled(true);

                txtProdCodigo.setText(null);
                txtProdDescricao.setText(null);
                txtProdQtde.setText(null);
                txtProdCustoMedio.setText(null);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    // método para consultar produtos
    private void pesquisar_produto() {
        String sql = "select * from Produto where descricao like ?";
        try {
            // passando o conteúdo da caixa de pesquisa para o ?
            //atenção ao "%" que é a continuação da String sql
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdPesquisar.getText() + "%");
            // a linha abaixo usa a biblioteca rs2xml.jar par preencher a tabela
            rs = pst.executeQuery();
            tblProduto.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para setar os campos do formulário com o conteúdo da tabela
    public void setar_campos() {
        // a linha abaixo desabilita o campo código
        txtProdCodigo.setEnabled(false);
        int setar = tblProduto.getSelectedRow();
        txtProdCodigo.setText(tblProduto.getModel().getValueAt(setar, 0).toString());
        txtProdDescricao.setText(tblProduto.getModel().getValueAt(setar, 1).toString());
        txtProdQtde.setText(tblProduto.getModel().getValueAt(setar, 2).toString());
        txtProdCustoMedio.setText(tblProduto.getModel().getValueAt(setar, 3).toString());

        // a linha abaixo desabilitra o botão adicionar
        btnProdAdicionar.setEnabled(false);

    }

    // método para adicionar produto
    private void adicionar() {
        String sql = "insert into Produto(codigo,descricao,qtde,customedio) values(?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdCodigo.getText());
            pst.setString(2, txtProdDescricao.getText());
            pst.setString(3, txtProdQtde.getText());
            pst.setString(4, txtProdCustoMedio.getText().replace(",", "."));
// validação dos campos obrigatórios
            if ((txtProdCodigo.getText().isEmpty()) || (txtProdDescricao.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

// a linha abaixo atualiza a tabela fornecedores com os dados do formulário
                // a estrutura abaixo é usada para confirmar a inserção dos dados na tabela abaixo
                int adicionado = pst.executeUpdate();

                // a linha abaixo serve de apoio ao entendimento da lógica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Produto adicionado com sucesso");
                    txtProdCodigo.setText(null);
                    txtProdDescricao.setText(null);
                    txtProdQtde.setText(null);
                    txtProdCustoMedio.setText(null);
                }
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Código já cadastrado");
        } catch (java.sql.SQLException e2) {
            JOptionPane.showMessageDialog(null, "Código e (ou) quantidade inválido(s)");
        } catch (Exception e3) {
            JOptionPane.showMessageDialog(null, e3);
        }
    }

    private void alterar() {
        String sql = "update produto set  descricao=? ,qtde=?, customedio=? where codigo=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdDescricao.getText());
            pst.setString(2, txtProdQtde.getText());
            pst.setString(3, txtProdCustoMedio.getText());
            pst.setString(4, txtProdCodigo.getText());
            if ((txtProdDescricao.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                // a linha abaixo atualiza a tabela fornecedor com os dados do formulário
                // a estrutura abaixo é usada para confirmar a alteração dos dados do produto na tabela abaixo
                int alterado = pst.executeUpdate();

                if (alterado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do Produto alterados com sucesso");
                    txtProdCodigo.setText(null);
                    txtProdDescricao.setText(null);
                    txtProdQtde.setText(null);
                    txtProdCustoMedio.setText(null);
                    btnProdAdicionar.setEnabled(true);
                }
            }

        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(null, "Quantidade inválida");
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }
    }
    // método responsável pela remoção de Produtos

    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este produto?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from produto where codigo =?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtProdCodigo.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Produto removido com sucesso");
                    txtProdCodigo.setText(null);
                    txtProdDescricao.setText(null);
                    txtProdQtde.setText(null);
                    txtProdCustoMedio.setText(null);

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            // a linha abaixo habilita o botão adicionar
            btnProdAdicionar.setEnabled(true);
            // a linha abaixo habilita o campo código
            txtProdCodigo.setEnabled(true);
        }
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
        txtProdCodigo = new javax.swing.JTextField();
        txtProdDescricao = new javax.swing.JTextField();
        txtProdQtde = new javax.swing.JTextField();
        btnProdAdicionar = new javax.swing.JButton();
        btnProdAlterar = new javax.swing.JButton();
        btnProdExcluir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduto = new javax.swing.JTable();
        txtProdPesquisar = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnProdPesquisar = new javax.swing.JButton();
        btnProdLimparCampos = new javax.swing.JButton();
        txtProdCustoMedio = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setPreferredSize(new java.awt.Dimension(670, 480));
        setVerifyInputWhenFocusTarget(false);

        jLabel1.setText("* Código:");

        jLabel2.setText("* Descrição:");

        jLabel3.setText("Quantidade:");

        jLabel4.setText("Custo Médio:");

        txtProdCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProdCodigoActionPerformed(evt);
            }
        });

        txtProdDescricao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProdDescricaoActionPerformed(evt);
            }
        });

        btnProdAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/create.png"))); // NOI18N
        btnProdAdicionar.setToolTipText("Adicionar");
        btnProdAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnProdAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdAdicionarActionPerformed(evt);
            }
        });

        btnProdAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/update.png"))); // NOI18N
        btnProdAlterar.setToolTipText("Alterar");
        btnProdAlterar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnProdAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdAlterarActionPerformed(evt);
            }
        });

        btnProdExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/delete.png"))); // NOI18N
        btnProdExcluir.setToolTipText("Excluir");
        btnProdExcluir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnProdExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdExcluirActionPerformed(evt);
            }
        });

        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 200));

        tblProduto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "Descrição", "Qtde", "CustoMédio"
            }
        ));
        tblProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProdutoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProduto);

        txtProdPesquisar.setToolTipText("Pesquisar Descrição");
        txtProdPesquisar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtProdPesquisarMouseClicked(evt);
            }
        });
        txtProdPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProdPesquisarActionPerformed(evt);
            }
        });
        txtProdPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProdPesquisarKeyReleased(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pesquisar.png"))); // NOI18N

        jLabel7.setText("* Campos Obrigatórios");

        btnProdPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/read.png"))); // NOI18N
        btnProdPesquisar.setToolTipText("Pesquisar");
        btnProdPesquisar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnProdPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdPesquisarActionPerformed(evt);
            }
        });

        btnProdLimparCampos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnProdLimparCampos.setText("Limpar Campos");
        btnProdLimparCampos.setToolTipText("Limpar Campos");
        btnProdLimparCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdLimparCamposActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(btnProdAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnProdPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(btnProdAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(btnProdExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtProdCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(txtProdQtde, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                                                .addComponent(txtProdCustoMedio))
                                            .addGap(134, 134, 134)
                                            .addComponent(btnProdLimparCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(txtProdDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtProdPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(45, 45, 45))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtProdPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProdCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtProdDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtProdQtde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtProdCustoMedio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(btnProdLimparCampos)))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnProdAlterar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnProdExcluir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnProdAdicionar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnProdPesquisar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
        );

        setBounds(0, 0, 670, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void txtProdCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProdCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProdCodigoActionPerformed

    private void txtProdDescricaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProdDescricaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProdDescricaoActionPerformed

    private void btnProdAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdAdicionarActionPerformed
        // chamando o método adicionar Produto
        adicionar();
    }//GEN-LAST:event_btnProdAdicionarActionPerformed

    private void btnProdAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdAlterarActionPerformed
        // chamando o método alterar Produto
        alterar();

    }//GEN-LAST:event_btnProdAlterarActionPerformed

    private void btnProdExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdExcluirActionPerformed
        // chamando o método para remover Produto
        remover();
        // a linha abaixo habilita o campo código
        txtProdCodigo.setEnabled(true);

    }//GEN-LAST:event_btnProdExcluirActionPerformed

    private void tblProdutoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProdutoMouseClicked
        // chamando o mpetodo para setar os campos
        setar_campos();
    }//GEN-LAST:event_tblProdutoMouseClicked

    private void txtProdPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProdPesquisarActionPerformed

    }//GEN-LAST:event_txtProdPesquisarActionPerformed

    private void txtProdPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProdPesquisarKeyReleased
        // chamar o método pesquisar produto
        pesquisar_produto();
    }//GEN-LAST:event_txtProdPesquisarKeyReleased

    private void btnProdPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdPesquisarActionPerformed
        //chamando o método consultar
        consultar();

    }//GEN-LAST:event_btnProdPesquisarActionPerformed

    private void txtProdPesquisarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtProdPesquisarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProdPesquisarMouseClicked

    private void btnProdLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdLimparCamposActionPerformed
        //método para limpar os campos manualmente
        limpar_campos();
    }//GEN-LAST:event_btnProdLimparCamposActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProdAdicionar;
    private javax.swing.JButton btnProdAlterar;
    private javax.swing.JButton btnProdExcluir;
    private javax.swing.JButton btnProdLimparCampos;
    private javax.swing.JButton btnProdPesquisar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblProduto;
    private javax.swing.JTextField txtProdCodigo;
    private javax.swing.JTextField txtProdCustoMedio;
    private javax.swing.JTextField txtProdDescricao;
    private javax.swing.JTextField txtProdPesquisar;
    private javax.swing.JTextField txtProdQtde;
    // End of variables declaration//GEN-END:variables

}
