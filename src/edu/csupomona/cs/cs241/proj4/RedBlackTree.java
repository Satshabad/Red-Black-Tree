package edu.csupomona.cs.cs241.proj4;

import java.util.ArrayList;

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

    private int longestString;

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
    public Node<K, V> root;

    /**
     * Initializes a Red Black Tree
     * 
     * @pre true
     * @post a RBT is made
     */
    public RedBlackTree() {
        root = null;
        theNilLeaf = new Node<K, V>(BLACK, null, null, null, null, null);
        longestString = 0;
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

        if (key.toString().length() > longestString) {
            longestString = key.toString().length();
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
        // System.out.println("place to add: " + placeToAdd.getKey());

        // the method traverseToAdd should never return null.
        assert (placeToAdd != null);

        // add as if it were a binary tree, greater than to the right, less or
        // equal to the left. Except now after adding we have to make sure that
        // the tree is still following it's conditions
        if (placeToAdd.getKey().compareTo(key) < 0) {
            // System.out.println("add to right");
            placeToAdd.setRightChild(new Node<K, V>(RED, theNilLeaf,
                    theNilLeaf, placeToAdd, value, key));
            // printTree();
            // System.out.println("%%%%");
            addBalance(placeToAdd.getRightChild());
        } else {
            // System.out.println("add to left");
            placeToAdd.setLeftChild(new Node<K, V>(RED, theNilLeaf, theNilLeaf,
                    placeToAdd, value, key));
            // printTree();
            // System.out.println("%%%%");
            addBalance(placeToAdd.getLeftChild());
        }

        return true;
    }

    public V delete(K key) {

        V returnValue;
        // the tree is empty
        if (root == null) {
            return null;
        }

        // find node to be deleted
        Node<K, V> toBeDeleted = traverseForDelete(key, root);

        // if this is null the node we want to delete doesn't exists
        if (toBeDeleted == null) {
            return null;
        }

        returnValue = toBeDeleted.getValue();
        // if the root is the only node
        if (toBeDeleted == root && root.getRightChild() == theNilLeaf
                && root.getLeftChild() == theNilLeaf) {
            root = null;
            return returnValue;
        }

        // this will tell if using the successor or predecessor
        boolean successor = false;
        Node<K, V> replaceNode;
        if (toBeDeleted.getRightChild() == theNilLeaf
                && toBeDeleted.getLeftChild() == theNilLeaf) {
            replaceNode = toBeDeleted;
        } else if (toBeDeleted.getRightChild() != theNilLeaf) {
            System.out.println("suc");
            successor = true;
            replaceNode = findInOrderSuccessor(toBeDeleted.getRightChild());

        } else {
            System.out.println("pred");
            replaceNode = findInOrderPredecessor(toBeDeleted.getLeftChild());
        }

        System.out.println("in order " + replaceNode.getKey());
        // this should never happen because we know the node to be deleted
        // exists
        assert (replaceNode != null);

        toBeDeleted.setMapping(replaceNode.getKey(), replaceNode.getValue());
        Node<K, V> nodeNeedingBalance;
        if (replaceNode.isRed()) {
            // System.out.println("the node we are removing is red");
            // if the node to be deleted is red than both of it's children
            // must be leaves because at least one of it's children is a leaf
            // otherwise it wouldn't be a successor or predecessor. Both child
            // must leaves or it it would violate the black rule.
            assert (replaceNode.getRightChild() == theNilLeaf && replaceNode
                    .getLeftChild() == theNilLeaf);

            // replace the node to with a nil leaf
            if (replaceNode.getParent().getRightChild() == replaceNode) {
                replaceNode.getParent().setRightChild(theNilLeaf);
            } else {
                replaceNode.getParent().setLeftChild(theNilLeaf);
            }
            return returnValue;

        } else {
            // System.out.println("the node we are removing is black");
            if (successor) {

                // if we have the successor to delete then the left child is
                // definitely going to be a leaf
                assert (replaceNode.getLeftChild() == theNilLeaf);

                // in some cases the in order successor will be a right child
                // and in some it will be the left
                if (replaceNode.getParent().getRightChild() == replaceNode) {
                    replaceNode.getParent().setRightChild(
                            replaceNode.getRightChild());
                } else {
                    replaceNode.getParent().setLeftChild(
                            replaceNode.getRightChild());
                }
                replaceNode.getRightChild().setParent(replaceNode.getParent());
                nodeNeedingBalance = replaceNode.getRightChild();
                replaceNode.setRightChild(null);
                replaceNode.setLeftChild(null);
                replaceNode.setParent(null);

            } else {

                // if we have the predecessor to delete then the right child is
                // definitely going to be a leaf
                assert (replaceNode.getRightChild() == theNilLeaf);

                // in some cases the in order pred will be a right child
                // and in some it will be the left
                if (replaceNode.getParent().getRightChild() == replaceNode) {
                    replaceNode.getParent().setRightChild(
                            replaceNode.getLeftChild());
                } else {
                    replaceNode.getParent().setLeftChild(
                            replaceNode.getLeftChild());
                }
                replaceNode.getLeftChild().setParent(replaceNode.getParent());
                nodeNeedingBalance = replaceNode.getLeftChild();
                replaceNode.setRightChild(null);
                replaceNode.setLeftChild(null);
                replaceNode.setParent(null);
            }

            if (nodeNeedingBalance.isRed()) {
                nodeNeedingBalance.setColor(BLACK);
                System.out
                        .println("case 0: child of replacer was red, replacer was black, repainted child black");
            } else {
                deleteBalance(nodeNeedingBalance);
            }
            System.out.println("BEFORE REBALANCE-------------------------");
            printTree();

        }

        return returnValue;

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

    public void prettyPrint() {
        ArrayList<DepthAndNode<K, V>> queue = new ArrayList<DepthAndNode<K, V>>();
        int depth = 0;
        int treeDepth = getTreeDepth(root);
        int beginningSpaces = longestString + 2;
        for (int i = 0; i < treeDepth - 2; i++) {
            beginningSpaces = (beginningSpaces * 2) + longestString + 2;
            System.out.println(beginningSpaces);
        }

        int inBetweenSpaces = beginningSpaces;
        System.out.println(beginningSpaces);
        queue.add(0, new DepthAndNode<K, V>(depth, root));
        DepthAndNode<K, V> m = null;
        for (int i = 0; i < beginningSpaces; i++) {
            System.out.print(" ");
        }

        while (depth < treeDepth) {

            // System.out.println(queue.toString());
            m = queue.remove(0);
            if (m.getDepth() > depth) {
                depth = m.getDepth();
                if (!(depth < treeDepth)) {
                    break;
                }
                System.out.println();
                inBetweenSpaces = beginningSpaces;
                beginningSpaces = ((beginningSpaces - (longestString + 2)) / 2);
                for (int i = 0; i < beginningSpaces; i++) {
                    System.out.print(" ");
                }

            }

            Node<K, V> right;
            Node<K, V> left;
            if (m.getNode() == null) {
                right = null;
                left = null;

            } else {
                right = m.getNode().getRightChild();
                left = m.getNode().getLeftChild();
            }

            queue.add(queue.size(), new DepthAndNode<K, V>(depth + 1, left));

            queue.add(queue.size(), new DepthAndNode<K, V>(depth + 1, right));

            if (m.getNode() == null) {
                for (int j = 0; j < longestString + 2; j++) {
                    System.out.print(" ");
                }
            } else if (m.getNode() == theNilLeaf) {
                for (int j = 0; j < longestString; j++) {
                    System.out.print("N");
                }
                System.out.print(":B");
            } else {
                System.out.print(m.getNode().getKey());

                for (int j = m.getNode().getKey().toString().length(); j < longestString; j++) {
                    System.out.print("E");
                }

                if (m.getNode().isRed()) {
                    System.out.print(":R");
                } else {
                    System.out.print(":B");
                }
            }

            for (int k = 0; k < inBetweenSpaces; k++) {
                System.out.print(" ");
            }

        }

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
            // System.out.println("case 1");
            return;
        }

        Node<K, V> parent = currentNode.getParent();

        // if the node is not the root then it should never have a null parent.
        assert (parent != null);

        // if parent is black no problem.
        if (!parent.isRed()) {
            // System.out.println("case 2");
            return;
        }
        // The parent should always be red here
        assert (parent.isRed());

        Node<K, V> grandparent = parent.getParent();
        Node<K, V> uncle = getUncle(currentNode);

        // if the uncle and parent are red, re-color both to black and re-color
        // gramp to red and call on gramp.
        if (uncle.isRed()) {
            // System.out.println("case 3");
            uncle.setColor(BLACK);
            parent.setColor(BLACK);
            grandparent.setColor(RED);
            // printTree();
            // System.out.println("%%%%");
            addBalance(currentNode.getParent().getParent());

            // cases 4 and 5 involve rotations.
        } else if (!uncle.isRed()) {

            // case 4a and 4b simply set up for case 5a and 5b.
            if (parent.getRightChild() == currentNode
                    && grandparent.getLeftChild() == parent) {
                // System.out.println("case 4a");

                rotateLeft(parent);

                // reset all the helpful variables
                currentNode = currentNode.getLeftChild();
                parent = currentNode.getParent();
                grandparent = parent.getParent();
                // printTree();
                // System.out.println("%%%%");

            } else if (parent.getLeftChild() == currentNode
                    && grandparent.getRightChild() == parent) {
                // System.out.println("case 4b");

                rotateRight(parent);

                // reset all the helpful variables
                currentNode = currentNode.getRightChild();
                parent = currentNode.getParent();
                grandparent = parent.getParent();
                // printTree();
                // System.out.println("%%%%");
            }

            // if either of these cases occur then we are done
            if (parent.getLeftChild() == currentNode
                    && grandparent.getLeftChild() == parent) {
                // System.out.println("case 5a");
                rotateRight(grandparent);
                grandparent.setColor(RED);
                parent.setColor(BLACK);

            } else if (parent.getRightChild() == currentNode
                    && grandparent.getRightChild() == parent) {
                // System.out.println("case 5b");
                rotateLeft(grandparent);
                grandparent.setColor(RED);
                parent.setColor(BLACK);

            }

        }

    }

    private void deleteBalance(Node<K, V> node) {
        if (node == root) {
            return;
        }

        Node<K, V> parent = node.getParent();
        Node<K, V> sibling = getSibling(node);

        // Case 2 the sibling is red
        if (sibling.isRed()) {
            System.out
                    .println("Case 2: the sibling is RED, set sibling to BLACK "
                            + "and parent to BLACK, rotate left if left child "
                            + "rotate right if right child, n = "
                            + node.getKey());
            printTree();
            System.out
                    .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            sibling.setColor(BLACK);
            parent.setColor(BLACK);

            if (node == parent.getLeftChild()) {
                rotateLeft(parent);
            } else {
                rotateRight(parent);
            }
            sibling = getSibling(node);
            parent = node.getParent();
        }

        // Case 3 4 5 and 6
        // the sibling is black
        if (!sibling.isRed()) {

            // Case 3
            if (!sibling.getRightChild().isRed()
                    && !sibling.getLeftChild().isRed()) {

                if (!parent.isRed()) {
                    System.out
                            .println("Case 3: sibling is BLACK, sibling's chilren are BLACK, "
                                    + "parent is BLACK. set sibling to RED and call again on parent, n = "
                                    + node.getKey());
                    sibling.setColor(RED);
                    printTree();
                    System.out
                            .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    deleteBalance(parent);
                }

                // Case 4
                else {
                    System.out
                            .println("Case 3: sibling is BLACK, sibling's chilren are BLACK, "
                                    + "parent is RED. set sibling to RED set parent to BLACK, n = "
                                    + node.getKey());
                    sibling.setColor(RED);
                    parent.setColor(BLACK);
                    printTree();
                    System.out
                            .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    return;
                }
            }
            // Sibling is still black but it's children aren't both black

            // Case 5

            if (node == parent.getLeftChild()
                    && !sibling.getRightChild().isRed()
                    && sibling.getLeftChild().isRed()) {
                System.out
                        .println("Case 5: sibling is BLACK, sib's right child is BLACK, "
                                + "sib's left child is RED. set the sibling to RED, sib's left child to BLACK. rotate right in sib. n = "
                                + node.getKey());

                sibling.setColor(RED);
                sibling.getLeftChild().setColor(BLACK);
                rotateRight(sibling);
                printTree();
                System.out
                        .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            } else if (node == parent.getRightChild()
                    && !sibling.getLeftChild().isRed()
                    && sibling.getRightChild().isRed()) {

                System.out
                        .println("Case 5: sibling is BLACK, sib's right child is RED, "
                                + "sib's left child is BLACK. set the sibling to RED, sib's right child to BLACK. rotate left on sib. n = "
                                + node.getKey());
                sibling.setColor(RED);
                sibling.getRightChild().setColor(BLACK);
                rotateLeft(sibling);
                printTree();
                System.out
                        .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            }
            sibling = getSibling(node);
            parent = node.getParent();
            // Case 6
            if (sibling.getRightChild().isRed()) {
                sibling.setColor(parent.isRed());
                parent.setColor(BLACK);
                System.out.println("Case 6: n = " + node.getKey());
                if (node == parent.getLeftChild()) {

                    sibling.getRightChild().setColor(BLACK);
                    rotateLeft(parent);
                } else {
                    sibling.getLeftChild().setColor(BLACK);
                    rotateRight(parent);
                }
                printTree();
                System.out
                        .println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            }

        }

    }

    private Node<K, V> getSibling(Node<K, V> node) {
        if (node.getParent().getRightChild() == node) {
            return node.getParent().getLeftChild();
        } else {
            return node.getParent().getRightChild();
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
        if (left == theNilLeaf && right == theNilLeaf) {
            return startNode;
        }

        // if the left leaf is null we may add here or keep going on the right
        // path
        if (left == theNilLeaf && right != theNilLeaf) {

            if (startNode.getKey().compareTo(key) > -1) {
                return startNode;
            } else {
                return traverseForAdd(key, right);
            }

        }

        // if the right leaf is null we may add here or keep going on the left
        // path
        if (right == theNilLeaf && left != theNilLeaf) {

            if (startNode.getKey().compareTo(key) < 0) {
                return startNode;
            } else {
                return traverseForAdd(key, left);
            }

        }

        // if neither leaves are null then we check to see which way we should
        // go.
        if (left != theNilLeaf && right != theNilLeaf) {

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

    private Node<K, V> traverseForDelete(K key, Node<K, V> startNode) {

        if (startNode.getKey().compareTo(key) == 0) {
            return startNode;
        }

        if (startNode.getKey().compareTo(key) < 0
                && startNode.getRightChild() != theNilLeaf) {
            return traverseForDelete(key, startNode.getRightChild());
        }

        if (startNode.getKey().compareTo(key) > -1
                && startNode.getLeftChild() != theNilLeaf) {
            return traverseForDelete(key, startNode.getLeftChild());
        }
        return null;
    }

    private Node<K, V> findInOrderSuccessor(Node<K, V> startNode) {
        if (startNode.getLeftChild() == theNilLeaf) {
            return startNode;
        }

        if (startNode.getLeftChild() != theNilLeaf) {
            return findInOrderSuccessor(startNode.getLeftChild());
        }
        if (startNode.getRightChild() != theNilLeaf) {
            return findInOrderSuccessor(startNode.getRightChild());
        }

        // we should never reach here
        assert (false);
        return null;
    }

    private Node<K, V> findInOrderPredecessor(Node<K, V> startNode) {

        if (startNode.getRightChild() == theNilLeaf) {
            return startNode;
        }

        if (startNode.getRightChild() != theNilLeaf) {
            return findInOrderPredecessor(startNode.getRightChild());
        }

        if (startNode.getLeftChild() != theNilLeaf) {
            return findInOrderPredecessor(startNode.getLeftChild());
        }

        // we should never reach here
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
            System.out.println(start.getKey() + " Red");

        } else {
            System.out.println(start.getKey());

        }
        internalInOrderPrint(start.getLeftChild(), depth + 1);

    }

    public int getTreeDepth(Node<K, V> start) {

        if (start == null) {
            return 0;
        }
        return Math.max(getTreeDepth(start.getRightChild()) + 1,
                getTreeDepth(start.getLeftChild()) + 1);
    }
}
