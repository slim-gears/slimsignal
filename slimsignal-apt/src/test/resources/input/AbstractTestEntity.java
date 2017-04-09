import com.slimgears.slimsignal.core.interfaces.conditions.PredicateType;

enum TestEnum {
    VALUE_1,
    VALUE_2
}

class CustomType {
}

class AbstractTestEntity {
    protected int id;
    protected String name;
    protected AbstractRelatedEntity related;
    protected ExistingEntity relatedExisting;
    protected TestEnum enumValue;
    protected CustomType customTypeValue;
}
