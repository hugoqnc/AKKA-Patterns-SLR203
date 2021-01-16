package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MergerActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ArrayList<ActorRef> listOfSenders = new ArrayList<ActorRef>();
	private ActorRef receiver;
	// Messages
	private ArrayList<String> listOfMessages = new ArrayList<String>(); //(Arrays.asList("", "", ""));

	public MergerActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(MergerActor.class, () -> {
			return new MergerActor();
		});
	}

	private boolean checkSendingStatus(){
		boolean flag = true;
		String first = listOfMessages.get(0);
		for(String message : listOfMessages){
			if (message != first) flag = false;
		}
		return flag;
	}

	private void resetListOfMessages(){
		for(int i=0; i<listOfMessages.size(); i++){
			listOfMessages.set(i, "");
		}
	}

	public void unjoin(ActorRef actor){
		if (listOfSenders.contains(actor)){
			int index = listOfSenders.indexOf(actor);
			listOfMessages.remove(index);
			listOfSenders.remove(actor);
		}
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageString){
			log.info("["+getSelf().path().name()+"] received message '"+((MessageString)message).data +"' from ["+ getSender().path().name() +"]");

			if (((MessageString)message).data=="join" && getSender().path().name()!="d") {
				listOfSenders.add(getSender());
				listOfMessages.add("");
			}

			else if (((MessageString)message).data=="unjoin" && getSender().path().name()!="d") {
				unjoin(getSender());
			}

			else if (getSender().path().name()=="d"){
				receiver = getSender();
			}

			else if (getSender().path().name()!="d"){
				int index = listOfSenders.indexOf(getSender());
				if (index!=-1){
					listOfMessages.set(index, ((MessageString)message).data);

						if (checkSendingStatus()){
							receiver.tell(message, this.getSelf());
							resetListOfMessages();
						}
				}
			}
		}
			
	}

}
