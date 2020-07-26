/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.adb.personaapp.controlador.persistencia.dao;

import co.edu.usbbog.adb.personaapp.modelo.Correo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 *
 * @author 305
 */
public class CorreoDaoMongoDB {
    
      MongoDBConexion mongoDBConexion =new MongoDBConexion();
    
    public void ingresarCorreo(Correo correo){
        MongoCollection<Document> coll = mongoDBConexion.getDatabase().getCollection("MVC");
        Document document = new Document("_id", correo.getCorreo()+"")                
               .append("Usuario", correo.getUsuario())
                ;
        
        coll.insertOne(document);
    }
    
    
    
     public FindIterable<Document> bsucarTodos(){
        MongoCollection<Document> coll = mongoDBConexion.getDatabase().getCollection("MVC");
        return coll.find();
    }
    
    
    
    
}
