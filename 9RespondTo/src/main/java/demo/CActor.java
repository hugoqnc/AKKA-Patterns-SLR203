package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CActor extends UntypedAbstractActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Empty Constructor
	public CActor() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(CActor.class, () -> {
			return new CActor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageString){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data+"' from ["+ getSender().path().name() +"]");
		}
    }
    
}
	
	