import java.util.concurrent.locks.ReentrantReadWriteLock;

class AVLNode {
    int key, height;
    AVLNode left, right;
    ReentrantReadWriteLock lock;

    AVLNode(int d) {
        key = d;
        height = 1;
        lock = new ReentrantReadWriteLock();
    }
}

public class ConcurrentAVLTree {
    private AVLNode root;

    public ConcurrentAVLTree() {
        root = null;
    }

    private int height(AVLNode N) {
        if (N == null)
            return 0;
        return N.height;
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        return y;
    }

    private int getBalance(AVLNode N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    public boolean add(int key) {
        if (contains(key)) return false;

        root = add(root, key);
        return true;
    }

    private AVLNode add(AVLNode node, int key) {
        if (node == null)
            return new AVLNode(key);

        if (key < node.key)
            node.left = add(node.left, key);
        else if (key > node.key)
            node.right = add(node.right, key);

        node.height = 1 + max(height(node.left), height(node.right));

        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public boolean remove(int key) {
        if (!contains(key)) return false;

        root = remove(root, key);
        return true;
    }

    private AVLNode remove(AVLNode node, int key) {
        if (node == null) return null;

        if (key < node.key)
            node.left = remove(node.left, key);
        else if (key > node.key)
            node.right = remove(node.right, key);
        else {
            if (node.left == null || node.right == null) {
                AVLNode temp = null;
                if (temp == node.left)
                    temp = node.right;
                else
                    temp = node.left;

                if (temp == null) {
                    temp = node;
                    node = null;
                } else
                    node = temp;
            } else {
                AVLNode temp = minValueNode(node.right);
                node.key = temp.key;
                node.right = remove(node.right, temp.key);
            }
        }

        if (node == null)
            return null;

        node.height = 1 + max(height(node.left), height(node.right));

        int balance = getBalance(node);

        // Perform rotations if necessary to balance the tree
        // (same code as in add method)

        return node;
    }

    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    public boolean contains(int key) {
        return contains(root, key);
    }

    private boolean contains(AVLNode node, int key) {
        if (node == null) return false;

        if (key == node.key) return true;

        if (key < node.key)
            return contains(node.left, key);
        else
            return contains(node.right, key);
    }

    public void inorder() {
        inorder(root);
    }

    private void inorder(AVLNode node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.key + " ");
            inorder(node.right);
        }
    }

    public static void main(String[] args) {
        ConcurrentAVLTree tree = new ConcurrentAVLTree();

        // Inserting nodes
        tree.add(10);
        tree.add(20);
        tree.add(30);
        tree.add(40);
        tree.add(50);
        tree.add(25);

        // Printing inorder traversal of the AVL tree
        System.out.println("Inorder traversal of constructed AVL tree is :");
        tree.inorder();
    }
}
