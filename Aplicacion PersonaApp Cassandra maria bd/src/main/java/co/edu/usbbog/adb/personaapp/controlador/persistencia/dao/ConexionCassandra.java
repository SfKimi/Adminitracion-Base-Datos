/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.adb.personaapp.controlador.persistencia.dao;

import com.datastax.driver.core.Cluster;


/**
 *
 * @author Miguel Rodriguez
 */
public class ConexionCassandra {
    Cluster cluster;

    public ConexionCassandra() {
        this.cluster = new Cluster.Builder().addContactPoints("192.168.37.2").withPort(9042).withCredentials("cassandra", "cassandra").build();
            }

    public Cluster getCluster() {
        return cluster;
    }
    
    public boolean conectar(){
        this.cluster.connect("personaapp");
        if (this.cluster.isClosed()) {
            return false;
        } else {
            System.out.println(getCluster().getConfiguration());
            return true;
        }
    }
    public boolean desconectar(){
        this.cluster.close();
        if (this.cluster.isClosed()) {
            return true;
        } else {
            return false;
        }
    }
    
}
