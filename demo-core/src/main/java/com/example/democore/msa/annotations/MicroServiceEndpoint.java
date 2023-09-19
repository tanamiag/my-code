package com.example.democore.msa.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MicroServiceEndpoint {
	String value();
}
