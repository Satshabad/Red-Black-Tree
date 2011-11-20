package edu.csupomona.cs.cs241.proj4;

public class TestDriver {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

       RedBlackTree<String, String> rbt =  new RedBlackTree<String, String>();
       rbt.add("r", "hellov");
       rbt.printTree();
       System.out.println("-------------------------");
       rbt.add("a", "hiS");
       rbt.printTree();
       System.out.println("-------------------------");
       rbt.add("d", "he");
       rbt.printTree();
       System.out.println("-------------------------");
       rbt.add("t", "hiS");
       rbt.printTree();
       System.out.println("-------------------------");
       rbt.add("x", "he");
       rbt.printTree();
       System.out.println("-------------------------");
       rbt.add("w", "hiS");
       rbt.printTree();
       System.out.println("-------------------------");
       rbt.add("f", "he");
       rbt.printTree();
       System.out.println("-------------------------");
       rbt.add("q", "hiS");
       rbt.printTree();
       System.out.println("-------------------------");
       rbt.add("z", "he");
       rbt.printTree();
       System.out.println("-------------------------");
       rbt.add("e", "hiS");
       rbt.printTree();
       System.out.println("-------------------------");
       rbt.add("n", "he");
       
       rbt.printTree();
       System.out.println("-------------------------");

       

    }  

}
