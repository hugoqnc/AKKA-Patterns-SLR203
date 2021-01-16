package demo;


import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SessionManagerActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef sessionRef;
	private ActorRef clientRef;

	public SessionManagerActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(SessionManagerActor.class, () -> {
			return new SessionManagerActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageSession){
			log.info("["+getSelf().path().name()+"] received Session message with status: '"+((MessageSession)message).status +"' from ["+ getSender().path().name() +"]");
			if (((MessageSession)message).status){
				this.clientRef = getSender();
				this.sessionRef = getContext().actorOf(SessionActor.createActor(), "session1");

				sessionRef.tell(new MessageStringRef("SessionManager", clientRef), this.getSelf());

				clientRef.tell(new MessageStringRef("SessionManager", sessionRef), this.getSelf());
			}
			else{
				getContext().stop(sessionRef);
			}
		}
			
	}

}
