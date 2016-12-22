package xl.playground.algo.tree;

import java.util.Random;

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

    public boolean isBalanced() {
        return false;
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

        root.count = countChildren(root);
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
        return left + right;
    }

    private static class Node<Item extends Comparable<Item>> {

        int rank;
        int count;
        Item value;
        Node<Item> left;
        Node<Item> right;

        public Node(int rank, Item value) {
            this.rank = rank;
            this.count = 1;
            this.value = value;
        }
    }
}
