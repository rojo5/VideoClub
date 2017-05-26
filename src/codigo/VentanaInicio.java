package codigo;

import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rojo5
 */
public class VentanaInicio extends javax.swing.JFrame {

    //Declaracion de variables
    Connection conexion;    //Almacena la conexion de la BBDD
    Statement estado;       //Almacena el estado de la conexion
    ResultSet resultado;    //Almacena el resultado de la consulta a la BBDD
    //ArrayList<String> listaUsuarios = new ArrayList();
    String usuario, password;
    int logueado=0;
    VentanaUsuario vUsuario = new VentanaUsuario();
   String [] datosUsuarios= new String[5];

    private void pedirUsuario() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //Indico los paquetes de conexion
            // IP cuando estoy en clase
            conexion = DriverManager.getConnection("jdbc:mysql://172.16.1.228/Metflix", "root", "rexct-7567");
            //IP cuando estoy en casa
            //conexion = DriverManager.getConnection("jdbc:mysql://192.168.1.13/Metflix", "root", "rexct-7567");
            
            //Realizo la conexcion
            estado = conexion.createStatement();

            //Realizo la consulta
            resultado = estado.executeQuery("SELECT * FROM Metflix.usuarios where DNI = "
                                            +usuario+" AND DNI = "+ password );
            
            resultado.last();
            logueado= resultado.getRow();
            
            datosUsuarios[0]= resultado.getString("DNI");
            datosUsuarios[1]= resultado.getString("Nombre");
            datosUsuarios[2]= resultado.getString("Apellido");
            datosUsuarios[3]= resultado.getString("Penalizacion");
            datosUsuarios[4]= resultado.getString("email");
            System.out.println("datps");
            

        } catch (ClassNotFoundException ex) {
            System.out.println("NO SE HA ENCONTRADO EL DRIVER");
        } catch (SQLException ex) {
            if(logueado==0){
                System.out.println("User no valido");
            }else{
             System.out.println("NO SE HA PODIDO CONECTAR");
            }
        }
    }

    /**
     * Creates new form VentanaInicio
     */
    public VentanaInicio() {
        
        initComponents();
        this.setTitle("METFLIX");
       
         
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tfUsuario = new javax.swing.JTextField();
        btnIniciar = new javax.swing.JButton();
        tfPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(213, 0, 0));

        logo.setBackground(new java.awt.Color(204, 0, 0));
        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/metflix.png"))); // NOI18N

        jLabel2.setText("Usuario");

        jLabel3.setText("Contraseña");

        tfUsuario.setText("5036787");

        btnIniciar.setText("Inciar sesión");
        btnIniciar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnIniciarMousePressed(evt);
            }
        });

        tfPassword.setText("5036787");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(167, 167, 167)
                        .addComponent(jLabel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tfPassword)
                    .addComponent(logo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(tfUsuario, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(61, 61, 61))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logo)
                .addGap(59, 59, 59)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIniciarMousePressed
        // TODO add your handling code here:
        
        usuario = tfUsuario.getText();
        password = String.valueOf(tfPassword.getPassword());
        pedirUsuario();
        if(logueado==1){
            System.out.println("Usuario logueado");
           
            vUsuario.jLabel1.setText("Bienvenida: "+datosUsuarios[1] + " " + datosUsuarios[2]);
            vUsuario.usuario= usuario;
            
            
            
           
            
            Image foto = (new ImageIcon(new ImageIcon(getClass().getResource("/fotosUsuarios/"+usuario + ".jpg"))
                .getImage().getScaledInstance(55, 63, Image.SCALE_DEFAULT))).getImage();
            
            ImageIcon fotoPerfil = new ImageIcon(foto);
            
            vUsuario.fotoPerfil.setIcon(fotoPerfil);
            
         
            vUsuario.setSize(800, 500);
            vUsuario.setVisible(true);
           this.setVisible(false);
           
           //DNI prueba 5036787
        }
       

    }//GEN-LAST:event_btnIniciarMousePressed

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
            java.util.logging.Logger.getLogger(VentanaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaInicio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel logo;
    private javax.swing.JPasswordField tfPassword;
    private javax.swing.JTextField tfUsuario;
    // End of variables declaration//GEN-END:variables
}
