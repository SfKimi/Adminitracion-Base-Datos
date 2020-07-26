/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.adb.personaapp.controlador.persistencia.dao;

import static co.edu.usbbog.adb.personaapp.modelo.Correos_.usuario;
import co.edu.usbbog.adb.personaapp.modelo.Usuarios;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miguel Rodriguez
 */
public class UsuarioDao {

    ConexionCassandra cassandra;

    public UsuarioDao() {
        this.cassandra = new ConexionCassandra();;
    }

    public boolean insertarUsuario(String nombre, String apellido, int edad, String tel1, String tel2,String cor1, String cor2, String genero, String user, String contraseña) {
        if (cassandra.conectar()) {
            System.out.println("llego ");
            Usuarios usuario =new Usuarios();
            //String query = "Insert into personaapp.usuarios(nombre,apellido,edad,telefono,correo,usuario,contrasenia) values('" + usuario.getNombre() + "','" + usuario.getApellido() + "',"+usuario.getEdad()+",'{'"+ usuario.getTelefonosList().get(0)+"','"+usuario.getTelefonosList().get(1)+"'},{'"+usuario.getCorreosList().get(0)+"','"+usuario.getCorreosList().get(1) + "'},'"+usuario.getGenero()+"','"+usuario.getUsuario()+"','"+usuario.getContasenia()+"")";
            String query="Insert into personaapp.usuarios(nombre,apellido,edad,telefono,correo,genero,usuario,contrasenia) values('" + nombre + "','" + apellido + "',"+edad+",{'"+ tel1+"','"+tel2+"'},{'"+cor1+"','"+cor2 + "'},'"+genero+"','"+user+"','"+contraseña+"')";
            ResultSet rs = cassandra.getCluster().newSession().execute(query);
            List<Row> datos = rs.all();
            cassandra.desconectar();
            if (datos.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public boolean EditarUsuario(String nombre, String apellido, int edad, String tel1, String tel2,String cor1, String cor2, String genero, String user, String contraseña) {
        if (cassandra.conectar()) {
            Usuarios usuario =new Usuarios();
            String query="UPDATE personaapp.usuarios SET apellido ='"+apellido+"',contrasenia='"+contraseña+"',correo={'"+cor1+"','"+cor2+"'},genero='"+genero+"',edad="+edad+",nombre='"+nombre+"',telefono={'"+tel1+"','"+tel2+"'} where usuario='"+user+"';";            
            System.out.println(query);
            ResultSet rs = cassandra.getCluster().newSession().execute(query);
            List<Row> datos = rs.all();
            cassandra.desconectar();
            if (datos.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
        public boolean eliminarUsuario(String user) {
        if (cassandra.conectar()) {
            Usuarios usuario =new Usuarios();
            String query="delete from  personaapp.usuarios where usuario='"+user+"';";            
            ResultSet rs = cassandra.getCluster().newSession().execute(query);
            List<Row> datos = rs.all();
            cassandra.desconectar();
            if (datos.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Usuarios deRowAUsuarios(Row row){
        Usuarios usuario = new Usuarios();
        usuario.setUsuario(row.get("usuario",String.class));
        usuario.setContasenia(row.get("contrasenia",String.class));
        return usuario;
    }
    
    public List<Usuarios> buscarTodos(){
        if (cassandra.conectar()) {
            String query = "Select * from personaapp.usuarios";
            ResultSet rs = cassandra.getCluster().newSession().execute(query);
            cassandra.desconectar();
           List<Usuarios> usuarios = new ArrayList<>();
            List<Row> datos = rs.all();
            for (Row dato : datos) {
                usuarios.add(deRowAUsuarios(dato));
            }
            return usuarios;
        } else {
            return null;
        }
    }
    public Usuarios buscarUsuarioPorUsaerName(String user){
        List<Usuarios> usuarios = buscarTodos();
        for (Usuarios usuario : usuarios) {
            if(usuario.getUsuario().equals(user)){
                return usuario;                
            }
        }
        return null;
    }
}
