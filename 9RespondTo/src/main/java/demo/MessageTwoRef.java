package demo;

import akka.actor.ActorRef;

public class MessageTwoRef {
    public final ActorRef a1;
    public final ActorRef a2;

    public MessageTwoRef(ActorRef a1, ActorRef a2) {
        this.a1 = a1;
        this.a2 = a2;
    }

}
