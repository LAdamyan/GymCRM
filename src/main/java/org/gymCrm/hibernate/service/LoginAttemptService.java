//package org.gymCrm.hibernate.service;
//
//import jakarta.persistence.criteria.CriteriaBuilder;
//import lombok.Data;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.TimeUnit;
//
//
//@Service
//public class LoginAttemptService {
//
//    private final Map<String,Integer>attempts = new ConcurrentHashMap<>();
//    private final Map<String,Long>lockTime = new ConcurrentHashMap<>();
//    private static final int MAX_ATTEMPTS =3;
//    private static final long LOCK_TIME_DURATION = TimeUnit.MINUTES.toMillis(5);
//
//    public void loginFailed(String username) {
//        int attemptCount = attempts.getOrDefault(username, 0);
//        if (attemptCount >= MAX_ATTEMPTS) {
//            lockTime.put(username, System.currentTimeMillis());
//        }
//        attempts.put(username, ++attemptCount);
//    }
//
//        public int getAttempts(String username) {
//        return attempts.getOrDefault(username, 0);
//    }
//
//    public boolean isBlocked(String username){
//        if(lockTime.containsKey(username)){
//            Long lockedTime = lockTime.get(username);
//            if (System.currentTimeMillis() - lockedTime < LOCK_TIME_DURATION) {
//                return true;
//            } else {
//                lockTime.remove(username);
//                attempts.remove(username);
//            }
//        }
//        return false;
//    }
//    public void resetAttempts(String username){
//        attempts.remove(username);
//        lockTime.remove(username);
//    }
//}
