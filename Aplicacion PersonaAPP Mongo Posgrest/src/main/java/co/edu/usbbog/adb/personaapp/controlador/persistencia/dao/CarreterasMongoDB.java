/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.adb.personaapp.controlador.persistencia.dao;

import co.edu.usbbog.dbd.carreterasapp.modelo.Carreteras;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author 305
 */
public class CarreterasMongoDB {
    
   
    MongoDBConexion mongoDBConexion =new MongoDBConexion();
    
    public void ingresarCarretera(Carreteras carretera){
        MongoCollection<Document> coll = mongoDBConexion.getDatabase().getCollection("Carreteras");
        Document document = new Document("_id", carretera.getIdCarreteras()+"")                
               .append("Categoria", carretera.getCategoria())
               .append("Tamanio", carretera.getTamanio());
        
        coll.insertOne(document);
    } 
    public FindIterable<Document> bsucarTodos(){
        MongoCollection<Document> coll = mongoDBConexion.getDatabase().getCollection("Carreteras");
        return coll.find();
    }
}
