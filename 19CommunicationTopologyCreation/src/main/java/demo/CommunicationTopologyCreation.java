package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class CommunicationTopologyCreation {

	public static void main(String[] args) {
		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate actors
		final ActorRef a1 = system.actorOf(BasicActor.createActor(), "a1");
		final ActorRef a2 = system.actorOf(BasicActor.createActor(), "a2");
		final ActorRef a3 = system.actorOf(BasicActor.createActor(), "a3");
		final ActorRef a4 = system.actorOf(BasicActor.createActor(), "a4");

	    final ActorRef manager = system.actorOf(ManagerActor.createActor(), "manager");
		
		ArrayList<ActorRef> list = new ArrayList<ActorRef>();
		list.add(a1);
		list.add(a2);
		list.add(a3);
		list.add(a4);
		manager.tell(new MessageListOfRef(list), ActorRef.noSender());
	    
	
	    // We wait 5 seconds before ending system (by default)
	    // But this is not the best solution.
	    try {
			waitBeforeTerminate();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			system.terminate();
		}
	}

	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(5000);
	}

}
