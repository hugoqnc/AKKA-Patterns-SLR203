package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Transmitter extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Empty Constructor
	public Transmitter() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(Transmitter.class, () -> {
			return new Transmitter();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageStringRef){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageStringRef)message).data+"' from ["+ getSender().path().name() +"]");
			MessageString m = new MessageString(((MessageStringRef)message).data);
			ActorRef bRef = ((MessageStringRef)message).a;
			bRef.tell(m, getSender());
		}
	}
	
	
}