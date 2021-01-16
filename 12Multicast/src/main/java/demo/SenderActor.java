package demo;

import java.time.Duration;
import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SenderActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	public ActorRef multicaster;
    public ActorRef r1;
    public ActorRef r2;
    public ActorRef r3;

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
		if (message instanceof MessageFourRef){
			log.info("[" + getSelf().path().name() + "] received message with four ref from [" + getSender().path().name() + "]");
			multicaster = ((MessageFourRef)message).multicaster;
			r1 = ((MessageFourRef)message).r1;
			r2 = ((MessageFourRef)message).r2;
			r3 = ((MessageFourRef)message).r3;


			ArrayList<ActorRef> l1 = new ArrayList<ActorRef>();
			l1.add(r1);
			l1.add(r2);
			ArrayList<ActorRef> l2 = new ArrayList<ActorRef>();
			l2.add(r2);
			l2.add(r3);
 
			MessageGroup g1 = new MessageGroup("group1", l1);
			MessageGroup g2 = new MessageGroup("group2", l2);
			multicaster.tell(g1, this.getSelf());
			multicaster.tell(g2, this.getSelf());

			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go1", getContext().system().dispatcher(), ActorRef.noSender());
		}

		else if (message instanceof String){
			log.info("["+getSelf().path().name()+"] received message '"+((String)message) +"' from ["+ getSender().path().name() +"]");
			if ((String)message == "go1"){
				MessageTwoString m  = new MessageTwoString("group1", "hello");
				multicaster.tell(m, this.getSelf());
				getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go2", getContext().system().dispatcher(), ActorRef.noSender());
			}

			if ((String)message == "go2"){
				MessageTwoString m  = new MessageTwoString("group2", "world");
				multicaster.tell(m, this.getSelf());
			}
		}
		
	}
	
	
}
