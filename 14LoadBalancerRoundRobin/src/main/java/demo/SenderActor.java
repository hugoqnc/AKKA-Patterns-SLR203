package demo;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SenderActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef balancerRef;

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
			balancerRef = ((MessageRef)message).a;

			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go1", getContext().system().dispatcher(), ActorRef.noSender());
		}
		if (message instanceof String){
			log.info("["+getSelf().path().name()+"] received message '"+((String)message) +"' from ["+ getSender().path().name() +"]");
			if ((String)message == "go1"){
				MessageString m1  = new MessageString("m1");
				balancerRef.tell(m1, this.getSelf());
				getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go2", getContext().system().dispatcher(), ActorRef.noSender());
			}
			if ((String)message == "go2"){
				MessageString m1  = new MessageString("m2");
				balancerRef.tell(m1, this.getSelf());
				getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go3", getContext().system().dispatcher(), ActorRef.noSender());
			}
			if ((String)message == "go3"){
				MessageString m1  = new MessageString("m3");
				balancerRef.tell(m1, this.getSelf());
				getContext().system().scheduler().scheduleOnce(Duration.ofMillis(2000), getSelf(), "go4", getContext().system().dispatcher(), ActorRef.noSender());
			}
			if ((String)message == "go4"){
				MessageString m1  = new MessageString("m4");
				balancerRef.tell(m1, this.getSelf());
			}
		}
		
	}
	
	
}
