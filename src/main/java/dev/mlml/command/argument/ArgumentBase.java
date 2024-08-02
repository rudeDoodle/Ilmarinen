package dev.mlml.command.argument;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class ArgumentBase<V> {
    final String name, description;
    @Getter
    final boolean isRequired;

    public ArgumentBase(String name, String description, boolean required) {
        this.name = name;
        this.description = description;
        this.isRequired = required;
    }

    public abstract V parse(String input);

    @SuppressWarnings("unchecked")
    public abstract static class Builder<B extends Builder<?, ?, ?>, V, S extends ArgumentBase<?>> {
        String name, description = "";
        boolean isRequired = false;

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
