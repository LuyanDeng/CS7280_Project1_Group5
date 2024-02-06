/*
 * CS7280 Special Topics in Database Management
 * Project 1: B-tree implementation.
 *
 * You need to code for the following functions in this program
 *   1. Lookup(int value) -> nodeLookup(int value, int node)
 *   2. Insert(int value) -> nodeInsert(int value, int node)
 *   3. Display(int node)
 *
 */

final class Btree {

  /* Size of Node. */
  private static final int NODESIZE = 5;

  /* Node array, initialized with length = 1. i.e. root node */
  private Node[] nodes = new Node[1];

  /* Number of currently used nodes. */
  private int cntNodes;

  /* Pointer to the root node. */
  private int root;

  /* Number of currently used values. */
  private int cntValues;

  /*
   * B+ tree Constructor.
   */
  public Btree() {
    root = initNode();
    nodes[root].children[0] = createLeaf();
  }

  /*********** B tree functions for Public ******************/

  /*
   * Lookup(int value)
   *   - True if the value was found.
   */
  public boolean Lookup(int value) {
    return nodeLookup(value, root);
  }
  /* during the insertion, when the algorithm searches for the key point to insert the value,
it will return -2 if the value already exists

if the node is full, split the node and insert the value into the parent node
so when we insert the node, the parent node won't be full and split again
if the node is root, create a new root and split the old root
*/
  public void split(int pointer) {


  }
  /*
   * Insert(int value)
   *    - If -1 is returned, the value is inserted and increase cntValues.
   *    - If -2 is returned, the value already exists.
   */
  public void Insert(int value) {
    if(nodeInsert(value, root) == -1) cntValues++;
    // check if the root is full
    if(nodes[root].size == NODESIZE) {
      // if the root is full, create a new root and split the old root
      int newRoot = initNode();
      nodes[newRoot].children[0] = root;
      split(newRoot);
      root = newRoot;
    }
    //if not full, insert the value
    nodeInsert(value, root);
  }


  /*
   * CntValues()
   *    - Returns the number of used values.
   */
  public int CntValues() {
    return cntValues;
  }

  /*********** B-tree functions for Internal  ******************/

  /*
   * nodeLookup(int value, int pointer)
   *    - True if the value was found in the specified node.
   *@param value the value to be looked up
   * @param pointer the index of the current node being looked up in the nodes array
   */
  private boolean nodeLookup(int value, int pointer) {
    /*
     use binary search to find the value, if found return true
     */
    int left = 0;
    int right = nodes[pointer].size - 1;
    int mid;
    while(left <= right) {
      mid = left +(right-left) / 2;
      if(nodes[pointer].values[mid]==value)
        return true;
      else if(nodes[pointer].values[mid] > value)
        right = mid - 1;
      else
        left = mid + 1;
    }

    // if not found, check if the node is a leaf node
    if(isLeaf(nodes[pointer]))
      // if the node is a leaf node, return false
      return false;
    // if the node is not a leaf node, use recursion to find the value
    return nodeLookup(value, nodes[pointer].children[left]);
  }

  /*
   * nodeInsert(int value, int pointer)
   *    - -2 if the value already exists in the specified node
   *    - -1 if the value is inserted into the node or
   *            something else if the parent node has to be restructured
   * insert the node into the correct position
   * @param value the value to be inserted
   * @param pointer the pointer to the node?
   */
  private int nodeInsert(int value, int pointer) {




  }


  /*********** Functions for accessing node  ******************/

  /*
   * isLeaf(Node node)
   *    - True if the specified node is a leaf node.
   *         (Leaf node -> a missing children)
   */
  boolean isLeaf(Node node) {
    return node.children == null; // TODO: check the children size is greater than 0
  }

  /*
   * initNode(): Initialize a new node and returns the pointer.
   *    - return node pointer
   */
  int initNode() {
    Node node = new Node();
    node.values = new int[NODESIZE];
    node.children =  new int[NODESIZE + 1];

    checkSize();
    nodes[cntNodes] = node;
    return cntNodes++;
  }

  /*
   * createLeaf(): Creates a new leaf node and returns the pointer.
   *    - return node pointer
   */
  int createLeaf() {
    Node node = new Node();
    node.values = new int[NODESIZE];

    checkSize();
    nodes[cntNodes] = node;
    return cntNodes++;
  }

  /*
   * checkSize(): Resizes the node array if necessary.
   */
  private void checkSize() {
    // if node array is already full, temp will be cntNodes * 2
    if(cntNodes == nodes.length) {
      Node[] tmp = new Node[cntNodes << 1];
      System.arraycopy(nodes, 0, tmp, 0, cntNodes);
      nodes = tmp;
    }
  }
}

/*
 * Node data structure.
 *   - This is the simplest structure for nodes used in B-tree
 *   - This will be used for both internal and leaf nodes.
 */
final class Node {
  /* Node Values (Leaf Values / Key Values for the children nodes).  */
  int[] values;

  /* Node Array, pointing to the children nodes.
   * This array is not initialized for leaf nodes.
   */
  int[] children;

  /* Number of entries
   * (Rule in B Trees:  d <= size <= 2 * d).
   */
  int size;

  // TODO: need a constructor
}