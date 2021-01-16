package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class PublishSubscribe {

	public static void main(String[] args) {
		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate first and second actor
		final ActorRef topic1 = system.actorOf(TopicBroadcastActor.createActor(), "topic1");
		final ActorRef topic2 = system.actorOf(TopicBroadcastActor.createActor(), "topic2");
		final ActorRef publisher1 = system.actorOf(PublisherActor.createActor(), "publisher1");
		final ActorRef publisher2 = system.actorOf(PublisherActor.createActor(), "publisher2");
		final ActorRef a = system.actorOf(ReceiverActor.createActor(), "a");
		final ActorRef b = system.actorOf(ReceiverActor.createActor(), "b");
		final ActorRef c = system.actorOf(ReceiverActor.createActor(), "c");
		
		MessageRef t1 = new MessageRef(topic1);
		MessageRef t2 = new MessageRef(topic2);
		publisher1.tell(t1, ActorRef.noSender());
		publisher2.tell(t2, ActorRef.noSender());

		MessageTwoRef topics = new MessageTwoRef(topic1, topic2);
		a.tell(topics, ActorRef.noSender());
		b.tell(topics, ActorRef.noSender());
		c.tell(topics, ActorRef.noSender());
	
	
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
