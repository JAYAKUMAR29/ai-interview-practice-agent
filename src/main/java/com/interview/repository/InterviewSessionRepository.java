package com.interview.repository;

import com.interview.model.InterviewSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InterviewSessionRepository {

    private final Map<String, InterviewSession> sessionMap = new ConcurrentHashMap<>();

    public InterviewSession save(InterviewSession session) {
        sessionMap.put(session.getSessionId(), session);
        return session;
    }

    public Optional<InterviewSession> findById(String sessionId) {
        return Optional.ofNullable(sessionMap.get(sessionId));
    }

    public List<InterviewSession> findAll() {
        return new ArrayList<>(sessionMap.values());
    }

    public boolean existsById(String sessionId) {
        return sessionMap.containsKey(sessionId);
    }

    public void deleteById(String sessionId) {
        sessionMap.remove(sessionId);
    }
}
