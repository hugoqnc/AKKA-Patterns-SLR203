package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CreatorActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private int count = 1;
	public ArrayList<ActorRef> childs = new ArrayList<ActorRef>();

	public CreatorActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(CreatorActor.class, () -> {
			return new CreatorActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageString){
			log.info("["+getSelf().path().name()+"] received Session message with status: '"+((MessageString)message).data +"' from ["+ getSender().path().name() +"]");
			if (((MessageString)message).data=="CREATE"){

				ActorRef child = getContext().actorOf(BasicActor.createActor(), "actor"+((Integer)count).toString());
				count++;

				childs.add(child);
				log.info(child.path().toString());
			}




		}
			
	}

}
