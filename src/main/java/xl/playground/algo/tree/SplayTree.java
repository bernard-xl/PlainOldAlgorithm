package xl.playground.algo.tree;

/**
 * Created by xl on 12/23/16.
 */
public class SplayTree<Key extends Comparable<Key>, Value> {

    private Node<Key, Value> root;

    public void add(Key key, Value value) {
        root = addNode(root, key, value);
    }

    public Value get(Key key) {
        root = splayNode(root, key);
        if (root == null || root.key.compareTo(key) != 0) {
            return null;
        }
        return root.value;
    }

    private Node<Key, Value> addNode(Node<Key, Value> root, Key key, Value value) {
        if (root == null) {
            return new Node<>(key, value);
        }

        if (key.compareTo(root.key) <= 0) {
            root.left = addNode(root.left, key, value);
        } else {
            root.right = addNode(root.right, key, value);
        }

        return root;
    }

    private Node<Key, Value> splayNode(Node<Key, Value> root, Key key) {
        if (root == null) {
            return null;
        }

        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            return root;
        } else if (cmp < 0) {
            if (root.left == null) {
                return root;
            }
            int leftCmp = key.compareTo(root.left.key);
            if (leftCmp < 0) {
                root.left.left = splayNode(root.left.left, key);
                root = rotateRight(root);
            } else if (leftCmp > 0) {
                root.left.right = splayNode(root.left.right, key);
                root = rotateLeft(root);
            }
            return (root.left == null) ? root : rotateRight(root);
        } else {
            if (root.right == null) {
                return root;
            }
            int rightCmp = key.compareTo(root.right.key);
            if (rightCmp < 0) {
                root.right.right = splayNode(root.right.right, key);
                root = rotateLeft(root);
            } else if (rightCmp > 0) {
                root.right.left = splayNode(root.right.left, key);
                root = rotateRight(root);
            }
            return (root.right == null) ? root : rotateLeft(root);
        }
    }

    private Node<Key, Value> rotateLeft(Node<Key, Value> node) {
        Node<Key, Value> root = node.right;
        node.right = root.left;
        root.left = node;
        return root;
    }

    private Node<Key, Value> rotateRight(Node<Key, Value> node) {
        Node<Key, Value> root = node.left;
        node.left = root.right;
        root.right = node;
        return root;
    }

    private static class Node<Key extends Comparable<Key>, Value> {

        Node<Key, Value> left;
        Node<Key, Value> right;
        Key key;
        Value value;

        public Node(Key key, Value value) {
            this.key = key;
        }
    }
}
