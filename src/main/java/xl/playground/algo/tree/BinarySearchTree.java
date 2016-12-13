package xl.playground.algo.tree;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

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

        if (root.value.compareTo(value) <= 0) {
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
        } else if (cmp < 0) {
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
        while (root.right != null) {
            root = root.right;
        }
        return root;
    }

    private Node<Item> maxNode(Node<Item> root) {
        while (root.left != null) {
            root = root.left;
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
            return (this.root.left == null) ? 0 : this.root.left.count;
        } else if (cmp < 0) {
            return rankNode(root.left, value);
        } else {
            int leftCount = (this.root.left == null) ? 0 : this.root.left.count;
            return leftCount + rankNode(root.right, value);
        }
    }

    private int countChildren(Node<Item> root) {
        int leftCount = (root.left == null) ? 0 : root.left.count;
        int rightCount = (root.right == null) ? 0 : root.right.count;
        return leftCount + rightCount;
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
                stack.add(current);
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
        }
    }
}
