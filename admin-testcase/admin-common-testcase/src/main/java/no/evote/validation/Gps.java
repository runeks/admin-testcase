package no.evote.validation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

@ReportAsSingleViolation
@Pattern(regexp = "(^-?\\d{1,2}(\\.\\d{0,10})? {0,3}, {0,3}-?\\d{1,3}(\\.\\d{0,10})? {0,3}$)?", message = Gps.MESSAGE)
@Constraint(validatedBy = GpsValidator.class)
@Target({ METHOD })
@Retention(RUNTIME)
public @interface Gps {

	public static final String MESSAGE = "{@validation.gps}";

	String message() default MESSAGE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
