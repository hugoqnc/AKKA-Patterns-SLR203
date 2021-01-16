package demo;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SenderActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef broadcastRef;

	// Empty Constructor
	public SenderActor() {
	}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(SenderActor.class, () -> {
			return new SenderActor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof MessageRef) {
			log.info("[" + getSelf().path().name() + "] received message with one ref from [" + getSender().path().name() + "]");
			broadcastRef = ((MessageRef) message).a;

			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());
		}
		if (message instanceof String){
			log.info("["+getSelf().path().name()+"] received message '"+((String)message) +"' from ["+ getSender().path().name() +"]");
			if ((String)message == "go"){
				MessageString m  = new MessageString("hello world!");
				broadcastRef.tell(m, this.getSelf());
			}
		}
		
	}
	
	
}
