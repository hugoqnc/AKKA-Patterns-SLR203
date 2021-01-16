package demo;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ReceiverActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef balancerRef; 
	// Empty Constructor
	public ReceiverActor() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(ReceiverActor.class, () -> {
			return new ReceiverActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof MessageRef) {
			log.info("[" + getSelf().path().name() + "] received message with one ref from [" + getSender().path().name() + "]");
			balancerRef = ((MessageRef) message).a;

			MessageString m = new MessageString("join");
			balancerRef.tell(m, this.getSelf());
			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(4000), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());

		}
		else if (message instanceof MessageString){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data +"' from ["+ getSender().path().name() +"]");
		}
		else if (message instanceof String && ((String)message)=="go" && getSelf().path().name()=="c"){
			log.info("["+getSelf().path().name()+"] received message '"+((String)message) +"' from ["+ getSender().path().name() +"]");
			MessageString m = new MessageString("unjoin");
			balancerRef.tell(m, this.getSelf());
		}
	}
	
	
}
