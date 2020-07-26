/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.adb.personaapp.controlador.persistencia.dao;
///////////////////////////////////////////////////////////////////////////////////////////
import co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa.exceptions.NonexistentEntityException;
import co.edu.usbbog.adb.personaapp.modelo.Correo;
import co.edu.usbbog.adb.personaapp.modelo.Telefono;
import co.edu.usbbog.adb.personaapp.modelo.Usuario;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import org.bson.Document;
////////////////////////////////////////////////////////////////////////////////////////


/**
 *
 * @author 305
 */
public class UsuarioDaoMongoDB {
    
    
    MongoDBConexion mongoDBConexion =new MongoDBConexion();
    
    
    public void ingresarUsuario(Usuario usuario){
        MongoCollection<Document> coll = mongoDBConexion.getDatabase().getCollection("MVC");
        Document document = new Document("_id", usuario.getUsuario()+"")                
               .append("Nombre", usuario.getNombre())
               .append("Apellido", usuario.getApellido())
                .append("Edad", usuario.getEdad())
                .append("Genero", usuario.getGenero())
                .append("Contrasenia", usuario.getContrasenia())
                .append("Correo", usuario.getCorreoList())
                .append("Telefono", usuario.getTelefonoList())
                ;
        
        coll.insertOne(document);
       
    }
    
    
    
     public FindIterable<Document> bsucarTodos(){
        MongoCollection<Document> coll = mongoDBConexion.getDatabase().getCollection("MVC");
        return coll.find();
    }
     
      
        //coll.updateOne('document', document)
     
     /////////////////////////////////////////////////////////////////////////////////////////////////////
      public void eliminarUsuario(Usuario usuario){
        MongoCollection<Document> coll = mongoDBConexion.getDatabase().getCollection("MVC");
        Document document = new Document("_id", usuario.getUsuario()+"")                
               .append("Nombre", usuario.getNombre())
               .append("Apellido", usuario.getApellido())
                .append("Edad", usuario.getEdad())
                .append("Genero", usuario.getGenero())
                .append("Contrasenia", usuario.getContrasenia())
                .append("Correo", usuario.getCorreoList())
                .append("Telefono", usuario.getTelefonoList())
                ;
        
        coll.deleteOne(document);
    }
 
    
}
