package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class BasicActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	ArrayList<ActorRef> knownRefList;

	public BasicActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(BasicActor.class, () -> {
			return new BasicActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageListOfRef){
			knownRefList = ((MessageListOfRef)message).list;

			String names = "";
			for (ActorRef actor : knownRefList){
				names = names.concat(actor.path().name()+",");
			}
			if (names!=""){
				names = names.substring(0, names.length()-1);
			}
			

			log.info("["+getSelf().path().name()+"] received message with the list of Ref '" + names + "' from ["+ getSender().path().name() +"]");

		}
		else if(message instanceof String){
			log.info("["+getSelf().path().name()+"] received message '"+((String)message) +"' from ["+ getSender().path().name() +"]");

			for(ActorRef receiver : knownRefList){
				receiver.tell(message, getSelf());
			}
		}
	}

}
