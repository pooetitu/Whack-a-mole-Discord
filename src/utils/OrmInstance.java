package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class OrmInstance {
	private OrmInstance() {
		super();
	}

	private static EntityManager em;

	public static void init() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("mole");
		em = emf.createEntityManager();
	}

	public static void persist(Object o) {
		em.getTransaction().begin();
		em.persist(o);
		em.getTransaction().commit();
	}

	public static <T> T getObject(Class<T> t, String id) {
		em.getTransaction().begin();
		T o = em.find(t, id);
		em.getTransaction().commit();
		return o;
	}

	public static <T> boolean objectExists(Class<T> t, String id) {
		em.getTransaction().begin();
		long i = (Long) em.createQuery("Select count(o) from " + t.getName() + " o where id='" + id + "'")
				.getSingleResult();
		em.getTransaction().commit();
		return (i != 0 ? true : false);
	}

	public static <T> void remove(Class<T> t,String id) {
		em.getTransaction().begin();
		em.remove(em.find(t,id));
		em.getTransaction().commit();
	}

	public static void update(Object o) {
		em.getTransaction().begin();
		em.merge(o);
		em.getTransaction().commit();
	}
}
