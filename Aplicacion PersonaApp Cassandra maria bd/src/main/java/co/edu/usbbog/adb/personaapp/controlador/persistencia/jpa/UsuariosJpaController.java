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
import co.edu.usbbog.adb.personaapp.modelo.Correos;
import java.util.ArrayList;
import java.util.List;
import co.edu.usbbog.adb.personaapp.modelo.Telefonos;
import co.edu.usbbog.adb.personaapp.modelo.Usuarios;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Miguel Rodriguez
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws PreexistingEntityException, Exception {
        if (usuarios.getCorreosList() == null) {
            usuarios.setCorreosList(new ArrayList<Correos>());
        }
        if (usuarios.getTelefonosList() == null) {
            usuarios.setTelefonosList(new ArrayList<Telefonos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Correos> attachedCorreosList = new ArrayList<Correos>();
            for (Correos correosListCorreosToAttach : usuarios.getCorreosList()) {
                correosListCorreosToAttach = em.getReference(correosListCorreosToAttach.getClass(), correosListCorreosToAttach.getDireccion());
                attachedCorreosList.add(correosListCorreosToAttach);
            }
            usuarios.setCorreosList(attachedCorreosList);
            List<Telefonos> attachedTelefonosList = new ArrayList<Telefonos>();
            for (Telefonos telefonosListTelefonosToAttach : usuarios.getTelefonosList()) {
                telefonosListTelefonosToAttach = em.getReference(telefonosListTelefonosToAttach.getClass(), telefonosListTelefonosToAttach.getNumero());
                attachedTelefonosList.add(telefonosListTelefonosToAttach);
            }
            usuarios.setTelefonosList(attachedTelefonosList);
            em.persist(usuarios);
            for (Correos correosListCorreos : usuarios.getCorreosList()) {
                Usuarios oldUsuarioOfCorreosListCorreos = correosListCorreos.getUsuario();
                correosListCorreos.setUsuario(usuarios);
                correosListCorreos = em.merge(correosListCorreos);
                if (oldUsuarioOfCorreosListCorreos != null) {
                    oldUsuarioOfCorreosListCorreos.getCorreosList().remove(correosListCorreos);
                    oldUsuarioOfCorreosListCorreos = em.merge(oldUsuarioOfCorreosListCorreos);
                }
            }
            for (Telefonos telefonosListTelefonos : usuarios.getTelefonosList()) {
                Usuarios oldUsuarioOfTelefonosListTelefonos = telefonosListTelefonos.getUsuario();
                telefonosListTelefonos.setUsuario(usuarios);
                telefonosListTelefonos = em.merge(telefonosListTelefonos);
                if (oldUsuarioOfTelefonosListTelefonos != null) {
                    oldUsuarioOfTelefonosListTelefonos.getTelefonosList().remove(telefonosListTelefonos);
                    oldUsuarioOfTelefonosListTelefonos = em.merge(oldUsuarioOfTelefonosListTelefonos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarios(usuarios.getUsuario()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getUsuario());
            List<Correos> correosListOld = persistentUsuarios.getCorreosList();
            List<Correos> correosListNew = usuarios.getCorreosList();
            List<Telefonos> telefonosListOld = persistentUsuarios.getTelefonosList();
            List<Telefonos> telefonosListNew = usuarios.getTelefonosList();
            List<Correos> attachedCorreosListNew = new ArrayList<Correos>();
            for (Correos correosListNewCorreosToAttach : correosListNew) {
                correosListNewCorreosToAttach = em.getReference(correosListNewCorreosToAttach.getClass(), correosListNewCorreosToAttach.getDireccion());
                attachedCorreosListNew.add(correosListNewCorreosToAttach);
            }
            correosListNew = attachedCorreosListNew;
            usuarios.setCorreosList(correosListNew);
            List<Telefonos> attachedTelefonosListNew = new ArrayList<Telefonos>();
            for (Telefonos telefonosListNewTelefonosToAttach : telefonosListNew) {
                telefonosListNewTelefonosToAttach = em.getReference(telefonosListNewTelefonosToAttach.getClass(), telefonosListNewTelefonosToAttach.getNumero());
                attachedTelefonosListNew.add(telefonosListNewTelefonosToAttach);
            }
            telefonosListNew = attachedTelefonosListNew;
            usuarios.setTelefonosList(telefonosListNew);
            usuarios = em.merge(usuarios);
            for (Correos correosListOldCorreos : correosListOld) {
                if (!correosListNew.contains(correosListOldCorreos)) {
                    correosListOldCorreos.setUsuario(null);
                    correosListOldCorreos = em.merge(correosListOldCorreos);
                }
            }
            for (Correos correosListNewCorreos : correosListNew) {
                if (!correosListOld.contains(correosListNewCorreos)) {
                    Usuarios oldUsuarioOfCorreosListNewCorreos = correosListNewCorreos.getUsuario();
                    correosListNewCorreos.setUsuario(usuarios);
                    correosListNewCorreos = em.merge(correosListNewCorreos);
                    if (oldUsuarioOfCorreosListNewCorreos != null && !oldUsuarioOfCorreosListNewCorreos.equals(usuarios)) {
                        oldUsuarioOfCorreosListNewCorreos.getCorreosList().remove(correosListNewCorreos);
                        oldUsuarioOfCorreosListNewCorreos = em.merge(oldUsuarioOfCorreosListNewCorreos);
                    }
                }
            }
            for (Telefonos telefonosListOldTelefonos : telefonosListOld) {
                if (!telefonosListNew.contains(telefonosListOldTelefonos)) {
                    telefonosListOldTelefonos.setUsuario(null);
                    telefonosListOldTelefonos = em.merge(telefonosListOldTelefonos);
                }
            }
            for (Telefonos telefonosListNewTelefonos : telefonosListNew) {
                if (!telefonosListOld.contains(telefonosListNewTelefonos)) {
                    Usuarios oldUsuarioOfTelefonosListNewTelefonos = telefonosListNewTelefonos.getUsuario();
                    telefonosListNewTelefonos.setUsuario(usuarios);
                    telefonosListNewTelefonos = em.merge(telefonosListNewTelefonos);
                    if (oldUsuarioOfTelefonosListNewTelefonos != null && !oldUsuarioOfTelefonosListNewTelefonos.equals(usuarios)) {
                        oldUsuarioOfTelefonosListNewTelefonos.getTelefonosList().remove(telefonosListNewTelefonos);
                        oldUsuarioOfTelefonosListNewTelefonos = em.merge(oldUsuarioOfTelefonosListNewTelefonos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuarios.getUsuario();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<Correos> correosList = usuarios.getCorreosList();
            for (Correos correosListCorreos : correosList) {
                correosListCorreos.setUsuario(null);
                correosListCorreos = em.merge(correosListCorreos);
            }
            List<Telefonos> telefonosList = usuarios.getTelefonosList();
            for (Telefonos telefonosListTelefonos : telefonosList) {
                telefonosListTelefonos.setUsuario(null);
                telefonosListTelefonos = em.merge(telefonosListTelefonos);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
