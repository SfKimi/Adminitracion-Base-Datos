/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.adb.personaapp.vista;

import java.awt.BorderLayout;

/**
 *
 * @author Chokolate
 */
public class Principal extends javax.swing.JFrame {

    private String sgdb;
    private Login login;
    private Menu menu;
    private Modificar modificar;
    private Registro registro;

    public Principal() {
        // /*
        this.setVisible(true);
        this.setTitle("Persona App");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        sgdb = "";
        iniciar();

    }

    public String getSgdb() {
        return sgdb;
    }

    public void setSgdb(String sgdb) {
        this.sgdb = sgdb;
    }

    public static void main(String args[]) {
        Principal p = new Principal();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        query1 = java.beans.Beans.isDesignTime() ? null : ((javax.persistence.EntityManager)null).createQuery("");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.persistence.Query query1;
    // End of variables declaration//GEN-END:variables

    public void iniciar() {
        this.login = new Login(this);
        this.login.setVisible(true);
        this.add(this.login);
        this.pack();
        this.setLocationRelativeTo(null);

    }

    public void deLoginAMenu(Login login) {
        this.remove(login);
        this.menu = new Menu(this);
        this.menu.setVisible(true);
        this.add(this.menu);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void deLoginARegistro(Login login) {
        this.remove(login);
        this.registro = new Registro(this);
        this.registro.setVisible(true);
        this.add(this.registro);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void deRegistroALogin(Registro registro) {
        this.remove(registro);
        this.login = new Login(this);
        this.login.setVisible(true);
        this.add(this.login);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void deMenuALogin(Menu menu) {
        this.remove(menu);
        this.login = new Login(this);
        this.login.setVisible(true);
        this.add(this.login);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void deMenuAmodificar(Menu menu) {
        this.remove(menu);
        this.modificar = new Modificar(this);
        this.modificar.setVisible(true);
        this.add(this.modificar);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void deModificarAMenu(Modificar modificar) {
        this.remove(modificar);
        this.menu = new Menu(this);
        this.menu.setVisible(true);
        this.add(this.menu);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public Login getLogin() {
        return login;
    }

    public Menu getMenu() {
        return menu;
    }

}
