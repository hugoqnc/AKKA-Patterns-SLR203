package demo;

import akka.actor.ActorRef;

public class MessageStringRef {
    public final String data;
    public final ActorRef a;

    public MessageStringRef(String data, ActorRef a) {
        this.data = data;
        this.a = a;
    }

}
