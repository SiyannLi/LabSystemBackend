package com.example.LabSystemBackend.jwt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @version 1.0
 * @author Cong Liu
 *
 * @SuperAdminLoginToken
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SuperAdminLoginToken {
    boolean required() default true;
}
