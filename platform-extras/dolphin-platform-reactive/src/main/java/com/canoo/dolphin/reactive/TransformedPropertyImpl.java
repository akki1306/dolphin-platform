package com.canoo.dolphin.reactive;

import com.canoo.dolphin.event.Subscription;
import com.canoo.dolphin.event.ValueChangeEvent;
import com.canoo.dolphin.event.ValueChangeListener;
import com.canoo.dolphin.mapping.Property;
import rx.functions.Action1;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by hendrikebbers on 18.05.16.
 */
public class TransformedPropertyImpl<T> implements TransformedProperty<T>, Action1<T> {

    private T value;

    private final Subscription subscription;

    private final List<ValueChangeListener<? super T>> listeners = new CopyOnWriteArrayList<>();

    public TransformedPropertyImpl(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void set(T value) {
        throw new RuntimeException("The transformed property is bound to another property!");
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public Subscription onChanged(ValueChangeListener<? super T> listener) {
        listeners.add(listener);
        return new Subscription() {
            @Override
            public void unsubscribe() {
                listeners.remove(listener);
            }
        };
    }

    protected void firePropertyChanged(final T oldValue, final T newValue) {
        final ValueChangeEvent<T> event = new ValueChangeEvent<T>() {
            @Override
            public Property<T> getSource() {
                return TransformedPropertyImpl.this;
            }

            @Override
            public T getOldValue() {
                return oldValue;
            }

            @Override
            public T getNewValue() {
                return newValue;
            }
        };
        for(ValueChangeListener<? super T> listener : listeners) {
            listener.valueChanged(event);
        }
    }

    @Override
    public void unsubscribe() {
        subscription.unsubscribe();
    }

    @Override
    public void call(T t) {
        T oldValue = this.value;
        this.value = t;
        firePropertyChanged(oldValue, value);
    }
}
