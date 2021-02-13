package HelloJME3;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import jme3test.helloworld.HelloJME3;

/**
 *
 * @author barri
 */
public class Lobby extends HelloJME3 implements ActionListener{
    JFrame frame;
    JPanel panel,info;
    JButton init;
    JTextField text;
    JMenuBar barra;
    JMenu menu;
    JMenuItem archivo;
    JTextField colorSel;
    
    String[] nombreCol = {"Usuario","ID"};
    Object[][] datos = {{"Usuario","ID"}};
    DefaultTableModel dtm;
    final JTable table;
    private final JScrollPane scrollPane;
    JComboBox cbbox;
    
    
    public Lobby(Object[] data){
        colorSel = new JTextField(20);
        frame = new JFrame();
        panel = new JPanel();
        init = new JButton("Iniciar Juego");
        init.addActionListener(this);
        
        cbbox = new JComboBox();
        cbbox.addItem("Rojo");
        cbbox.addItem("Azul");
        cbbox.addItem("Amarillo");
        cbbox.addItem("Verde");
        cbbox.addItem("Morado");
        cbbox.addItem("Naranja");
        cbbox.addItem("Blanco");
        cbbox.addItem("Negro");
        
        cbbox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                colorSel.setText(cbbox.getSelectedItem().toString());
            }
        });
        
        dtm = new DefaultTableModel(datos,nombreCol);
        addRow(data);
        table = new JTable(dtm);
        table.setPreferredScrollableViewportSize(new Dimension(250,100));
        scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
       
       
        text = new JTextField("LOBBY");
        barra = new JMenuBar();
        menu = new JMenu("Archivo");
        archivo = new JMenuItem("Abrir");
        
        
        panel.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        panel.setLayout(new GridLayout(0,1));
        frame.add(panel,BorderLayout.SOUTH);
        panel.add(init);
        panel.add(cbbox);
        
        frame.add(text,BorderLayout.CENTER);
        menu.add(archivo);
        barra.add(menu);
        
        table.setBackground(Color.lightGray);
        frame.add(table);
        frame.add(barra,BorderLayout.NORTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Lobby - AirHockey AyP3D!");
        frame.pack();
        frame.setSize(1280,720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); 
    }
    
    public void addRow(Object[] newRow){
        dtm.addRow(newRow);
    }
    
    public void deleteRow(int num){
        dtm.removeRow(num);
    }
    
    public String getColorSel(){
        return this.colorSel.getText();
    }
    
    

    
    Thread t = new Thread(new Runnable(){
        @Override
        public void run(){
            HelloJME3 app = new HelloJME3();
            app.start();
        }
    });
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==init){
            frame.setVisible(false);
            t.start();
        }
    }
    
}
