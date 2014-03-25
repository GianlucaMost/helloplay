package schedule;

import models.Spiel;
import play.Logger;
import akka.actor.UntypedActor;

public class RssActor extends UntypedActor {

	@Override
	public void onReceive(Object msg) throws Exception {
		Logger.info("Starting frequently repeated RSScheck");
		Spiel.setResultWithRss();
	}
}
