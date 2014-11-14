import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.Props;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.api.Play;
import play.libs.Akka;
import scala.Option;
import scala.concurrent.duration.Duration;
import schedule.RssActor;


public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		final Option<Object> configProperty = Play.current().configuration().getInt("rss.intervall");
		Integer intervall = configProperty.isDefined() ? new Integer(configProperty.get().toString()) : 0;
		
		ActorRef rssActor = Akka.system().actorOf(new Props(RssActor.class));  
		if (intervall!=0) {
			Akka.system().scheduler().schedule(
				Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
				Duration.create(intervall, TimeUnit.MINUTES),     //Intervall in minutes
				rssActor,
				"RSScheck",
				Akka.system().dispatcher(),
				null
	    	);
			
		}
	}
	
	

}
