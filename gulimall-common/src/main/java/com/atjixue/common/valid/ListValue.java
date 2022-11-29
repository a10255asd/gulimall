package com.atjixue.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author LiuJixue
 * @Date 2022/8/9 10:47
 * @PackageName:com.atjixue.valid
 * @ClassName: ListValue
 * @Description: TODO
 * @Version 1.0
 */
@Documented
@Constraint(
        validatedBy = {ListValueConstrainValidator.class}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListValue {
    String message() default "{com.atjixue.common.valid.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] vals() default {};
}
