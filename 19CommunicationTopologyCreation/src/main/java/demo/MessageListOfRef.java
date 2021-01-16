package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;

public class MessageListOfRef {
    public ArrayList<ActorRef> list;

    public MessageListOfRef(ArrayList<ActorRef> list) {
        this.list = list;
    }

}
