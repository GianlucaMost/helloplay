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
import play.db.jpa.Transactional;

/**
 * User entity managed by JPA
 */
@Entity 
@SequenceGenerator(name = "user_seq", sequenceName = "user_seq")
public class User{  
	@Id													//id der tbl
	@GeneratedValue(strategy=GenerationType.AUTO)		// autoincrement
    public Long id;
    
    @Constraints.Required
    public String name;
    
    @Constraints.Required
    public String password;
    
    /**
     * check username, when the name already exist, 'true' is returned, otherwise 'false'
     */
    @Transactional(readOnly=true)
    public static boolean userExist(String name) {
    	EntityManager em = JPA.em();
    	
    	Query query = JPA.em().createQuery("SELECT u FROM User u WHERE u.name = :pName");
    	query.setParameter("pName", name);
    	Collection<User> coll = query.getResultList();
    	
    	return !coll.isEmpty();
    }
	
    /**
     * Find an user by id.
     */
    @Transactional(readOnly=true)
    public static User findById(Long id){
    	EntityManager em = JPA.em();
    	return em.find(User.class, id);
    }
    
    /**
     * Get all users.
     */
    @Transactional(readOnly=true)
    public static Collection<User> findAll(){
        Query query = JPA.em().createQuery("SELECT u FROM User u");
        return (Collection<User>) query.getResultList();
    }
    
    /**
     * Add a new user.
     * @param name
     */
    @Transactional
    public static void add(String name, String pw) {
    	if(!userExist(name)) {
    		EntityManager em = JPA.em();   	
        	User user = new User();
        	user.name=name;
        	user.password=pw;
    		em.persist(user);
    	}
    }
    
    /**
     * Update this user.
     * @param name
     */
    @Transactional
    public void update(String name, String pw){
    	EntityManager em = JPA.em();
		User user = em.find(User.class, this.id);
    	user.name = name;
    	user.password = pw;
    	em.persist(user);
    }
 
    /**
     * Delete this user.
     */
    @Transactional
    public void delete(){
        JPA.em().remove(this);
    }
    
//    public String validate() {
//        if (User.authenticate(email, password) == null) {
//          return "Invalid user or password";
//        }
//        return null;
//    }
    
    
}