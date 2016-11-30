package xl.playground.algo.tree;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by xl on 11/30/16.
 */
public class BinaryHeap<Item extends Comparable<Item>> implements Queue<Item> {

    private Item[] items;
    private int i;

    @SuppressWarnings("unchecked")
    public BinaryHeap(int capacity) {
        items = (Item[]) new Comparable[capacity + 1];
        i = 0;
    }

    @Override
    public int size() {
        return i;
    }

    @Override
    public boolean isEmpty() {
        return i == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int j = 0; j < i; j++) {
            if (items[j].equals(o)) return true;
        }
        return false;
    }

    @Override
    public Iterator<Item> iterator() {
        if (i == 0) {
            return Collections.emptyIterator();
        }
        return Arrays.asList(items).subList(1, i + 1).iterator();
    }

    @Override
    public Object[] toArray() {
        if (i == 0) return new Object[0];
        return Arrays.copyOfRange(items, 1, i + 1);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        System.arraycopy(items, 1, a, 0, i + 1);
        return a;
    }

    @Override
    public boolean add(Item item) {
        return offer(item);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    @Override
    public boolean addAll(Collection<? extends Item> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        items = (Item[]) new Comparable[items.length];
        i = 1;
    }

    @Override
    public boolean equals(Object o) {
        if (!getClass().isAssignableFrom(o.getClass())) return false;
        BinaryHeap other = (BinaryHeap) o;
        return i == other.i && Arrays.equals(items, other.items);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(items);
    }

    @Override
    public boolean offer(Item item) {
        if (i == items.length - 1) {
            items = Arrays.copyOf(items, items.length * 2);
        }
        items[++i] = item;
        swimUp(i);
        return true;
    }

    @Override
    public Item remove() {
        if (i == 0) {
            throw new NoSuchElementException();
        }
        Item result = items[1];
        exchange(i--, 1);
        swimDown(i);
        items[i + 1] = null;
        return result;
    }

    @Override
    public Item poll() {
        try {
            return remove();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public Item element() {
        if (i == 0) {
            throw new NoSuchElementException();
        }
        return items[1];
    }

    @Override
    public Item peek() {
        try {
            return element();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        if (i == 0) return "[]";
        return Arrays.asList(items).subList(1, i + 1).toString();
    }

    private void swimUp(int i) {
        while (i > 1 && items[i].compareTo(items[i / 2]) > 0) {
            exchange(i, i / 2);
            i /= 2;
        }
    }

    private void swimDown(int i) {
        int parent = 1;
        int ii = i / 2;
        while (parent < ii) {
            int left = parent * 2;
            int right = parent * 2 + 1;
            int swap = (items[left].compareTo(items[right]) >= 0) ? left : right;

            if (items[parent].compareTo(items[left]) < 0) {
                exchange(parent, swap);
                parent = left;
            } else if (items[parent].compareTo(items[right]) < 0) {
                exchange(parent, swap);
                parent = right;
            } else break;
        }
    }

    private void exchange(int i, int j) {
        Item tmp = items[i];
        items[i] = items[j];
        items[j] = tmp;
    }

    public static void main(String... args) {
        BinaryHeap<Integer> heap = new BinaryHeap<>(5);
        heap.addAll(IntStream.range(0, 10).boxed().collect(Collectors.toList()));
        System.out.println(heap);

        verify(heap);

        for (int i = 0; i < 10; i++) {
            heap.remove();
            System.out.println(heap);
            verify(heap);
        }
    }

    private static <T extends Comparable<T>> void verify(BinaryHeap<T> heap) {
        T[] items = heap.items;
        int ii = heap.size() / 2;
        for (int i = 1; i < ii; i++) {
            if (items[i].compareTo(items[i * 2]) < 0) {
                throw new IllegalStateException(
                        String.format("heap rule violated: items[%d] < items[%d]", i, i * 2));
            }
            if (items[i].compareTo(items[i * 2 + 1]) < 0) {
                throw new IllegalStateException(
                        String.format("heap rule violated: items[%d] < items[%d]", i, i * 2 + 1));
            }
        }
    }
}
