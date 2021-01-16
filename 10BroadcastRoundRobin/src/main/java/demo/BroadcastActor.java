package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class BroadcastActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ArrayList<ActorRef> listOfReceivers = new ArrayList<ActorRef>();

	public BroadcastActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(BroadcastActor.class, () -> {
			return new BroadcastActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageString){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data +"' from ["+ getSender().path().name() +"]");
			if (((MessageString)message).data=="join" && getSender().path().name()!="a") {
				listOfReceivers.add(getSender());
			}
			else if (getSender().path().name()=="a"){
				for (ActorRef receiver : listOfReceivers){
					receiver.tell(message, getSender());
				}
			}
		}
			
	}

}
