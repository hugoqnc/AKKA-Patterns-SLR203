package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class LoadBalancerRoundRobin {

	public static void main(String[] args) {
		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate first and second actor
		final ActorRef balancer = system.actorOf(LoadBalancerActor.createActor(), "balancer");
		final ActorRef a = system.actorOf(SenderActor.createActor(), "a");
		final ActorRef b = system.actorOf(ReceiverActor.createActor(), "b");
		final ActorRef c = system.actorOf(ReceiverActor.createActor(), "c");
		
		MessageRef m = new MessageRef(balancer);
		a.tell(m, ActorRef.noSender());
		b.tell(m, ActorRef.noSender());
		c.tell(m, ActorRef.noSender());
	
	
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
		Thread.sleep(7000);
	}

}
