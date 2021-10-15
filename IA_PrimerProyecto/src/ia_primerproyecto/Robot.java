/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia_primerproyecto;

import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
//import java.util.Timer;


/* *
 * @author andre
 */
public class Robot {

    //**PANTALLA INTRODUCCTORIA**//
    static JFrame ventana;
    final JPanel mipanel;
    final JButton btnInicio;//iniciar
    final JLabel fondo;
    ImageIcon background;

    //**PANTALLA DEL MENU**//
    JPanel mimenu;//panelmenu
    final JButton btn[];//botones
    JLabel fondoMenu;
    ImageIcon backgroundMenu;//imagenfondomenu
    int p;//mis pruebas

    //**LABERINTO]]//
    static JPanel mimapa;//mi juegp
    JLabel fondoMapa;
    ImageIcon backgroundMapa;//imagenfondo
    static int matriz[][];
    static JLabel matrizImagen[][];

    String opcionMapa;
    //**POSICIONES ROBOT**//
    int x;
    int y;
    //**POSICIONES ITEM**//
    int xp;
    int yp;
    //**SENSORES*//
    int sensorArriba;
    int sensorAbajo;
    int sensorIzq;
    int sensorDer;
    //**INDICA LOS CAMINOS LOBRE LUEGO DE USAR LOS SENSORES
    int libreArriba;
    int libreAbajo;
    int libreIzq;
    int libreDer;
    //PARA SABER CUANDO HEMOS ATRAPADO A LA PLANTA
    boolean planta;

    //ARCHIVOS
    JFileChooser buscarArchivo;
    JTextField ruta;
    String linea[][];
    
    int cantLineas;
    int arriba;
    int abajo;
    int izq;
    int der;
    
    
    boolean movArriba;
    boolean movAbajo;
    boolean movIzq;
    boolean movDer;
    int cambiarMov;
    int contador = 0;
    String accion;

    //para movimiento automatico
    Timer tiempo;
    //  TimerTask tarea;
    int velocidad = 2;
    int velmil = velocidad * 1000;

    public Robot() {
        System.out.println("public Robot");
        ventana = new JFrame("Proyecto de Inteligencia Artificial");
        ventana.setSize(1500, 950);
        ventana.setLayout(null);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mipanel = new JPanel();
        mipanel.setLayout(null);
        mipanel.setBounds(0, 0, ventana.getWidth(), ventana.getHeight());
        mipanel.setVisible(true);
        mipanel.setBackground(Color.white);

        btnInicio = new JButton("Iniciar");
        btnInicio.setBounds(ventana.getWidth() - (ventana.getWidth() / 2) - 100, ventana.getHeight() - (ventana.getHeight() / 2) - 50, 200, 100);
        btnInicio.setVisible(true);
        btnInicio.setBackground(Color.white);
        btnInicio.setFont(Font.getFont("Century Gothic"));
        mipanel.add(btnInicio, 0);

        fondo = new JLabel();
        fondo.setBounds(0, 0, ventana.getWidth(), ventana.getHeight());
        background = new ImageIcon("imagenes/walle2.jpg");
        background = new ImageIcon(background.getImage().getScaledInstance(ventana.getWidth(), ventana.getHeight(), Image.SCALE_DEFAULT));
        fondo.setIcon(background);
        fondo.setVisible(true);
        mipanel.add(fondo, 0);

        //menu
        btn = new JButton[5];
        for (int i = 0; i < btn.length; i++) {
            btn[i] = new JButton();

        }

        
        //BOTONO INICIAR LUEGO DE SOLICITAR MAPA
        btnInicio.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("boton iniciar");
                Menu();
                botonMenu();

            }
        });
        ventana.add(mipanel);

        ventana.setVisible(true);

        //--------PARA CREAR EL MAPA-------------
        matriz = new int[9][9];
        opcionMapa = JOptionPane.showInputDialog("Existen 3 mapas, con cual desea iniciar?","Escribe un numero");
        while (opcionMapa == null || opcionMapa.compareTo("Escribe un numero") == 0 || opcionMapa.compareTo("") == 0 || !(Integer.parseInt(opcionMapa)>0 && Integer.parseInt(opcionMapa)<4)) {
            opcionMapa = JOptionPane.showInputDialog("Existen 3 mapas, con cual desea iniciar?","Escribe un numero");
        }

        matriz = mapa(Integer.parseInt(opcionMapa));
        matrizImagen = new JLabel[9][9];

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                matrizImagen[i][j] = new JLabel();
            }
        }

        //posicion para la robot
        x = 1;
        y = 2;
        matriz[x][y] = 2;

        //posicion para el item
        xp = (int) (Math.random()*7+1);
        yp = 2;
        matriz[xp][yp] = 3;

        
        sensorAbajo = 0;
        sensorArriba = 0;
        sensorIzq = 0;
        sensorDer = 0;

        //------------------------------
    }//FIN CONSTRUCTOR DE CLASE

    public void IniciarMapa() {//aqui esta todo lo necesario para dibujar el laberinto del robot
        System.out.println("void IniciarMapa");

        mimenu.setVisible(false);//ESCONDER EL PANEL ANTERIOR
        mimapa = new JPanel();
        mimapa.setLayout(null);
        mimapa.setBounds(0, 0, ventana.getWidth(), ventana.getHeight());
        mimapa.setVisible(true);

        fondoMapa = new JLabel();
        fondoMapa.setBounds(0, 0, ventana.getWidth(), ventana.getHeight());
        backgroundMapa = new ImageIcon("imagenes/eva.jpg");
        backgroundMapa = new ImageIcon(backgroundMapa.getImage().getScaledInstance(ventana.getWidth(), ventana.getHeight(), Image.SCALE_DEFAULT));
        fondoMapa.setIcon(backgroundMapa);
        fondoMapa.setVisible(true);
        mimapa.add(fondoMapa, 0);
        
        //CONSTRUCCIPN DEL MAPA EN IMAGENES

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                matrizImagen[i][j].setIcon(new ImageIcon("imagenes/mapa/" + matriz[i][j] + ".png"));
                matrizImagen[i][j].setBounds(400 + (i * 95), 25 + (j * 95), 95, 95);
                matrizImagen[i][j].setVisible(true);
                mimapa.add(matrizImagen[i][j], 0);
            }
        }
        
        moverEva();//MOVIMIENTOS DE EVA
        
        ventana.add(mimapa);

    }

    
    ///PARA PINTAR EL MAPA LUEGO DE REALIZAR UN MOVIMIENTO
    public static void pintarMapa() {
        System.out.println("pintarMapa");
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                matrizImagen[i][j].setIcon(new ImageIcon("imagenes/mapa/" + matriz[i][j] + ".png"));
                matrizImagen[i][j].setBounds(400 + (i * 95), 25 + (j * 95), 95, 95);
                matrizImagen[i][j].setVisible(true);
                mimapa.add(matrizImagen[i][j], 0);
            }
        }

    }

    
    //PARA EL MOVIMIENTO DE LA ROBOT EVA
    public void moverEva() {
        planta = false;
        cambiarMov = 0;
        System.out.println("entrando a moverEva");
try{
        tiempo = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                evaluar1();///EVALUAR EL MOVIMIENTO INDICADO EN EL ARCHIVO Y LOS DIPONIBLES
                System.out.println("Hola ");

                //////ARRIBA
                if (movArriba && (matriz[x][y - 1] == 0 || matriz[x][y - 1] == 3)) {//EVALUA MOV INGRESADO POR ARRCHIVO Y SI SE ENCUENTRA DISPONIBLE
                    if (matriz[x][y - 1] == 3) {
                        System.out.println("GANASTE");
                        planta = true;
                    }

                    matriz[x][y] = 0;
                    y = y - 1;
                    matriz[x][y] = 2;
                    pintarMapa();

                } else if (movArriba && matriz[x][y - 1] == 1) {
                    cambiarMov = cambiarMov + 1;
                    if (cambiarMov > 4) {
                        cambiarMov = 0;
                    }
                }

                ////////ABAJO
                if (movAbajo && (matriz[x][y + 1] == 0 || matriz[x][y + 1] == 3)) {
                    if (matriz[x][y + 1] == 3) {
                        System.out.println("GANASTE");
                        planta = true;

                    }

                    matriz[x][y] = 0;
                    y = y + 1;
                    matriz[x][y] = 2;
                    pintarMapa();

                } else if (movAbajo && (matriz[x][y + 1] == 1)) {
                    cambiarMov = cambiarMov + 1;
                    if (cambiarMov > 4) {
                        cambiarMov = 0;
                    }

                }

                ///////IZQUIERDA
                if (movIzq && (matriz[x - 1][y] == 0 || matriz[x - 1][y] == 3)) {
                    if (matriz[x - 1][y] == 3) {
                        System.out.println("GANASTE");
                        planta = true;

                    }

                    matriz[x][y] = 0;
                    x = x - 1;
                    matriz[x][y] = 2;
                    pintarMapa();

                } else if (movIzq && matriz[x - 1][y] == 1) {
                    cambiarMov = cambiarMov + 1;
                    if (cambiarMov > 4) {
                        cambiarMov = 0;
                    }
                }

                ///////DERECHA
                if (movDer && (matriz[x + 1][y] == 0 || matriz[x + 1][y] == 3)) {

                    if (matriz[x + 1][y] == 3) {
                        System.out.println("GANASTE");
                        planta = true;

                    }

                    matriz[x][y] = 0;
                    x = x + 1;
                    matriz[x][y] = 2;
                    pintarMapa();

                } else if (movDer && matriz[x + 1][y] == 1) {
                    cambiarMov = cambiarMov + 1;
                    if (cambiarMov > 4) {
                        cambiarMov = 0;
                    }
                }

                //////ENCONTRAR LA PLANTA, LA ENCONTRAR LA PLANTA MOSTRAR EN PANTALLA EL LOGRO
                if (planta) {
                    JOptionPane.showMessageDialog(ventana, "Eva ha encontrado la planta!");
                    tiempo.stop();
                }
            }

        });
        tiempo.start();//INICAR EL TEMPROIZADOR SWING
        
}catch(Exception e){
    JOptionPane.showMessageDialog(ventana,"Parece que Eva quedó atrapada!!!");
}

//        
        System.out.println("salir moverEva");
    }
//
//    public void evaluar2() {
//        System.out.println("Evaluar 2");
//    
//
//        arriba = Integer.parseInt(linea[0][0]);
//        abajo = Integer.parseInt(linea[1][0]);
//        der = Integer.parseInt(linea[2][0]);
//        izq = Integer.parseInt(linea[3][0]);
//        accion = linea[4][0];
//            evaluar1();
//        if (libreArriba == arriba && libreAbajo == abajo && libreIzq == izq && libreDer == der && accion == "abajo") {
//            movArriba = false;
//            movAbajo = true;
//            movIzq = false;
//            movDer = false;
//            System.out.println("ruta elegida: ABAJO");
//        }
//
//    }

    public void evaluar1() {//EVALUA CON LOS SENSORES LOS ESPACIOS VIABLES Y COLOCA UNA BANDERA DE LIBRE=1

        System.out.println("Evaluar 1");

        libreArriba = 0;
        libreAbajo = 0;
        libreDer = 0;
        libreIzq = 0;

        if (matriz[x][y - 1] == 0 || matriz[x][y - 1] == 3) {
            libreArriba = 1;

            sensorArriba = 1;
            sensorAbajo = 0;
            sensorIzq = 0;
            sensorDer = 0;
        }
        if (matriz[x][y + 1] == 0 || matriz[x][y + 1] == 3) {
            libreAbajo = 1;

            sensorArriba = 0;
            sensorAbajo = 1;
            sensorIzq = 0;
            sensorDer = 0;
        }
        if (matriz[x - 1][y] == 0 || matriz[x - 1][y] == 3) {
            libreIzq = 1;

            sensorArriba = 0;
            sensorAbajo = 0;
            sensorIzq = 1;
            sensorDer = 0;
        }
        if (matriz[x + 1][y] == 0 || matriz[x + 1][y] == 3) {
            libreDer = 1;

            sensorArriba = 0;
            sensorAbajo = 0;
            sensorIzq = 0;
            sensorDer = 1;
        }

        try{//INICIA LA MATRIZ DONDE SE GUARDARON LAS LINEAS DEL ARCHIVO Y COMPARA SI EL CAMINO ES VIABLE O NO
        System.out.println("cambiarMov: " + cambiarMov);
        arriba = Integer.parseInt(linea[0][cambiarMov]);
        abajo = Integer.parseInt(linea[1][cambiarMov]);
        izq = Integer.parseInt(linea[2][cambiarMov]);
        der = Integer.parseInt(linea[3][cambiarMov]);
        accion = linea[4][cambiarMov];
        System.out.println("libreArriba: " + libreArriba + " arriba: " + arriba);
        System.out.println("libreAbajo: " + libreAbajo + " abajo: " + abajo);
        System.out.println("libreIzq: " + libreIzq + " izq: " + izq);
        System.out.println("libreDer: " + libreDer + " der: " + der);
        System.out.println("accion: " + accion);
        }catch(Exception error){
             JOptionPane.showMessageDialog(ventana,"Las instrucciones que le diste a Eva no son las adecuadas!!!");
        }
        ///////ABAJO
        if ((libreArriba == arriba && libreAbajo == abajo && libreIzq == izq && libreDer == der && accion.equalsIgnoreCase("abajo")) /*   || (libreArriba == arriba && libreAbajo == 1 && libreIzq == 1 && libreDer == 1 && accion.equalsIgnoreCase("abajo"))*/) {
            movArriba = false;
            movAbajo = true;
            movIzq = false;
            movDer = false;
            System.out.println("ruta elegida: ABAJO");
        }

        /////ARRIBA
        if ((libreArriba == arriba && libreAbajo == abajo && libreIzq == izq && libreDer == der && accion.equalsIgnoreCase("arriba"))) {
            movArriba = true;
            movAbajo = false;
            movIzq = false;
            movDer = false;
            System.out.println("ruta elegida: ARRIBA");
        }

        //////DERECHA
        if (libreArriba == arriba && libreAbajo == abajo && libreIzq == izq && libreDer == der && accion.equalsIgnoreCase("derecha")) {
            movArriba = false;
            movAbajo = false;
            movIzq = false;
            movDer = true;
            System.out.println("ruta elegida: DERECHA");
        }

        ///////IZQUIERDA
        if (libreArriba == arriba && libreAbajo == abajo && libreIzq == izq && libreDer == der && accion.equalsIgnoreCase("izquierda")) {
            movArriba = false;
            movAbajo = false;
            movIzq = true;
            movDer = false;
            System.out.println("ruta elegida: IZQUIERDA");
        }

    }

    public int[][] mapa(int opcion) {//CONFIGURACIÓN DE MAPAS
        System.out.println("int[][] mapa");
        int[][] mapa1 = new int[9][9];
        if (opcion == 1) {
            int mapa[][] = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 1, 1, 0, 1, 1},
                {1, 1, 0, 1, 1, 1, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 1, 1, 1, 0, 1, 1},
                {1, 1, 0, 1, 1, 1, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},};
            return mapa;
        }

        if (opcion == 2) {

            int mapa[][] = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 1, 1, 1, 0, 1, 1},
                {1, 1, 0, 1, 0, 1, 0, 1, 1},
                {1, 1, 0, 1, 0, 1, 0, 1, 1},
                {1, 1, 0, 1, 0, 0, 0, 1, 1},
                {1, 1, 0, 1, 1, 1, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},};

            return mapa;

        }

        if (opcion == 3) {

            int mapa[][] = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},};

            return mapa;

        }

        return mapa1;

    }

    public void Menu() {/// EL MENU
        System.out.println("void Menu");
        mipanel.setVisible(false);

        mimenu = new JPanel();
        mimenu.setLayout(null);
        mimenu.setBounds(0, 0, ventana.getWidth(), ventana.getHeight());
        mimenu.setVisible(true);

        fondoMenu = new JLabel();
        fondoMenu.setBounds(0, 0, ventana.getWidth(), ventana.getHeight());
        backgroundMenu = new ImageIcon("imagenes/eva.jpg");
        backgroundMenu = new ImageIcon(backgroundMenu.getImage().getScaledInstance(ventana.getWidth(), ventana.getHeight(), Image.SCALE_DEFAULT));
        fondoMenu.setIcon(backgroundMenu);
        fondoMenu.setVisible(true);
        mimenu.add(fondoMenu, 0);

        btn[0].setText("Cargar Archivo");
        btn[1].setText("Empezar de nuevo");
        btn[2].setText("Salir");

        btn[0].setBounds(50, 350, 200, 80);
        btn[0].setBackground(Color.white);
        btn[0].setVisible(true);
        mimenu.add(btn[0], 0);
        for (int i = 1; i < btn.length; i++) {

            btn[i].setBounds(50, btn[i - 1].getY() + 100, 200, 80);
            btn[i].setBackground(Color.white);
            btn[i].setVisible(true);
            mimenu.add(btn[i], 0);
        }

        ventana.add(mimenu);
    }

    public void botonMenu() {

        btn[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {//LECTURA DEL ARCHIVO

                System.out.println("boton Cargar Archivo");
                buscarArchivo = new JFileChooser();
                buscarArchivo.showOpenDialog(buscarArchivo);
                try {

                    String patch = buscarArchivo.getSelectedFile().getAbsolutePath();
                    System.out.println("ruta: " + patch);
                    BufferedReader bufferPrevio = new BufferedReader(new FileReader(patch));
                    FileInputStream archivo = new FileInputStream(patch);
                    DataInputStream entrada = new DataInputStream(archivo);
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));

                    String temp = "";
                    String bfRead;
                    int contador = 0;
                    System.out.println("inicio lectura");
                    while ((bfRead = bufferPrevio.readLine()) != null) {//leer mientras no llegue al final del archivo
                        contador = contador + 1;

                    }
                    linea = new String[5][contador];
                    contador = 0;
                    while ((bfRead = buffer.readLine()) != null) {//leer mientras no llegue al final del archivo
                        System.out.println(bfRead);

                        linea[0][contador] = bfRead.substring(0, 1);
                        System.out.println(linea[0][contador]);
                        linea[1][contador] = bfRead.substring(2, 3);
                        System.out.println(linea[1][contador]);
                        linea[2][contador] = bfRead.substring(4, 5);
                        System.out.println(linea[2][contador]);
                        linea[3][contador] = bfRead.substring(6, 7);
                        System.out.println(linea[3][contador]);
                        linea[4][contador] = bfRead.substring(8);
                        System.out.println(linea[4][contador]);

                        contador = contador + 1;

                    }

                } catch (IOException x) {
                    System.out.println("ocurrio un error" + x.getMessage());
                }

                IniciarMapa();

            }
        });

        btn[1].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Menu();

            }
        });

        btn[2].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);

            }
        });
    }
}
