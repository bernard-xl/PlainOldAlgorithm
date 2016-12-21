package xl.playground.algo.tree;

import java.util.ArrayList;

/**
 * Created by xl on 12/20/16.
 */
public class Trie {

    private static final int ALPHABET_COUNT = 256;

    private Node root;

    public void put(String word) {
        root = internalPut(root, word, 0);
    }

    public void delete(String word) {
        root = internalDelete(root, word, 0);
    }

    public boolean contains(String word) {
        return internalSearch(root, word) != null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public String longestPrefixOf(String word) {
        int prefixLength = internalPrefixLength(root, word);
        return word.substring(0, prefixLength);
    }

    public Iterable<String> wordsWithPrefix(String prefix) {
        Node node = internalSearch(root, prefix);
        ArrayList<String> result = new ArrayList<>();
        internalPrefixCollect(node, prefix, result);
        return result;
    }

    public Iterable<String> wordsThatMatch(String pattern) {
        ArrayList<String> result = new ArrayList<>();
        internalMatchCollect(root, pattern, "", result);
        return result;
    }

    public Iterable<String> words() {
        ArrayList<String> result = new ArrayList<>();
        internalPrefixCollect(root, "", result);
        return result;
    }

    private Node internalPut(Node root, String word, int i) {
        if (root == null) {
            root = new Node();
        }

        if (i == word.length()) {
            root.word = true;
            return root;
        }

        char c = checked(word.charAt(i));
        root.children[c] = internalPut(root.children[c], word, ++i);

        return root;
    }

    private Node internalDelete(Node root, String word, int i) {
        if (root == null) {
            return null;
        }

        if (i == word.length()) {
            root.word = false;
        } else {
            char c = checked(word.charAt(i));
            root.children[c] = internalDelete(root.children[c], word, ++i);
        }

        if (root.word) {
            return root;
        }

        for (int j = 0; j < ALPHABET_COUNT; j++) {
            if (root.children[j] != null) return root;
        }
        return null;
    }

    private Node internalSearch(Node root, String word) {
        if (root == null) {
            return null;
        }

        for (int i = 0; i < word.length(); i++) {
            char c = checked(word.charAt(i));
            if (root.children[c] == null) {
                return null;
            }
            root = root.children[c];
        }

        return root;
    }

    private int internalPrefixLength(Node root, String word) {
        if (root == null) {
            return 0;
        }

        int result = 0;
        for (int i = 0; i < word.length(); i++) {
            char c = checked(word.charAt(i));
            if (root.children[c] == null) break;
            result += 1;
            root = root.children[c];
        }

        return result;
    }

    private void internalPrefixCollect(Node root, String prefix, ArrayList<String> result) {
        if (root == null) {
            return;
        }

        for (char i = 0; i < ALPHABET_COUNT; i++) {
            Node child = root.children[i];
            if (child != null) {
                if (child.word) {
                    result.add(prefix + i);
                }
                internalPrefixCollect(child, prefix + i, result);
            }
        }
    }

    private void internalMatchCollect(Node root, String pattern, String prefix, ArrayList<String> result) {
        if (root == null) {
            return;
        }

        if (prefix.length() == pattern.length()) {
            if (root.word) result.add(prefix);
            return;
        }

        char p = checked(pattern.charAt(prefix.length()));
        if (p == '.') {
            for (char i = 0; i < ALPHABET_COUNT; i++) {
                internalMatchCollect(root.children[i], pattern, prefix + i, result);
            }
        } else {
            internalMatchCollect(root.children[p], pattern, prefix + p, result);
        }
    }

    private char checked(char c) {
        if (c >= ALPHABET_COUNT) {
            throw new IllegalArgumentException("character '" + c + "' is unsupported");
        }
        return c;
    }

    private static class Node {

        boolean word = false;
        Node[] children = new Node[ALPHABET_COUNT];
    }

    public static void main(String... args) {
        Trie trie = new Trie();

        System.out.println("is empty: " + trie.isEmpty());

        System.out.println("inserting words into trie...");
        trie.put("sea");
        trie.put("sells");
        trie.put("shell");
        trie.put("by");
        trie.put("the");
        trie.put("she");

        try {
            trie.put("你好");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("is empty: " + trie.isEmpty());

        System.out.println(trie.longestPrefixOf("themselves"));
        System.out.println(trie.wordsWithPrefix("shel"));
        System.out.println(trie.wordsThatMatch(".he"));
        System.out.println(trie.contains("themselves"));
        System.out.println(trie.contains("by"));
        System.out.println(trie.words());

        System.out.println("deleting words from trie...");
        trie.delete("sea");
        System.out.println(trie.contains("sea"));
        trie.delete("sells");
        System.out.println(trie.contains("sells"));
        trie.delete("shell");
        trie.delete("by");
        trie.delete("the");
        trie.delete("she");

        System.out.println("is empty: " + trie.isEmpty());
    }
}
