package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class TopicBroadcastActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ArrayList<ActorRef> listOfReceivers = new ArrayList<ActorRef>();

	public TopicBroadcastActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(TopicBroadcastActor.class, () -> {
			return new TopicBroadcastActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageSubscribe){
			log.info("["+getSelf().path().name()+"] received Subscribe message with status: '"+((MessageSubscribe)message).sub +"' from ["+ getSender().path().name() +"]");
			if (((MessageSubscribe)message).sub){
				listOfReceivers.add(getSender());
			}
			else{
				listOfReceivers.remove(getSender());
			}
		}

		if(message instanceof MessageString){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data +"' from ["+ getSender().path().name() +"]");

			for (ActorRef subscriber : listOfReceivers){
				subscriber.tell(message, getSender());
			}
			
		}
			
	}

}
