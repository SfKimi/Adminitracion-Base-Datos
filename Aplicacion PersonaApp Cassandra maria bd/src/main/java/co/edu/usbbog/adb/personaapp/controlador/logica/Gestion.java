/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.adb.personaapp.controlador.logica;

import co.edu.usbbog.adb.personaapp.controlador.persistencia.dao.UsuarioDao;
import co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa.CorreosJpaController;
import co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa.TelefonosJpaController;
import co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa.UsuariosJpaController;
import co.edu.usbbog.adb.personaapp.modelo.Correos;
import static co.edu.usbbog.adb.personaapp.modelo.Correos_.usuario;
import co.edu.usbbog.adb.personaapp.modelo.Telefonos;
import co.edu.usbbog.adb.personaapp.modelo.Usuarios;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Chokolate
 */
public class Gestion {

    public boolean insertarUsuario(String usuario, String apellido, String contraseña, String correo1, String correo2, String tel1, String tel2, String nombre) {
        return false;
    }

    public boolean iniciarSesion(String db, String user, String pass) {
        switch (db) {
            case "maria":
                UsuariosJpaController controller = new UsuariosJpaController(Persistence.createEntityManagerFactory("pu_personaapp"));
                Usuarios u = controller.findUsuarios(user);
                return u != null && u.getUsuario().equals(user) && u.getContasenia().equals(pass);
            case "cassandra":
                UsuarioDao usuarioDAO = new UsuarioDao();
                Usuarios u1 = usuarioDAO.buscarUsuarioPorUsaerName(user);
                return u1 != null && u1.getUsuario().equals(user) && u1.getContasenia().equals(pass);
            default:
                return false;
        }
    }

    public static void insertarUsuario(String apellido, String contraseña, String genero, String nombre,
            String user, int edad, String tel1, String tel2, String cor1, String cor2) {

        try {
            Usuarios usuario = new Usuarios();
            usuario.setUsuario(user);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setContasenia(contraseña);
            usuario.setEdad(edad);
            usuario.setGenero(genero);

            UsuariosJpaController ujc = new UsuariosJpaController(Persistence.createEntityManagerFactory("pu_personaapp"));
            ujc.create(usuario);

            Telefonos telefono1 = new Telefonos();
            telefono1.setNumero(tel1);
            telefono1.setUsuario(usuario);

            TelefonosJpaController tjc = new TelefonosJpaController(Persistence.createEntityManagerFactory("pu_personaapp"));
            tjc.create(telefono1);

            Telefonos telefono2 = new Telefonos();
            telefono2.setNumero(tel2);
            telefono2.setUsuario(usuario);
            tjc.create(telefono2);

            Correos correo1 = new Correos();
            correo1.setDireccion(cor1);
            correo1.setUsuario(usuario);

            CorreosJpaController cjc = new CorreosJpaController(Persistence.createEntityManagerFactory("pu_personaapp"));
            cjc.create(correo1);

            Correos correo2 = new Correos();
            correo2.setDireccion(cor2);
            correo2.setUsuario(usuario);
            cjc.create(correo2);
            /*List<Usuarios> ListaUsuarios = ujc.findUsuariosEntities();
            for (int i = 0; i < ListaUsuarios.size(); i++) {
                System.out.println(ListaUsuarios+"\n");
            }
             */
        } catch (Exception ex) {
            System.out.println("no se pudo crear");
            //Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    String mensaje = "";

    public String BorrarUsuario(String usuario) {
        try {

            UsuariosJpaController ujc = new UsuariosJpaController(Persistence.createEntityManagerFactory("pu_personaapp"));
            ujc.destroy(usuario);
            mensaje = "eliminado con exito";
        } catch (Exception e) {
            System.out.println("Mensaje en guardar" + e.getMessage());
            mensaje = "No se pudo eliminar el usuario";
        }
        return mensaje;
    }

/////////////////////////////////IMPORTANTE/////////////////////////IMPORTANTE/////////////////////////////IMPORTANTE/////////////////
    public static void telefonos(String tel1, String tel2) {
        try {

            Usuarios usuario = new Usuarios();
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            Telefonos telefono1 = new Telefonos();
            telefono1.setNumero(tel1);
            telefono1.setUsuario(usuario);
            TelefonosJpaController tjc = new TelefonosJpaController(Persistence.createEntityManagerFactory("pu_personaapp"));
            tjc.create(telefono1);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            Telefonos telefono2 = new Telefonos();
            telefono2.setNumero(tel2);
            telefono2.setUsuario(usuario);
            tjc.create(telefono2);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            List<Telefonos> telefonosDelUsuario = new ArrayList<>();
            telefonosDelUsuario.add(telefono1);
            telefonosDelUsuario.add(telefono2);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            usuario.setTelefonosList(telefonosDelUsuario);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        } catch (Exception e) {
            System.out.println("no se pudo");
        }
    }
/////////////////////////////////IMPORTANTE/////////////////////////IMPORTANTE/////////////////////////////IMPORTANTE/////////////////  
    
    
/////////////////////////////////IMPORTANTE/////////////////////////IMPORTANTE/////////////////////////////IMPORTANTE/////////////////
    public static void correos(String cor1, String cor2) {
        try {

            Usuarios usuario = new Usuarios();
//////////////////////////////////////////////////////////            
            Correos correo1 = new Correos();
            correo1.setDireccion(cor1);
            correo1.setUsuario(usuario);
//////////////////////////////////////////////////////////
            CorreosJpaController cjc = new CorreosJpaController(Persistence.createEntityManagerFactory("pu_personaapp"));
            cjc.create(correo1);
/////////////////////////////////////////////////////////////////////////////////
            Correos correo2 = new Correos();
            correo2.setDireccion(cor2);
            correo2.setUsuario(usuario);
            cjc.create(correo2);
///////////////////////////////////////////////////////////
            List<Correos> correosDelUsuario = new ArrayList<>();
            correosDelUsuario.add(correo1);
            correosDelUsuario.add(correo2);
////////////////////////////////////////////////////////
            usuario.setCorreosList(correosDelUsuario);
///////////////////////////////////////////////////////////////////
        } catch (Exception e) {
            System.out.println("no se pudo");

        }
    }
/////////////////////////////////IMPORTANTE/////////////////////////IMPORTANTE/////////////////////////////IMPORTANTE/////////////////
    
/////////////////////////////////IMPORTANTE/////////////////////////IMPORTANTE/////////////////////////////IMPORTANTE/////////////////  
    
    public String ActualizarUsuario(String nombre, String apellido, int edad, String tel1, String tel2, String cor1, String cor2, String genero, String user, String contraseña) {

        try {
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            UsuariosJpaController ujc = new UsuariosJpaController(Persistence.createEntityManagerFactory("pu_personaapp"));
            Usuarios usuario = ujc.findUsuarios(user);
            System.out.println(user);
            usuario.setApellido(apellido);
            usuario.setContasenia(contraseña);
            usuario.setEdad(edad);
            usuario.setGenero(genero);
            usuario.setNombre(nombre);
/*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            Telefonos telefono1 = new Telefonos();
            telefono1.setNumero(tel1);
            telefono1.setUsuario(usuario);
 
            Telefonos telefono2 = new Telefonos();
            telefono2.setNumero(tel2);
            telefono2.setUsuario(usuario);
            ujc.create(usuario);
            List<Telefonos> telefonosDelUsuario = new ArrayList<>();
            telefonosDelUsuario.add(telefono1);
            telefonosDelUsuario.add(telefono2);
            usuario.setTelefonosList(telefonosDelUsuario);
            ujc.edit(usuario);
            
            Correos correo1 = new Correos();
            correo1.setDireccion(cor1);
            correo1.setUsuario(usuario);
 
            Correos correo2 = new Correos();
            correo2.setDireccion(cor1);
            correo2.setUsuario(usuario);
            
            ujc.create(usuario);
            List<Correos> correosDelUsuario = new ArrayList<>();
            correosDelUsuario.add(correo1);
            correosDelUsuario.add(correo2);
            usuario.setCorreosList(correosDelUsuario);
*/
            ujc.edit(usuario);
            mensaje = "actualizado con exito";
/////////////////////////////////IMPORTANTE/////////////////////////IMPORTANTE/////////////////////////////IMPORTANTE/////////////////
        } catch (Exception ex) {
            Logger.getLogger(Gestion.class.getName()).log(Level.SEVERE, null, ex);
            mensaje = "actualizado mal";
        } finally {
            return mensaje;
        }
    }
}
