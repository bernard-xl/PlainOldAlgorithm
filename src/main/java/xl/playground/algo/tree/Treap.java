package xl.playground.algo.tree;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xl on 12/22/16.
 */
public class Treap<Item extends Comparable<Item>> {

    private final Random random = new Random();

    private Node<Item> root;

    public void add(Item value) {
        root = addNode(root, value);
    }

    public void remove(Item value) {
        root = removeNode(root, value);
    }

    public int size() {
        return (root == null) ? 0 : root.count;
    }

    public int depth() {
        return (root == null) ? 0 : root.level;
    }

    public int balance() {
        if (root == null) {
            return 0;
        }

        int left = (root.left == null) ? 0 : root.left.level;
        int right = (root.right == null) ? 0 : root.right.level;

        return left - right;
    }

    public boolean isBinarySearchTree() {
        return checkBinarySearchTreeRule(root);
    }

    @Override
    public String toString() {
        ArrayList<List<Item>> levels = new ArrayList<>();
        List<Node<Item>> last = Collections.singletonList(root);

        while (last.stream().anyMatch(Objects::nonNull)) {
            levels.add(last.stream().map(i -> i != null ? i.value : null).collect(Collectors.toList()));

            ArrayList<Node<Item>> current = new ArrayList<>(last.size() * 2);
            for (Node<Item> item : last) {
                if (item == null) {
                    current.add(null);
                    current.add(null);
                } else {
                    current.add(item.left);
                    current.add(item.right);
                }
            }
            last = current;
        }

        return levels.toString();
    }

    private Node<Item> addNode(Node<Item> root, Item value) {
        if (root == null) {
            return new Node<>(random.nextInt(), value);
        }

        if (value.compareTo(root.value) <= 0) {
            root.left = addNode(root.left, value);
            if (root.rank > root.left.rank) {
                root = rotateRight(root);
            }
        } else {
            root.right = addNode(root.right, value);
            if (root.rank > root.right.rank) {
                root = rotateLeft(root);
            }
        }

        root.count = countChildren(root);
        root.level = countLevel(root);
        return root;
    }

    private Node<Item> removeNode(Node<Item> root, Item value) {
        if (root == null) {
            return null;
        }

        int cmp = value.compareTo(root.value);
        if (cmp == 0) {
            root = sinkNode(root);
        } else if (cmp < 0) {
            root.left = removeNode(root.left, value);
        } else {
            root.right = removeNode(root.right, value);
        }

        if (root != null) {
            root.count = countChildren(root);
            root.level = countLevel(root);
        }
        return root;
    }

    private Node<Item> sinkNode(Node<Item> root) {
        boolean hasLeftChild = root.left != null;
        boolean hasRightChild = root.right != null;

        if (hasLeftChild && hasRightChild) {
            if (root.left.rank < root.right.rank) {
                root = rotateRight(root);
                root.right = sinkNode(root.right);
            } else {
                root = rotateLeft(root);
                root.left = sinkNode(root.left);
            }

            root.count = countChildren(root);
            root.level = countLevel(root);
            return root;
        }

        return hasLeftChild ? root.left : root.right;
    }

    private Node<Item> rotateLeft(Node<Item> node) {
        Node<Item> root = node.right;
        node.right = root.left;
        root.left = node;
        return root;
    }

    private Node<Item> rotateRight(Node<Item> node) {
        Node<Item> root = node.left;
        node.left = root.right;
        root.right = node;
        return root;
    }

    private int countChildren(Node<Item> root) {
        int left = (root.left == null) ? 0 : root.left.count;
        int right = (root.right == null) ? 0 : root.right.count;
        return 1 + left + right;
    }

    private int countLevel(Node<Item> root) {
        int left = (root.left == null) ? 0 : root.left.level;
        int right = (root.right == null) ? 0 : root.right.level;
        return 1 + Math.max(left, right);
    }

    private boolean checkBinarySearchTreeRule(Node<Item> root) {
        if (root == null) {
            return true;
        }

        boolean leftOk = root.left == null ||
                (root.left.value.compareTo(root.value) <= 0 && checkBinarySearchTreeRule(root.left));

        boolean rightOk = root.right == null ||
                (root.right.value.compareTo(root.value) > 0 && checkBinarySearchTreeRule(root.right));

        return leftOk && rightOk;
    }

    private static class Node<Item extends Comparable<Item>> {

        int rank;
        int count;
        int level;
        Item value;
        Node<Item> left;
        Node<Item> right;

        public Node(int rank, Item value) {
            this.rank = rank;
            this.count = 1;
            this.level = 1;
            this.value = value;
        }
    }

    public static void main(String... args) {
        Treap<Integer> treap = new Treap<>();

        System.out.println("Adding....\n");

        for (int i = 0; i < 4096; i++) {
            treap.add(i);
            if (!treap.isBinarySearchTree()) {
                throw new IllegalStateException("treap violate binary search tree rule");
            }
        }
        printTreap(treap);

        System.out.println("\nDeleting....\n");

        for (int i = 0; i < 4096; i++) {
            treap.remove(i);
            if (!treap.isBinarySearchTree()) {
                throw new IllegalStateException("treap violate binary search tree rule");
            }
        }
        printTreap(treap);
    }

    private static void printTreap(Treap<?> treap) {
        System.out.println("size         : " + treap.size());
        System.out.println("depth        : " + treap.depth() + " (optimal is " + (int) (Math.log(treap.size()) + 1.5) + ")");
        System.out.println("balance      : " + treap.balance());
        // System.out.println("internal view: " + treap);
        System.out.println();
    }
}
