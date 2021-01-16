package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class Multicast {

	public static void main(String[] args) {
		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate first and second actor
		final ActorRef multicaster = system.actorOf(MulticasterActor.createActor(), "multicaster");
		final ActorRef sender = system.actorOf(SenderActor.createActor(), "sender");
		final ActorRef r1 = system.actorOf(ReceiverActor.createActor(), "r1");
		final ActorRef r2 = system.actorOf(ReceiverActor.createActor(), "r2");
		final ActorRef r3 = system.actorOf(ReceiverActor.createActor(), "r3");

		MessageFourRef m = new MessageFourRef(multicaster, r1, r2, r3);
		sender.tell(m, ActorRef.noSender());
		
	
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
