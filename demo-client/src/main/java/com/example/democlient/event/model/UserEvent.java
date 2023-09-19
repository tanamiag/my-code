package com.example.democlient.event.model;

public class UserEvent extends EventTemplate<UserModel> {
    public UserEvent(UserModel source, String event) { this(source, source, event); }

    public UserEvent(UserModel before, UserModel after, String event) { super(before, after, event); }

    public static UserEvent beforeCreate(UserModel source) {
        return new UserEvent(source, BEFORE_CREATE);
    }

    public static UserEvent afterCreate(UserModel source) {
        return new UserEvent(source, AFTER_CREATE);
    }

    public static UserEvent beforeModify(UserModel before, UserModel after) {
        return new UserEvent(before, after, BEFORE_MODIFY);
    }

    public static UserEvent afterModify(UserModel before, UserModel after) {
        return new UserEvent(before, after, AFTER_MODIFY);
    }

    public static UserEvent beforeDelete(UserModel source) {
        return new UserEvent(source, BEFORE_DELETE);
    }

    public static UserEvent afterDelete(UserModel source) {
        return new UserEvent(source, AFTER_DELETE);
    }
}