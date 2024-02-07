package Index;

// An abstract class necessary so that InternalNode and LeafNode can be
// passed in as Node objects to the InternalNode constructor
public abstract class Node {
    private int order;

    public Node(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
