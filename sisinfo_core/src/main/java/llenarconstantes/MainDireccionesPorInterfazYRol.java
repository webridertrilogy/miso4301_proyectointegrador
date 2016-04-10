/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package llenarconstantes;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Asistente
 */
public class MainDireccionesPorInterfazYRol extends JFrame implements ActionListener{

    //LOCAL
    final static String bdLocal = "sisinfo";
    final static String loginLocal = "root";
    final static String passwordLocal = "mysql";
    final static String urlLocal = "jdbc:mysql://localhost:3306/";//+ bd;
    final static String bdPreProd3 = "preprod3";
    final static String loginPreProd3 = "preprod3";
    final static String bdPreProd2 = "preprod2";
    final static String loginPreProd2 = "preprod2";
    final static String bdPreProd1 = "preprod1";
    final static String loginPreProd1 = "preprod1";
    final static String bdProd = "prod";
    final static String loginProd = "prod";
    //  Preprod
    final static String passwordPreProd = "5tgbnhy6";
    final static String urlServidor = "jdbc:mysql://sisinfo.uniandes.edu.co:3306/";// + bd;
    //   Prod
    final static String passwordProd = "4htasef128";

    private final static String AGREGAR = "Agregar";

    private final static String CARGAR = "Cargar";

    private JTable tabla;

    private JButton btnAgregar;

    private JButton btnCargarDeArchivo;

    private String usuario;

    private String password;

    private String url;

    private String bd;

    private Connection conn;

    private HashMap<String,Long> mapaRoles;

    private HashMap<Long,String> mapaIdRoles;

    public MainDireccionesPorInterfazYRol(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Configurar acciones");
        setSize(400, 500);
        setLocationRelativeTo(null);
        try{
            configurarEntorno();
            cargarRoles();
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error conectando con la base de datos","Error",JOptionPane.ERROR_MESSAGE);
        }
        
        setLayout(new BorderLayout());
        tabla = new JTable();

        add(new JScrollPane(tabla),BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        panelSur.setLayout(new GridLayout(1,2));
        btnAgregar = new JButton("Agregar");
        btnAgregar.setActionCommand(AGREGAR);
        btnAgregar.addActionListener(this);
        btnCargarDeArchivo = new JButton("Cargar de archivo");
        btnCargarDeArchivo.setActionCommand(CARGAR);
        btnCargarDeArchivo.addActionListener(this);
        panelSur.add(btnAgregar);
        panelSur.add(btnCargarDeArchivo);
        add(panelSur,BorderLayout.SOUTH);
        refrescarTabla();
    }

    public void refrescarTabla(){
        try{

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from DirectorioInterfacesPorRol");
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCols = rsmd.getColumnCount();
            String[] columnNames = new String[numCols];
            for (int i = 1; i <= columnNames.length; i++) {
                columnNames[i-1] = rsmd.getColumnName(i);
            }

            DefaultTableModel dtm = new DefaultTableModel(columnNames, 0);
            while(rs.next()){
                Vector data = new Vector();
                for (int i = 1; i <= numCols; i++) {
                    if(columnNames[i-1].equals("rol_id")){
                        data.add(mapaIdRoles.get(rs.getLong(i)));
                    }else{
                        data.add(rs.getObject(i));
                    }
                }
                dtm.addRow(data);
            }
            tabla.setModel(dtm);
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error conectando con la base de datos","Error",JOptionPane.ERROR_MESSAGE);
        }        
    }

    public void configurarEntorno()throws Exception{
        String entorno = (String)JOptionPane.showInputDialog(this,"Seleccione el entorno de desarrollo","Entorno de desarrollo",JOptionPane.INFORMATION_MESSAGE,null,new String[]{"local","preprod1","preprod2","preprod3","prod"},"local");
        if(entorno.equals("local")){
            usuario = loginLocal;
            password = passwordLocal;
            bd = bdLocal;
            url = urlLocal+bd;
        }else if(entorno.equals("preprod1")){
            usuario = loginPreProd1;
            password = passwordPreProd;
            bd = bdPreProd1;
            url = urlServidor+bd;
        }else if(entorno.equals("preprod2")){
            usuario = loginPreProd2;
            password = passwordPreProd;
            bd = bdPreProd2;
            url = urlServidor+bd;
        }else if(entorno.equals("preprod3")){
            usuario = loginPreProd3;
            password = passwordPreProd;
            bd = bdPreProd3;
            url = urlServidor+bd;
        }else if(entorno.equals("prod")){
            usuario = loginProd;
            password = passwordProd;
            bd = bdProd;
            url = urlServidor+bd;
        }
        conn = DriverManager.getConnection(url, usuario, password);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if(cmd.equals(AGREGAR)){
            agregarNuevo();
        }else if(cmd.equals(CARGAR)){
            cargarArchivo();
        }
    }

    public void agregarNuevo(){
        try{
            JPanel panel = new JPanel();
            //JTextField txtRol = new JTextField();
            JComboBox cbRol = new JComboBox();
            DefaultComboBoxModel dcbm = new DefaultComboBoxModel(mapaRoles.keySet().toArray());
            cbRol.setModel(dcbm);
            File[] files = new File("../../SisinfoBeans/ServiciosEnrutacion/src/java/co/uniandes/sisinfo/serviciosnegocio/").listFiles();
            String[] items = new String[files.length];
            for (int i = 0; i < items.length; i++) {
                if(files[i].isDirectory())
                    continue;
                items[i] = "co.uniandes.sisinfo.serviciosnegocio."+files[i].getName().substring(0,files[i].getName().length()-5);
            }
            JComboBox cbDireccionInterfaz = new JComboBox();
            dcbm = new DefaultComboBoxModel(items);
            cbDireccionInterfaz.setModel(dcbm);
            JCheckBox chkActivo = new JCheckBox("Activo");
            JPanel panelIzq = new JPanel();
            panelIzq.setLayout(new GridLayout(3,1,2,5));
            JPanel panelDer = new JPanel();
            panelDer.setLayout(new GridLayout(3,1,2,5));
            panel.setLayout(new BorderLayout(5,5));
            panelIzq.add(new JLabel("Rol id"));
            panelDer.add(cbRol);
            panelIzq.add(new JLabel("Direccion Interfaz"));
            panelDer.add(cbDireccionInterfaz);
            panelIzq.add(new JLabel(""));
            panelDer.add(chkActivo);
            panel.add(panelIzq,BorderLayout.WEST);
            panel.add(panelDer,BorderLayout.EAST);
            int option = JOptionPane.showConfirmDialog(this, panel, "Nuevo registro", JOptionPane.OK_CANCEL_OPTION);
            if(option != JOptionPane.OK_OPTION)
                return;
            String nuevoRol = ""+mapaRoles.get(cbRol.getSelectedItem());
            String nuevaInterfaz = cbDireccionInterfaz.getSelectedItem().toString();
            boolean activo = chkActivo.isSelected();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select max(id) from DirectorioInterfacesPorRol");
            rs.next();
            int id = rs.getInt(1)+1;
            String sql = String.format("insert into DirectorioInterfacesPorRol (id,activo,direccionInterfaz,rol_id) values(%d,%b,\"%s\",\"%s\")",id,activo,nuevaInterfaz,nuevoRol);
            st.execute(sql);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("./data/directorioInterfacesPorRol.txt", true)));
            out.println(String.format("%s;%s;%b",nuevaInterfaz,nuevoRol,activo));
            out.close();
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }finally{
            refrescarTabla();
        }
    }

    public void cargarArchivo(){
        try{
            Statement st = conn.createStatement();
            st.execute("truncate table DirectorioInterfacesPorRol");
            ArrayList<String> errores = new ArrayList();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./data/directorioInterfacesPorRol.txt")));
            String ln = br.readLine();
            int count = 1;
            while(ln!=null){
                if(ln.trim().isEmpty()){
                    ln = br.readLine();
                    continue;
                }
                String[] info = ln.split(";");
                if(info.length!=3){
                    errores.add("Error leyendo la linea "+count);
                }else{
                    String nuevaInterfaz = info[0];
                    String nuevoRol = info[1];
                    boolean activo = Boolean.parseBoolean(info[2]);

                    String sql = String.format("insert into DirectorioInterfacesPorRol (id,activo,direccionInterfaz,rol_id) values(%d,%b,\"%s\",\"%s\")",count,activo,nuevaInterfaz,nuevoRol);
                    st.execute(sql);

                }
                count++;
                ln = br.readLine();
            }
            if(!errores.isEmpty())
            {
                String str = "Se han encontrado los siguientes errores:\n";
                for (String string : errores) {
                    str+=string+"\n";
                }
                JOptionPane.showMessageDialog(this,str,"Error",JOptionPane.ERROR_MESSAGE);
            }
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
        refrescarTabla();

    }

    public void cargarRoles()
    {
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select id,rol from rol");
            mapaRoles = new HashMap<String, Long>();
            mapaIdRoles = new HashMap<Long, String>();
            while(rs.next()){
                long id = rs.getLong(1);
                String rol = rs.getString(2);
                mapaRoles.put(rol, id);
                mapaIdRoles.put(id, rol);
            }
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error cargando roles","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args){
        MainDireccionesPorInterfazYRol main = new MainDireccionesPorInterfazYRol();
        main.setVisible(true);
    }

}
