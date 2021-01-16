package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ClientActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef sessionManagerRef;
	private ActorRef sessionRef;


	// Empty Constructor
	public ClientActor() {
	}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(ClientActor.class, () -> {
			return new ClientActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof MessageStringRef) {
			log.info("[" + getSelf().path().name() + "] received message '"+((MessageStringRef)message).data +"' + one ref from [" + getSender().path().name() + "]");

			if(((MessageStringRef)message).data=="SessionChild"){
				sessionManagerRef = ((MessageStringRef) message).a;
				MessageSession newSession = new MessageSession(true);
				sessionManagerRef.tell(newSession, getSelf());
				
			} else if(((MessageStringRef)message).data=="SessionManager"){
				sessionRef = ((MessageStringRef) message).a;

				sessionRef.tell(new MessageString("m1"), getSelf());
			}
			
		}

		else if (message instanceof MessageString){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data +"' from ["+ getSender().path().name() +"]");

			String data = ((MessageString)message).data;
			if (data=="m2"){
				sessionRef.tell(new MessageString("m3"), getSelf());

				MessageSession endSession = new MessageSession(false);
				sessionManagerRef.tell(endSession, getSelf());
			}
		}
		
	}
	
	
}
