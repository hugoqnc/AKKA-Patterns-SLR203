package demo;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

public class AActor extends UntypedAbstractActor {

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef bRef;

	public AActor() {
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(AActor.class, () -> {
			return new AActor();
		});
	}

	public void startSending(int numberMessages) {
		for (int i = 0; i < numberMessages; i++) {
			MessageString m = new MessageString("msg: " + String.valueOf(i));
			// bRef.tell(m, this.getSelf());
			Timeout timeout = Timeout.create(Duration.ofSeconds(5));
			Future<Object> future = Patterns.ask(bRef, m, timeout);
			try {
				MessageString res = (MessageString) Await.result(future, timeout.duration());
				log.info("["+getSelf().path().name()+"] received message '"+((MessageString)res).data+"' from ["+ getSender().path().name() +"]");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageRef){
			log.info("["+getSelf().path().name()+"] received message with bRef from ["+ getSender().path().name() +"]");
			this.bRef = ((MessageRef)message).a;

			startSending(30);
		}
	}

}
