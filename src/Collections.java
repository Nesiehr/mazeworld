import java.util.*;

/* Represents a Node or Sentinel
 * FIELDS: 
 *  next = ANode<T>
 *  prev = ANode<T>
 * METHODS:
 *  size(int senCount)
 *  find(IPred<T> pred, int senCount)
 *  removeNode(ANode<T> node, int senCount)
 *  sameNodeData(Node<T> node)
 *  removeOperations()
 * METHODS ON FIELDS:
 *  next and prev have all the same methods as this
 */
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  ANode(ANode<T> next, ANode<T> prev) {
    this.next = next;
    this.prev = prev;
  }

  // counts the number of nodes in a deque
  public abstract int size(int senCount);

  // returns the first Node that fulfills the given pred
  public abstract ANode<T> find(IPred<T> pred, int senCount);

  // EFFECT: removes the given ANode
  // if the given node is a Sentinel, do nothing
  public abstract void removeNode(ANode<T> node, int senCount);

  // does this node have the same data as the given node?
  public abstract boolean sameNodeData(Element<T> node);

  // abstracted operations for remove functions
  public void removeOperations() {
    this.prev.next = this.next;
    this.next.prev = this.prev;
  }
}

/* Represents a Node
 * FIELDS: Same as ANode
 *  data - T
 * METHODS: Same as ANode
 */
class Element<T> extends ANode<T> {
  T data;

  Element(T data) {
    super(null, null);
    this.data = data;
  }

  //EFFECT: updates the given nodes to refer back to this node
  Element(T data, ANode<T> next, ANode<T> prev) {
    super(next, prev);
    this.data = data;

    if (next == null) {
      new IllegalArgumentException("node cannot be null");
    } else {
      next.prev = this;
    }
    if (prev == null) {
      new IllegalArgumentException("node cannot be null");
    } else {
      prev.next = this;
    }
  }

  // keeps count of the number of nodes in the list
  public int size(int senCount) {
    return 1 + this.next.size(senCount);
  }

  // produces the first node for which the given predicate returns true.
  public ANode<T> find(IPred<T> pred, int senCount) {
    if (pred.apply(this.data)) {
      return this;
    } else {
      return this.next.find(pred, senCount);
    }
  }

  // EFFECT: removes the given node from this
  // if the given node is the header, do nothing
  public void removeNode(ANode<T> node, int senCount) {
    if (node.sameNodeData(this)) {
      this.removeOperations();
    }
    this.next.removeNode(node, senCount);
  } 

  // does this node have the same data as the given node?
  public boolean sameNodeData(Element<T> node) {
    return this.data.equals(node.data);
  }
}

/* Represents a Sentinel
 * FIELDS: Same as ANode
 * METHODS: Same as ANode
 */
class Sentinel<T> extends ANode<T> {
  Sentinel() { 
    super(null, null);
    this.next = this;
    this.prev = this;
  }

  // counts the number of nodes until it reaches another Sentinel
  public int size(int senCount) {
    if (senCount > 0) {
      return 0;
    }
    return this.next.size(senCount + 1);
  }

  // produces the first node following this for which the given predicate returns true.
  // if no following nodes fulfill the predicate, the method returns this
  public ANode<T> find(IPred<T> pred, int senCount) {
    if (senCount > 0) {
      return this;
    } else {
      return this.next.find(pred, senCount + 1);
    }
  }

  // EFFECT: removes the given node
  // if the given node is the header, do nothing
  public void removeNode(ANode<T> node, int senCount) {
    if (senCount > 0) {
      return;
    } else {
      this.next.removeNode(node, senCount + 1);
    }
  }

  // does this node have the same data as the given node?
  public boolean sameNodeData(Element<T> node) {
    return false;
  }
}

/* Represents a Deque
 * FIELDS:
 *  header - Sentinel<T>
 * METHODS:
 *  size
 *  addAtHead(T val)
 *  addAtTail(T val)
 *  removeFromHead()
 *  removeFromHead()
 *  handleRemove(ANode<T> node)
 *  removeNode(ANode<T> node)
 *  find(IPred<T> pred)
 * METHODS ON FIELDS:
 *  header - has all methods of Sentinel
 */
class Deque<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  boolean isEmpty() {
    return this.header.next.size(0) == 0;
  }

  // the number of nodes in this deque
  int size() {
    return this.header.size(0);
  }

  // adds the given value to the front of this list
  void addAtHead(T val) {
    Element<T> item = new Element<T>(val);
    item.next = this.header.next;
    item.prev = this.header;
    this.header.next.prev = item;
    this.header.next = item;
  }

  // adds the given value to the end of this list
  void addAtTail(T val) {
    Element<T> item = new Element<T>(val);
    item.prev = this.header.prev;
    item.next = this.header;
    this.header.prev.next = item;
    this.header.prev = item;
  }

  // EFFECT: removes the first ANode of this list
  // returns the removed ANode
  T removeFromHead() {
    if (this.size() > 0) {
      Element<T> item = (Element<T>) this.header.next;
      this.handleRemove(item);
      return item.data;
    } else {
      throw new RuntimeException("Goddammit Rheisen.");
    }
  }

  // EFFECT: removes the last ANode of this list
  // returns the removed ANode
  ANode<T> removeFromTail() {
    ANode<T> item = this.header.prev;
    this.handleRemove(item);
    return item;
  }

  // EFFECT: removes the given node from the list
  // throws a runtime exception if the list is empty.
  void handleRemove(ANode<T> node) {
    if (node.equals(this.header)) {
      new RuntimeException("Cannot call this method on an empty list");
    } else {
      node.removeOperations();
    }
  }

  // EFFECT: removes the given node from this
  // if the given node is the header, do nothing
  void removeNode(ANode<T> node) {
    this.header.removeNode(node, 0);
  }

  // produces the first node in this Deque for which the given predicate returns true.
  // if no nodes fulfill the predicate, this method returns the header of this deque.
  ANode<T> find(IPred<T> pred) {
    return this.header.find(pred, 0);
  }
}

// Represents a boolean-valued question over values of type T
interface IPred<T> {
  boolean apply(T t);
}

// Represents a mutable collection of items
interface ICollection<T> { 
  // Is this collection empty?
  boolean isEmpty();
  
  // EFFECT: adds the item to the collection
  void add(T item);
  
  // Returns the first item of the collection
  // EFFECT: removes that first item
  T remove();
}

class Stack<T> implements ICollection<T> {
  
  Deque<T> contents;
  
  Stack() {
    this.contents = new Deque<T>();
  }
  
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }
  
  public T remove() {
    return this.contents.removeFromHead();
  }
  
  public void add(T item) {
    this.contents.addAtHead(item);
  }
}

class Queue<T> implements ICollection<T> {
  
  Deque<T> contents;
  
  Queue() {
    this.contents = new Deque<T>();
  }
  
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }
  
  public T remove() {
    return this.contents.removeFromHead();
  }
  
  public void add(T item) {
    this.contents.addAtTail(item); // NOTE: Different from Stack!
  }
}