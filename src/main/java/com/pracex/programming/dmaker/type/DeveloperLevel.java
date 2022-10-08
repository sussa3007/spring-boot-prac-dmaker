package com.pracex.programming.dmaker.type;

import com.pracex.programming.dmaker.exception.DMakerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static com.pracex.programming.dmaker.constant.DMakerConstant.*;
import static com.pracex.programming.dmaker.exception.DMakerErrorCode.*;

@Getter
@AllArgsConstructor
public enum DeveloperLevel {


    NEW("신입 개발자",
            years -> years == 0),

    JUNIOR("주니어 개발자",
            years -> years <= MAX_JUNIOR_EXPERIENCE_YEARS),

    JUNGNIOR("중니어 개발자", years ->
            years>MAX_JUNIOR_EXPERIENCE_YEARS
            && years<MIN_SENIOR_EXPERIENCE_YEARS),

    SENIOR("시니어 개발자",
            years -> years >= MIN_SENIOR_EXPERIENCE_YEARS);


    private final String description;
    private final Function<Integer, Boolean> validateFunction;

    public void validateExperienceYears(Integer years){
        if(!validateFunction.apply(years))
            throw new DMakerException(
                    LEVEL_EXPERIENCE_YEARS_NOT_MATCHED
            );

    }

}
