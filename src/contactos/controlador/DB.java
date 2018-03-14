/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contactos.controlador;

import contactos.modelo.Persona;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Procesamiento Señal
 */
public class DB {

    private final String URL = "jdbc:derby://localhost:1527/contactos";
    private final String USUARIO = "app";
    private final String CLAVE = "12345";
    private Connection conexion;
    private PreparedStatement seleccionarPersona;
    private PreparedStatement seleccionarPersonaporApellido;
    private PreparedStatement insertarNuevaPersona;

    public DB() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            //System.out.println("Driver JAVA DB cargado !!!");
        } catch (ClassNotFoundException ex) {
            System.out.println("ERROR: No se encuentra el Driver" + ex);
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("Conexion establecida a la base de datos");
        } catch (SQLException ex) {
            System.out.println("ERROR: No se pudo establecer conexion a la base de datos" + ex);
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Persona> getPersonas() {
        List<Persona> lista = new ArrayList<>();
        try {
            seleccionarPersona = conexion.prepareStatement("Select * from persona");
            ResultSet resultado = seleccionarPersona.executeQuery();

            while (resultado.next()) {
                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                String apellido = resultado.getString("apellido");
                String email = resultado.getString("email");
                String telefono = resultado.getString("telefono");
                lista.add(new Persona(id, nombre, apellido, email, telefono));
            }
            resultado.close();
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar la consulta" + ex);
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public List<Persona> getPersonasXApellido(String apellidob) {
        List<Persona> lista = new ArrayList<>();
        try {
            seleccionarPersonaporApellido = conexion.prepareStatement("Select * from persona where apellido = '" + apellidob+"'");
            ResultSet resultado = seleccionarPersonaporApellido.executeQuery();

            while (resultado.next()) {
                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                String apellido = resultado.getString("apellido");
                String email = resultado.getString("email");
                String telefono = resultado.getString("telefono");
                Persona p = new Persona(id, nombre, apellido, email, telefono);
                System.out.println(p);
                lista.add(p);
            }
            resultado.close();
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar la consulta" + ex);
        }
        return lista;
    }

    public int agregarPersona(String nombre, String apellido, String email, String telefono) {
        try {
            insertarNuevaPersona = conexion.prepareStatement("insert into persona values("
                    + (this.getPersonas().size() + 1) + ", '"
                    + nombre + "', '"
                    + apellido + "', '"
                    + email + "', '"
                    + telefono + "')");
            return insertarNuevaPersona.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
}
