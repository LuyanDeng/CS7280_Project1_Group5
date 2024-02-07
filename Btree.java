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

if the node is full, split the node and insert the value into the parent node
so when we insert the node, the parent node won't be full and split again
if the node is root, create a new root and split the old root
split the node's children into two nodes
@param node the parent node of the node to be split
@param pointer the index of the node to be split in the nodes array
*/
  public void split(Node node, int pointer) {
    //get children of the node
    int[] children = node.children;

    // get full size of child node
    int children_size = children.length;
    //get the left mid of the children
    int mid = children_size / 2;
    // build a new list store the right half of the children
    int[] right_children = new int[children_size - mid];
    //copy the right half of the children to the right list
    System.arraycopy(children, mid, right_children, 0, children_size - mid);






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
      split(nodes[root], newRoot);
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
    //  if the node is a leaf node
    if(isLeaf(nodes[pointer])){
      int left = 0;
      int right = nodes[pointer].size - 1;
      int mid;
        while(left <= right) {
            mid = left + (right - left) / 2;
            if (nodes[pointer].values[mid] == value)
            return true;
            else if (nodes[pointer].values[mid] > value)
            right = mid - 1;
            else
            left = mid + 1;
        }
        return false;
    }
    // if the node is not a leaf node, use recursion to find the value
    int left = 0;
    int right = nodes[pointer].size - 1;
    int mid;
    //Default to the rightmost child if value is greater than all values in the node

    int childrenIndex = nodes[pointer].size;
    while(left <= right) {
      mid = left + (right - left) / 2;
      if (nodes[pointer].values[mid] == value)
        return true;
      else if (nodes[pointer].values[mid] > value) {
        right = mid - 1;
        //update the children index to the left child
        childrenIndex = mid;
      } else {
        left = mid + 1;
      }
    }

    return nodeLookup(value, nodes[pointer].children[childrenIndex]);
  }

  /*
   * nodeInsert(int value, int pointer)
   *    - -2 if the value already exists in the specified node
   *    - -1 if the value is inserted into the node or
   *            something else if the parent node has to be restructured
   * insert the node into the correct position
   * @param value the value to be inserted
   * @param pointer the index of the node in the nodes array
   */

  /*
   fine if the value in the given array, if found return True
   @param arr the array to be searched
   @param value the value to be searched
   */
  private boolean binarySearch(int[] arr, int value,int size) {
    int left = 0;
    int right = size-1;
    int mid;
    while(left <= right) {
      mid = left + (right - left) / 2;
      if(arr[mid] == value)
        return true;
      else if(arr[mid] > value)
        right = mid - 1;
      else
        left = mid + 1;
    }
    return false;
  }
  private int nodeInsert(int value, int pointer) {
    //build a node
    Node node = nodes[pointer];
    // check if the value already exists in the node
    if(nodeLookup(value, pointer))
      return -2;
    // if the node is a leaf node return -1
    if(isLeaf(node)) {
      int index = binarySearch(node.values, value);
      //insert the value into the node
      for(int i = node.size; i > index; i--) {
        node.values[i] = node.values[i - 1];
      }
      node.values[index] = value;
      node.size++;
      return -1;
    }




  }


  /*********** Functions for accessing node  ******************/

  /*
   * isLeaf(Node node)
   *    - True if the specified node is a leaf node.
   *         (Leaf node -> a missing children)
   */
  boolean isLeaf(Node node) {
    return node.children == null || node.children.length == 0; //  check the children size is greater than 0
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

    /* Constructor */
    Node() {
      size = 0;
    }
    //get the value of the node
    public int getValue(int index) {
      return values[index];
    }

    //get the child node of the node
    public int getChild(int index) {
        return children[index];
    }
   //get size of the node
    public int getSize() {
      return size;
    }
    //set the value of the node
    public void setValue(int index, int value) {
      values[index] = value;
    }
    //set the child node of the node
    public void setChild(int index, int child) {
      children[index] = child;
    }
    //set the size of the node
    public void setSize(int size) {
      this.size = size;
    }
}
