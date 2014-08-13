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
    
    @ManyToMany
    @JoinTable(
        name="user_trunde",
        joinColumns={@JoinColumn(name="fk_uid", referencedColumnName="uid")},
        inverseJoinColumns={@JoinColumn(name="fk_trid", referencedColumnName="trid")})
    private Collection<Trunde> trunden;
    
    @OneToMany(mappedBy="tradmin", targetEntity=Trunde.class)
    private Collection<Trunde> tradmin;
    
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
    public Collection<Trunde> getTrunden(){
    	return this.trunden;
    }
    
    /**
     * set TippRunde from this user
     * @param tr
     */
    public void addTrunde(Trunde tr){
    	this.trunden.add(tr);
    }
    
    /**
     * removes this user from the given TippRunde
     * @param tr
     */
    public void removeFromTrunde(Trunde tr){
    	this.trunden.remove(tr);
    }
    
    /**
     * get tipps from this user
     * @return
     */
    public Collection<Tipp> getTipps(){
    	return this.tipps;
    }
    
    /**
     * get all TR where this user is admin from
     * @return
     */
    public Collection<Trunde> getTrAdmin(){
    	return this.tradmin;
    }
    
//    /**
//     * remove the given TR from the Collection of TRs this user is admin from
//     * @param tr
//     */
//    public void removeTrAdmin(Trunde tr){
//    	this.tradmin.remove(tr);
//    }
    
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
    }

    
    /**
     * ueberprueft das uebergebene passwort
     * @param pw
     * @return
     */
    @Transactional
    public boolean checkPw(String pw) {
    	return BCrypt.checkpw(pw, this.pw);
    }
    
    /**
     * gibt eine collection mit tipps zurueck, fuer die noch keine Punkte erhalten worden.
     * @return
     */
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
