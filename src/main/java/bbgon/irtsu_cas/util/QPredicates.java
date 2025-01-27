package bbgon.irtsu_cas.util;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import com.querydsl.core.types.Predicate;
import java.util.function.Function;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicates {

    private List<Predicate> predicates= new ArrayList<>();

    public <T> QPredicates add(T object, Function<T, Predicate> function) {
        if(object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public Predicate buildAnd(){
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate buildOr(){
        return ExpressionUtils.anyOf(predicates);
    }

    public static QPredicates builder() {
        return new QPredicates();
    }
}
