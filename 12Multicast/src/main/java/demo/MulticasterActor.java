package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MulticasterActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Group reference
	private ArrayList<MessageGroup> listOfGroups = new ArrayList<MessageGroup>();

	public MulticasterActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(MulticasterActor.class, () -> {
			return new MulticasterActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageGroup){
			log.info("["+getSelf().path().name()+"] received Group message '"+((MessageGroup)message).groupName +"' + a list of ref from ["+ getSender().path().name() +"]");

			listOfGroups.add((MessageGroup)message);
		}


		if(message instanceof MessageTwoString){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageTwoString)message).groupName +"' + '"+((MessageTwoString)message).data +"' from ["+ getSender().path().name() +"]");
			
			if (getSender().path().name()=="sender") {
				for (MessageGroup group : listOfGroups){
					if (((MessageTwoString)message).groupName == group.groupName){
						for (ActorRef a : group.actorList){
							MessageString m = new MessageString(((MessageTwoString)message).data);
							a.tell(m, getSender());
						}
					}
				}
			}
		}
	}


}
