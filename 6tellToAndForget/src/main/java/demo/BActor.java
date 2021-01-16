package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class BActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Empty Constructor
	public BActor() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(BActor.class, () -> {
			return new BActor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageString){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data+"' from ["+ getSender().path().name() +"]");
			MessageString m = new MessageString("hi!");
			getSender().tell(m, this.getSelf());
		}
	}
	
	
}
