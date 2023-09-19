package com.example.democlient.event.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public abstract class EventTemplate<T> extends ApplicationEvent {
    public static final String BEFORE_CREATE = "before-create";
    public static final String AFTER_CREATE = "after-create";
    public static final String BEFORE_MODIFY = "before-modify";
    public static final String AFTER_MODIFY = "after-modify";
    public static final String BEFORE_DELETE = "before-delete";
    public static final String AFTER_DELETE = "after-delete";

    private final String event;

    private final T before;

    private final T after;

//    public EventTemplate(T source, String event) {
//        this(source, source, event);
//    }

    public EventTemplate(T before, T after, String event) {
        super(before);
        this.before = before;
        this.after = after;
        this.event = event;
    }
}