package demo;

import akka.actor.ActorRef;

public class MessageFourRef {
    public final ActorRef multicaster;
    public final ActorRef r1;
    public final ActorRef r2;
    public final ActorRef r3;

    public MessageFourRef(ActorRef multicaster, ActorRef r1, ActorRef r2, ActorRef r3) {
        this.multicaster = multicaster;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

}
