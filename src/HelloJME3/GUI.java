package HelloJME3;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author barri
 */
public class GUI implements ActionListener {
       
    JFrame frame,userChange;
    JPanel panel;
    JButton create,join,about,exit,change,buttontxt;
    JLabel saludo;
    static String usuario = "Player1";
    static String ID;
    Font fuente;
    JTextField txt;
    
    public GUI(String usuario){
        frame = new JFrame();
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        panel.setLayout(new GridLayout(0,1));
        
        create = new JButton("Crear Partida");
        join = new JButton("Unirse a Partida");
        about = new JButton("Sobre el juego");
        exit = new JButton("Salir");
        change = new JButton("Cambiar nombre de usuario");
        
        try {
            BufferedImage myImage = ImageIO.read(new File("Assets/Images/AirHockeyMenu.jpg"));
            JLabel background = new JLabel(new ImageIcon(myImage));
            frame.add(background);
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //Dice el nombre del usuario
        fuente = new Font("Cambria",1,20);
        saludo = new JLabel("Bienvenido, " + usuario + "!",SwingConstants.CENTER);
        saludo.setFont(fuente);
        
        panel.add(saludo);
        frame.add(panel,BorderLayout.SOUTH);
        panel.add(create);
        panel.add(join);
        panel.add(change);
        panel.add(about);
        panel.add(exit);
        
       // frame.add(label,BorderLayout.NORTH);
        create.addActionListener(this);
        join.addActionListener(this);
        exit.addActionListener(this);
        change.addActionListener(this);
        panel.setBackground(Color.white);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("AirHockey AyP3D!");
        frame.pack();
        frame.setSize(1280,720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);        
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==exit){
            int input = JOptionPane.showConfirmDialog(null,"¿Estás seguro que deseas salir?");
            if(input==0)
                System.exit(0);
        }else if(e.getSource()==create){
            initLobby();
            System.out.println("Creando Lobby...");
            frame.setVisible(false);
        }else if(e.getSource()==join){
            initLobby();
            System.out.println("Uniendo a Lobby...");
            frame.setVisible(false);
        }else if(e.getSource()==change){
            frame.setVisible(false);
            changeUser(); 
        }else if(e.getSource()==txt || e.getSource()==buttontxt){
            userChange.setVisible(false);
            setUsuario(txt.getText());
            GUI newgui = new GUI(getUsuario());
            
        }
    }
    
    public void initLobby(){
        String user = getUsuario();
        String uniqueID = UUID.randomUUID().toString();
        Object[] dato = {user,uniqueID};
        Lobby lobby = new Lobby(dato);
        
    }
    
    public void changeUser(){
        userChange = new JFrame("Cambiar de Nombre de Usuario");
        txt = new JTextField();
        JPanel paneltxt = new JPanel();
        JLabel labeltxt = new JLabel("Ingrese su nuevo Nombre de Usuario",SwingConstants.CENTER);
        buttontxt = new JButton("Aceptar");
        
        
        buttontxt.addActionListener(this);
        
        labeltxt.setFont(fuente);
        
        paneltxt.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        paneltxt.setLayout(new GridLayout(0,1));
        
        
        userChange.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        txt.setBounds(50,50,200,30);
        userChange.add(paneltxt);
        
        paneltxt.add(labeltxt);
        
        paneltxt.add(txt,BorderLayout.CENTER);
        userChange.add(buttontxt,BorderLayout.SOUTH);
        userChange.pack();
        userChange.setSize(500,250);
        userChange.setLocationRelativeTo(null);
        userChange.setVisible(true);
        txt.addActionListener(this);
        setUsuario(txt.getText());
    }
    
    
    public void setUsuario(String user){
        this.usuario = user;
    }
    
    public String getUsuario(){
        return usuario;
    }
}