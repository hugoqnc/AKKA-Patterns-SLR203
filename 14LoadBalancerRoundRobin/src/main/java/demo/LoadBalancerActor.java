package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class LoadBalancerActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ArrayList<ActorRef> listOfReceivers = new ArrayList<ActorRef>();
	private int count = 0;

	public LoadBalancerActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(LoadBalancerActor.class, () -> {
			return new LoadBalancerActor();
		});
	}

	private ActorRef chooseTheReceiver(){
		int nbReceivers = listOfReceivers.size();
		if (count>=nbReceivers){
			count=0;
		} 
		ActorRef receiver = listOfReceivers.get(count);
		count++;
		return receiver;
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageString){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data +"' from ["+ getSender().path().name() +"]");
			if (((MessageString)message).data=="join" && getSender().path().name()!="a") {
				listOfReceivers.add(getSender());
			} else if (((MessageString)message).data=="unjoin" && getSender().path().name()!="a") {
				listOfReceivers.remove(getSender());
			}
			else if (getSender().path().name()=="a"){
				chooseTheReceiver().tell(message, getSender());
			}
			
		}
			
	}

}
