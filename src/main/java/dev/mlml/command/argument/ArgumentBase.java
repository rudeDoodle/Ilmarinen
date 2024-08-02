package dev.mlml.command.argument;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class ArgumentBase<V> {
    final String name, description;
    @Getter
    final V defaultValue;
    final boolean isRequired;
    final boolean isVArgs;

    public ArgumentBase(String name, String description, boolean required, V defaultValue, boolean isVArgs) {
        this.name = name;
        this.description = description;
        this.isRequired = required;
        this.defaultValue = defaultValue;
        this.isVArgs = isVArgs;
    }

    public V getValue(String input) {
        V value = parse(input);
        return Objects.isNull(value) ? defaultValue : value;
    }

    public abstract V parse(String input);

    @SuppressWarnings("unchecked")
    public abstract static class Builder<B extends Builder<?, ?, ?>, V, S extends ArgumentBase<?>> {
        String name, description = "";
        boolean isRequired = false;
        V defaultValue;
        boolean isVArgs = false;

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

        public B isVArgs() {
            this.isVArgs = true;
            return getThis();
        }

        public abstract S get();

        protected B getThis() {
            return (B) this;
        }
    }
}
