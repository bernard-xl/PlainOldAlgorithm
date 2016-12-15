package xl.playground.algo.tree;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by xl on 12/12/16.
 */
public class BinarySearchTree<Item extends Comparable<Item>> {

    private Node<Item> root;
    private boolean flip;

    public void add(Item value) {
        root = addNode(root, value);
    }

    public void remove(Item value) {
        root = removeNode(root, value, flip);
        flip = !flip;
    }

    public Item min() {
        return (root == null) ? null : minNode(root).value;
    }

    public Item max() {
        return (root == null) ? null : maxNode(root).value;
    }

    public Item floor(Item value) {
        Node<Item> foundNode = floorNode(root, value);
        return (foundNode == null) ? null : foundNode.value;
    }

    public Item ceiling(Item value) {
        Node<Item> foundNode = ceilingNode(root, value);
        return (foundNode == null) ? null : foundNode.value;
    }

    public int rank(Item value) {
        return rankNode(root, value);
    }

    public Range range(Item start, Item end) {
        if (end.compareTo(start) <= 0) {
            throw new IllegalArgumentException("start must be less than end");
        }
        return new Range(start, end);
    }

    public Iterable<Item> preOrderTraversal() {
        Iterator<Item> iterator = new PreOrderIterator<>(root);
        return () -> iterator;
    }

    public Iterable<Item> inOrderTraversal() {
        Iterator<Item> iterator = new InOrderIterator<>(root);
        return () -> iterator;
    }

    public Iterable<Item> postOrderTraversal() {
        Iterator<Item> iterator = new PostOrderIterator<>(root);
        return () -> iterator;
    }

    private Node<Item> addNode(Node<Item> root, Item value) {
        if (root == null) {
            return new Node<>(value);
        }

        if (root.value.compareTo(value) >= 0) {
            root.left = addNode(root.left, value);
        } else {
            root.right = addNode(root.right, value);
        }

        root.count = 1 + countChildren(root);
        return root;
    }

    private Node<Item> removeNode(Node<Item> root, Item value, boolean flip) {
        if (root == null) {
            return null;
        }

        int cmp = root.value.compareTo(value);
        if (cmp == 0) {
            boolean hasLeftChild = root.left != null;
            boolean hasRightChild = root.right != null;

            if (hasLeftChild && hasRightChild) {
                if (flip) {
                    Node<Item> r = maxNode(root.left);
                    root.left = removeNode(root.left, r.value, true);
                    root.value = r.value;
                } else {
                    Node<Item> r = minNode(root.right);
                    root.right = removeNode(root.right, r.value, false);
                    root.value = r.value;
                }

                root.count = 1 + countChildren(root);
                return root;
            }

            return hasLeftChild ? root.left : root.right;
        } else if (cmp > 0) {
            root.left = removeNode(root.left, value, flip);
            root.count = 1 + countChildren(root);
            return root;
        } else {
            root.right = removeNode(root.right, value, flip);
            root.count = 1 + countChildren(root);
            return root;
        }
    }

    private Node<Item> minNode(Node<Item> root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    private Node<Item> maxNode(Node<Item> root) {
        while (root.right != null) {
            root = root.right;
        }
        return root;
    }

    private Node<Item> floorNode(Node<Item> root, Item value) {
        if (root == null) {
            return null;
        }

        int cmp = root.value.compareTo(value);
        if (cmp == 0) {
            return root;
        } else if (cmp < 0) {
            Node<Item> right = floorNode(root.right, value);
            return (right == null) ? root : right;
        } else {
            return floorNode(root.left, value);
        }
    }

    private Node<Item> ceilingNode(Node<Item> root, Item value) {
        if (root == null) {
            return null;
        }

        int cmp = root.value.compareTo(value);
        if (cmp == 0) {
            return root;
        } else if (cmp < 0) {
            return ceilingNode(root.right, value);
        } else {
            Node<Item> left = ceilingNode(root.left, value);
            return (left == null) ? root : left;
        }
    }

    private int rankNode(Node<Item> root, Item value) {
        if (root == null) {
            return 0;
        }

        int cmp = root.value.compareTo(value);
        if (cmp == 0) {
            return (root.left == null) ? 0 : root.left.count;
        } else if (cmp > 0) {
            return rankNode(root.left, value);
        } else {
            int leftCount = (root.left == null) ? 0 : root.left.count;
            return 1 + leftCount + rankNode(root.right, value);
        }
    }

    private int countChildren(Node<Item> root) {
        int leftCount = (root.left == null) ? 0 : root.left.count;
        int rightCount = (root.right == null) ? 0 : root.right.count;
        return leftCount + rightCount;
    }

    public class Range {

        private Item start;
        private Item end;

        Range(Item start, Item end) {
            this.start = start;
            this.end = end;
        }

        public int count() {
            return rank(end) - rank(start);
        }

        public Iterable<Item> values() {
            ArrayList<Item> list = new ArrayList<>();
            includeValues(root, list);
            return list;
        }

        private void includeValues(Node<Item> root, ArrayList<Item> list) {
            if (root == null) {
                return;
            }

            boolean afterStart = root.value.compareTo(start) >= 0;
            boolean beforeEnd = root.value.compareTo(end) < 0;

            if (afterStart) {
                includeValues(root.left, list);
            }

            if (afterStart && beforeEnd) {
                list.add(root.value);
            }

            if (afterStart && beforeEnd) {
                includeValues(root.right, list);
            }
        }
    }

    private static class PreOrderIterator<Item extends Comparable<Item>> implements Iterator<Item> {

        private Deque<Node<Item>> stack;

        public PreOrderIterator(Node<Item> node) {
            stack = new LinkedList<>();
            if (node != null) stack.push(node);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Item next() {
            Node<Item> result = stack.pop();
            if (result.right != null) stack.push(result.right);
            if (result.left != null) stack.push(result.left);
            return result.value;
        }
    }

    private static class InOrderIterator<Item extends Comparable<Item>> implements Iterator<Item> {

        private Deque<Node<Item>> stack;

        public InOrderIterator(Node<Item> node) {
            stack = new LinkedList<>();
            populateStack(node);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Item next() {
            Node<Item> result = stack.pop();
            populateStack(result.right);
            return result.value;
        }

        private void populateStack(Node<Item> current) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
        }
    }

    private static class PostOrderIterator<Item extends Comparable<Item>> implements Iterator<Item> {

        private Deque<Item> stack;

        public PostOrderIterator(Node<Item> node) {
            stack = populateStack(node);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Item next() {
            return stack.pop();
        }

        private Deque<Item> populateStack(Node<Item> node) {
            LinkedList<Node<Item>> tmp = new LinkedList<>();
            LinkedList<Item> result = new LinkedList<>();

            tmp.push(node);

            while (!tmp.isEmpty()) {
                Node<Item> i = tmp.pop();
                result.push(i.value);
                if (i.left != null) tmp.push(i.left);
                if (i.right != null) tmp.push(i.right);
            }

            return result;
        }
    }

    private static class Node<Item extends Comparable<Item>> {

        public Node<Item> left;
        public Node<Item> right;
        public Item value;
        public int count;

        public Node(Item value) {
            this.value = value;
            this.count = 1;
        }
    }

    @SafeVarargs
    private static <Item extends Comparable<Item>> BinarySearchTree<Item> makeTree(Item... values) {
        BinarySearchTree<Item> tree = new BinarySearchTree<>();
        for (Item v : values) {
            tree.add(v);
        }
        return tree;
    }

    private static <Item extends Comparable<Item>> String valuesOf(Iterable<Item> itemIterable) {
        return StreamSupport.stream(itemIterable.spliterator(), false)
                .map(Object::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private static void testAddAndDelete() {
        System.out.println("Test add and delete: ");
        System.out.println("=====================");

        BinarySearchTree<Integer> tree = makeTree(4, 2, 6, 1, 3, 5, 7);

        tree.remove(4);
        System.out.println(valuesOf(tree.preOrderTraversal()));
        tree.remove(5);
        System.out.println(valuesOf(tree.preOrderTraversal()));
        tree.remove(3);
        System.out.println(valuesOf(tree.preOrderTraversal()));
        tree.remove(2);
        System.out.println(valuesOf(tree.preOrderTraversal()));
        tree.remove(6);
        System.out.println(valuesOf(tree.preOrderTraversal()));
        tree.remove(7);
        System.out.println(valuesOf(tree.preOrderTraversal()));
        tree.remove(1);
        System.out.println(valuesOf(tree.preOrderTraversal()));

        System.out.println();
    }

    private static void testMinAndMax() {
        System.out.println("Test min and max: ");
        System.out.println("=====================");

        BinarySearchTree<Integer> tree = makeTree(4, 2, 6, 1, 3, 5, 7);

        System.out.println("min() = " + tree.min());
        System.out.println("max() = " + tree.max());

        System.out.println();
    }

    private static void testFloorAndCeiling() {
        System.out.println("Test floor and ceiling: ");
        System.out.println("========================");

        BinarySearchTree<Integer> tree = makeTree(8, 4, 12, 1, 6, 10, 14);

        System.out.println("floor(0) = " + tree.floor(0));
        System.out.println("floor(3) = " + tree.floor(3));
        System.out.println("floor(12) = " + tree.floor(12));

        System.out.println("ceiling(16) = " + tree.ceiling(16));
        System.out.println("ceiling(13) = " + tree.ceiling(13));
        System.out.println("ceiling(6) = " + tree.ceiling(6));

        System.out.println();
    }

    private static void testRank() {
        System.out.println("Test rank: ");
        System.out.println("===========");

        BinarySearchTree<Integer> tree = makeTree(8, 4, 12, 1, 6, 10, 14);

        System.out.println("rank(1) = " + tree.rank(1));
        System.out.println("rank(2) = " + tree.rank(2));
        System.out.println("rank(3) = " + tree.rank(3));
        System.out.println("rank(4) = " + tree.rank(4));
        System.out.println("rank(5) = " + tree.rank(5));
        System.out.println("rank(6) = " + tree.rank(6));
        System.out.println("rank(16) = " + tree.rank(16));

        System.out.println();
    }

    private static void testRange() {
        System.out.println("Test range: ");
        System.out.println("===========");

        BinarySearchTree<Integer> tree = makeTree(8, 4, 12, 1, 6, 10, 14);

        System.out.println("range(4, 12).count() = " + tree.range(4, 12).count());
        System.out.println("range(4, 12).values() = " + valuesOf(tree.range(4, 12).values()));

        System.out.println("range(0, 16).count() = " + tree.range(0, 16).count());
        System.out.println("range(0, 16).values() = " + valuesOf(tree.range(0, 16).values()));

        System.out.println("range(7, 9).count() = " + tree.range(7, 9).count());
        System.out.println("range(7, 9).values() = " + valuesOf(tree.range(7, 9).values()));

        System.out.println("range(-99, 0).count() = " + tree.range(-99, 0).count());
        System.out.println("range(-99, 0).values() = " + valuesOf(tree.range(-99, 0).values()));

        System.out.println("range(99, 999).count() = " + tree.range(99, 999).count());
        System.out.println("range(99, 999).values() = " + valuesOf(tree.range(99, 999).values()));

        System.out.println();
    }

    private static void testTraversal() {
        System.out.println("Test traversal: ");
        System.out.println("================");

        BinarySearchTree<Integer> tree = makeTree(4, 2, 6, 1, 3, 5, 7);

        System.out.println("pre-order traversal:  " + valuesOf(tree.preOrderTraversal()));
        System.out.println("in-order traversal:   " + valuesOf(tree.inOrderTraversal()));
        System.out.println("post-order traversal: " + valuesOf(tree.postOrderTraversal()));

        System.out.println();
    }

    public static void main(String... args) {
        testAddAndDelete();
        testMinAndMax();
        testFloorAndCeiling();
        testRank();
        testRange();
        testTraversal();
    }
}
