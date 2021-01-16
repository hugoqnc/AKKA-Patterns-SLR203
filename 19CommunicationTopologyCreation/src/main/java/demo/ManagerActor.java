package demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ManagerActor extends UntypedAbstractActor {

	private ArrayList<ActorRef> listOfActorRef;
	private HashMap<Integer, ArrayList<Integer>> matrix = new HashMap<Integer, ArrayList<Integer>>();

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	// Empty Constructor
	public ManagerActor() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(ManagerActor.class, () -> {
			return new ManagerActor();
		});
	}

	private void createMatrix(){
		matrix.put(1, new ArrayList<Integer>(Arrays.asList(2,3)));
		matrix.put(2, new ArrayList<Integer>(Arrays.asList(4)));
		matrix.put(3, new ArrayList<Integer>(Arrays.asList(1,4)));
		matrix.put(4, new ArrayList<Integer>(Arrays.asList(1,4)));
	}

	private void sendRef(){
		for(int i = 1; i<=listOfActorRef.size(); i++){

			ArrayList<ActorRef> actorsToSend = new ArrayList<ActorRef>();

			for (int j : matrix.get(i)){
				actorsToSend.add(listOfActorRef.get(j-1));
			}
			listOfActorRef.get(i-1).tell(new MessageListOfRef(actorsToSend), getSelf());
		}
	}

	@Override
	public void onReceive(Object message) throws Throwable {

		if (message instanceof MessageListOfRef){
			listOfActorRef = ((MessageListOfRef)message).list;

			String names = "";
			for (ActorRef actor : listOfActorRef){
				names = names.concat(actor.path().name()+",");
			}
			names = names.substring(0, names.length()-1);

			log.info("["+getSelf().path().name()+"] received message with the list of Ref '" + names + "' from ["+ getSender().path().name() +"]");


			createMatrix();
			sendRef();
		}

	}
	
	
}
