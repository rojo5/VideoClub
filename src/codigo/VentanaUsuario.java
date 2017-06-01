/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

/**
 *
 *
 * @author rojo5
 */
public class VentanaUsuario extends javax.swing.JFrame {

    String usuario;
    String idUser;
    //Declaracion de variables
    Connection conexion;    //Almacena la conexion de la BBDD
    Statement estado;       //Almacena el estado de la conexion
    Statement alquiler;
    ResultSet resultado;    //Almacena el resultado de la consulta a la BBDD
    ResultSet resulClasificacion;
    ResultSet buscador;
    ResultSet alquilar;
    ResultSet resulPeliAlquiladas;
    ArrayList<String[]> datosPelis = new ArrayList();
    ArrayList<String[]> clasiPelis = new ArrayList();
    ArrayList<String[]> buscaPelis = new ArrayList();
    ArrayList<String[]> alquiPelis = new ArrayList();
    int contador = 0;
    int contador2 = 0;
    int contador3 = 0;
    
    int sumatorio = 0;
    int totalPelis;
    String auxiliar;
    boolean chivato = true;
    JLabel label;
    JLabel label2;
    JLabel label3;
    JLabel labelAlquilado;
    JLabel labelFecha;
    Toolkit t = Toolkit.getDefaultToolkit();
    Dimension pantalla = t.getScreenSize();

    public void cargaElementos() {

        usuario = fotoPerfil.getText();

        Image flechaDer = (new ImageIcon(new ImageIcon(getClass().getResource("/iconos/derecha.png"))
                .getImage().getScaledInstance(56, 80, Image.SCALE_DEFAULT))).getImage();

        ImageIcon derechaEscalada = new ImageIcon(flechaDer);

        derecha.setIcon(derechaEscalada);
        derecha.updateUI();
        derecha1.setIcon(derechaEscalada);
        derecha1.updateUI();

        Image flechaIzq = (new ImageIcon(new ImageIcon(getClass().getResource("/iconos/izquierda.png"))
                .getImage().getScaledInstance(46, 80, Image.SCALE_DEFAULT))).getImage();

        ImageIcon izquierdaEscalada = new ImageIcon(flechaIzq);

        izquierda.setIcon(izquierdaEscalada);
        izquierda.updateUI();
        izquierda1.setIcon(izquierdaEscalada);
        izquierda1.updateUI();

        generaCaratula(0);  //El 0 es de posicion de coordena X por la que empieza
        generaCaratulasClasi(0);  //El 0 es de posicion de coordena X por la que empieza
        System.out.println("el user es: " + usuario);

    }

    public void cargaPelis() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            //Indico los paquetes de conexion
            // IP cuando estoy en clase
            //conexion = DriverManager.getConnection("jdbc:mysql://172.16.1.228/Metflix", "root", "rexct-7567");
            //IP cuando estoy en casa
            conexion = DriverManager.getConnection("jdbc:mysql://192.168.1.13/Metflix", "root", "rexct-7567");

            //Realizo la conexcion
            estado = conexion.createStatement();

            //Realizo la consulta
            resultado = estado.executeQuery("SELECT * FROM Metflix.peliculas");

            while (resultado.next()) {
                String[] aux = new String[8];
                aux[0] = resultado.getString("id_pelicula");
                aux[1] = resultado.getString("titulo");
                aux[2] = resultado.getString("año");
                aux[3] = resultado.getString("pais");
                aux[4] = resultado.getString("genero");
                aux[5] = resultado.getString("imdb");
                aux[6] = resultado.getString("clasificacion_imdb");
                aux[7] = resultado.getString("resumen");
                datosPelis.add(aux);
            }
            totalPelis = datosPelis.size();

            resulClasificacion = estado.executeQuery("SELECT * FROM Metflix.peliculas ORDER BY `peliculas`.`clasificacion_imdb` DESC");
            while (resulClasificacion.next()) {
                String[] aux2 = new String[8];
                aux2[0] = resulClasificacion.getString("id_pelicula");
                aux2[1] = resulClasificacion.getString("titulo");
                aux2[2] = resulClasificacion.getString("año");
                aux2[3] = resulClasificacion.getString("pais");
                aux2[4] = resulClasificacion.getString("genero");
                aux2[5] = resulClasificacion.getString("imdb");
                aux2[6] = resulClasificacion.getString("clasificacion_imdb");
                aux2[7] = resulClasificacion.getString("resumen");
                clasiPelis.add(aux2);

            }

            System.out.println("todo perfecto");
        } catch (ClassNotFoundException ex) {
            System.out.println("NO SE HA ENCONTRADO EL DRIVER");
        } catch (SQLException ex) {

            System.out.println("NO SE HA PODIDO CONECTAR");

        }

    }

    public void comprobarAlquiler() {
        
        try {
            resulPeliAlquiladas = estado.executeQuery("SELECT * FROM `prestamos` WHERE `DNIUsuario`= '" + idUser + "' ORDER BY `FechaDevolucion` ASC");
            while (resulPeliAlquiladas.next()) {
                String[] aux3 = new String[5];
                aux3[0] = resulPeliAlquiladas.getString("id_pelicula");
                aux3[1] = resulPeliAlquiladas.getString("NumeroEjemplar");
                aux3[2] = resulPeliAlquiladas.getString("DNIUsuario");
                aux3[3] = resulPeliAlquiladas.getString("FechaPrestamo");
                aux3[4] = resulPeliAlquiladas.getString("FechaDevolucion");
                alquiPelis.add(aux3);
                System.out.println("me quedo aqui");
            }
        } catch (SQLException ex) {
            Logger.getLogger(VentanaUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void pelisAlquiladas() {
        int contador4 = 0;
        String peli;
        String titulo = "";
        int altura = 5;

        jLabel18.setText(String.valueOf(alquiPelis.size()));

        while (contador4 < alquiPelis.size()) {
            peli = alquiPelis.get(contador4)[0];

            for (int i = 0; i < datosPelis.size(); i++) {
                if (peli.equals(datosPelis.get(i)[0])) {
                    titulo = datosPelis.get(i)[1];
                    break;
                }
            }

            labelAlquilado = new JLabel();
            labelAlquilado.setFont(new java.awt.Font("calibri", 1, 13));
            labelAlquilado.setText(titulo);
            labelAlquilado.setBounds(5, altura, 300, 20);
            labelAlquilado.setVisible(true);
            jPanel7.add(labelAlquilado);

            labelFecha = new JLabel();
            labelFecha.setFont(new java.awt.Font("calibri", 1, 13));
            labelFecha.setText(alquiPelis.get(contador4)[4]);
            labelFecha.setBounds(320, altura, 70, 20);
            labelFecha.setVisible(true);
            jPanel7.add(labelFecha);

            altura += 20;
            contador4++;
        }
        jPanel7.setPreferredSize(new Dimension(993, altura + 10));
    }

    public void generaCaratula(int _posX) {
        String nomCaratula;

        int posX = _posX;
        int anchoCaratula = 80;

        for (int u = 0; u < 7; u++) {

            if (contador < totalPelis) {
                label = new JLabel();
                nomCaratula = datosPelis.get(contador)[0];

                //Se añaden los labels al jPanel3
                label.setBounds(posX, 1, 80, 140);
                //label.setIcon(adaptaCaratulas(nomCaratula));
                label.setText(datosPelis.get(contador)[0]);
                label.setVisible(true);
                //peliDatos(Integer.valueOf(label.getText()));
                //peliDatos(1);

                jPanel3.add(label);
                jPanel3.updateUI();

                posX += anchoCaratula + 15;

            }
            contador++;
        }
    }

    public void generaCaratulasClasi(int _posX) {
        String nomCaratula;
        int posX = _posX;
        int anchoCaratula = 80;

        for (int u = 0; u < 7; u++) {
            if (contador2 < clasiPelis.size()) {
                label2 = new JLabel();
                nomCaratula = clasiPelis.get(contador2)[0];

                label2.setBounds(posX, 1, 80, 140);
                label2.setIcon(adaptaCaratulas(nomCaratula));
                label2.setText(clasiPelis.get(contador2)[0]);
                label2.setVisible(true);

                jPanel4.add(label2);
                jPanel4.updateUI();
                posX += anchoCaratula + 15;
            }
            contador2++;
        }
    }

    public void generaCaratulasBuscadas(int _posX) {
        String nomCaratula;
        int posX = _posX;
        int anchoCaratula = 80;
        int posVertical = 1;

        while (contador3 < buscaPelis.size()) {
            for (int u = 0; u < 8; u++) {
                if (contador3 < buscaPelis.size()) {
                    label3 = new JLabel();
                    nomCaratula = buscaPelis.get(contador3)[0];
                    label3.setBounds(posX, posVertical, 80, 140);
                    label3.setIcon(adaptaCaratulas(nomCaratula));
                    label3.setText(buscaPelis.get(contador3)[0]);
                    label3.setVisible(true);
                    jPanel5.add(label3);
                    jPanel5.updateUI();
                    posX += anchoCaratula + 15;
                }
                contador3++;
            }
            posVertical += 140 + 15;
            sumatorio = posVertical;
            posX = _posX;

        }
        //  label3 = new JLabel();
        //label3.setBounds(posX, posVertical,80,140);

    }

    public void alquilarPeli(int _idPelicula, int _numeroEjemplar, int _dni, String _fechaPrestamo, String _fechaDevolucion) {

        try {

            alquiler = conexion.createStatement();

            String introduceDatos = "INSERT INTO Metflix.prestamos (id_pelicula, NumeroEjemplar, DNIUsuario, FechaPrestamo,"
                    + "FechaDevolucion) VALUES ('" + _idPelicula + "', '" + _numeroEjemplar + "', '" + _dni + "', '" + _fechaPrestamo + "', '" + _fechaDevolucion + "');";
            System.out.println(introduceDatos);
            alquiler.executeUpdate(introduceDatos);

        } catch (SQLException ex) {
            Logger.getLogger(VentanaUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void peliDatos(String numPeli) {
        String nomCaratula = "";
        int indice;
        chivato = true;
        btnAlquilar.setEnabled(true);
        for (indice = 0; indice < datosPelis.size(); indice++) {
            if (numPeli.equals(datosPelis.get(indice)[0])) {
                nomCaratula = numPeli;                
                break;
            }

        }
        if (!nomCaratula.equals("")) {
            //Genera el nombre de la imagen
            for (int i = nomCaratula.length(); i < 6; i++) {
                nomCaratula = "0" + nomCaratula;
            }

            Image foto = (new ImageIcon(new ImageIcon(getClass().getResource("/caratulas/" + nomCaratula + ".jpg"))
                    .getImage().getScaledInstance(175, 210, Image.SCALE_DEFAULT))).getImage();
            ImageIcon caratula = new ImageIcon(foto);

            jlCaratula.setIcon(caratula);
            jlCaratula.updateUI();

            labelTitulo.setText(datosPelis.get(indice)[1]);
            labelAño.setText(datosPelis.get(indice)[2]);
            labelPais.setText(datosPelis.get(indice)[3]);
            labelGenero.setText(datosPelis.get(indice)[4]);
            labelImdb.setText(datosPelis.get(indice)[5]);
            labelClasificacion.setText(datosPelis.get(indice)[6]);
            taResumen.setLineWrap(true);
            taResumen.setText(datosPelis.get(indice)[7]);
            
                
              
            
                alquilarDatos(indice);
            
            
            //comprobarAlquiler();
            for (int z = 0; z < alquiPelis.size(); z++) {
                if (alquiPelis.get(z)[0].contentEquals(datosPelis.get(indice)[0])) {
                    btnAlquilar.setEnabled(false);
                    chivato = false;
                }
            }
        }
    }

    public void alquilarDatos(int _indice) {
        
        JTextField tf = new JTextField(20);
        
       
        System.out.println(idUser);
        aLabeluser.setText(idUser);
        auxiliar = datosPelis.get(_indice)[0];
        aLabelTitulo.setText(datosPelis.get(_indice)[1]);
        aLabelFP.setText(fechaActual());

        combo.removeAllItems();
        combo.addItem("1");
        combo.addItem("2");
        combo.addItem("3");
        combo.addItem("4");

        jDateChooser1.setMinSelectableDate(new Date());
        jDateChooser1.setMaxSelectableDate(sumarDias(new Date()));
    }

    public Date sumarDias(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, 10);
        return calendar.getTime();
    }

    public ImageIcon adaptaCaratulas(String nomCaratula) {
        int anchoCaratula = 80;
        for (int i = nomCaratula.length(); i < 6; i++) {
            nomCaratula = "0" + nomCaratula;
        }
        Image foto = (new ImageIcon(new ImageIcon(getClass().getResource("/caratulas/" + nomCaratula + ".jpg"))
                .getImage().getScaledInstance(anchoCaratula, 140, Image.SCALE_DEFAULT))).getImage();
        ImageIcon caratula = new ImageIcon(foto);
        return caratula;
    }

    public static String fechaActual() {
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        return formatoFecha.format(fecha);
    }

    /**
     * Creates new form VentanaUsuario
     */
    public VentanaUsuario() {

        initComponents();
        this.setTitle("METFLIX");
        this.setLocation(pantalla.width / 3, pantalla.height / 4);
        dialogInfoPeli.setTitle("METFLIX");
        dialogInfoPeli.setLocation(pantalla.width / 3, pantalla.height / 4);
        cargaPelis();
        cargaElementos();

        //generaCaratula(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogInfoPeli = new javax.swing.JDialog();
        jlCaratula = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        labelTitulo = new javax.swing.JLabel();
        labelAño = new javax.swing.JLabel();
        labelPais = new javax.swing.JLabel();
        labelGenero = new javax.swing.JLabel();
        labelImdb = new javax.swing.JLabel();
        labelClasificacion = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taResumen = new javax.swing.JTextArea();
        btnAlquilar = new javax.swing.JButton();
        alquilaPeli = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        aLabelTitulo = new javax.swing.JLabel();
        aLabeluser = new javax.swing.JLabel();
        combo = new javax.swing.JComboBox<>();
        aLabelFP = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        derecha = new javax.swing.JLabel();
        izquierda = new javax.swing.JLabel();
        izquierda1 = new javax.swing.JLabel();
        derecha1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jtBuscador = new javax.swing.JTextField();
        btnBuscador = new javax.swing.JButton();
        contenedor = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        fotoPerfil = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        jlCaratula.setText("jLabel4");

        jLabel5.setText("TITULO:");

        jLabel6.setText("AÑO:");

        jLabel7.setText("PAIS:");

        jLabel8.setText("GENERO:");

        jLabel9.setText("Nº IMDB:");

        jLabel10.setText("CLASIFICACIÓN:");

        jLabel11.setText("RESUMEN:");

        labelTitulo.setText("jLabel12");

        labelAño.setText("jLabel12");

        labelPais.setText("jLabel12");

        labelGenero.setText("jLabel12");

        labelImdb.setText("jLabel12");

        labelClasificacion.setText("jLabel12");

        taResumen.setEditable(false);
        taResumen.setColumns(20);
        taResumen.setRows(5);
        taResumen.setEnabled(false);
        jScrollPane1.setViewportView(taResumen);

        btnAlquilar.setText("Alquilar");
        btnAlquilar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnAlquilarMousePressed(evt);
            }
        });

        javax.swing.GroupLayout dialogInfoPeliLayout = new javax.swing.GroupLayout(dialogInfoPeli.getContentPane());
        dialogInfoPeli.getContentPane().setLayout(dialogInfoPeliLayout);
        dialogInfoPeliLayout.setHorizontalGroup(
            dialogInfoPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogInfoPeliLayout.createSequentialGroup()
                .addGroup(dialogInfoPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dialogInfoPeliLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(dialogInfoPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dialogInfoPeliLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelAño))
                            .addGroup(dialogInfoPeliLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelPais))
                            .addGroup(dialogInfoPeliLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelGenero))
                            .addGroup(dialogInfoPeliLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelTitulo)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelImdb)
                        .addGap(93, 93, 93))
                    .addGroup(dialogInfoPeliLayout.createSequentialGroup()
                        .addGroup(dialogInfoPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dialogInfoPeliLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelClasificacion))
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jlCaratula, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1)
            .addGroup(dialogInfoPeliLayout.createSequentialGroup()
                .addGap(220, 220, 220)
                .addComponent(btnAlquilar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dialogInfoPeliLayout.setVerticalGroup(
            dialogInfoPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogInfoPeliLayout.createSequentialGroup()
                .addGroup(dialogInfoPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(dialogInfoPeliLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlCaratula, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dialogInfoPeliLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(dialogInfoPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(labelTitulo)
                            .addComponent(labelImdb))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dialogInfoPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(labelAño))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dialogInfoPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(labelPais))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dialogInfoPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(labelGenero))
                        .addGap(52, 52, 52)
                        .addGroup(dialogInfoPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(labelClasificacion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btnAlquilar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jLabel4.setText("Titulo:");

        jLabel13.setText("Cantidad de peliculas");

        jButton1.setText("Aceptar");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
        });

        jButton2.setText("Cancelar");

        jLabel14.setText("Fecha de prestamo");

        jLabel15.setText("Fecha de Devolución:(Formato:dd/mm/aaaa)");

        jLabel16.setText("Usuario:");

        aLabelTitulo.setText("jLabel17");

        aLabeluser.setText("jLabel18");

        combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        aLabelFP.setText("jLabel19");

        jDateChooser1.setDateFormatString("yyyy/MM/dd");
        jDateChooser1.setMaxSelectableDate(new java.util.Date(253370764897000L));

        javax.swing.GroupLayout alquilaPeliLayout = new javax.swing.GroupLayout(alquilaPeli.getContentPane());
        alquilaPeli.getContentPane().setLayout(alquilaPeliLayout);
        alquilaPeliLayout.setHorizontalGroup(
            alquilaPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alquilaPeliLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(alquilaPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(alquilaPeliLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(aLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(alquilaPeliLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aLabeluser)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(alquilaPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alquilaPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alquilaPeliLayout.createSequentialGroup()
                            .addComponent(jLabel14)
                            .addGap(150, 150, 150))
                        .addGroup(alquilaPeliLayout.createSequentialGroup()
                            .addComponent(jLabel15)
                            .addContainerGap()))
                    .addGroup(alquilaPeliLayout.createSequentialGroup()
                        .addGroup(alquilaPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(aLabelFP, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27))))
            .addGroup(alquilaPeliLayout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(91, 91, 91))
        );
        alquilaPeliLayout.setVerticalGroup(
            alquilaPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alquilaPeliLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(alquilaPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(aLabeluser))
                .addGap(39, 39, 39)
                .addGroup(alquilaPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alquilaPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aLabelTitulo)
                    .addComponent(aLabelFP))
                .addGap(34, 34, 34)
                .addGroup(alquilaPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alquilaPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alquilaPeliLayout.createSequentialGroup()
                        .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                        .addGroup(alquilaPeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton1)))
                    .addGroup(alquilaPeliLayout.createSequentialGroup()
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(0, 600));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("jLabel1");

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabbedPane1MousePressed(evt);
            }
        });

        jLabel2.setText("TOP peliculas");

        jLabel3.setText("Peliculas recientes");

        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel3MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 873, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );

        derecha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                derechaMousePressed(evt);
            }
        });

        izquierda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                izquierdaMousePressed(evt);
            }
        });

        izquierda1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                izquierda1MousePressed(evt);
            }
        });

        derecha1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                derecha1MousePressed(evt);
            }
        });

        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel4MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 863, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(izquierda, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(derecha, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 898, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(izquierda1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(derecha1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(derecha, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(izquierda, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(derecha1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(izquierda1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(48, 48, 48))
        );

        jTabbedPane1.addTab("Peliculas", jPanel1);

        btnBuscador.setText("Buscar");
        btnBuscador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnBuscadorMousePressed(evt);
            }
        });

        contenedor.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contenedor.setAutoscrolls(true);
        contenedor.setPreferredSize(new java.awt.Dimension(995, 400));

        jPanel5.setAutoscrolls(true);
        jPanel5.setPreferredSize(new java.awt.Dimension(993, 50));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel5MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        contenedor.setViewportView(jPanel5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(324, 324, 324)
                .addComponent(jtBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBuscador)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(contenedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscador))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(contenedor, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Buscador", jPanel2);

        jLabel12.setText("Peliculas alquiladas");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1041, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 464, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel7);

        jLabel20.setText("Fecha de devolución");

        jLabel17.setText("Peliculas alquiladas:");

        jLabel18.setText("jLabel18");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addGap(191, 191, 191)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addGap(124, 124, 124))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel12)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17)
                        .addComponent(jLabel18)))
                .addGap(9, 9, 9)
                .addComponent(jScrollPane2))
        );

        jTabbedPane1.addTab("Alquiler", jPanel6);

        jMenu1.setText("Opciones");
        jMenu1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        jMenuItem1.setText("Cerrar Sesión");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem1MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addComponent(fotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jMenuItem1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MousePressed
        // TODO add your handling code here:
        VentanaInicio vInicio = new VentanaInicio();
        this.setVisible(false);
        vInicio.setVisible(true);
    }//GEN-LAST:event_jMenuItem1MousePressed

    private void derechaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_derechaMousePressed
        // TODO add your handling code here:
        if (contador < totalPelis) {
            jPanel3.removeAll();
            jPanel3.updateUI();
            generaCaratula(0);
            jPanel3.updateUI();
        }

    }//GEN-LAST:event_derechaMousePressed

    private void izquierdaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_izquierdaMousePressed
        // TODO add your handling code here:

        contador = contador - 14;
        if (contador > 7) {
            jPanel3.removeAll();
            generaCaratula(0);
            jPanel3.updateUI();
        } else {
            contador = 0;
            jPanel3.removeAll();
            generaCaratula(0);
            jPanel3.updateUI();
        }
    }//GEN-LAST:event_izquierdaMousePressed

    private void jPanel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MousePressed
        // TODO add your handling code here:
        Point punto = new Point();
        punto.x = evt.getX();
        punto.y = evt.getY();
//        System.out.println(jPanel3.getComponentCount());
        if (jPanel3.getComponentAt(punto) instanceof JLabel) {
            label = (JLabel) jPanel3.getComponentAt(punto);
            labelMousePressed(label);
        }

    }//GEN-LAST:event_jPanel3MousePressed

    private void izquierda1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_izquierda1MousePressed
        // TODO add your handling code here:
        contador2 = contador2 - 14;
        if (contador2 > 7) {
            jPanel4.removeAll();
            generaCaratulasClasi(0);
            jPanel4.updateUI();
        } else {
            contador2 = 0;
            jPanel4.removeAll();
            generaCaratulasClasi(0);
            jPanel4.updateUI();
        }
    }//GEN-LAST:event_izquierda1MousePressed

    private void derecha1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_derecha1MousePressed
        // TODO add your handling code here:
        if (contador2 < clasiPelis.size()) {
            jPanel4.removeAll();
            jPanel4.updateUI();
            generaCaratulasClasi(0);
            jPanel4.updateUI();
        }
    }//GEN-LAST:event_derecha1MousePressed

    private void jPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MousePressed
        // TODO add your handling code here:
        Point punto = new Point();
        punto.x = evt.getX();
        punto.y = evt.getY();
//        System.out.println(jPanel3.getComponentCount());
        if (jPanel4.getComponentAt(punto) instanceof JLabel) {
            label2 = (JLabel) jPanel4.getComponentAt(punto);
            labelMousePressed(label2);
        }
    }//GEN-LAST:event_jPanel4MousePressed

    private void btnBuscadorMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscadorMousePressed
        String texto = jtBuscador.getText();
        buscaPelis.clear();
        contador3 = 0;
        jPanel5.removeAll();
        jPanel5.updateUI();

        try {
            buscador = estado.executeQuery("SELECT * FROM Metflix.peliculas WHERE titulo LIKE '%" + texto + "%'");

            while (buscador.next()) {
                String[] aux3 = new String[8];
                aux3[0] = buscador.getString("id_pelicula");
                aux3[1] = buscador.getString("titulo");
                aux3[2] = buscador.getString("año");
                aux3[3] = buscador.getString("pais");
                aux3[4] = buscador.getString("genero");
                aux3[5] = buscador.getString("imdb");
                aux3[6] = buscador.getString("clasificacion_imdb");
                aux3[7] = buscador.getString("resumen");
                buscaPelis.add(aux3);
            }
            System.out.println("Sale GOOD");

            generaCaratulasBuscadas(10);
            jPanel5.setPreferredSize(new Dimension(993, sumatorio + 10));
            jPanel5.updateUI();
        } catch (SQLException ex) {
            Logger.getLogger(VentanaUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBuscadorMousePressed

    private void jPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MousePressed
        // TODO add your handling code here:
        Point punto = new Point();
        punto.x = evt.getX();
        punto.y = evt.getY();
//        System.out.println(jPanel3.getComponentCount());
        if (jPanel5.getComponentAt(punto) instanceof JLabel) {
            label3 = (JLabel) jPanel5.getComponentAt(punto);
            labelMousePressed(label3);
        }
    }//GEN-LAST:event_jPanel5MousePressed

    private void btnAlquilarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlquilarMousePressed
        // TODO add your handling code here:
        if (chivato == true) {
            alquilaPeli.setSize(525, 292);

            alquilaPeli.setVisible(true);
        }
    }//GEN-LAST:event_btnAlquilarMousePressed

    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
        // TODO add your handling code here:
        int idPeli = Integer.valueOf(auxiliar);
        int numEjemplar = Integer.valueOf(combo.getSelectedItem().toString());
        int dni = Integer.valueOf(aLabeluser.getText());

        String formato = "yyyy/MM/dd";
        Date fecha = jDateChooser1.getDate();
        SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);

        String fechaD = formatoFecha.format(fecha);

        String fechaP = aLabelFP.getText();

        alquilarPeli(idPeli, numEjemplar, dni, fechaP, fechaD);

        alquilaPeli.setVisible(false);
        dialogInfoPeli.setVisible(false);
    }//GEN-LAST:event_jButton1MousePressed

    private void jTabbedPane1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MousePressed
        // TODO add your handling code here:
        jPanel7.removeAll();
        alquiPelis.clear();
        comprobarAlquiler();
        pelisAlquiladas();
    }//GEN-LAST:event_jTabbedPane1MousePressed

    private void labelMousePressed(JLabel peli) {

        peliDatos(peli.getText());

        dialogInfoPeli.setSize(800, 500);

        dialogInfoPeli.setVisible(true);
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
            java.util.logging.Logger.getLogger(VentanaUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaUsuario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aLabelFP;
    private javax.swing.JLabel aLabelTitulo;
    private javax.swing.JLabel aLabeluser;
    private javax.swing.JDialog alquilaPeli;
    private javax.swing.JButton btnAlquilar;
    private javax.swing.JButton btnBuscador;
    private javax.swing.JComboBox<String> combo;
    private javax.swing.JScrollPane contenedor;
    private javax.swing.JLabel derecha;
    private javax.swing.JLabel derecha1;
    private javax.swing.JDialog dialogInfoPeli;
    public javax.swing.JLabel fotoPerfil;
    private javax.swing.JLabel izquierda;
    private javax.swing.JLabel izquierda1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel jlCaratula;
    private javax.swing.JTextField jtBuscador;
    private javax.swing.JLabel labelAño;
    private javax.swing.JLabel labelClasificacion;
    private javax.swing.JLabel labelGenero;
    private javax.swing.JLabel labelImdb;
    private javax.swing.JLabel labelPais;
    private javax.swing.JLabel labelTitulo;
    private javax.swing.JTextArea taResumen;
    // End of variables declaration//GEN-END:variables
}
