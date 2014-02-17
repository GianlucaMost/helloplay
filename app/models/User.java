package models;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

/**
 * User entity managed by JPA
 */
@Entity 
@SequenceGenerator(name = "user_seq", sequenceName = "user_seq")
public class User
{  
	@Id													//id der tbl
	@GeneratedValue(strategy=GenerationType.AUTO)		// autoincrement
    public Long id;
    
    @Constraints.Required
    public String name;
    
    @Constraints.Required
    public String password;
	
    /**
     * Find an user by id.
     */
    public static User findById(Long id)
    {
    	EntityManager em = JPA.em();
        return em.find(User.class, id);
    }
    
    /**
     * Get all users.
     */
    public static Collection<User> findAll()
    {
        Query query = JPA.em().createQuery("SELECT u FROM User u");
        return (Collection<User>) query.getResultList();
    }
    
    /**
     * Add a new user.
     * @param name
     */
    public static void add(String name, String pw)
    {
    	EntityManager em = JPA.em();   	
    	User user = new User();
    	user.name=name;
    	user.password=pw;
    	
    	Query query = JPA.em().createQuery("SELECT u FROM User u where name = :pName ");
    	query.setParameter("pName", name);
    	
    	if(query.getResultList().isEmpty())
    	{
    		user.delete();
    	}else
    	{
    		em.persist(user);
    	}
    	
    	//2do hier muss ich mir noch was anderes ueberlegen, um nur den namen zu checken
//    	if (findAll().contains(user)==true)
//    	{
//    		user.delete();
//    	}else
//    	{
//    		em.persist(user);
//    	}

    }
    
    /**
     * Update this user.
     * @param name
     */
    public void update(String name, String pw)
    {
    	EntityManager em = JPA.em();
		User user = em.find(User.class, this.id);
    	user.name = name;
    	user.password = pw;
    	em.persist(user);
    }
 
    /**
     * Delete this user.
     */
    public void delete()
    {
        JPA.em().remove(this);
    }
    
//    public String validate() {
//        if (User.authenticate(email, password) == null) {
//          return "Invalid user or password";
//        }
//        return null;
//    }
    
    
}
