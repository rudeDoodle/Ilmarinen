package dev.mlml.command.argument;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class ArgumentBase<V> {
    final String name, description;
    @Getter
    final boolean isRequired;
    final boolean isVArgs;

    public ArgumentBase(String name, String description, boolean required, boolean isVArgs) {
        this.name = name;
        this.description = description;
        this.isRequired = required;
        this.isVArgs = isVArgs;
    }

    public abstract V parse(String input);

    @SuppressWarnings("unchecked")
    public abstract static class Builder<B extends Builder<?, ?, ?>, V, S extends ArgumentBase<?>> {
        String name, description = "";
        boolean isRequired = false;
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
