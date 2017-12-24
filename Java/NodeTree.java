import com.sun.media.jfxmedia.events.NewFrameEvent;

public class Tree {

    public class Node {
        private int val;
        private Node left;
        private Node mid;
        private Node right;

        Node(int val) {
            this.val = val;
        }
    }

    private Node root;

    Tree() {
        this.root = null;
    }

    /* 
    * Inserts val into the tree: Method calls recursive implementation of method insertInto
    */
    public void insert(int val) {
        if (this.root == null) {
            this.root = new Node(val);
        } else {
            if (this.root.val > val) {
                this.root.left = this.insertInto(this.root.left, val);
            } else if (this.root.val < val) {
                this.root.right = this.insertInto(this.root.right, val);
            } else if (this.root.val == val) {
                this.root.mid = this.insertInto(this.root.mid, val);
            }
        }
    }

    /**
    Recurive method that initialised node when it hits a null root;
    Otherwise, builds tree;
    **/

    private Node insertInto(Node root, int val) {
        if (root == null) {
            return root = new Node(val);
        } else {
            if (root.val > val) {
                root.left = this.insertInto(root.left, val);
            } else if (root.val < val) {
                root.right = this.insertInto(root.right, val);
            } else if (root.val == val) {
                root.mid = this.insertInto(root.mid, val);
            }
            return root;
        }
    }

    /* 
        delete method first finds the parent node of the node where the val exisits.
        Then, it calls the rebuildTree method for the various cases that can exist 
     */
    public void delete(int val) {
        Node n = this.findParent(val);
        if (n != null) {
            if (n.val > val) {
                n.left = this.rebuildTree(n.left);
            } else if (n.val < val) {
                n.right = this.rebuildTree(n.right);
            } else if (n.mid != null) {
                while (n.mid.mid != null) {
                    n = n.mid;
                }
                n.mid = null;
            }
            if (n == this.root) {
                this.root = this.rebuildTree(n);
            } else {
                n = this.rebuildTree(n);
            }
        }
    }

    /**
    This method handles cases where either one of the subtree is null or both sub-trees are null or both sub-trees are not null;
    For the last case, we take the min value from the right sub-tree and make it our node and delete the node.
    **/
    private Node rebuildTree(Node root) {
        if (root.left != null && root.right != null) {
            Node current = root.right;
            if (current.left != null) {
                while (current.left.left != null) {
                    current = current.left;
                }
                root.val = current.left.val;
                root.mid = current.left.mid;
                current.left = current.left.right;
            } else {
                root.val = current.val;
                root.mid = current.mid;
                root.right = null;
            }
            return root;
        } else if (root.left == null && root.right != null) {
            return root.right;
        } else if (root.left != null && root.right == null) {
            return root.left;
        } else {
            return null;
        }
    }

    /*
    This method finds the parent node of the node which contains the val being deleted.
    */
    private Node findParent(int val) {
        Node current = this.root;
        if (current.val == val) {
            return current;
        }
        while (current != null) {
            if (current.val > val) {
                if (current.left != null && current.left.mid == null && current.left.val == val) {
                    System.out.println(current.left.val + "Here");
                    return current;
                }
                current = current.left;
            } else if (current.val < val) {
                if (current.right != null && current.right.mid == null && current.right.val == val) {
                    return current;
                }
                current = current.right;
            } else if (current.val == val) {
                if (current.mid.mid == null) {
                    return current;
                } else {
                    current = current.mid;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Tree t = new Tree();
        t.insert(5);
    }
}
