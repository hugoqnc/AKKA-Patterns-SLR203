package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class AActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef bRef;
	private ActorRef transmitterRef;

	public AActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(AActor.class, () -> {
			return new AActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageTwoRef){
			log.info("["+getSelf().path().name()+"] received message with 2 refs from ["+ getSender().path().name() +"]");
			this.transmitterRef = ((MessageTwoRef)message).a1;
			this.bRef = ((MessageTwoRef)message).a2;
		}

		if(message instanceof MessageString){
			if (((MessageString)message).data == "start") {
				log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data+"' from ["+ getSender().path().name() +"]");
				MessageStringRef m = new MessageStringRef("hello", bRef);
	    		transmitterRef.tell(m, this.getSelf());
			}
			if (((MessageString)message).data == "hi!") {
				log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data+"' from ["+ getSender().path().name() +"]");
			}
		}
	}

}
