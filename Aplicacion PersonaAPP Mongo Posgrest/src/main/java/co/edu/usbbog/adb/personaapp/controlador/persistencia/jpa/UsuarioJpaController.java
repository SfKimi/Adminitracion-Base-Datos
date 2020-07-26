/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa;

import co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa.exceptions.NonexistentEntityException;
import co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.usbbog.adb.personaapp.modelo.Correo;
import java.util.ArrayList;
import java.util.List;
import co.edu.usbbog.adb.personaapp.modelo.Telefono;
import co.edu.usbbog.adb.personaapp.modelo.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author 305
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getCorreoList() == null) {
            usuario.setCorreoList(new ArrayList<Correo>());
        }
        if (usuario.getTelefonoList() == null) {
            usuario.setTelefonoList(new ArrayList<Telefono>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Correo> attachedCorreoList = new ArrayList<Correo>();
            for (Correo correoListCorreoToAttach : usuario.getCorreoList()) {
                correoListCorreoToAttach = em.getReference(correoListCorreoToAttach.getClass(), correoListCorreoToAttach.getCorreo());
                attachedCorreoList.add(correoListCorreoToAttach);
            }
            usuario.setCorreoList(attachedCorreoList);
            List<Telefono> attachedTelefonoList = new ArrayList<Telefono>();
            for (Telefono telefonoListTelefonoToAttach : usuario.getTelefonoList()) {
                telefonoListTelefonoToAttach = em.getReference(telefonoListTelefonoToAttach.getClass(), telefonoListTelefonoToAttach.getNumero());
                attachedTelefonoList.add(telefonoListTelefonoToAttach);
            }
            usuario.setTelefonoList(attachedTelefonoList);
            em.persist(usuario);
            for (Correo correoListCorreo : usuario.getCorreoList()) {
                Usuario oldUsuarioOfCorreoListCorreo = correoListCorreo.getUsuario();
                correoListCorreo.setUsuario(usuario);
                correoListCorreo = em.merge(correoListCorreo);
                if (oldUsuarioOfCorreoListCorreo != null) {
                    oldUsuarioOfCorreoListCorreo.getCorreoList().remove(correoListCorreo);
                    oldUsuarioOfCorreoListCorreo = em.merge(oldUsuarioOfCorreoListCorreo);
                }
            }
            for (Telefono telefonoListTelefono : usuario.getTelefonoList()) {
                Usuario oldUsuarioOfTelefonoListTelefono = telefonoListTelefono.getUsuario();
                telefonoListTelefono.setUsuario(usuario);
                telefonoListTelefono = em.merge(telefonoListTelefono);
                if (oldUsuarioOfTelefonoListTelefono != null) {
                    oldUsuarioOfTelefonoListTelefono.getTelefonoList().remove(telefonoListTelefono);
                    oldUsuarioOfTelefonoListTelefono = em.merge(oldUsuarioOfTelefonoListTelefono);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getUsuario());
            List<Correo> correoListOld = persistentUsuario.getCorreoList();
            List<Correo> correoListNew = usuario.getCorreoList();
            List<Telefono> telefonoListOld = persistentUsuario.getTelefonoList();
            List<Telefono> telefonoListNew = usuario.getTelefonoList();
            List<Correo> attachedCorreoListNew = new ArrayList<Correo>();
            for (Correo correoListNewCorreoToAttach : correoListNew) {
                correoListNewCorreoToAttach = em.getReference(correoListNewCorreoToAttach.getClass(), correoListNewCorreoToAttach.getCorreo());
                attachedCorreoListNew.add(correoListNewCorreoToAttach);
            }
            correoListNew = attachedCorreoListNew;
            usuario.setCorreoList(correoListNew);
            List<Telefono> attachedTelefonoListNew = new ArrayList<Telefono>();
            for (Telefono telefonoListNewTelefonoToAttach : telefonoListNew) {
                telefonoListNewTelefonoToAttach = em.getReference(telefonoListNewTelefonoToAttach.getClass(), telefonoListNewTelefonoToAttach.getNumero());
                attachedTelefonoListNew.add(telefonoListNewTelefonoToAttach);
            }
            telefonoListNew = attachedTelefonoListNew;
            usuario.setTelefonoList(telefonoListNew);
            usuario = em.merge(usuario);
            for (Correo correoListOldCorreo : correoListOld) {
                if (!correoListNew.contains(correoListOldCorreo)) {
                    correoListOldCorreo.setUsuario(null);
                    correoListOldCorreo = em.merge(correoListOldCorreo);
                }
            }
            for (Correo correoListNewCorreo : correoListNew) {
                if (!correoListOld.contains(correoListNewCorreo)) {
                    Usuario oldUsuarioOfCorreoListNewCorreo = correoListNewCorreo.getUsuario();
                    correoListNewCorreo.setUsuario(usuario);
                    correoListNewCorreo = em.merge(correoListNewCorreo);
                    if (oldUsuarioOfCorreoListNewCorreo != null && !oldUsuarioOfCorreoListNewCorreo.equals(usuario)) {
                        oldUsuarioOfCorreoListNewCorreo.getCorreoList().remove(correoListNewCorreo);
                        oldUsuarioOfCorreoListNewCorreo = em.merge(oldUsuarioOfCorreoListNewCorreo);
                    }
                }
            }
            for (Telefono telefonoListOldTelefono : telefonoListOld) {
                if (!telefonoListNew.contains(telefonoListOldTelefono)) {
                    telefonoListOldTelefono.setUsuario(null);
                    telefonoListOldTelefono = em.merge(telefonoListOldTelefono);
                }
            }
            for (Telefono telefonoListNewTelefono : telefonoListNew) {
                if (!telefonoListOld.contains(telefonoListNewTelefono)) {
                    Usuario oldUsuarioOfTelefonoListNewTelefono = telefonoListNewTelefono.getUsuario();
                    telefonoListNewTelefono.setUsuario(usuario);
                    telefonoListNewTelefono = em.merge(telefonoListNewTelefono);
                    if (oldUsuarioOfTelefonoListNewTelefono != null && !oldUsuarioOfTelefonoListNewTelefono.equals(usuario)) {
                        oldUsuarioOfTelefonoListNewTelefono.getTelefonoList().remove(telefonoListNewTelefono);
                        oldUsuarioOfTelefonoListNewTelefono = em.merge(oldUsuarioOfTelefonoListNewTelefono);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<Correo> correoList = usuario.getCorreoList();
            for (Correo correoListCorreo : correoList) {
                correoListCorreo.setUsuario(null);
                correoListCorreo = em.merge(correoListCorreo);
            }
            List<Telefono> telefonoList = usuario.getTelefonoList();
            for (Telefono telefonoListTelefono : telefonoList) {
                telefonoListTelefono.setUsuario(null);
                telefonoListTelefono = em.merge(telefonoListTelefono);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
