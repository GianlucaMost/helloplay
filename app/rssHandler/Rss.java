package rssHandler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Mannschaft;
import models.Spiel;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class Rss {
	
	public static void updateWithRss(){
		for(DataHelper pro: checkFeed(loadFeed())){
			try {
//				final Spiel spiel = findGame(pro.mh, pro.mg);
				final Byte th = pro.th, tg = pro.tg;
				final Mannschaft mh = pro.mh, mg = pro.mg;
//				spiel.setErgebnis(pro.th, pro.tg);
				
//				JPA.withTransaction(new play.libs.F.Callback0() {
//					@Override
//					public void invoke() throws Throwable {
//						Spiel spiel = findGame(mh, mg);
//						spiel.setErgebnis(th, tg);
//					}
//				});
				
				final Spiel spiel = JPA.withTransaction(new F.Function0<Spiel>() {
					@Override
					public Spiel apply() throws Throwable {
						return findGame(mh, mg);
					}
				});
				
				JPA.withTransaction(new F.Callback0() {
					@Override
					public void invoke() throws Throwable {
						// TODO Auto-generated method stub
						spiel.setErgebnis(th, tg);
						if (spiel.gameOver()){
							Spiel.handOutUserPoints(spiel.getTipps(), th, tg);
							if(spiel.checked==0){
								Spiel.handOutTeamPoints(spiel, mh, mg, th, tg);
								Spiel.setFinalGames(spiel);
							}
						}
					}
				});
				
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		
		for(SyndEntry se: entries){
			String title=se.getTitle();
//	   		Pattern pattern = Pattern.compile("^(.*)? - (.*)? ([0-9]):([0-9]) (.*)$");
			Pattern pattern = Pattern.compile("^(.*)? - (.*)? ([0-9]):([0-9])$");
			Matcher matcher = pattern.matcher(title);
			if(matcher.matches()){
				Logger.info("RSSfeed match");
				mhName = matcher.group(1);
    			mgName = matcher.group(2);
    			th = Byte.parseByte((matcher.group(3)));
    			tg = Byte.parseByte((matcher.group(4)));
				Logger.info(mhName + " " + th + " - " + mgName + " " + tg);
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
		return proofedList;
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
				return Mannschaft.findByName(name);
			}
		});
		return mannschaft;
	}
	
	private static Spiel findGame(final Mannschaft mh, final Mannschaft mg) throws Throwable {
		final Spiel spiel = JPA.withTransaction(new F.Function0<Spiel>() {
			@Override
			public Spiel apply() throws Throwable {
				return Spiel.findGame(mh, mg);
			}
		});
		return spiel;
	}
	
}
