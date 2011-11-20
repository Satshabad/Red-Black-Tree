package edu.csupomona.cs.cs241.proj4;

import java.util.ArrayList;
import java.util.Stack;

/**
 * This class is a Red Black Tree.
 * 
 * @author Satshabad
 * 
 * @param <K>
 *            the keys that map to the values
 * @param <V>
 *            the values that the nodes hold.
 */
public class RedBlackTree<K extends Comparable<K>, V> {

    /**
     * The constant boolean for the color red
     */
    private final boolean RED = true;
    /**
     * The constant boolean for the color black
     */
    private final boolean BLACK = false;
    /**
     * The one node that represents all of the nil leaves at once.
     */
    private Node<K, V> theNilLeaf;
    /**
     * A special node called the root
     */
    private Node<K, V> root;

    /**
     * Initializes a Red Black Tree
     * 
     * @pre true
     * @post a RBT is made
     */
    public RedBlackTree() {
        root = null;
        theNilLeaf = new Node<K, V>(BLACK, null, null, null, null, null);
    }

    /**
     * This method adds the mapping to the tree.
     * 
     * @pre true
     * @post the key added matches the value added with it. If there was a
     *       previous mapping of the same key results are not guaranteed
     * 
     * @param key
     *            the key that will map to the value for look up
     * @param value
     *            the object to be stored
     * @return whether the mapping was made or not
     */
    public boolean add(K key, V value) {

        // don't add null mapping, it could interfere with other methods.
        if (key == null || value == null) {
            return false;
        }

        // special case when there is no root.
        if (root == null) {
            root = new Node<K, V>(BLACK, theNilLeaf, theNilLeaf, null, value,
                    key);
            root.getLeftChild().setParent(root);
            root.getRightChild().setParent(root);
            return true;
        }

        // finds the node on which to add the new node
        Node<K, V> placeToAdd = traverseForAdd(key, root);
        System.out.println("place to add: " + placeToAdd.getKey());

        // the method traverseToAdd should never return null.
        assert (placeToAdd != null);

        // add as if it were a binary tree, greater than to the right, less or
        // equal to the left. Except now after adding we have to make sure that
        // the tree is still following it's conditions
        if (placeToAdd.getKey().compareTo(key) < 0) {
            System.out.println("add to right");
            placeToAdd.setRightChild(new Node<K, V>(RED, theNilLeaf,
                    theNilLeaf, placeToAdd, value, key));
            printTree();
            System.out.println("%%%%");
            addBalance(placeToAdd.getRightChild());
        } else {
            System.out.println("add to left");
            placeToAdd.setLeftChild(new Node<K, V>(RED, theNilLeaf, theNilLeaf,
                    placeToAdd, value, key));
            printTree();
            System.out.println("%%%%");
            addBalance(placeToAdd.getLeftChild());
        }

        return true;
    }

    public V delete(K key) {
        return null;
    }

    public V lookup(K key) {
        return null;

    }

    /**
     * This will print the tree sideways.
     * 
     * @pre true
     * @post the tree is printed sideways
     */
    public void printTree() {
        internalInOrderPrint(root, 0);
    }

    /**
     * This internal method will check the 5 cases that might occur when a node
     * is added and it breaks the 4 RBT rules.
     * 
     * @pre the only rules that are broken are from the latest add proc.
     * @post all 4 of the RBT rules are not violated
     * @param currentNode
     *            the node to start cheking on
     */
    private void addBalance(Node<K, V> currentNode) {

        // first case is when the root is not black. so color it black.
        if (currentNode == root) {
            currentNode.setColor(BLACK);
            System.out.println("case 1");
            return;
        }

        Node<K, V> parent = currentNode.getParent();

        // if the node is not the root then it should never have a null parent.
        assert (parent != null);

        // if parent is black no problem.
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

            // cases 4 and 5 involve rotations.
        } else if (!uncle.isRed()) {

            // case 4a and 4b simply set up for case 5a and 5b.
            if (parent.getRightChild() == currentNode
                    && grandparent.getLeftChild() == parent) {
                System.out.println("case 4a");

                rotateLeft(parent);

                // reset all the helpful variables
                currentNode = currentNode.getLeftChild();
                parent = currentNode.getParent();
                grandparent = parent.getParent();
                printTree();
                System.out.println("%%%%");

            } else if (parent.getLeftChild() == currentNode
                    && grandparent.getRightChild() == parent) {
                System.out.println("case 4b");

                rotateRight(parent);

                // reset all the helpful variables
                currentNode = currentNode.getRightChild();
                parent = currentNode.getParent();
                grandparent = parent.getParent();
                printTree();
                System.out.println("%%%%");
            }

            // if either of these cases occur then we are done
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

    /**
     * Performs a right rotation on the node
     * 
     * @pre the node called on must not be a node with less than 2 levels of
     *      children below it.
     * @post a left rotation is performed
     * @param node
     *            the node to rotate on
     */
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

                // after rotation node should be child of old child
                assert (node.getParent() == nodesOldLeftChild);

            } else if (node.getParent().getRightChild() == node) {
                node.getLeftChild().setParent(node.getParent());
                node.getParent().setRightChild(node.getLeftChild());

                Node<K, V> nodesOldLeftChild = node.getLeftChild();
                node.setLeftChild(node.getLeftChild().getRightChild());
                node.getLeftChild().setParent(node);
                nodesOldLeftChild.setRightChild(node);
                node.setParent(nodesOldLeftChild);

                // after rotation node should be child of old child
                assert (node.getParent() == nodesOldLeftChild);

            }
        } else {
            node.getLeftChild().setParent(null);
            root = node.getLeftChild();

            node.setLeftChild(node.getLeftChild().getRightChild());
            node.getLeftChild().setParent(node);
            root.setRightChild(node);
            node.setParent(root);

            // after rotation node should be child of root
            assert (node.getParent() == root);

        }

    }

    /**
     * Performs a left rotation on the node
     * 
     * @pre the node called on must not be a node with less than 2 levels of
     *      children below it.
     * @post a left rotation is performed
     * @param node
     *            the node to rotate on
     */
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

                // after rotation node should be child of old child
                assert (node.getParent() == nodesOldRightChild);

            } else if (node.getParent().getRightChild() == node) {
                node.getParent().setRightChild(node.getRightChild());
                node.getRightChild().setParent(node.getParent());

                Node<K, V> nodesOldRightChild = node.getRightChild();
                node.setRightChild(node.getRightChild().getLeftChild());
                node.getRightChild().setParent(node);
                nodesOldRightChild.setLeftChild(node);
                node.setParent(nodesOldRightChild);

                // after rotation node should be child of old child
                assert (node.getParent() == nodesOldRightChild);
            }
        } else {
            node.getRightChild().setParent(null);
            root = node.getRightChild();

            node.setRightChild(node.getRightChild().getLeftChild());
            node.getRightChild().setParent(node);
            root.setLeftChild(node);
            node.setParent(root);

            // after rotation node should be child of root
            assert (node.getParent() == root);
        }

    }

    /**
     * Finds the uncle of the given node
     * 
     * @pre the passed node is not the root
     * @post the uncle of the node is returned
     * @param currentNode
     *            the node to find the uncle of.
     * @return the uncle of currentNode
     */
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

    /**
     * The method find the node onto which the new node will be added
     * 
     * @pre true
     * @post the node that should be added onto is returned. it is up to caller
     *       to determine which side to add on to.
     * 
     * @param key
     *            the key of the node to be added
     * @param startNode
     *            the node to currently look at for adding
     * @return the node to add onto.
     */
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

    /**
     * This method can print any branch of the tree by doing an in-order
     * traversal.
     * 
     * @pre true
     * @post the branch is printed
     * @param start
     *            the node to start the tree with
     * @param depth
     *            the depth of the starting node
     */
    private void internalInOrderPrint(Node<K, V> start, int depth) {

        if (start == null) {
            return;
        }
        internalInOrderPrint(start.getRightChild(), depth + 1);
        for (int i = 0; i < depth; i++) {
            System.out.print(" ");
        }
        if (start.isRed()) {
            System.out.println(start.getKey() + " red");
        } else {
            System.out.println(start.getKey());
        }
        internalInOrderPrint(start.getLeftChild(), depth + 1);

    }
}
