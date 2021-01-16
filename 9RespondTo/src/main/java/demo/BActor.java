package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class BActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef cRef; 
	// Empty Constructor
	public BActor() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(BActor.class, () -> {
			return new BActor();
		});
	}

	public String computeOutput(String input){
		if (input=="req1"){
			return "res1";
		}
		else {
			return "other";
		}
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageStringRef){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageStringRef)message).data+"' + one ref from ["+ getSender().path().name() +"]");
			String res = computeOutput(((MessageStringRef)message).data);
			cRef = ((MessageStringRef)message).a;
			MessageString m = new MessageString(res);
			cRef.tell(m, this.getSelf());
		}
	}
	
	
}
