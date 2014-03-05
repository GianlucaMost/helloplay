package models;

import java.util.Collection;
import java.util.List;

import org.mindrot.jbcrypt.*;

import javax.persistence.*;

import models.*;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

/**
 * User entity managed by JPA
 */
@Entity
@Table(name="user")
public class User {  
	@Id													// id der tbl
	@GeneratedValue(strategy=GenerationType.AUTO)		// autoincrement
	@Column(name="uid")
    public int uid;
	
	@Column(name="fk_trid")
	public int fk_trid=0;
    
    @Constraints.Required
    public String name;
    
    @Constraints.Required
    public String pw;
    
    @Constraints.Required
    public int punkte=0;
    
    @Constraints.Required
    public byte admin=0;
    
    /**
     * default constuctor
     */
    public User(){
    	
    }
    
    /**
     * Konstruktor
     * @param name
     * @param pw
     */
    public User(String name, String pw){
    	this.name=name;
    	this.pw=BCrypt.hashpw(pw, BCrypt.gensalt());
    }
    
    /**
     * Konstruktor
     * @param name
     * @param pw
     * @param admin
     */
    public User(String name, String pw, byte admin){
    	this.name=name;
    	this.pw=BCrypt.hashpw(pw, BCrypt.gensalt());
    	this.admin=admin;
    }
    
    /**
     * check username, when the name already exist, 'true' is returned, otherwise 'false'
     */
    @Transactional(readOnly=true)
    public static boolean userExist(String name) {
    	try {
	    	Query query = JPA.em().createQuery("SELECT u.name FROM User u WHERE u.name = :pName");
	    	query.setParameter("pName", name);
	    	String tmp = (String) query.getSingleResult();
	    	return true;
    	} catch (NoResultException ex) {
    		return false;
    	}
    }
	
    /**
     * Find an user by id.
     */
    @Transactional(readOnly=true)
    public static User findById(int id) {
    	return JPA.em().find(User.class, id);
    }
    
    /**
     * find user by name
     * @param name
     * @return
     */
    @Transactional
    public static User findByName(String name) {
    	if (User.userExist(name)) {
    		Query query = JPA.em().createQuery("SELECT u FROM User u WHERE u.name = :pName");
	    	query.setParameter("pName", name);
	    	return (User) query.getSingleResult();
    	}else {
    		return null;
    	}
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
     * non static user persisting
     * @param user
     */
    @Transactional
    public void add() {
		JPA.em().persist(this);
    }
    
    /**
     * Add an user
     * @param user
     */
    @Transactional
    public static void add(User user) {
		JPA.em().persist(user);
    }
    
    
    /**
     * Add a new user.
     * @param name
     */
    @Transactional
    public static void add(String name, String pwHash) {
		EntityManager em = JPA.em();
    	User user = new User();
    	user.name=name;
    	user.pw=pwHash;
		em.persist(user);
    }
    
    /**
     * Update this user.
     * @param name
     */
    @Transactional
    public void update(String name, String pwHash) {
    	EntityManager em = JPA.em();
		User user = em.find(User.class, this.uid);
    	user.name = name;
    	user.pw = pwHash;
    	em.persist(user);
    }
 
    /**
     * Delete this user.
     */
    @Transactional
    public void delete(){
        JPA.em().remove(this);
    }
    
    /**
     * validate a user
     * @param name
     * @param pw
     * @return
     */
    @Transactional
    public static boolean validate(String name, String pw) {
    	try {
	    	Query query = JPA.em().createQuery("SELECT u FROM User u WHERE u.name = :pName");
	    	query.setParameter("pName", name);
	    	User user = (User) query.getSingleResult();
	    	return BCrypt.checkpw(pw, user.pw);
    	} catch (NoResultException ex) {
    		return false;
    	}
    }
    
    
}
