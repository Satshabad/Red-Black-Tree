package edu.csupomona.cs.cs241.proj4;

import java.util.ArrayList;
import java.util.Stack;

public class RedBlackTree<K extends Comparable<K>, V> {

    private final boolean RED = true;
    private final boolean BLACK = false;

    private Node<K, V> root;

    public RedBlackTree() {
        root = null;
    }

    public boolean add(K key, V value) {

        if (key == null || value == null) {
            return false;
        }

        if (root == null) {
            root = new Node<K, V>(BLACK, new Node<K, V>(BLACK, null, null,
                    null, null, null), new Node<K, V>(BLACK, null, null, null,
                    null, null), null, value, key);
            root.getLeftChild().setParent(root);
            root.getRightChild().setParent(root);
            return true;
        }

        Node<K, V> placeToAdd = traverseForAdd(key, root);
        System.out.println("place to add: " +placeToAdd.getKey());
        assert (placeToAdd != null);

        if (placeToAdd.getKey().compareTo(key) < 0) {
            System.out.println("add to right");
            placeToAdd.getRightChild().setParent(null);
            placeToAdd.setRightChild(new Node<K, V>(RED, new Node<K, V>(BLACK,
                    null, null, null, null, null), new Node<K, V>(BLACK, null,
                    null, null, null, null), placeToAdd, value, key));
            placeToAdd.getRightChild().getRightChild()
                    .setParent(placeToAdd.getRightChild());
            placeToAdd.getRightChild().getLeftChild()
                    .setParent(placeToAdd.getRightChild());
            printTree();
            System.out.println("%%%%");
             addBalance(placeToAdd.getRightChild());
        } else {
            System.out.println("add to left");
            placeToAdd.getLeftChild().setParent(null);
            placeToAdd.setLeftChild(new Node<K, V>(RED, new Node<K, V>(BLACK,
                    null, null, null, null, null), new Node<K, V>(BLACK, null,
                    null, null, null, null), placeToAdd, value, key));
            placeToAdd.getLeftChild().getRightChild()
                    .setParent(placeToAdd.getLeftChild());
            placeToAdd.getLeftChild().getLeftChild()
                    .setParent(placeToAdd.getLeftChild());
            printTree();
            System.out.println("%%%%");
             addBalance(placeToAdd.getLeftChild());
        }

        return false;
    }

    public V delete(K key) {
        return null;
    }

    public V lookup(K key) {
        return null;

    }

    public void printTree(){
        internalInOrderPrint(root, 0);
    }
    
    private void addBalance(Node<K, V> currentNode) {

        if (currentNode == root) {
            currentNode.setColor(BLACK);
            System.out.println("case 1");
            printTree();
            System.out.println("%%%%");
            return;
        }

        Node<K, V> parent = currentNode.getParent();
        if (!parent.isRed()) {
            System.out.println("case 2");
            return;
        }
        // The parent should always be red here
        assert (parent.isRed());

        Node<K, V> grandparent = parent.getParent();
        Node<K, V> uncle = getUncle(currentNode);
        // if the uncle and parent are red, re-color both to black and re-color
        // gramp to red and call on gramp.
        if (uncle.isRed()) {
            System.out.println("case 3");
            uncle.setColor(BLACK);
            parent.setColor(BLACK);
            grandparent.setColor(RED);
            printTree();
            System.out.println("%%%%");
            addBalance(currentNode.getParent().getParent());

        } else if (!uncle.isRed()) {

            if (parent.getRightChild() == currentNode
                    && grandparent.getLeftChild() == parent) {
                System.out.println("case 4a");
                rotateLeft(parent);
                currentNode = currentNode.getLeftChild();
                parent = currentNode.getParent();
                grandparent = parent.getParent();
                printTree();
                System.out.println("%%%%");

            } else if (parent.getLeftChild() == currentNode
                    && grandparent.getRightChild() == parent) {
                System.out.println("case 4b");
                rotateRight(parent);
                currentNode = currentNode.getRightChild();
                parent = currentNode.getParent();
                grandparent = parent.getParent();
                printTree();
                System.out.println("%%%%");
            }

            if (parent.getLeftChild() == currentNode
                    && grandparent.getLeftChild() == parent) {
                System.out.println("case 5a");
                rotateRight(grandparent);
                grandparent.setColor(RED);
                parent.setColor(BLACK);
                
                
            } else if (parent.getRightChild() == currentNode
                    && grandparent.getRightChild() == parent) {
                System.out.println("case 5b");
                rotateLeft(grandparent);
                grandparent.setColor(RED);
                parent.setColor(BLACK);
                
            }

        }

    }

    private void rotateRight(Node<K, V> node) {
        
        if (node != root) {
            if (node.getParent().getLeftChild() == node) {
                node.getLeftChild().setParent(node.getParent());
                node.getParent().setLeftChild(node.getLeftChild());

                Node<K, V> nodesOldLeftChild = node.getLeftChild();
                node.setLeftChild(node.getLeftChild().getRightChild());
                node.getLeftChild().setParent(node);
                nodesOldLeftChild.setRightChild(node);
                node.setParent(nodesOldLeftChild);

            } else if (node.getParent().getRightChild() == node) {
                node.getLeftChild().setParent(node.getParent());
                node.getParent().setRightChild(node.getLeftChild());

                Node<K, V> nodesOldLeftChild = node.getLeftChild();
                node.setLeftChild(node.getLeftChild().getRightChild());
                node.getLeftChild().setParent(node);
                nodesOldLeftChild.setRightChild(node);
                node.setParent(nodesOldLeftChild);

            }
        }
        else{
            node.getLeftChild().setParent(null);
            root = node.getLeftChild();
            
            node.setLeftChild(node.getLeftChild().getRightChild());
            node.getLeftChild().setParent(node);
            root.setRightChild(node);
            node.setParent(root);
            
            
        }

    }

    private void rotateLeft(Node<K, V> node) {

        if (node != root) {
            if (node.getParent().getLeftChild() == node) {
                node.getParent().setLeftChild(node.getRightChild());
                node.getRightChild().setParent(node.getParent());

                Node<K, V> nodesOldRightChild = node.getRightChild();
                node.setRightChild(node.getRightChild().getLeftChild());
                node.getRightChild().setParent(node);
                nodesOldRightChild.setLeftChild(node);
                node.setParent(nodesOldRightChild);

            } else if (node.getParent().getRightChild() == node) {
                node.getParent().setRightChild(node.getRightChild());
                node.getRightChild().setParent(node.getParent());

                Node<K, V> nodesOldRightChild = node.getRightChild();
                node.setRightChild(node.getRightChild().getLeftChild());
                node.getRightChild().setParent(node);
                nodesOldRightChild.setLeftChild(node);
                node.setParent(nodesOldRightChild);
            }
        }else{
            node.getRightChild().setParent(null);
            root = node.getRightChild();
            
            node.setRightChild(node.getRightChild().getLeftChild());
            node.getRightChild().setParent(node);
            root.setLeftChild(node);
            node.setParent(root);
        }

    }

    private Node<K, V> getUncle(Node<K, V> currentNode) {
        // this method should never be called on a node without a parent. i. e.
        // the root
        assert (currentNode.getParent() != null);

        Node<K, V> parent = currentNode.getParent();

        // just making sure that the root is black
        if (parent.getParent() == null) {
            assert (!parent.isRed());
        }

        if (parent.getParent().getLeftChild() == parent) {
            return parent.getParent().getRightChild();
        } else if (parent.getParent().getRightChild() == parent) {
            return parent.getParent().getLeftChild();
        }

        // we must have returned by now.
        assert (false);
        return null;

    }

    private Node<K, V> traverseForAdd(K key, Node<K, V> startNode) {
        /* There are four cases of adding, I enumerated all of them one by one. */
        Node<K, V> left = startNode.getLeftChild();
        Node<K, V> right = startNode.getRightChild();

        // if both leave are null this is the place to add
        if (left.getKey() == null && right.getKey() == null) {
            return startNode;
        }

        // if the left leaf is null we may add here or keep going on the right
        // path
        if (left.getKey() == null && right.getKey() != null) {

            if (startNode.getKey().compareTo(key) > -1) {
                return startNode;
            } else {
                return traverseForAdd(key, right);
            }

        }

        // if the right leaf is null we may add here or keep going on the left
        // path
        if (right.getKey() == null && left.getKey() != null) {

            if (startNode.getKey().compareTo(key) < 0) {
                return startNode;
            } else {
                return traverseForAdd(key, left);
            }

        }

        // if neither leaves are null then we check to see which way we should
        // go.
        if (left.getKey() != null && right.getKey() != null) {

            if (startNode.getKey().compareTo(key) < 0) {
                return traverseForAdd(key, right);
            } else {
                return traverseForAdd(key, left);
            }

        }

        // this should never happen.
        assert (false);
        return null;

    }

    
private void internalInOrderPrint(Node<K, V> start, int depth) {
               
            if (start == null){ return;}
            internalInOrderPrint(start.getRightChild(), depth +1);
            for (int i = 0; i < depth; i++) {
              System.out.print(" ");
            }
            if (start.isRed()) {
                System.out.println(start.getKey()+ " red");
            }else{
                System.out.println(start.getKey());
            }
            internalInOrderPrint(start.getLeftChild(), depth +1);

    }
}
