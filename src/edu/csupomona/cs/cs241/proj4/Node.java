package edu.csupomona.cs.cs241.proj4;

public class Node<K extends Comparable<K>,V> {

    /**
     * The right child of this node
     */
    private Node<K, V> rightChild;
    /**
     * The left child of this node
     */
    private Node<K, V> leftChild;
    /**
     * The parent of this node
     */
    private Node<K, V> parent;
    /**
     * The color value of this node, if true, red, if false, black
     */
    private boolean red;
    /**
     * The value held by this node
     */
    private V value;
    /**
     * The key associated with the value held by this node
     */
    private K key;

    /**
     * Initializes this node with the given attributes
     * 
     * @pre true
     * @post the node is created
     * 
     * 
     * @param color
     *            The color value of this node, if true, red, if false, black
     * @param leftChild
     *            The left child of this node
     * @param rightChild
     *            The right child of this node
     * @param parent
     *            The parent of this node
     * @param value
     *            The value held by this node
     * @param key
     *            The key associated with the value held by this node
     */
    public Node(boolean color, Node<K, V> leftChild, Node<K, V> rightChild,
            Node<K, V> parent, V value, K key) {
        red = color;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.parent = parent;
        this.value = value;
        this.key = key;
    }

    /**
     * Gets The right child of this node
     * 
     * @pre true
     * @post the right child of this node is returned
     * 
     * @return the rightChild
     */
    public Node<K, V> getRightChild() {
        return rightChild;
    }

    /**
     * Sets The right child of this node
     * 
     * @pre true
     * @post the right child of this node is one that was passed in
     * 
     * @param rightChild
     *            the rightChild of this node to set
     */
    public void setRightChild(Node<K, V> rightChild) {
        this.rightChild = rightChild;
    }

    /**
     * Gets The left child of this node
     * 
     * @pre true
     * @post the left child of this node is returned
     * @return the leftChild
     */
    public Node<K, V> getLeftChild() {
        return leftChild;
    }

    /**
     * Sets The left child of this node
     * 
     * @pre true
     * @post the left child of this node is one that was passed in
     * 
     * @param leftChild
     *            the leftChild of this node to set
     */
    public void setLeftChild(Node<K, V> leftChild) {
        this.leftChild = leftChild;
    }

    /**
     * Gets the parent of this node
     * 
     * @pre true
     * @post the parent of this node is returned
     * 
     * @return the parent
     */
    public Node<K, V> getParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node
     * 
     * @pre true
     * @post the parent of this node is now the node that was passed in
     * 
     * @param parent
     *            the parent of this node to set
     */
    public void setParent(Node<K, V> parent) {
        this.parent = parent;
    }

    /**
     * Checks what the current color of this node is, true for red, false for
     * black.
     * 
     * @pre true
     * @post the color is returned
     * 
     * @return the color of the node
     */
    public boolean isRed() {
        return red;
    }

    /**
     * Sets the color of this node, true for red, false for black
     * 
     * @pre true
     * @post The color of this node is set to the boolean specified
     * 
     * @param red
     *            the color (true for red) to set
     */
    public void setColor(boolean red) {
        this.red = red;
    }

    /**
     * Gets the value held by this node
     * 
     * @pre true
     * @post the value is returned
     * 
     * @return the value
     */
    public V getValue() {
        return value;
    }

    /**
     * Gets the key associated with the value of this node
     * 
     * @pre true
     * @post the value returned is the one associated with the key held by this
     *       node
     * 
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * Creates a new mapping from a key to a value in this node
     * 
     * @pre true
     * @post the key passed in is now the one associated with the value passed
     *       in
     * 
     * @param key
     *            the key to set
     * @param value
     *            the value to set
     */
    public void setMapping(K key, V value) {
        this.key = key;
    }

}
