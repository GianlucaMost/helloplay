package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.Mannschaft;
import models.Spiel;
import models.Tipp;
import models.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

public class TrundeService {
//	
//	/**
//     * search all Tipps from this trunde (NO DAO!)
//     * @return
//     */
//    @Transactional
//    public Collection<Tipp> searchTipps() {
//    	Collection<Tipp> tipps = new ArrayList<Tipp>();
//    	
//    	for(User m: this.getMember()){
//    		for (Tipp t: m.getTipps()){
//    			tipps.add(t);
//    		}
//    	}
//    	return tipps;
//    }
//    
//    /**
//     * find all tipps from this trunde for the given spiel with the tipp from the current user at first place
//     * @param s
//     * @param cU
//     * @return
//     */
//    @Transactional
//    public TreeMap<Spiel, List<Tipp>> searchTippsSpielSorted(Spiel s, User cU) {
//    	List<Tipp> tipps = new ArrayList<Tipp>();
//    	TreeMap<Spiel, List<Tipp>> tippMap = new TreeMap<Spiel, List<Tipp>>();
//    	Collection<User> member = this.getMember();
//    	
//    	for(User m: member){
//    		for (Tipp t: m.getTipps()){
//    			if(t.getSpiel().equals(s)){
//    				if(t.getUser().equals(cU)){
//    					tipps.add(t);
//    				}
//    			}
//    		}
//    	}
//    	for(User m: member){
//    		for (Tipp t: m.getTipps()){
//    			if(t.getSpiel().equals(s)){
//    				if(!t.getUser().equals(cU)){
//    					tipps.add(t);
//    				}
//    			}
//    		}
//    	}
//        
//        for (Tipp t : tipps) {
//        	if (!tippMap.containsKey(t.getSpiel())) {
//        		List<Tipp> tippList = new ArrayList<Tipp>();
//        		tippList.add(t);
//        		tippMap.put(t.getSpiel(), tippList);
//        	} else {
//        		tippMap.get(t.getSpiel()).add(t);
//        	}
//        }
//        return tippMap;
//    }
//    
//    /**
//     * return the sum of all points from every user in this tipprunde
//     * @return
//     */
//    @Transactional
//    public int punkte() {
//    	int p=0;
//    	for(User u: this.getMember()){
//    		p=p+u.punkte;
//    	}
//    	return p;
//    }
}
