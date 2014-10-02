package rssHandler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Mannschaft;
import models.Spiel;
import models.Tipp;
import play.Logger;
import play.db.jpa.JPA;
import play.libs.F;
import services.SpielService;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import dao.MannschaftDao;
import dao.MannschaftDaoImpl;
import dao.SpielDao;
import dao.SpielDaoImpl;

public class Rss {
	
	private static MannschaftDao mannschaftDao = new MannschaftDaoImpl();
	private static SpielDao spielDao = new SpielDaoImpl();
	
	//Aufruf der Funktionen zum Verteilen aller Punkte und zur Ermittlung neuer Spiele.
	public static void updateWithRss() {
		int i=1;
		List<SyndEntry> feedList = loadFeed();
		List<DataHelper> checkedList = checkFeed(feedList);
		for(DataHelper pro: checkedList){
			try {
				final Byte th = pro.th, tg = pro.tg;
				final Mannschaft mh = pro.mh, mg = pro.mg;
//				Logger.info("Durchlauf " + i);
				final Spiel spiel = findGame(mh, mg);
				final Collection<Tipp> tipps = spiel.getTipps();
				if (spiel.checked==0) {
					setResult(spiel, th, tg);
					if (spiel.gameOver()) {
						check(spiel);
						if (spiel.getBezeichnung().startsWith("gg")) {
							handOutTeamPoints(spiel, th, tg);
						}
						handOutUserPoints(tipps, th, tg);
						setFinalGames(spiel.getBezeichnung());
					}
				}
				i++;
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Logger.info("--------------------");
		Logger.info("| --- complete --- |");
		Logger.info("--------------------");
	}
	
	private static void check(final Spiel s) {
		JPA.withTransaction(new F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				spielDao.check(s);
				spielDao.update(s);
			}
		});
	}

	private static List<SyndEntry> loadFeed(){
		try {
	//		URL feedSource = new URL("http://rss.kicker.de/live/wm");
			URL feedSource = new File("rss.xml").toURI().toURL();
			SyndFeedInput input = new SyndFeedInput();
	    	SyndFeed feed = input.build(new XmlReader(feedSource));
	    	
	    	List<SyndEntry> entries = feed.getEntries();
	    	return entries;
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static List<DataHelper> checkFeed(List<SyndEntry> entries){
		String mhName="";
		String mgName="";
		byte th=0;
		byte tg=0;
		List<DataHelper> proofedList = new ArrayList<DataHelper>();
		int i = 0, ls;
		
		for(SyndEntry se: entries){
			String title=se.getTitle();
//	   		Pattern pattern = Pattern.compile("^(.*)? - (.*)? ([0-9]):([0-9]) (.*)$");
			Pattern pattern = Pattern.compile("^(.*)? - (.*)? ([0-9]):([0-9])$");
			Matcher matcher = pattern.matcher(title);
			if(matcher.matches()){
//				Logger.info("RSSfeed match " + i);
				mhName = renameTeam(matcher.group(1));
    			mgName = renameTeam(matcher.group(2));
    			th = Byte.parseByte((matcher.group(3)));
    			tg = Byte.parseByte((matcher.group(4)));
//				Logger.info(mhName + " " + th + " - " + mgName + " " + tg);
				i++;
    			try {
					proofedList.add(new DataHelper(findMannschaft(mhName), findMannschaft(mgName), th, tg));
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				Logger.warn("Found RSSfeed, that doesnt match!");
				Logger.info("Title: " + title);
			}
		}
		ls=proofedList.size();
//		Logger.info(ls + " games in list");
		if(ls!=i){
			Logger.error("An exception accoured while loading the matching feeds in a list.");
			return null;
		}else{
			Logger.info(i + " feed-entries matched.");
			return proofedList;
		}
	}
	
	private static String renameTeam(String rename){
		//umbenennen nicht identischer mannschafts-bezeichnungen
		switch(rename){
		case "Elfenbeinküste":
			rename="Elfenbeinkueste";
			break;
		case "Bosnien-Herzegowina":
			rename="Bosnien-H.";
			break;
		case "Südkorea":
			rename="Korea Republik";
			break;
		}
		return rename;
	}
	
	private static Mannschaft findMannschaft(final String name) throws Throwable {
		final Mannschaft mannschaft = JPA.withTransaction(new F.Function0<Mannschaft>() {
			@Override
			public Mannschaft apply() throws Throwable {
				return mannschaftDao.findByName(name);
			}
		});
		return mannschaft;
//		return mannschaftDao.findByName(name);
	}
	
	private static Spiel findGame(final Mannschaft mh, final Mannschaft mg) throws Throwable {
		final Spiel spiel = JPA.withTransaction(new F.Function0<Spiel>() {
			@Override
			public Spiel apply() throws Throwable {
				return spielDao.findGame(mh, mg);
			}
		});
		return spiel;
	}
	
	private static void setResult(final Spiel s, final Byte th, final Byte tg){
		JPA.withTransaction(new F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				String mhb = s.getMannschaftHeim().bezeichnung;
				String mgb = s.getMannschaftGast().bezeichnung;
				Logger.info("Setze Ergebnis " + mhb + " " + th + " - " + mgb + " " + tg);
				spielDao.setErgebnis(s, th, tg);
			}
		});
	}
	
	private static void handOutUserPoints(final Collection<Tipp> tipps, final Byte th, final Byte tg){
		JPA.withTransaction(new F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				Logger.info("Verteile Punkte an Benutzer");
				SpielService.handOutUserPoints(tipps, th, tg);
			}
		});
	}
	
	private static void handOutTeamPoints(final Spiel spiel, final Byte th, final Byte tg){
		JPA.withTransaction(new F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				Logger.info("Verteile Punkte an Mannschaften");
				SpielService.handOutTeamPoints(spiel, th, tg);
			}
		});
	}
	
	private static void setFinalGames(final String sBezeichnung){
		JPA.withTransaction(new F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				SpielService.setFinalGames(sBezeichnung);
			}
		});
	}
}
