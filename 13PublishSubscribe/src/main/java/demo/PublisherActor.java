package demo;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PublisherActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef topicBroadcast;

	private int timer;
	private String m0;
	private int count=1;

	// Empty Constructor
	public PublisherActor() {
	}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(PublisherActor.class, () -> {
			return new PublisherActor();
		});
	}

	private void content(){
		String name = this.getSelf().path().name();
		switch (name) {
			case "publisher1":
				if (count==1){
					m0 = "hello";
					timer = 1000;
					getContext().system().scheduler().scheduleOnce(Duration.ofMillis(timer), getSelf(), "go1", getContext().system().dispatcher(), ActorRef.noSender());
				} else {
					m0 = "hello2";
					timer = 3000;
					getContext().system().scheduler().scheduleOnce(Duration.ofMillis(timer), getSelf(), "go2", getContext().system().dispatcher(), ActorRef.noSender());
				}
				break;
			case "publisher2":
				if (count==1){
					m0 = "world";
					timer = 2000;
					getContext().system().scheduler().scheduleOnce(Duration.ofMillis(timer), getSelf(), "go1", getContext().system().dispatcher(), ActorRef.noSender());
				}
				break;
			default:
				log.info("ERROR : Unlisted publisher");
				break;
		}
		count--;
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof MessageRef) {
			log.info("[" + getSelf().path().name() + "] received message with one ref from [" + getSender().path().name() + "]");
			topicBroadcast = ((MessageRef) message).a;

			content();
		}
		if (message instanceof String ){
			log.info("["+getSelf().path().name()+"] received message '"+((String)message) +"' from ["+ getSender().path().name() +"]");
			if ((String)message == "go1"){
				MessageString m1 = new MessageString(m0);
				topicBroadcast.tell(m1, this.getSelf());

				content();
			}
			else if ((String)message == "go2"){
				MessageString m1 = new MessageString(m0);
				topicBroadcast.tell(m1, this.getSelf());
			}
		}
		
	}
	
	
}
