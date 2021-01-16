package demo;

import java.time.Duration;

import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class ClientActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef creatorRef;


	// Empty Constructor
	public ClientActor() {
	}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(ClientActor.class, () -> {
			return new ClientActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof MessageRef) {
			log.info("[" + getSelf().path().name() + "] received one ref from [" + getSender().path().name() + "]");

			creatorRef = ((MessageRef)message).a;

			MessageString create = new MessageString("CREATE");
			creatorRef.tell(create, getSelf());

			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go1", getContext().system().dispatcher(), ActorRef.noSender());

		}
			


		else if (message instanceof String){
			log.info("["+getSelf().path().name()+"] received message '"+((String)message) +"' from ["+ getSender().path().name() +"]");

			String data = ((String)message);
			if (data=="go1"){
				MessageString create = new MessageString("CREATE");
				creatorRef.tell(create, getSelf());
				
				getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go2", getContext().system().dispatcher(), ActorRef.noSender());
			}
			else if (data=="go2"){
				log.info(creatorRef.path().toString());

				ActorSelection a = getContext().actorSelection("akka://system/user/a");
				log.info(a.pathString());

				ActorSelection actor1 = getContext().actorSelection("akka://system/user/a/actor1");
				log.info(actor1.pathString());

				ActorSelection actor2 = getContext().actorSelection("akka://system/user/a/actor2");
				log.info(actor2.pathString());

			}
		}
		
	}
	
	
}
