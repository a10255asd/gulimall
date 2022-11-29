package com.atjixue.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author LiuJixue
 * @Date 2022/8/9 11:04
 * @PackageName:com.atjixue.valid
 * @ClassName: ListValueConstrainValidator
 * @Description: TODO
 * @Version 1.0
 */
public class ListValueConstrainValidator implements ConstraintValidator<ListValue,Integer> {

    private Set<Integer> set = new HashSet<>();
    // 初始方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for(int val:vals){
            set.add(val);
        }

    }
    /**
     * @param integer 需要校验的值
     * @param constraintValidatorContext
     * @return
     * */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {

        return set.contains(integer);
    }
}
