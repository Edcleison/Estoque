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

public class TelaSaida extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaSaida
     */
    public TelaSaida() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void limpar_campos() {
        txtSaiCodigo.setText(null);
        txtSaiData.setText(null);
        txtSaiQtde.setText(null);
        txtSaiValor.setText(null);
        txtSaiCodProd.setText(null);
        txtSaiCodCli.setText(null);
        // a linha abaixo habilita o campo código
        txtSaiCodigo.setEnabled(true);
        // a linha abaixo habilita o botão adicionar
        btnSaiAdicionar.setEnabled(true);
    }

    private void consultar() {
        // a linha abaixo desabilita o campo código
        txtSaiCodigo.setEnabled(false);
        String sql = "select * from saida where codigo =?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtSaiCodigo.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtSaiData.setText(rs.getString(2));
                txtSaiQtde.setText(rs.getString(3));
                txtSaiValor.setText(rs.getString(4));
                txtSaiCodProd.setText(rs.getString(5));
                txtSaiCodCli.setText(rs.getString(5));

            } else {
                JOptionPane.showMessageDialog(null, "Saida não cadastrado");
                // a linha abaixo habilita o  campo código
                txtSaiCodigo.setEnabled(true);
                // as linhas abaixo "limpam os campos"
                txtSaiCodigo.setText(null);
                txtSaiData.setText(null);
                txtSaiQtde.setText(null);
                txtSaiValor.setText(null);
                txtSaiCodProd.setText(null);
                txtSaiCodCli.setText(null);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para consultar entrada
    private void pesquisar_saida() {
        String sql = "select * from saida where codigo like?";
        try {
            // passando o conteúdo da caixa de pesquisa para o ?
            //atenção ao "%" que é a continuação da String sql
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtSaiPesquisar.getText() + "%");
            // a linha abaixo usa a biblioteca rs2xml.jar par preencher a tabela
            rs = pst.executeQuery();
            tblSaida.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para setar os campos do formulário com o conteúdo da tabela
    public void setar_campos() {
        // a linha abaixo desabilita o campo código
        txtSaiCodigo.setEnabled(false);
        int setar = tblSaida.getSelectedRow();
        txtSaiCodigo.setText(tblSaida.getModel().getValueAt(setar, 0).toString());
        txtSaiData.setText(tblSaida.getModel().getValueAt(setar, 1).toString());
        txtSaiQtde.setText(tblSaida.getModel().getValueAt(setar, 2).toString());
        txtSaiValor.setText(tblSaida.getModel().getValueAt(setar, 3).toString());
        txtSaiCodCli.setText(tblSaida.getModel().getValueAt(setar, 4).toString());
        txtSaiCodProd.setText(tblSaida.getModel().getValueAt(setar, 5).toString());

        // a linha abaixo desabilitra o botão adicionar
        btnSaiAdicionar.setEnabled(false);

    }

    private void pesquisar_cliente() {
        String sql = "select codigo, nome  from cliente";
        rbtCli.equals("Cliente");
        rbtCli.setSelected(true);
        try {

            pst = conexao.prepareStatement(sql);
            // a linha abaixo usa a biblioteca rs2xml.jar par preencher a tabela
            rs = pst.executeQuery();
            tblCliProd.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void pesquisar_produto() {

        String sql = "select codigo, descricao from produto";
        rbtProd.equals("Produto");
        rbtProd.setSelected(true);
        try {
            // passando o conteúdo da caixa de pesquisa para o ?
            //atenção ao "%" que é a continuação da String sql
            pst = conexao.prepareStatement(sql);
            // a linha abaixo usa a biblioteca rs2xml.jar par preencher a tabela
            rs = pst.executeQuery();
            tblCliProd.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void setar_campos_cliente_produto() {
        int setar = tblCliProd.getSelectedRow();
        if (rbtCli.isSelected()) {
            txtSaiCodCli.setText(tblCliProd.getModel().getValueAt(setar, 0).toString());
        } else {
            txtSaiCodProd.setText(tblCliProd.getModel().getValueAt(setar, 0).toString());
        }
    }

    // método para adicionar produto
    private void adicionar() {
        String sql = "insert into saida (codigo,qtde,valor,cliente,produto) values(?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtSaiCodigo.getText());
            pst.setString(2, txtSaiQtde.getText());
            pst.setString(3, txtSaiValor.getText().replace(",", "."));
            pst.setString(4, txtSaiCodCli.getText());
            pst.setString(5, txtSaiCodProd.getText());
// validação dos campos obrigatórios
            if ((txtSaiCodigo.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

// a linha abaixo atualiza a tabela fornecedores com os dados do formulário
                // a estrutura abaixo é usada para confirmar a inserção dos dados na tabela abaixo
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Saída registrada");
                    txtSaiCodigo.setText(null);
                    txtSaiData.setText(null);
                    txtSaiQtde.setText(null);
                    txtSaiValor.setText(null);
                    txtSaiCodProd.setText(null);
                    txtSaiCodCli.setText(null);
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
        String sql = "update saida set qtde=?, valor=?, fornecedor=?,produto=? where codigo=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtSaiQtde.getText());
            pst.setString(2, txtSaiValor.getText());
            pst.setString(3, txtSaiCodProd.getText());
            pst.setString(4, txtSaiCodCli.getText());
            pst.setString(5, txtSaiCodigo.getText());

            // a linha abaixo atualiza a tabela entrada com os dados do formulário
            // a estrutura abaixo é usada para confirmar a alteração dos dados da entrada na tabela abaixo
            int alterado = pst.executeUpdate();

            // a linha abaixo serve de apoio ao entendimento da lógica
            //System.out.println(adicionado);
            if (alterado > 0) {
                JOptionPane.showMessageDialog(null, "Dados da Saída alterados com sucesso");
                txtSaiCodigo.setText(null);
                txtSaiData.setText(null);
                txtSaiQtde.setText(null);
                txtSaiValor.setText(null);
                txtSaiCodProd.setText(null);
                txtSaiCodCli.setText(null);
                // a linha abaixo habilita o campo código
                txtSaiCodigo.setEnabled(true);
            }

        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(null, "Quantidade inválida");
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }
    }
    // método responsável pela remoção de Produtos

    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover esta saída?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from saida where codigo =?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtSaiCodigo.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Saída removida com sucesso");
                    txtSaiCodigo.setText(null);
                    txtSaiData.setText(null);
                    txtSaiQtde.setText(null);
                    txtSaiValor.setText(null);
                    txtSaiCodProd.setText(null);
                    txtSaiCodCli.setText(null);

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }
        // a linha abaixo habilita o campo código
        txtSaiCodigo.setEnabled(true);
        // a linha abaixo habilita o botão adicionar
        btnSaiAdicionar.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSaiCodigo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtSaiQtde = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtSaiCodProd = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtSaiData = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtSaiValor = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtSaiCodCli = new javax.swing.JTextField();
        btnEntLimparCampos = new javax.swing.JButton();
        btnSaiAdicionar = new javax.swing.JButton();
        btnSaiPesquisar = new javax.swing.JButton();
        btnEntAlterar = new javax.swing.JButton();
        btnSaiExcluir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSaida = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtSaiPesquisar = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCliProd = new javax.swing.JTable();
        rbtProd = new javax.swing.JRadioButton();
        rbtCli = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Saída");
        setPreferredSize(new java.awt.Dimension(670, 480));

        jLabel2.setText("* Campos Obrigatórios");

        jLabel3.setText("* Código:");

        jLabel5.setText("Quantidade:");

        txtSaiQtde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaiQtdeActionPerformed(evt);
            }
        });

        jLabel1.setText("Código do Produto:");

        txtSaiCodProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaiCodProdActionPerformed(evt);
            }
        });

        jLabel4.setText("Data:");

        txtSaiData.setEnabled(false);

        jLabel6.setText("Valor:");

        jLabel8.setText("Código do Cliente:");

        txtSaiCodCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaiCodCliActionPerformed(evt);
            }
        });

        btnEntLimparCampos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnEntLimparCampos.setText("Limpar Campos");
        btnEntLimparCampos.setToolTipText("Limpar Campos");
        btnEntLimparCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntLimparCamposActionPerformed(evt);
            }
        });

        btnSaiAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/create.png"))); // NOI18N
        btnSaiAdicionar.setToolTipText("Adicionar");
        btnSaiAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnSaiAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaiAdicionarActionPerformed(evt);
            }
        });

        btnSaiPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/read.png"))); // NOI18N
        btnSaiPesquisar.setToolTipText("Pesquisar");
        btnSaiPesquisar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnSaiPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaiPesquisarActionPerformed(evt);
            }
        });

        btnEntAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/update.png"))); // NOI18N
        btnEntAlterar.setToolTipText("Alterar");
        btnEntAlterar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnEntAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntAlterarActionPerformed(evt);
            }
        });

        btnSaiExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/delete.png"))); // NOI18N
        btnSaiExcluir.setToolTipText("Excluir");
        btnSaiExcluir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnSaiExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaiExcluirActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Saída"));
        jPanel2.setPreferredSize(new java.awt.Dimension(280, 180));

        tblSaida.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Data", "Qtde", "Valor", "Cliente", "Produto"
            }
        ));
        tblSaida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSaidaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblSaida);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pesquisar.png"))); // NOI18N

        txtSaiPesquisar.setToolTipText("Pesquisar Código");
        txtSaiPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaiPesquisarActionPerformed(evt);
            }
        });
        txtSaiPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSaiPesquisarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtSaiPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtSaiPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente/Produto"));
        jPanel1.setPreferredSize(new java.awt.Dimension(280, 180));

        tblCliProd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "", ""
            }
        ));
        tblCliProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCliProdMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCliProd);

        buttonGroup1.add(rbtProd);
        rbtProd.setText("Produto");
        rbtProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtProdActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtCli);
        rbtCli.setText("Cliente");
        rbtCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtCliActionPerformed(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pesquisar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtCli)
                .addGap(53, 53, 53)
                .addComponent(rbtProd)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbtCli)
                        .addComponent(rbtProd))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSaiQtde))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtSaiCodigo))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSaiCodCli, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(279, 279, 279))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSaiValor, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtSaiCodProd, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(btnEntLimparCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtSaiData, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47))))
            .addGroup(layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(btnSaiAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(btnSaiPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnEntAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(btnSaiExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)))
                .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtSaiCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSaiData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtSaiValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(txtSaiQtde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEntLimparCampos)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(txtSaiCodCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)
                                .addComponent(txtSaiCodProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnSaiAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSaiPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnSaiExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnEntAlterar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        setBounds(0, 0, 670, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSaiQtdeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaiQtdeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaiQtdeActionPerformed

    private void txtSaiCodProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaiCodProdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaiCodProdActionPerformed

    private void txtSaiCodCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaiCodCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaiCodCliActionPerformed

    private void btnEntLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntLimparCamposActionPerformed
        //método para limpar os campos manualmente
        limpar_campos();
    }//GEN-LAST:event_btnEntLimparCamposActionPerformed

    private void btnSaiAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaiAdicionarActionPerformed
        // chamando o método adicionar Produto
        adicionar();
    }//GEN-LAST:event_btnSaiAdicionarActionPerformed

    private void btnSaiPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaiPesquisarActionPerformed
        //chamando o método consultar
        consultar();
    }//GEN-LAST:event_btnSaiPesquisarActionPerformed

    private void btnEntAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntAlterarActionPerformed
        // chamando o método alterar Produto
        alterar();

    }//GEN-LAST:event_btnEntAlterarActionPerformed

    private void btnSaiExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaiExcluirActionPerformed
        // chamando o método para remover Produto
        remover();

    }//GEN-LAST:event_btnSaiExcluirActionPerformed

    private void tblSaidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSaidaMouseClicked
        // chamando o mpetodo para setar os campos
        setar_campos();
    }//GEN-LAST:event_tblSaidaMouseClicked

    private void tblCliProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCliProdMouseClicked
        // chamando o mpetodo para setar os campos
        setar_campos_cliente_produto();
    }//GEN-LAST:event_tblCliProdMouseClicked

    private void rbtProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtProdActionPerformed
        pesquisar_produto();
    }//GEN-LAST:event_rbtProdActionPerformed

    private void rbtCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtCliActionPerformed
        // // chamando o método para pesquisar a lista de fornecedores
        pesquisar_cliente();
    }//GEN-LAST:event_rbtCliActionPerformed

    private void txtSaiPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaiPesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaiPesquisarActionPerformed

    private void txtSaiPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSaiPesquisarKeyReleased
        // chamar o método pesquisar saída
        pesquisar_saida();
    }//GEN-LAST:event_txtSaiPesquisarKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntAlterar;
    private javax.swing.JButton btnEntLimparCampos;
    private javax.swing.JButton btnSaiAdicionar;
    private javax.swing.JButton btnSaiExcluir;
    private javax.swing.JButton btnSaiPesquisar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rbtCli;
    private javax.swing.JRadioButton rbtProd;
    private javax.swing.JTable tblCliProd;
    private javax.swing.JTable tblSaida;
    private javax.swing.JTextField txtSaiCodCli;
    private javax.swing.JTextField txtSaiCodProd;
    private javax.swing.JTextField txtSaiCodigo;
    private javax.swing.JTextField txtSaiData;
    private javax.swing.JTextField txtSaiPesquisar;
    private javax.swing.JTextField txtSaiQtde;
    private javax.swing.JTextField txtSaiValor;
    // End of variables declaration//GEN-END:variables
}
