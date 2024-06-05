package com.zhj.webrtc;

import javax.websocket.Session;
import java.util.ArrayList;

public class User {
    public String id;
    public String name;
    public Session session;
    public String sessionDescription;
    public ArrayList<String> candidates;
    public String friendId;
    public int role;// 1--host, 2--guest

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getSessionDescription() {
        return sessionDescription;
    }

    public void setSessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
    }

    public ArrayList<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(ArrayList<String> candidates) {
        this.candidates = candidates;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
