/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularioInicial;

import BD.BD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author andre
 */
public class PesquisarPaciente extends javax.swing.JFrame {

    /**
     * Creates new form PesquisarPaciente
     */
    public PesquisarPaciente() {
        initComponents();
        fecharJanela();
        
        tabela_paciente.setModel(new javax.swing.table.DefaultTableModel(
            
                new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "RG", "DataNasc"
            }
        ){@Override
        public boolean isCellEditable(int row, int column) {
        //all cells false
            return false;
        }});
                    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabela_paciente = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pesquisar = new javax.swing.JButton();
        nome = new javax.swing.JTextField();
        RG = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        tabela_paciente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "RG", "DataNasc"
            }
        ));
        tabela_paciente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabela_pacienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabela_paciente);

        jLabel1.setText("Nome");

        jLabel2.setText("RG");

        pesquisar.setText("Pesquisar");
        pesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pesquisarActionPerformed(evt);
            }
        });

        jButton1.setText("Sair");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(67, 67, 67)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(RG, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(nome)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(pesquisar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(RG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pesquisar)
                    .addComponent(jButton1))
                .addGap(35, 35, 35))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pesquisarActionPerformed
        // TODO add your handling code here:
        
        pesquisarPaciente();
    }//GEN-LAST:event_pesquisarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(Variaveis.exame == 1){
            int aux = Funcoes.voltar("Deseja voltar?","SIM","NÃO");
            if (aux == 1){
                this.dispose();
            }
        }else{
            int aux = Funcoes.voltar("Deseja voltar ao menu de opções?","SIM","NÃO");
            if (aux == 1){
                new MenuOpcoes().setVisible(true);
                this.dispose();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tabela_pacienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabela_pacienteMouseClicked
        // TODO add your handling code here:
        if(Variaveis.pesqPac == 0){
            new MenuOpcoes().setVisible(true);
        }else{
            Variaveis.idPacientePesquisa = tabela_paciente.getValueAt(tabela_paciente.getSelectedRow(), 0).toString();
            Variaveis.clickouPesquisa = 1;
        }
        this.dispose();
    }//GEN-LAST:event_tabela_pacienteMouseClicked
    private void fecharJanela(){
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
    }
        private void exitForm(java.awt.event.WindowEvent evt) {
        /*Variaveis.voltar = 0;
        Variaveis.fechar = 0;
        new ConfirmarSaida().setVisible(true);*/
        if(Variaveis.exame == 1){
            int aux = Funcoes.voltar("Deseja voltar?","SIM","NÃO");
            if (aux == 1){
                this.dispose();
            }
        }else{
            int aux = Funcoes.voltar("Deseja voltar ao menu de opções?","SIM","NÃO");
            if (aux == 1){
                new MenuOpcoes().setVisible(true);
                this.dispose();
            }
        }        
    }
    
    BD con = new BD();
    public void pesquisarPaciente(){
        try{
            Connection c = con.conectar();
            if(nome.getText().equals("") && RG.getText().equals("")){
                JOptionPane.showMessageDialog(null,"Preencha ao menos um campo de pesquisa");
            }else{
                if(nome.getText().equals("")){
                    PreparedStatement pesquisarStmt = c.prepareStatement("Select idPaciente, nome, dataNasc, RG From paciente where RG = ?");
                    pesquisarStmt.setString(1, RG.getText());
                    ResultSet rs = pesquisarStmt.executeQuery();
                    
                    
                    
                    DefaultTableModel model;
                    model = (DefaultTableModel) tabela_paciente.getModel();
                    model.setNumRows(0);
                    if(rs == null){
                        JOptionPane.showMessageDialog(null, "Registro não encontrado");    
                        }else{
                        while(rs.next()){
                                model.addRow(
                                    new Object[]{
                                        rs.getString("idPaciente"),
                                        rs.getString("Nome"),
                                        rs.getString("RG"),
                                        rs.getString("dataNasc"),
                                    }
                                );
                            }
                            
                        }
                }else{
                    if(RG.getText().equals("")){
                        PreparedStatement pesquisarStmt = c.prepareStatement("Select idPaciente, nome, dataNasc, RG From paciente where nome LIKE ?");
                        pesquisarStmt.setString(1,"%"+ nome.getText()+"%");
                        ResultSet rs = pesquisarStmt.executeQuery();
                        DefaultTableModel model;
                        model = (DefaultTableModel) tabela_paciente.getModel();
                        model.setNumRows(0);
                        if(rs == null){
                            JOptionPane.showMessageDialog(null, "Registro não encontrado");    
                        }else{
                            while(rs.next()){
                                model.addRow(
                                    new Object[]{
                                        rs.getString("idPaciente"),
                                        rs.getString("Nome"),
                                        rs.getString("RG"),
                                        rs.getString("dataNasc"),
                                    }
                                );
                            }
                            
                        }
                    }else{
                        PreparedStatement pesquisarStmt = c.prepareStatement("Select idPaciente, nome, dataNasc, RG From paciente where nome LIKE ? and RG = ?");
                        pesquisarStmt.setString(1,"%"+ nome.getText()+"%");
                        pesquisarStmt.setString(2, RG.getText());
                        ResultSet rs = pesquisarStmt.executeQuery();
                        DefaultTableModel model;
                        model = (DefaultTableModel) tabela_paciente.getModel();
                        model.setNumRows(0);
                        if(rs == null){
                            JOptionPane.showMessageDialog(null, "Registro não encontrado");    
                        }else{
                            while(rs.next()){
                                model.addRow(
                                    new Object[]{
                                        rs.getString("idPaciente"),
                                        rs.getString("Nome"),
                                        rs.getString("RG"),
                                        rs.getString("dataNasc"),
                                    }
                                );
                            }
                            
                        }
                    }
            }      
            }
        }catch (SQLException e){
            //JOptionPane.showMessageDialog(null, "Registro não encontrado");
            //System.out.println("Ocorreu um erro ao conectar");
        }
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PesquisarPaciente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarPaciente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarPaciente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarPaciente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PesquisarPaciente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField RG;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nome;
    private javax.swing.JButton pesquisar;
    private javax.swing.JTable tabela_paciente;
    // End of variables declaration//GEN-END:variables
}
