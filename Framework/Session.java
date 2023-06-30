package etu001935.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Session {
    
}
