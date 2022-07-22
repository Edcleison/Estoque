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

public class TelaEntrada extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaEntrada
     */
    public TelaEntrada() {

        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void limpar_campos() {
        txtEntCodigo.setText(null);
        txtEntData.setText(null);
        txtEntQtde.setText(null);
        txtEntValor.setText(null);
        txtEntCodForn.setText(null);
        txtEntCodProd.setText(null);
        // a linha abaixo habilita o campo código
        txtEntCodigo.setEnabled(true);
        // a linha abaixo habilita o botão adicionar
        btnEntAdicionar.setEnabled(true);
    }

    private void consultar() {
        // a linha abaixo desabilita o campo código
        txtEntCodigo.setEnabled(false);
        String sql = "select * from entrada where codigo =?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtEntCodigo.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtEntData.setText(rs.getString(2));
                txtEntQtde.setText(rs.getString(3));
                txtEntValor.setText(rs.getString(4));
                txtEntCodForn.setText(rs.getString(5));
                txtEntCodProd.setText(rs.getString(5));

            } else {
                JOptionPane.showMessageDialog(null, "Entrada não cadastrada");
                // a linha abaixo habilita o  campo código
                txtEntCodigo.setEnabled(true);
                // as linhas abaixo "limpam os campos"
                txtEntCodigo.setText(null);
                txtEntData.setText(null);
                txtEntQtde.setText(null);
                txtEntValor.setText(null);
                txtEntCodForn.setText(null);
                txtEntCodProd.setText(null);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para consultar entrada
    private void pesquisar_entrada() {
        String sql = "select *  from entrada where codigo like ?";
        try {
            // passando o conteúdo da caixa de pesquisa para o ?
            //atenção ao "%" que é a continuação da String sql
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtEntPesquisar.getText() + "%");
            // a linha abaixo usa a biblioteca rs2xml.jar par preencher a tabela
            rs = pst.executeQuery();
            tblEntrada.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para setar os campos do formulário com o conteúdo da tabela
    public void setar_campos() {
        // a linha abaixo desabilita o campo código
        txtEntCodigo.setEnabled(false);
        int setar = tblEntrada.getSelectedRow();
        txtEntCodigo.setText(tblEntrada.getModel().getValueAt(setar, 0).toString());
        txtEntData.setText(tblEntrada.getModel().getValueAt(setar, 1).toString());
        txtEntQtde.setText(tblEntrada.getModel().getValueAt(setar, 2).toString());
        txtEntValor.setText(tblEntrada.getModel().getValueAt(setar, 3).toString());
        txtEntCodForn.setText(tblEntrada.getModel().getValueAt(setar, 4).toString());
        txtEntCodProd.setText(tblEntrada.getModel().getValueAt(setar, 5).toString());

        // a linha abaixo desabilitra o botão adicionar
        btnEntAdicionar.setEnabled(false);

    }

    private void pesquisar_fornecedor() {
        String sql = "select codigo, nome  from fornecedor";
        rbtForn.equals("Fornecedor");
        rbtForn.setSelected(true);
        try {

            pst = conexao.prepareStatement(sql);
            // a linha abaixo usa a biblioteca rs2xml.jar par preencher a tabela
            rs = pst.executeQuery();
            tblFornProd.setModel(DbUtils.resultSetToTableModel(rs));
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
            tblFornProd.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void setar_campos_fornecedor_produto() {
        int setar = tblFornProd.getSelectedRow();
        if (rbtForn.isSelected()) {
            txtEntCodForn.setText(tblFornProd.getModel().getValueAt(setar, 0).toString());
        } else {
            txtEntCodProd.setText(tblFornProd.getModel().getValueAt(setar, 0).toString());
        }
    }

    // método para adicionar produto
    private void adicionar() {
        String sql = "insert into entrada(codigo,qtde,valor,fornecedor,produto) values(?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtEntCodigo.getText());
            pst.setString(2, txtEntQtde.getText());
            pst.setString(3, txtEntValor.getText().replace(",", "."));
            pst.setString(4, txtEntCodForn.getText());
            pst.setString(5, txtEntCodProd.getText());
// validação dos campos obrigatórios
            if ((txtEntCodigo.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
// a linha abaixo atualiza a tabela fornecedores com os dados do formulário
                // a estrutura abaixo é usada para confirmar a inserção dos dados na tabela abaixo
                int adicionado = pst.executeUpdate();

                // a linha abaixo serve de apoio ao entendimento da lógica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Entrada registrada");
                    txtEntCodigo.setText(null);
                    txtEntData.setText(null);
                    txtEntQtde.setText(null);
                    txtEntValor.setText(null);
                    txtEntCodForn.setText(null);
                    txtEntCodProd.setText(null);
                }
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Código inválido, digite um código valido!");
        } catch (java.sql.SQLException e2) {
            JOptionPane.showMessageDialog(null, "Código e (ou) quantidade inválido(s)");
        } catch (Exception e3) {
            JOptionPane.showMessageDialog(null, e3);
        }

    }

    private void alterar() {
        String sql = "update entrada set qtde=?, valor=?, fornecedor=?,produto=? where codigo=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtEntQtde.getText());
            pst.setString(2, txtEntValor.getText());
            pst.setString(3, txtEntCodForn.getText());
            pst.setString(4, txtEntCodProd.getText());
            pst.setString(5, txtEntCodigo.getText());
            if ((txtEntCodigo.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                // a linha abaixo atualiza a tabela entrada com os dados do formulário
                // a estrutura abaixo é usada para confirmar a alteração dos dados da entrada na tabela abaixo
                int alterado = pst.executeUpdate();

                if (alterado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados da Entrada alterados com sucesso");
                    txtEntCodigo.setText(null);
                    txtEntData.setText(null);
                    txtEntQtde.setText(null);
                    txtEntValor.setText(null);
                    txtEntCodForn.setText(null);
                    txtEntCodProd.setText(null);
                    // a linha abaixo habilita o botão adicionar
                    btnEntAdicionar.setEnabled(true);
                    // a linha abaixo habilita o campo código
                    txtEntCodigo.setEnabled(true);
                }
            }

        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(null, "Quantidade inválida");
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }
    }

    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover esta entrada?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from entrada where codigo =?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtEntCodigo.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Entrada removida com sucesso");
                    txtEntCodigo.setText(null);
                    txtEntData.setText(null);
                    txtEntQtde.setText(null);
                    txtEntValor.setText(null);
                    txtEntCodForn.setText(null);
                    txtEntCodProd.setText(null);

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }
        // a linha abaixo habilita o campo código
        txtEntCodigo.setEnabled(true);
        // a linha abaixo habilita o botão adicionar
        btnEntAdicionar.setEnabled(true);
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
        txtEntCodigo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtEntQtde = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtEntCodForn = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtEntCodProd = new javax.swing.JTextField();
        btnEntAdicionar = new javax.swing.JButton();
        btnEntPesquisar = new javax.swing.JButton();
        btnEntAlterar = new javax.swing.JButton();
        btnEntExcluir = new javax.swing.JButton();
        btnEntLimparCampos = new javax.swing.JButton();
        txtEntValor = new javax.swing.JTextField();
        txtEntData = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFornProd = new javax.swing.JTable();
        rbtProd = new javax.swing.JRadioButton();
        rbtForn = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEntrada = new javax.swing.JTable();
        txtEntPesquisar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Entrada");
        setPreferredSize(new java.awt.Dimension(670, 480));

        jLabel2.setText("* Campos Obrigatórios");

        jLabel3.setText("* Código:");

        jLabel4.setText("Data:");

        jLabel5.setText("Quantidade:");

        txtEntQtde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntQtdeActionPerformed(evt);
            }
        });

        jLabel6.setText("Valor:");

        jLabel1.setText("Código do Fornecedor:");

        txtEntCodForn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntCodFornActionPerformed(evt);
            }
        });

        jLabel8.setText("Código do Produto:");

        txtEntCodProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntCodProdActionPerformed(evt);
            }
        });

        btnEntAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/create.png"))); // NOI18N
        btnEntAdicionar.setToolTipText("Adicionar");
        btnEntAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnEntAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntAdicionarActionPerformed(evt);
            }
        });

        btnEntPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/read.png"))); // NOI18N
        btnEntPesquisar.setToolTipText("Pesquisar");
        btnEntPesquisar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnEntPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntPesquisarActionPerformed(evt);
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

        btnEntExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/delete.png"))); // NOI18N
        btnEntExcluir.setToolTipText("Excluir");
        btnEntExcluir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnEntExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntExcluirActionPerformed(evt);
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

        txtEntData.setEnabled(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Fornecedor/Produto"));
        jPanel1.setPreferredSize(new java.awt.Dimension(280, 180));

        tblFornProd.setModel(new javax.swing.table.DefaultTableModel(
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
        tblFornProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFornProdMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblFornProd);

        buttonGroup1.add(rbtProd);
        rbtProd.setText("Produto");
        rbtProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtProdActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtForn);
        rbtForn.setText("Fornecedor");
        rbtForn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtFornActionPerformed(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pesquisar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbtForn)
                        .addGap(30, 30, 30)
                        .addComponent(rbtProd))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbtForn)
                        .addComponent(rbtProd))
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Entrada"));
        jPanel2.setPreferredSize(new java.awt.Dimension(280, 180));

        tblEntrada.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Data", "Qtde", "Valor", "Fornecedor", "Produto"
            }
        ));
        tblEntrada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEntradaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEntrada);

        txtEntPesquisar.setToolTipText("Pesquisar Código");
        txtEntPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntPesquisarActionPerformed(evt);
            }
        });
        txtEntPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEntPesquisarKeyReleased(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pesquisar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtEntPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 14, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEntPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(btnEntAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnEntPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnEntAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnEntExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtEntCodForn, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtEntCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                            .addComponent(txtEntQtde))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEntValor, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEntData, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtEntCodProd, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEntLimparCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(64, 64, 64))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtEntCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtEntData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEntQtde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(txtEntValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEntCodForn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtEntCodProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(btnEntLimparCampos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnEntExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEntAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEntPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEntAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63))
        );

        setBounds(0, 0, 670, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void txtEntQtdeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntQtdeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntQtdeActionPerformed

    private void txtEntCodFornActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntCodFornActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntCodFornActionPerformed

    private void txtEntCodProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntCodProdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntCodProdActionPerformed

    private void btnEntAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntAdicionarActionPerformed
        // chamando o método adicionar Produto
        adicionar();
    }//GEN-LAST:event_btnEntAdicionarActionPerformed

    private void btnEntPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntPesquisarActionPerformed
        //chamando o método consultar
        consultar();
    }//GEN-LAST:event_btnEntPesquisarActionPerformed

    private void btnEntAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntAlterarActionPerformed
        // chamando o método alterar Produto
        alterar();

    }//GEN-LAST:event_btnEntAlterarActionPerformed

    private void btnEntExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntExcluirActionPerformed
        // chamando o método para remover Produto
        remover();

    }//GEN-LAST:event_btnEntExcluirActionPerformed

    private void btnEntLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntLimparCamposActionPerformed
        //método para limpar os campos manualmente
        limpar_campos();
    }//GEN-LAST:event_btnEntLimparCamposActionPerformed

    private void tblFornProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFornProdMouseClicked
        // chamando o mpetodo para setar os campos
        setar_campos_fornecedor_produto();
    }//GEN-LAST:event_tblFornProdMouseClicked

    private void tblEntradaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEntradaMouseClicked
        // chamando o mpetodo para setar os campos
        setar_campos();
    }//GEN-LAST:event_tblEntradaMouseClicked

    private void txtEntPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntPesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntPesquisarActionPerformed

    private void txtEntPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntPesquisarKeyReleased
        // chamar o método pesquisar entrada
        pesquisar_entrada();

    }//GEN-LAST:event_txtEntPesquisarKeyReleased

    private void rbtFornActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtFornActionPerformed
        // // chamando o método para pesquisar a lista de fornecedores
        pesquisar_fornecedor();
    }//GEN-LAST:event_rbtFornActionPerformed

    private void rbtProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtProdActionPerformed
        pesquisar_produto();
    }//GEN-LAST:event_rbtProdActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntAdicionar;
    private javax.swing.JButton btnEntAlterar;
    private javax.swing.JButton btnEntExcluir;
    private javax.swing.JButton btnEntLimparCampos;
    private javax.swing.JButton btnEntPesquisar;
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
    private javax.swing.JRadioButton rbtForn;
    private javax.swing.JRadioButton rbtProd;
    private javax.swing.JTable tblEntrada;
    private javax.swing.JTable tblFornProd;
    private javax.swing.JTextField txtEntCodForn;
    private javax.swing.JTextField txtEntCodProd;
    private javax.swing.JTextField txtEntCodigo;
    private javax.swing.JTextField txtEntData;
    private javax.swing.JTextField txtEntPesquisar;
    private javax.swing.JTextField txtEntQtde;
    private javax.swing.JTextField txtEntValor;
    // End of variables declaration//GEN-END:variables
}
