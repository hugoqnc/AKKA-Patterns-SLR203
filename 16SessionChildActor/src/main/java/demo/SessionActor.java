package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SessionActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef clientRef;

	// Empty Constructor
	public SessionActor() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(SessionActor.class, () -> {
			return new SessionActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof MessageStringRef) {
			log.info("[" + getSelf().path().name() + "] received message '"+((MessageStringRef)message).data +"' + one ref from [" + getSender().path().name() + "]");

			if(((MessageStringRef)message).data=="SessionManager"){
				clientRef = ((MessageStringRef) message).a;
			}

		}
		else if (message instanceof MessageString){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data +"' from ["+ getSender().path().name() +"]");

			String data = ((MessageString)message).data;
			if (data=="m1"){
				clientRef.tell(new MessageString("m2"), getSelf());
			}
		}

	}

	@Override
	public void postStop(){
		log.info("The session ["+ getSelf().path().name() +"] was stopped by [" + getContext().getParent().path().name() + "].");
	}
	
	
}
