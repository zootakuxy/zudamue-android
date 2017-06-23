package st.zudamue.support.android.sql.type;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * Created by dchost on 03/02/17.
 */

public class ListCharSequence extends BaseTypeCharSequence<List<CharSequence>>{


    public ListCharSequence() {
        super(new LinkedList<CharSequence>());
    }

    public ListCharSequence(List<CharSequence> value) {
        super(value);
    }


    private String asString () {
        String string = "";
        for(CharSequence charSequence: this.value()) {
            string += String.valueOf(charSequence);
        }
        return string;
    }

    public void add(IntegerCharSequence charSequence) {
        this.value().add(charSequence);
    }

    public int size() {
        return value().size();
    }

    public int lastIndexOf(Object o) {
        return value().lastIndexOf(o);
    }

    public CharSequence get(int index) {
        return value().get(index);
    }

    public boolean containsAll(Collection<?> c) {
        return value().containsAll(c);
    }

    public void clear() {
        value().clear();
    }


    public CharSequence set(int index, CharSequence element) {
        return value().set(index, element);
    }

    public Iterator<CharSequence> iterator() {
        return value().iterator();
    }

    public ListIterator<CharSequence> listIterator() {
        return value().listIterator();
    }

    public boolean contains(Object o) {
        return value().contains(o);
    }

    public boolean isEmpty() {
        return value().isEmpty();
    }

    public ListIterator<CharSequence> listIterator(int index) {
        return value().listIterator(index);
    }

    public CharSequence remove(int index) {
        return value().remove(index);
    }

    public Object[] toArray() {
        return value().toArray();
    }

    public boolean add(CharSequence charSequence) {
        return value().add(charSequence);
    }

    public List<CharSequence> subList(int fromIndex, int toIndex) {
        return value().subList(fromIndex, toIndex);
    }

    public boolean addAll(Collection<? extends CharSequence> c) {
        return value().addAll(c);
    }

    public void add(int index, CharSequence element) {
        value().add(index, element);
    }

    public int indexOf(Object o) {
        return value().indexOf(o);
    }

    public <T> T[] toArray(T[] a) {
        return value().toArray(a);
    }

    public boolean retainAll(Collection<?> c) {
        return value().retainAll(c);
    }

    public boolean addAll(int index, Collection<? extends CharSequence> c) {
        return value().addAll(index, c);
    }

    public void sort(Comparator<? super CharSequence> c) {
        Collections.sort(this.value(), c);
    }

    public boolean removeAll(Collection<?> c) {
        return value().removeAll(c);
    }


    public void forEach(Consumer<? super CharSequence> action) {
        for (CharSequence t : this.value()) {
            action.accept(t);
        }
    }

    public boolean remove(Object o) {
        return value().remove(o);
    }



    public boolean removeIf(Predicate<? super CharSequence> filter) {
        boolean removed = false;
        final Iterator<CharSequence> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    @Override
    public boolean equals(Object o) {
        return value().equals(o);
    }

    interface Consumer<T> {
        /**
         * Performs this operation on the given argument.
         *
         * @param t the input argument
         */
        void accept(T t);
    }
    
    interface Predicate <T> {

        /**
         * Evaluates this predicate on the given argument.
         *
         * @param t the input argument
         * @return {@code true} if the input argument matches the predicate,
         * otherwise {@code false}
         */
        boolean test(T t);
    }
}
