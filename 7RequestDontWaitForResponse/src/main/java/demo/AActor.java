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


	public AActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(AActor.class, () -> {
			return new AActor();
		});
	}

	public void startSending(int numberMessages){
		for(int i=0; i<numberMessages; i++){
			MessageString m = new MessageString("msg: "+ String.valueOf(i));
			bRef.tell(m, this.getSelf());
		}
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageRef){
			log.info("["+getSelf().path().name()+"] received message with bRef from ["+ getSender().path().name() +"]");
			this.bRef = ((MessageRef)message).a;

			startSending(30);
		}

		if(message instanceof MessageString){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data+"' from ["+ getSender().path().name() +"]");
			
		}
	}

}
