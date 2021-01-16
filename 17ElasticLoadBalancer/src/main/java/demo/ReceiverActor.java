package demo;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ReceiverActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	//private ActorRef balancerRef; 

	// Empty Constructor
	public ReceiverActor() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(ReceiverActor.class, () -> {
			return new ReceiverActor();
		});
	}

	private int durationOfTask(String task){
		int d=1000;
		switch (task) {
			case "t1":
				d = 2500;
				break;
		
			case "t2":
				d = 2500;
				break;
		
			case "t3":
				d = 1000;
				break;
		
			default:
				break;
		}
		return d;
	}


	@Override
	public void onReceive(Object message) throws Throwable {

		if (message instanceof MessageString){
			String m = ((MessageString)message).data;
			log.info("["+getSelf().path().name()+"] received TASK '"+m+"' from ["+ getSender().path().name() +"]");

			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(durationOfTask(m)), getSelf(), m, getContext().system().dispatcher(), ActorRef.noSender());
		} else if (message instanceof String){
			log.info("["+getSelf().path().name()+"] has completed task: '"+message+"' (sent from ["+ getSender().path().name() +"])");

			this.getContext().getParent().tell(new MessageFinished((String)message), getSelf());
		}

	}

	@Override
	public void postStop(){
		log.info("The session ["+ getSelf().path().name() +"] was stopped by [" + getContext().getParent().path().name() + "].");
	}
	
	
}
