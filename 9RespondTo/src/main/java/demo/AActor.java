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
	private ActorRef cRef;

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
			bRef = ((MessageTwoRef)message).a1;
			cRef = ((MessageTwoRef)message).a2;

			MessageStringRef m = new MessageStringRef("req1", cRef);
			bRef.tell(m, this.getSelf());
		}
			
	}

}
