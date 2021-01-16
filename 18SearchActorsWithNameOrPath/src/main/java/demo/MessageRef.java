package demo;

import akka.actor.ActorRef;

public class MessageRef {
    public final ActorRef a;

    public MessageRef(ActorRef a) {
        this.a = a;
    }

}
