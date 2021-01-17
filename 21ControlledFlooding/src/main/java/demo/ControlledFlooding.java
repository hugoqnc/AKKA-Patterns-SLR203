package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class ControlledFlooding {

	public static void main(String[] args) {
		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate actors
		final ActorRef a = system.actorOf(BasicActor.createActor(), "a");
		final ActorRef b = system.actorOf(BasicActor.createActor(), "b");
		final ActorRef c = system.actorOf(BasicActor.createActor(), "c");
		final ActorRef d = system.actorOf(BasicActor.createActor(), "d");
		final ActorRef e = system.actorOf(BasicActor.createActor(), "e");

	    final ActorRef manager = system.actorOf(ManagerActor.createActor(), "manager");
		
		ArrayList<ActorRef> list = new ArrayList<ActorRef>();
		list.add(a);
		list.add(b);
		list.add(c);
		list.add(d);
		list.add(e);
		manager.tell(new MessageListOfRef(list), ActorRef.noSender());
	    
	
	    // We wait 5 seconds before ending system (by default)
	    // But this is not the best solution.
	    try {
			waitBeforeTerminate();
		} catch (InterruptedException error) {
			error.printStackTrace();
		} finally {
			system.terminate();
		}
	}

	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(5000);
	}

}
