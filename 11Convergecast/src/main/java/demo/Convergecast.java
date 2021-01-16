package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class Convergecast {

	public static void main(String[] args) {
		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate first and second actor
		final ActorRef broadcast = system.actorOf(MergerActor.createActor(), "broadcast");
		final ActorRef a = system.actorOf(SenderActor.createActor(), "a");
		final ActorRef b = system.actorOf(SenderActor.createActor(), "b");
		final ActorRef c = system.actorOf(SenderActor.createActor(), "c");
		final ActorRef d = system.actorOf(ReceiverActor.createActor(), "d");
		
		MessageRef m = new MessageRef(broadcast);
		a.tell(m, ActorRef.noSender());
		b.tell(m, ActorRef.noSender());
		c.tell(m, ActorRef.noSender());
		d.tell(m, ActorRef.noSender());
	
	
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
