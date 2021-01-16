package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

public class BasicActor extends UntypedAbstractActor {

	// Empty Constructor
	public BasicActor() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(BasicActor.class, () -> {
			return new BasicActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {

	}

	
}
