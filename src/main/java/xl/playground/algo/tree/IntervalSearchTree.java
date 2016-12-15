package xl.playground.algo.tree;

import xl.playground.algo.util.Interval;

import java.util.ArrayList;
import java.util.StringJoiner;

/**
 * Created by xl on 12/15/16.
 */
public class IntervalSearchTree<E extends Comparable<E>> {

    private Node<E> root;
    private boolean flip;

    public void add(Interval<E> value) {
        root = addNode(root, value);
    }

    public void remove(Interval<E> value) {
        root = removeNode(root, value);
        flip = !flip;
    }

    public Iterable<Interval<E>> intersect(Interval<E> value) {
        ArrayList<Interval<E>> result = new ArrayList<>();
        intersectNode(root, value, result);
        return result;
    }

    public void visit(Visitor<E> visitor) {
        visitNode(root, visitor);
    }

    private Node<E> addNode(Node<E> root, Interval<E> value) {
        if (root == null) {
            return new Node<>(value);
        }

        int cmp = value.compareTo(root.value);
        if (cmp <= 0) {
            root.left = addNode(root.left, value);
            invalidateMaxEndFromLeft(root);
        } else {
            root.right = addNode(root.right, value);
            invalidateMaxEndFromRight(root);
        }

        return root;
    }

    private Node<E> removeNode(Node<E> root, Interval<E> value) {
        if (root == null) {
            return null;
        }

        int cmp = value.compareTo(root.value);
        if (cmp == 0 && root.value.equals(value)) {
            boolean hasLeftChild = root.left != null;
            boolean hasRightChild = root.right != null;

            if (hasLeftChild && hasRightChild) {
                Node<E> r;
                if (flip) {
                    r = minNode(root.right);
                    r.right = removeNode(root.right, r.value);
                    r.left = root.left;
                } else {
                    r = maxNode(root.left);
                    r.left = removeNode(root.left, r.value);
                    r.right = root.right;
                }
                invalidateMaxEndFromBoth(root);
                return r;
            }

            return hasLeftChild ? root.left : root.right;
        } else if (cmp <= 0) {
            root.left = removeNode(root.left, value);
            invalidateMaxEndFromLeft(root);
        } else {
            root.right = removeNode(root.right, value);
            invalidateMaxEndFromRight(root);
        }

        return root;
    }

    private Node<E> minNode(Node<E> root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    private Node<E> maxNode(Node<E> root) {
        while (root.right != null) {
            root = root.right;
        }
        return root;
    }

    private void intersectNode(Node<E> root, Interval<E> value, ArrayList<Interval<E>> result) {
        if (root == null) {
            return;
        }

        if (root.left != null && root.left.maxEnd.compareTo(value.getStart()) > 0) {
            intersectNode(root.left, value, result);
        }

        if (root.value.isIntersectWith(value)) {
            result.add(root.value);
        }

        intersectNode(root.right, value, result);
    }

    private void visitNode(Node<E> root, Visitor<E> visitor) {
        if (root == null) {
            return;
        }

        visitor.visit(root.value, root.maxEnd);
        visitNode(root.left, visitor);
        visitNode(root.right, visitor);
    }

    private void invalidateMaxEndFromLeft(Node<E> root) {
        Node<E> child = root.left;
        if (child != null && child.maxEnd.compareTo(root.maxEnd) > 0) {
            root.maxEnd = child.maxEnd;
        }
    }

    private void invalidateMaxEndFromRight(Node<E> root) {
        Node<E> child = root.right;
        if (child != null && child.maxEnd.compareTo(root.maxEnd) > 0) {
            root.maxEnd = child.maxEnd;
        }
    }

    private void invalidateMaxEndFromBoth(Node<E> root) {
        E newMaxEnd = root.value.getEnd();
        if (root.left != null && root.left.maxEnd.compareTo(newMaxEnd) > 0) {
            newMaxEnd = root.left.value.getEnd();
        }
        if (root.right != null && root.right.maxEnd.compareTo(newMaxEnd) > 0) {
            newMaxEnd = root.right.value.getEnd();
        }
        root.maxEnd = newMaxEnd;
    }

    public interface Visitor<E extends Comparable<E>> {

        void visit(Interval<E> value, E maxEnd);
    }

    private static class Node<E extends Comparable<E>> {

        public Node<E> left;
        public Node<E> right;
        public Interval<E> value;
        public E maxEnd;

        public Node(Interval<E> value) {
            this.value = value;
            this.maxEnd = value.getEnd();
        }
    }

    @SafeVarargs
    private static IntervalSearchTree<Integer> makeTree(Interval<Integer>... values) {
        IntervalSearchTree<Integer> result = new IntervalSearchTree<>();
        for (Interval<Integer> v : values) {
            result.add(v);
        }
        return result;
    }

    private static String valuesOf(IntervalSearchTree<Integer> tree) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        tree.visit((iv, max) -> joiner.add(iv + " <" + max + ">"));
        return joiner.toString();
    }

    private static void testAddAndRemove() {
        System.out.println("Test add and remove: ");
        System.out.println("=====================");

        IntervalSearchTree<Integer> tree = makeTree(
                new Interval<>(5, 8),
                new Interval<>(4, 8),
                new Interval<>(7, 10)
        );

        System.out.println(valuesOf(tree));

        tree.remove(new Interval<>(5, 8));
        System.out.println(valuesOf(tree));
        tree.remove(new Interval<>(7, 10));
        System.out.println(valuesOf(tree));
        tree.remove(new Interval<>(4, 8));
        System.out.println(valuesOf(tree));

        System.out.println();
    }

    private static void testIntersect() {
        System.out.println("Test intersect: ");
        System.out.println("================");

        IntervalSearchTree<Integer> tree = makeTree(
                new Interval<>(17, 19),
                new Interval<>(5, 8),
                new Interval<>(21, 24),
                new Interval<>(4, 8),
                new Interval<>(15, 18),
                new Interval<>(7, 10),
                new Interval<>(16, 22)
        );

        System.out.println(valuesOf(tree));

        System.out.println("intersect(23, 25) = " + tree.intersect(new Interval<>(23, 25)));
        System.out.println("intersect(21, 23) = " + tree.intersect(new Interval<>(21, 23)));
        System.out.println("intersect(12, 14) = " + tree.intersect(new Interval<>(12, 14)));
        System.out.println("intersect(3, 12) = " + tree.intersect(new Interval<>(3, 12)));
    }

    public static void main(String... args) {
        testAddAndRemove();
        testIntersect();
    }
}
