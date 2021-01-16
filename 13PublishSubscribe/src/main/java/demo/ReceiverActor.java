package demo;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ReceiverActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef topic1Ref;
	private ActorRef topic2Ref;

	private int count=1;

	// Empty Constructor
	public ReceiverActor() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(ReceiverActor.class, () -> {
			return new ReceiverActor();
		});
	}

	private void subscribe(){
		String name = this.getSelf().path().name();
		if (count==1){
			MessageSubscribe sub = new MessageSubscribe(true);
			switch (name) {
				case "a":
					topic1Ref.tell(sub, this.getSelf());
					getContext().system().scheduler().scheduleOnce(Duration.ofMillis(3000), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());
					break;
				case "b":
					topic1Ref.tell(sub, this.getSelf());
					topic2Ref.tell(sub, this.getSelf());
					break;
				case "c":
					topic2Ref.tell(sub, this.getSelf());
					break;
				default:
					log.info("ERROR : Unlisted receiver");
					break;
			}
		} else if (count==0){
			if (name == "a"){
				MessageSubscribe sub = new MessageSubscribe(false);
				topic1Ref.tell(sub, this.getSelf());
			}
		}
		
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof MessageTwoRef) {
			log.info("[" + getSelf().path().name() + "] received message with two ref from [" + getSender().path().name() + "]");
			topic1Ref = ((MessageTwoRef) message).a1;
			topic2Ref = ((MessageTwoRef) message).a2;

			subscribe();

		}
		else if (message instanceof MessageString){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data +"' from ["+ getSender().path().name() +"]");
		}
		else if (message instanceof String){
			log.info("["+getSelf().path().name()+"] received message '"+((String)message) +"' from ["+ getSender().path().name() +"]");
			count--;
			subscribe();
		}
	}
	
	
}
