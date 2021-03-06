package com.josephcrawley.Spitfire.entities;

import com.josephcrawley.util.Log;

public class Type extends Declaration {

    public static final Type NUMBER = new Type("number");
    public static final Type BOOLEAN = new Type("boolean");
    public static final Type CHARACTER = new Type("character");
    public static final Type STRING = new Type("string");
    public static final Type ANY = new Type("any");
    public static final Type ARRAY = new Type("[]");
    public static final Type NULL_TYPE = new Type("<>");

    /**
     * An internal type for functions.
     */
    public static final Type FUNCTION = new Type("<function>");

    /**
     * A type representing the union of all types.  It is assigned to an entity whose typename is
     * not in scope to allow compilation to proceed without generating too many spurious errors.
     * It is compatible with all other types.
     */
    public static final Type ARBITRARY = new Type("<arbitrary>");

    /**
     * The type of any array.
     */
    public static final Type ARBITRARY_ARRAY = new Type("<arbitrary array>");

    // The type of arrays of this type.  Created only if needed.
    private ArrayType arrayOfThisType = null;

    /**
     * Constructs a type with the given name.
     */
    public Type(String name) {
        super(name);
    }

    /**
     * Returns whether this type is a reference type.
     */
    public boolean isReference() {
        return this == STRING
            || this instanceof ArrayType
            || this == ARBITRARY;
    }

    /**
     * Returns whether this type is an arithmetic type.
     */
    public boolean isPrimitive() {
        return this == BOOLEAN || this == NUMBER || this == CHARACTER
            || this == STRING || this == BOOLEAN || this == ARBITRARY;
    }

    /**
     * Returns whether this type is an arithmetic type.
     */
    public boolean isArithmetic() {
        return this == NUMBER || this == ARBITRARY;
    }

    /**
     * Returns the type that is an array of this type, lazily creating it.
     */
    public Type array() {
        if (arrayOfThisType == null) {
            arrayOfThisType = new ArrayType(this);
        }
        return arrayOfThisType;
    }

    /**
     * Returns whether an expression of this type can be assigned to an expression of another
     * type.
     */
    public boolean canBeAssignedTo(Type that) {
        return this == that
        || this == NULL_TYPE && that.isReference()
        || this == ARBITRARY_ARRAY && that instanceof ArrayType
        || this == ARBITRARY
        || that == ARBITRARY
        || this instanceof ArrayType && that instanceof ArrayType &&
                ArrayType.class.cast(this).getBaseType().canBeAssignedTo(
                        ArrayType.class.cast(that).getBaseType());
    }

    /**
     * A default implementation that does nothing, since most type subclasses need no analysis.
     */
    @Override
    public void analyze(Log log, SymbolTable table, Subroutine owner, boolean inLoop) {
        // Intentionally empty.
    }
}
