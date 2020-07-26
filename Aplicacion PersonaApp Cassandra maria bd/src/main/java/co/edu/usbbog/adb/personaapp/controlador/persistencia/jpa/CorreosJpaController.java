/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa;

import co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa.exceptions.NonexistentEntityException;
import co.edu.usbbog.adb.personaapp.controlador.persistencia.jpa.exceptions.PreexistingEntityException;
import co.edu.usbbog.adb.personaapp.modelo.Correos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.usbbog.adb.personaapp.modelo.Usuarios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Miguel Rodriguez
 */
public class CorreosJpaController implements Serializable {

    public CorreosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Correos correos) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuario = correos.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getUsuario());
                correos.setUsuario(usuario);
            }
            em.persist(correos);
            if (usuario != null) {
                usuario.getCorreosList().add(correos);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCorreos(correos.getDireccion()) != null) {
                throw new PreexistingEntityException("Correos " + correos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Correos correos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Correos persistentCorreos = em.find(Correos.class, correos.getDireccion());
            Usuarios usuarioOld = persistentCorreos.getUsuario();
            Usuarios usuarioNew = correos.getUsuario();
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getUsuario());
                correos.setUsuario(usuarioNew);
            }
            correos = em.merge(correos);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getCorreosList().remove(correos);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getCorreosList().add(correos);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = correos.getDireccion();
                if (findCorreos(id) == null) {
                    throw new NonexistentEntityException("The correos with id " + id + " no longer exists.");
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
            Correos correos;
            try {
                correos = em.getReference(Correos.class, id);
                correos.getDireccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The correos with id " + id + " no longer exists.", enfe);
            }
            Usuarios usuario = correos.getUsuario();
            if (usuario != null) {
                usuario.getCorreosList().remove(correos);
                usuario = em.merge(usuario);
            }
            em.remove(correos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Correos> findCorreosEntities() {
        return findCorreosEntities(true, -1, -1);
    }

    public List<Correos> findCorreosEntities(int maxResults, int firstResult) {
        return findCorreosEntities(false, maxResults, firstResult);
    }

    private List<Correos> findCorreosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Correos.class));
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

    public Correos findCorreos(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Correos.class, id);
        } finally {
            em.close();
        }
    }

    public int getCorreosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Correos> rt = cq.from(Correos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
