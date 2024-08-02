package dev.mlml.command.argument;

import lombok.Getter;

@Getter
public abstract class ArgumentBase<V> {
    final String name, description;
    final V defaultValue;
    final boolean isRequired;

    public ArgumentBase(String name, String description, boolean required, V defaultValue) {
        this.name = name;
        this.description = description;
        this.isRequired = required;
        this.defaultValue = defaultValue;
    }

    public V getValue(String input) {
        V value = parse(input);
        return value == null ? defaultValue : value;
    }

    public abstract V parse(String input);

    @SuppressWarnings("unchecked")
    public abstract static class Builder<B extends Builder<?, ?, ?>, V, S extends ArgumentBase<?>> {
        String name, description = "";
        boolean isRequired = false;
        V defaultValue;

        protected Builder(String name) {
            this.name = name;
        }

        public B name(String name) {
            this.name = name;
            return getThis();
        }

        public B description(String description) {
            this.description = description;
            return getThis();
        }

        public B defaultValue(V defaultValue) {
            this.defaultValue = defaultValue;
            return getThis();
        }

        public B require() {
            this.isRequired = true;
            return getThis();
        }

        public abstract S get();

        protected B getThis() {
            return (B) this;
        }
    }
}
