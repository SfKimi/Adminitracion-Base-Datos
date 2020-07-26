/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa;

import co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa.exceptions.NonexistentEntityException;
import co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa.exceptions.PreexistingEntityException;
import co.edu.usbbog.adb.personaapp.modelo.Correo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.usbbog.adb.personaapp.modelo.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author 305
 */
public class CorreoJpaController implements Serializable {

    public CorreoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Correo correo) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = correo.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getUsuario());
                correo.setUsuario(usuario);
            }
            em.persist(correo);
            if (usuario != null) {
                usuario.getCorreoList().add(correo);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCorreo(correo.getCorreo()) != null) {
                throw new PreexistingEntityException("Correo " + correo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Correo correo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Correo persistentCorreo = em.find(Correo.class, correo.getCorreo());
            Usuario usuarioOld = persistentCorreo.getUsuario();
            Usuario usuarioNew = correo.getUsuario();
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getUsuario());
                correo.setUsuario(usuarioNew);
            }
            correo = em.merge(correo);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getCorreoList().remove(correo);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getCorreoList().add(correo);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = correo.getCorreo();
                if (findCorreo(id) == null) {
                    throw new NonexistentEntityException("The correo with id " + id + " no longer exists.");
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
            Correo correo;
            try {
                correo = em.getReference(Correo.class, id);
                correo.getCorreo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The correo with id " + id + " no longer exists.", enfe);
            }
            Usuario usuario = correo.getUsuario();
            if (usuario != null) {
                usuario.getCorreoList().remove(correo);
                usuario = em.merge(usuario);
            }
            em.remove(correo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Correo> findCorreoEntities() {
        return findCorreoEntities(true, -1, -1);
    }

    public List<Correo> findCorreoEntities(int maxResults, int firstResult) {
        return findCorreoEntities(false, maxResults, firstResult);
    }

    private List<Correo> findCorreoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Correo.class));
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

    public Correo findCorreo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Correo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCorreoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Correo> rt = cq.from(Correo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
