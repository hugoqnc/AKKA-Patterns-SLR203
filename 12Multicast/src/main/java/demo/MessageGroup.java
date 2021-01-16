package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;

public class MessageGroup {
    
    public String groupName;
    public ArrayList<ActorRef> actorList;

    public MessageGroup(String groupName, ArrayList<ActorRef> actorList) {
        this.groupName = groupName;
        this.actorList = actorList;
    }

}
