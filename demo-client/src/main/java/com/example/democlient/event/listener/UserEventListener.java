package com.example.democlient.event.listener;

import com.example.democlient.event.model.UserEvent;
import com.example.democlient.event.model.UserModel;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import static com.example.democlient.event.model.EventTemplate.*;

@Component
public class UserEventListener implements ApplicationListener<UserEvent> {

    @Override
    public void onApplicationEvent(UserEvent event) {

        UserModel before = event.getBefore();
        UserModel after = event.getAfter();

        switch (event.getEvent()) {
            case BEFORE_CREATE:
                beforeCreate(after); // 또는 before
                break;
            case AFTER_CREATE:
                afterCreate(after); // 또는 before
                break;
            case BEFORE_MODIFY:
                beforeModify(before, after);
                break;
            case AFTER_MODIFY:
                afterModify(before, after);
                break;
            case BEFORE_DELETE:
                break;
            case AFTER_DELETE:
                break;
            default:
                break;
        }

    }

    private void beforeCreate(UserModel user) {
        // 이벤트가 발생한 도메인(User)과는 다른 도메인의 작업을 진행
    }
    private void afterCreate(UserModel user) {
        // 이벤트가 발생한 도메인(User)과는 다른 도메인의 작업을 진행
    }

    private void beforeModify(UserModel before, UserModel after) {
        // 이벤트가 발생한 도메인(User)과는 다른 도메인의 작업을 진행
    }
    private void afterModify(UserModel before, UserModel after) {
        // 이벤트가 발생한 도메인(User)과는 다른 도메인의 작업을 진행
    }
}
