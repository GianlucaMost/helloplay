import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.Props;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import schedule.RssActor;


public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		ActorRef rssActor = Akka.system().actorOf(new Props(RssActor.class));  
		
		Akka.system().scheduler().schedule(
				Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
				Duration.create(5, TimeUnit.MINUTES),     //Frequency 5 minutes
				rssActor,
				"RSScheck",
				Akka.system().dispatcher(),
				null
	    	);
	}
	
	

}
