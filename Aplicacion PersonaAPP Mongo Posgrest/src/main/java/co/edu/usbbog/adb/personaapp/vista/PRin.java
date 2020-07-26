/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.adb.personaapp.vista;

import co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa.UsuarioJpaController;
import co.edu.usbbog.adb.personaapp.modelo.Usuario;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;

/**
 *
 * @author 305
 */
public class PRin {
  
    public static void main(String[] args) {
        
        try {
            
            /*Usuario usuario = new Usuario();
            usuario.setUsuario("Admin");
            usuario.setNombre("haydin");
            usuario.setApellido("mora");
            usuario.setEdad(18);
            usuario.setGenero("masculino");
            usuario.setContrasenia("haydin");
            */
            UsuarioJpaController ujc= new UsuarioJpaController(Persistence.createEntityManagerFactory("pu_personaapp"));
            //ujc.create(usuario);
            List<Usuario> listUsuarios=ujc.findUsuarioEntities();
            for (Usuario listUsuario : listUsuarios) {
                System.out.println(listUsuario.getUsuario());
                System.out.println(listUsuario.getNombre());
                System.out.println(listUsuario.getApellido());
                System.out.println(listUsuario.getEdad());
                System.out.println(listUsuario.getGenero());
                System.out.println(listUsuario.getContrasenia());
            }
        } catch (Exception ex) {
            System.out.println("La persona no fue creada");
            Logger.getLogger(PRin.class.getName()).log(Level.SEVERE, null, ex);
        
        
        
    }
    
}
}
    
        
