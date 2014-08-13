package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import models.Tipp;
import models.User;

import org.mindrot.jbcrypt.BCrypt;

import play.db.jpa.Transactional;

public class UserService {
    /**
     * toggle between user.admin=1/0
     */
    @Transactional
    public void switchAdmin(User u) {
    	if (u.admin==1){
    		u.admin=0;
    	}else{
    		u.admin=1;
    	}
    }

    
    /**
     * ueberprueft das uebergebene passwort
     * @param pw
     * @return
     */
    @Transactional
    public boolean checkPw(User u, String pw) {
    	return BCrypt.checkpw(pw, u.pw);
    }
    
    /**
     * gibt eine collection mit tipps zurueck, fuer die noch keine Punkte erhalten worden.
     * @return
     */
    @Transactional(readOnly=true)
    public Collection<Tipp> uncheckedTipps(User u) {
    	List<Tipp> list = new ArrayList<Tipp>();
    	for (Tipp t: u.getTipps()){
    		if(t.checked==0){
    			list.add(t);
    		}
    	}
    	Collection<Tipp> col = list;
        return col;
    }
}
