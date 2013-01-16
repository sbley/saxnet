package fr.brouillard.javafx.weld;

import java.lang.annotation.*;

import javax.inject.Qualifier;

@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartupScene {
}
