package models;

import java.util.ArrayList;
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
    
    @Constraints.Required
    @Column(name="name")
    public String name;
    
    @Constraints.Required
    @Column(name="pw")
    public String pw;
    
    @Constraints.Required
    @Column(name="punkte")
    public int punkte=0;
    
    @Constraints.Required
    @Column(name="admin")
    public byte admin=0;
    
    @OneToMany(mappedBy="user", targetEntity=Tipp.class)
    private Collection<Tipp> tipps;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="fk_trid", referencedColumnName="trid")
    private Trunde trunde;
    
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
    public User(String name, String pwHash){
    	this.name=name;
    	this.pw=pwHash;
    	this.trunde=Trunde.findById(0);
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
     * get TippRunde from this user
     * @return
     */
    public Trunde getTrunde(){
    	return this.trunde;
    }
    
    /**
     * set TippRunde from this user
     * @param tr
     */
    public void setTrunde(Trunde tr){
    	this.trunde=tr;
    }
    
    /**
     * get tipps from this user
     * @return
     */
    public Collection<Tipp> getTipps(){
    	return this.tipps;
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
     * persist the user
     */
    @Transactional
    public void persist() {
		JPA.em().persist(this);
    }
    
    /**
     * toggle between user.admin=1/0
     */
    @Transactional
    public void switchAdmin() {
    	if (this.admin==1){
    		this.admin=0;
    	}else{
    		this.admin=1;
    	}
		JPA.em().persist(this);
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
    
    @Transactional(readOnly=true)
    public Collection<Tipp> uncheckedTipps() {
    	List<Tipp> list = new ArrayList<Tipp>();
    	for (Tipp t: this.getTipps()){
    		if(t.checked==0){
    			list.add(t);
    		}
    	}
    	Collection<Tipp> col = list;
        return col;
    }
}
