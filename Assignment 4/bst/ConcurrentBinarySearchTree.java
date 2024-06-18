class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    private final Object lock = new Object();

    public TreeNode(int val) {
        this.val = val;
        this.left = null;
        this.right = null;
    }

    public void lock() {
        synchronized (lock) {
            // Acquire lock
        }
    }

    public void unlock() {
        synchronized (lock) {
            // Release lock
        }
    }
}

public class ConcurrentBinarySearchTree {
    private TreeNode root;

    public ConcurrentBinarySearchTree() {
        this.root = null;
    }

    public boolean add(int val) {
        TreeNode newNode = new TreeNode(val);
        if (root == null) {
            root = newNode;
            return true;
        }
        TreeNode current = root;
        TreeNode parent = null;
        while (true) {
            parent = current;
            if (val < current.val) {
                current.lock();
                if (current.left == null) {
                    current.left = newNode;
                    current.unlock();
                    return true;
                }
                current.unlock();
                current = current.left;
            } else if (val > current.val) {
                current.lock();
                if (current.right == null) {
                    current.right = newNode;
                    current.unlock();
                    return true;
                }
                current.unlock();
                current = current.right;
            } else {
                // Value already exists
                return false;
            }
        }
    }

    public boolean remove(int key) {
        TreeNode parent = null;
        TreeNode current = root;
        boolean isLeftChild = false;

        while (current != null && current.val != key) {
            parent = current;
            if (key < current.val) {
                current.lock();
                current = current.left;
                isLeftChild = true;
            } else {
                current.lock();
                current = current.right;
                isLeftChild = false;
            }
            parent.unlock(); // Release lock of parent after moving to the next node
        }

        if (current == null) {
            return false;
        }

        // Critical section for deleting the node
        current.lock();
        if (current.left == null && current.right == null) {
            if (current == root) {
                root = null;
            } else if (isLeftChild) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        } else if (current.right == null) {
            if (current == root) {
                root = current.left;
            } else if (isLeftChild) {
                parent.left = current.left;
            } else {
                parent.right = current.left;
            }
        } else if (current.left == null) {
            if (current == root) {
                root = current.right;
            } else if (isLeftChild) {
                parent.left = current.right;
            } else {
                parent.right = current.right;
            }
        } else {
            TreeNode successor = getSuccessor(current);
            if (current == root) {
                root = successor;
            } else if (isLeftChild) {
                parent.left = successor;
            } else {
                parent.right = successor;
            }
            successor.left = current.left;
        }
        current.unlock();
        return true;
    }

    public boolean contains(int key) {
        TreeNode current = root;
        while (current != null) {
            current.lock();
            if (key == current.val) {
                current.unlock();
                return true;
            } else if (key < current.val) {
                TreeNode leftChild = current.left;
                current.unlock();
                if (leftChild == null)
                    return false;
                current = leftChild;
            } else {
                TreeNode rightChild = current.right;
                current.unlock();
                if (rightChild == null)
                    return false;
                current = rightChild;
            }
        }
        return false;
    }
    

    private TreeNode getSuccessor(TreeNode deleteNode) {
        TreeNode successor = null;
        TreeNode successorParent = null;
        TreeNode current = deleteNode.right;

        while (current != null) {
            successorParent = successor;
            successor = current;
            current = current.left;
        }

        if (successor != deleteNode.right) {
            successorParent.left = successor.right;
            successor.right = deleteNode.right;
        }

        return successor;
    }

    public void inorderTraversal(TreeNode node) {
        if (node != null) {
            node.lock();
            inorderTraversal(node.left);
            System.out.print(node.val + " ");
            inorderTraversal(node.right);
            node.unlock();
        }
    }

    public static void main(String[] args) {
        ConcurrentBinarySearchTree bst = new ConcurrentBinarySearchTree();
        bst.add(50);
        bst.add(30);
        bst.add(70);
        bst.add(20);
        bst.add(40);
        bst.add(60);
        bst.add(80);

        System.out.println("Inorder traversal before deletion:");
        bst.inorderTraversal(bst.root);
        System.out.println();

        bst.remove(30);

        System.out.println("Inorder traversal after deletion:");
        bst.inorderTraversal(bst.root);
        System.out.println();
    }
}
