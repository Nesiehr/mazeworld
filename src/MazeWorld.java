// Assignment 10
// Dennis, Rheisen
// rheisen
// Lee, Jaedyn
// jaedynlee

import javalib.impworld.*;
import java.util.*;
import java.awt.Color;
import javalib.worldimages.*;

// Represents a comparison of Edges by their weights
class EdgeComparator implements Comparator<Edge> {

  // Compares two edges by their weights
  public int compare(Edge e1, Edge e2) {
    return e1.weight - e2.weight;
  }
}

// Represents the edge connecting two nodes
class Edge {
  int weight;
  Node nodeOne;
  Node nodeTwo;
  boolean isCorridor;

  // Creates a new edge with random weight connecting two nodes
  Edge(Node nodeOne, Node nodeTwo) {
    Random r = new Random();
    this.weight = r.nextInt(1000);
    this.nodeOne = nodeOne;
    this.nodeTwo = nodeTwo;
    this.isCorridor = false;
  }

  // Creates a new edge with the given weight connecting two nodes
  Edge(Node nodeOne, Node nodeTwo, int weight) {
    this.weight = weight;
    this.nodeOne = nodeOne;
    this.nodeTwo = nodeTwo;
    this.isCorridor = false; 
  }

  /* TEMPLATE:
  FIELDS:
    this.weight                     -- int
    this.nodeOne                    -- Node
    this.nodeTwo                    -- Node
    this.isCorridor                 -- boolean
  METHODS:
    this.render(int,int,Color)      -> WorldImage
    this.sameEdge(Edge)             -> boolean
  METHODS ON FIELDS:
   */

  // Renders this edge either white or black depending on if it is a corridor or not
  WorldImage render(int w, int h, Color c) {
    if (this.isCorridor) {
      return new RectangleImage(w, h, OutlineMode.SOLID, 
          c);
    } else {
      return new RectangleImage(w, h, OutlineMode.SOLID, 
          Color.GRAY);
    }
  }

  // Determines if this edge connects the same nodes as the given edge
  boolean sameEdge(Edge edge) {
    return (this.nodeOne.id == edge.nodeOne.id || this.nodeOne.id == edge.nodeTwo.id)
        && (this.nodeTwo.id == edge.nodeOne.id || this.nodeTwo.id == edge.nodeTwo.id);
  }
}

// Represents a node within a maze
class Node {
  int id;
  int x;
  int y;
  int bottomWeight;
  int rightWeight;
  Edge bottom;
  Edge right;
  Edge top;
  Edge left;
  Color color;
  Color prevColor;

  // Creates a new node with edges of random weights at the given x y coordinates
  Node(int id, int x, int y, Color color) {
    this.id = id;
    this.x = x;
    this.y = y;
    Random r = new Random();
    this.bottomWeight = r.nextInt(1000);
    this.rightWeight = r.nextInt(1000);
    this.bottom = new Edge(this, this, bottomWeight);
    this.right = new Edge(this, this, rightWeight);
    this.top = new Edge(this, this);
    this.left = new Edge(this, this);
    this.color = color;
    this.prevColor = color;
  }

  // Creates a new node with edges of given weights at the given x y coordinates
  Node(int id, int x, int y, int bottomWeight, int rightWeight, Color color) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.bottomWeight = bottomWeight;
    this.rightWeight = rightWeight;
    this.bottom = new Edge(this, this, bottomWeight);
    this.right = new Edge(this, this, rightWeight);
    this.top = new Edge(this, this);
    this.left = new Edge(this, this);
    this.color = color;
    this.prevColor = color;
  }

  /* TEMPLATE:
  FIELDS:
    this.id                                                         -- int
    this.x                                                          -- int
    this.y                                                          -- int
    this.bottomWeight                                               -- int
    this.rightWeight                                                -- int
    this.bottom                                                     -- Edge
    this.right                                                      -- Edge
    this.top                                                        -- Edge
    this.left                                                       -- Edge
    this.color                                                      -- Color
  METHODS:
    this.render()                                                   -> WorldImage
    this.linkRight(Node)                                            -> void
    this.linkBottom(Node)                                           -> void
    this.changeColor(Color)                                         -> void
  METHODS ON FIELDS:
   */

  // Renders this node as a rectangle
  WorldImage render() {
    WorldImage node = new RectangleImage(MazeWorld.NODE_SIZE, MazeWorld.NODE_SIZE, 
        OutlineMode.SOLID, this.color);
    node = new BesideAlignImage(AlignModeY.MIDDLE, node, this.right.render(MazeWorld.EDGE_SIZE, 
        MazeWorld.NODE_SIZE, this.color));
    node = new AboveAlignImage(AlignModeX.LEFT, node, this.bottom.render(MazeWorld.NODE_SIZE, 
        MazeWorld.EDGE_SIZE, this.color));
    return node;
  }

  // EFFECT:
  // Modifies this node's right edge to connect with the given node and the given node's
  // left edge to connect with this node
  void linkRight(Node node) {
    this.right = new Edge(this, node, this.rightWeight);
    node.left = new Edge(node, this);
  }

  // EFFECT:
  // Modifies this node's bottom edge to connect with the given node and the given node's
  // top edge to connect with this node
  void linkBottom(Node node) {
    this.bottom = new Edge(this, node, this.bottomWeight);
    node.top = new Edge(node, this);
  }

  // EFFECT:
  // Modifies the previous color to be the current color and modifies the current color to be the
  // given color
  void changeColor(Color newColor) {
    this.prevColor = this.color;
    this.color = newColor;
  }
}

// Represents a randomly generated maze
class MazeWorld extends World {
  // Defines an integer constant that represents the maze width
  static final int MAZE_SIZE_X = 100;
  // Defines an integer constant that represents the maze height
  static final int MAZE_SIZE_Y = 60;
  // Defines an integer constant that represents node size
  static final int NODE_SIZE = 10;
  // Defines an integer constant that represents edge size
  static final int EDGE_SIZE = 1;

  ArrayList<Node> maze;
  ArrayList<Edge> mazeCorridors;
  ICollection<Node> worklist;
  ArrayList<Node> blacklist;
  HashMap<Node,Node> backtrace;
  Node user;
  int depthFirstSteps;
  int breadthFirstSteps;
  int playerSteps;
  boolean depthSearching;
  boolean breadthSearching;
  boolean displayingHeatmap;
  boolean displayingUserPath;
  boolean buildingMaze;

  // Creates a new random maze
  MazeWorld() {
    this.maze = new ArrayList<Node>();
    this.mazeCorridors = new ArrayList<Edge>();
    initMaze(this.maze);
    createMaze(this.maze);
    this.blacklist = new ArrayList<Node>();
    this.backtrace = new HashMap<Node, Node>();
    this.user = this.maze.get(0);
    this.breadthFirstSteps = 0;
    this.depthFirstSteps = 0;
    this.playerSteps = 0;
    this.depthSearching = false;
    this.breadthSearching = false;
    this.displayingHeatmap = false;
    this.displayingUserPath = false;
    this.buildingMaze = true;
  }

  /* TEMPLATE:
  FIELDS:
    this.maze                                                 -- ArrayList<Node>
    this.mazeCorridors                                        -- ArrayList<Edge>
    this.worklist                                             -- ICollection<Node>
    this.blacklist                                            -- ArrayList<Node>
    this.backtrace                                            -- HashMap<Integer, Edge>
    this.user                                                 -- Node
    this.breadthFirstSteps                                    -- int
    this.depthFirstSteps                                      -- int
    this.playerSteps                                          -- int
    this.depthSearching                                       -- boolean
    this.breadthSearching                                     -- boolean
    this.displayingHeatmap                                    -- boolean
    this.buildingMaze                                         -- boolean
  METHODS:
    this.initMaze(ArrayList<Node>)                            -> void
    this.initMazeVertical(ArrayList<Node>)                    -> void
    this.initMazeHorizontal(ArrayList<Node>)                  -> void
    this.createMaze(ArrayList<Node>)                          -> void
    this.hashmapReplaceLink(HashMap<Node,Node>,Node,Node)     -> void
    this.animateMazeCreation()                                -> void
    this.search()                                             -> void
    this.highlight(Node)                                      -> void
    this.showBlacklist()                                      -> void
    this.hideBlacklist()                                      -> void
    this.heatMap()                                            -> void
    this.hideHeatMap()                                        -> void
    this.showHeatMap()                                        -> void
    this.maxSteps(ICollection<Node>)                          -> int
    this.searchSteps(ICollection<Node>)                       -> int
    this.reset()                                              -> void
    this.resetColor()                                         -> void
    this.newMaze()                                            -> void
    this.newVerticalMaze()                                    -> void
    this.newHorizontalMaze()                                  -> void
    this.runSearch(ICollection<Node>)                         -> void
    this.animationsRunning()                                  -> boolean
    this.onKeyEvent(String)                                   -> void
    this.onTick()                                             -> void
    this.makeScene()                                          -> WorldScene
  METHODS ON FIELDS:
   */

  // EFFECT:
  // Adds unlinked maze nodes of random edge weights and links them
  void initMaze(ArrayList<Node> maze) {
    int nodeId = 0;
    int count = 0;

    for (int r = 0; r < MazeWorld.MAZE_SIZE_Y; r += 1) {
      for (int c = 0; c < MazeWorld.MAZE_SIZE_X; c += 1) {
        Color color = Color.LIGHT_GRAY;
        if (r == 0 && c == 0) {
          color = new Color(30,117,62); // dark green
        } else if (r == MazeWorld.MAZE_SIZE_Y - 1 && c == MazeWorld.MAZE_SIZE_X - 1) {
          color = new Color(97,30,117); // purple
        }
        maze.add(new Node(nodeId,c,r,color));
        nodeId += 1;
      }
    }

    for (int r = 0; r < MazeWorld.MAZE_SIZE_Y; r += 1) {
      for (int c = 0; c < MazeWorld.MAZE_SIZE_X; c += 1) {
        if (c < MazeWorld.MAZE_SIZE_X - 1) {
          maze.get(count).linkRight(maze.get(count + 1));
        }
        if (r < MazeWorld.MAZE_SIZE_Y - 1) {
          maze.get(count).linkBottom(maze.get(count + MazeWorld.MAZE_SIZE_X));
        }
        count += 1;
      }
    }
  }  

  // EFFECT:
  // Adds unlinked maze nodes with predominantly vertical heavy edge weights and links them
  void initMazeVertical(ArrayList<Node> maze) {
    int nodeId = 0;
    int count = 0;
    Random random = new Random();

    for (int r = 0; r < MazeWorld.MAZE_SIZE_Y; r += 1) {
      for (int c = 0; c < MazeWorld.MAZE_SIZE_X; c += 1) {
        Color color = Color.LIGHT_GRAY;
        if (r == 0 && c == 0) {
          color = new Color(30,117,62); // dark green
        } else if (r == MazeWorld.MAZE_SIZE_Y - 1 && c == MazeWorld.MAZE_SIZE_X - 1) {
          color = new Color(97,30,117); // purple
        }
        int bottom = random.nextInt(50);
        int right = random.nextInt(1000);
        maze.add(new Node(nodeId,c,r,bottom,right,color));
        nodeId += 1;
      }
    }

    for (int r = 0; r < MazeWorld.MAZE_SIZE_Y; r += 1) {
      for (int c = 0; c < MazeWorld.MAZE_SIZE_X; c += 1) {
        if (c < MazeWorld.MAZE_SIZE_X - 1) {
          maze.get(count).linkRight(maze.get(count + 1));
        }
        if (r < MazeWorld.MAZE_SIZE_Y - 1) {
          maze.get(count).linkBottom(maze.get(count + MazeWorld.MAZE_SIZE_X));
        }
        count += 1;
      }
    }
  }

  // EFFECT:
  // Adds unlinked maze nodes with predominantly horizontal heavy edge weights and links them
  void initMazeHorizontal(ArrayList<Node> maze) {
    int nodeId = 0;
    int count = 0;
    Random random = new Random();

    for (int r = 0; r < MazeWorld.MAZE_SIZE_Y; r += 1) {
      for (int c = 0; c < MazeWorld.MAZE_SIZE_X; c += 1) {
        Color color = Color.LIGHT_GRAY;
        if (r == 0 && c == 0) {
          color = new Color(30,117,62); // dark green
        } else if (r == MazeWorld.MAZE_SIZE_Y - 1 && c == MazeWorld.MAZE_SIZE_X - 1) {
          color = new Color(97,30,117); // purple
        }
        int bottom = random.nextInt(1000);
        int right = random.nextInt(50);
        maze.add(new Node(nodeId,c,r,bottom,right,color));
        nodeId += 1;
      }
    }

    for (int r = 0; r < MazeWorld.MAZE_SIZE_Y; r += 1) {
      for (int c = 0; c < MazeWorld.MAZE_SIZE_X; c += 1) {
        if (c < MazeWorld.MAZE_SIZE_X - 1) {
          maze.get(count).linkRight(maze.get(count + 1));
        }
        if (r < MazeWorld.MAZE_SIZE_Y - 1) {
          maze.get(count).linkBottom(maze.get(count + MazeWorld.MAZE_SIZE_X));
        }
        count += 1;
      }
    }
  }

  // EFFECT:
  // Creates the minimum spanning tree and mutates the maze accordingly
  void createMaze(ArrayList<Node> maze) {
    ArrayList<Edge> mazeEdges = new ArrayList<Edge>();

    for (int n = 0; n < maze.size(); n += 1) {
      Node mazeCell = this.maze.get(n);
      mazeEdges.add(mazeCell.right);
      mazeEdges.add(mazeCell.bottom);
    }
    mazeEdges.sort(new EdgeComparator());

    HashMap<Node, Node> links = new HashMap<Node, Node>();
    for (Node n : maze) {
      links.put(n, n);
    }

    for (Edge e : mazeEdges) {
      if (links.get(e.nodeOne).id == links.get(e.nodeTwo).id) {
        // don't do anything
      } else {
        hashmapReplaceLink(links,links.get(e.nodeTwo),links.get(e.nodeOne));
        this.mazeCorridors.add(e);
      }
    }
  }

  // EFFECT:
  // Animates the construction of the maze by removing maze walls in waves
  void animateMazeCreation() {
    int n = 0;
    while (n < 200) {
      if (!this.mazeCorridors.isEmpty()) {
        Edge e = this.mazeCorridors.get(0);
        e.isCorridor = true;
        if (e.sameEdge(e.nodeTwo.left)) {
          e.nodeTwo.left.isCorridor = true;
        } else {
          e.nodeTwo.top.isCorridor = true;
        }
        this.mazeCorridors.remove(0);
      } else {
        this.buildingMaze = false;
      }
      n += 1;
    }
  }

  // EFFECT:
  // Replaces all links within the given hashmap that have values of the given oldLink
  // with the given newLink
  void hashmapReplaceLink(HashMap<Node,Node> hashmap, Node oldLink, Node newLink) {
    Iterator<Map.Entry<Node, Node>> hashmapIterator = hashmap.entrySet().iterator();
    while (hashmapIterator.hasNext()) {
      Map.Entry<Node, Node> entry = hashmapIterator.next(); 
      if (entry.getValue().id == oldLink.id) {
        entry.setValue(newLink);
      }
    }
  }

  // EFFECT:
  // Modifies cell blocks to display depth or breadth first search patterns and solves the maze
  void search() {
    int n = 0;

    while (n <= 50) { // controls the speed of the search
      n += 1;
      if (!this.worklist.isEmpty()) {
        Node next = this.worklist.remove();
        if (blacklist.contains(next)) {
          // do nothing
        } else if (next.id == this.maze.get(this.maze.size() - 1).id) {
          this.depthSearching = false;
          this.breadthSearching = false;
          this.blacklist = new ArrayList<Node>();
          highlight(next);
          return;
        } else {
          if (next.id != 0) {
            next.changeColor(new Color(134,175,240));
          }
          if (next.right.isCorridor && !blacklist.contains(next.right.nodeTwo)) {
            this.worklist.add(next.right.nodeTwo);
            this.backtrace.put(next.right.nodeTwo, next);
          }
          if (next.bottom.isCorridor && !blacklist.contains(next.bottom.nodeTwo)) {
            this.worklist.add(next.bottom.nodeTwo);
            this.backtrace.put(next.bottom.nodeTwo, next);
          }
          if (next.left.isCorridor && !blacklist.contains(next.left.nodeTwo)) {
            this.worklist.add(next.left.nodeTwo);
            this.backtrace.put(next.left.nodeTwo, next);
          }
          if (next.top.isCorridor && !blacklist.contains(next.top.nodeTwo)) {
            this.worklist.add(next.top.nodeTwo);
            this.backtrace.put(next.top.nodeTwo, next);
          }
          blacklist.add(next);
        }
      }
    }
  }

  // EFFECT:
  // Modifies cell colors to display the solution to the maze
  void highlight(Node n) {
    n = this.backtrace.get(n);
    if (n.id == 0) {
      // do nothing
    } else {
      n.changeColor(new Color(53,107,197));
      n.changeColor(new Color(53,107,197));
      this.highlight(n);
    }
  }

  // EFFECT:
  // Displays the users current path by changing the color of all cells in the blacklist
  void showBlacklist() {
    for (Node n : this.blacklist) {
      n.color = new Color(134,175,240);
    }
  }

  // EFFECT:
  // Hides the users current path by reverting all cells in the blacklist to their previous color
  void hideBlacklist() {
    for (Node n : this.blacklist) {
      n.color = n.prevColor;
    }
  }

  // EFFECT: 
  // Modifies the prev color of each node in the maze based on its distance from the finish
  void heatMap() {
    HashMap<Node,Double> colorBacktrace = new HashMap<Node,Double>();
    HashMap<Node,Node> backtrace = new HashMap<Node,Node>();
    Queue<Node> worklist = new Queue<Node>();
    worklist.add(this.maze.get(0));
    ArrayList<Node> blacklist = new ArrayList<Node>();
    int n = maxSteps(new Queue<Node>());
    double x = 255.0 / n;
    
    while (!worklist.isEmpty()) {
      Node next = worklist.remove();
      if (!blacklist.contains(next)) {
        next.prevColor = new Color(0,0,255);
        if (next.id != 0) {
          Color prevColor = backtrace.get(next).prevColor;
          Double prevIncrement = colorBacktrace.get(next);
          int r;
          int b;
          if (prevColor.getRed() + x > 255) {
            r = 255;
            b = 0;
          } else {
            r = (int) (prevIncrement + x);
            b = (int) (255.0 - (prevIncrement + x));
          } 
          Color newColor = new Color(r, 0, b);
          next.prevColor = newColor;
        }
        if (next.right.isCorridor) {
          worklist.add(next.right.nodeTwo);
          backtrace.put(next.right.nodeTwo, next);
          if (colorBacktrace.containsKey(next)) {
            colorBacktrace.put(next.right.nodeTwo, colorBacktrace.get(next) + x); 
          } else {
            colorBacktrace.put(next.right.nodeTwo, x);
          }
        }
        if (next.bottom.isCorridor) {
          worklist.add(next.bottom.nodeTwo);
          backtrace.put(next.bottom.nodeTwo, next);
          if (colorBacktrace.containsKey(next)) {
            colorBacktrace.put(next.bottom.nodeTwo, colorBacktrace.get(next) + x); 
          } else {
            colorBacktrace.put(next.bottom.nodeTwo, x);
          }
        }
        if (next.left.isCorridor) {
          worklist.add(next.left.nodeTwo);
          backtrace.put(next.left.nodeTwo, next);
          if (colorBacktrace.containsKey(next)) {
            colorBacktrace.put(next.left.nodeTwo, colorBacktrace.get(next) + x); 
          } else {
            colorBacktrace.put(next.left.nodeTwo, x);
          }
        }
        if (next.top.isCorridor) {
          worklist.add(next.top.nodeTwo);
          backtrace.put(next.top.nodeTwo, next);
          if (colorBacktrace.containsKey(next)) {
            colorBacktrace.put(next.top.nodeTwo, colorBacktrace.get(next) + x); 
          } else {
            colorBacktrace.put(next.top.nodeTwo, x);
          }
        }
        blacklist.add(next);
      }
    }
  }
  
  // EFFECT:
  // Modifies all cells that haven't been searched by a trace to revert back to light gray
  void hideHeatMap() {
    for (Node n : this.maze) {
      if (n.color.getRGB() == new Color(134,175,240).getRGB() 
          || n.color.getRGB() == new Color(53,107,197).getRGB()) {
        // Do Nothing
      } else {
        n.color = Color.LIGHT_GRAY;
      }
    }
  }
  
  // EFFECT:
  // Modifies all cells that haven't been searched by a trace to take on its heat map color
  void showHeatMap() {
    this.heatMap();
    for (Node n : this.maze) {
      if (n.color.getRGB() == new Color(134,175,240).getRGB() 
          || n.color.getRGB() == new Color(53,107,197).getRGB()) {
        // Do Nothing
      } else {
        n.color = n.prevColor;
      }
    }
  }

  // Produces the maximum number of steps that a search could take to cover the entire maze
  int maxSteps(ICollection<Node> worklist) {
    Node lastNode = this.maze.get(0);
    HashMap<Node,Integer> longestPathLength = new HashMap<Node,Integer>();
    ArrayList<Node> blacklist = new ArrayList<Node>();
    worklist.add(this.maze.get(0));
    while (!worklist.isEmpty()) {
      Node next = worklist.remove();
      if (!blacklist.contains(next)) {
        int acc = 0;
        lastNode = next;
        if (longestPathLength.containsKey(next)) {
          acc = longestPathLength.get(next);
        }
        if (next.right.isCorridor) {
          worklist.add(next.right.nodeTwo);
          longestPathLength.put(next.right.nodeTwo, acc + 1);
        }
        if (next.bottom.isCorridor) {
          worklist.add(next.bottom.nodeTwo);
          longestPathLength.put(next.bottom.nodeTwo, acc + 1);
        }
        if (next.left.isCorridor) {
          worklist.add(next.left.nodeTwo);
          longestPathLength.put(next.left.nodeTwo, acc + 1);
        }
        if (next.top.isCorridor) {
          worklist.add(next.top.nodeTwo);
          longestPathLength.put(next.top.nodeTwo, acc + 1);
        }
        blacklist.add(next);
      }
    }
    return longestPathLength.get(lastNode);
  }
  
  // Produces the number of cells a search type will visit before reaching the exit
  int searchSteps(ICollection<Node> worklist) {
    ArrayList<Node> blacklist = new ArrayList<Node>();
    worklist.add(this.maze.get(0));
    int acc = 0;
    while (!worklist.isEmpty()) {
      Node next = worklist.remove();
      if (next.id == this.maze.get(this.maze.size() - 1).id) {
        return acc;
      }
      if (!blacklist.contains(next)) {
        acc += 1;
        if (next.right.isCorridor) {
          worklist.add(next.right.nodeTwo);
        }
        if (next.bottom.isCorridor) {
          worklist.add(next.bottom.nodeTwo);
        }
        if (next.left.isCorridor) {
          worklist.add(next.left.nodeTwo);
        }
        if (next.top.isCorridor) {
          worklist.add(next.top.nodeTwo);
        }
        blacklist.add(next);
      }
    }
    return acc;
  }

  // EFFECT:
  // Resets the current maze
  void reset() {
    this.mazeCorridors = new ArrayList<Edge>();
    this.blacklist = new ArrayList<Node>();
    this.backtrace = new HashMap<Node, Node>();
    this.user = this.maze.get(0);
    this.breadthFirstSteps = 0;
    this.depthFirstSteps = 0;
    this.playerSteps = 0;
    this.depthSearching = false;
    this.breadthSearching = false;
    this.displayingHeatmap = false;
    this.displayingUserPath = false;
    this.buildingMaze = true;
    this.resetColor();
  }

  // EFFECT:
  // Resets each node's color to default grey, unless it is the start or finish node
  void resetColor() {
    for (Node n : this.maze) {
      if (n.id != 0 && n.id != this.maze.size() - 1) {
        n.color = Color.LIGHT_GRAY;
      }
    }
  }

  // EFFECT:
  // Creates a new random maze
  void newMaze() {
    this.reset();
    this.maze = new ArrayList<Node>();
    initMaze(this.maze);
    createMaze(this.maze);
    this.user = this.maze.get(0);
  }

  // EFFECT:
  // Creates a new random maze with predominantly vertical walls
  void newVerticalMaze() {
    this.reset();
    this.maze = new ArrayList<Node>();
    initMazeVertical(this.maze);
    createMaze(this.maze);
    this.user = this.maze.get(0);
  }

  // EFFECT:
  // Creates a new random maze with predominantly horizontal walls
  void newHorizontalMaze() {
    this.reset();
    this.maze = new ArrayList<Node>();
    initMazeHorizontal(this.maze);
    createMaze(this.maze);
    this.user = this.maze.get(0);
  }

  // EFFECT:
  // Produces a new depth or breadth first search that modifies cell colors
  void runSearch(ICollection<Node> worklist) {
    if (this.displayingHeatmap) {
      this.resetColor();
      this.showHeatMap();
    } else {
      this.resetColor();
    }
    this.blacklist = new ArrayList<Node>();
    this.backtrace = new HashMap<Node,Node>();
    this.worklist = worklist;
    this.user = this.maze.get(0);
    worklist.add(this.maze.get(0));
  }

  // Determines if any animations are currently being run on the maze
  boolean animationsRunning() {
    return this.buildingMaze || this.breadthSearching || this.depthSearching;
  }

  // EFFECT:
  // Performs tasks on key events that mutate the maze
  public void onKeyEvent(String k) {
    if (k.equals("n")) {
      this.newMaze();
    } else if (k.equals("v")) {
      this.newVerticalMaze();
    } else if (k.equals("h")) {
      this.newHorizontalMaze();
    } else if (k.equals("r")) {
      this.reset();
    } else if (k.equals("s")) {
      if (this.displayingUserPath) {
        this.hideBlacklist();
      } else {
        this.showBlacklist();
      }
      this.displayingUserPath = !this.displayingUserPath;
    } else if (k.equals("d") && !this.animationsRunning()) {
      this.depthSearching = true;
      this.runSearch(new Stack<Node>());
      this.depthFirstSteps = this.searchSteps(new Stack<Node>());
    } else if (k.equals("b") && !this.animationsRunning()) {
      this.breadthSearching = true;
      this.runSearch(new Queue<Node>());
      this.breadthFirstSteps = this.searchSteps(new Queue<Node>());
    } else if (k.equals(" ") && !this.animationsRunning()) {
      if (this.displayingHeatmap) {
        this.hideHeatMap();
      } else {
        this.showHeatMap();
      }
      this.displayingHeatmap = !this.displayingHeatmap;
    } else if (k.equals("up") && !this.animationsRunning()) {
      if (this.user.top.isCorridor) {
        if (this.displayingUserPath) {
          this.user.color = new Color(134,175,240);
        }
        this.playerSteps += 1;
        this.user = this.user.top.nodeTwo;
        this.blacklist.add(this.user);
      }
    } else if (k.equals("left") && !this.animationsRunning()) {
      if (this.user.left.isCorridor) {
        if (this.displayingUserPath) {
          this.user.color = new Color(134,175,240);
        }
        this.playerSteps += 1;
        this.user = this.user.left.nodeTwo;
        this.blacklist.add(this.user);
      }  
    } else if (k.equals("down") && !this.animationsRunning()) {
      if (this.user.bottom.isCorridor) {
        if (this.displayingUserPath) {
          this.user.color = new Color(134,175,240);
        }
        this.playerSteps += 1;
        this.user = this.user.bottom.nodeTwo;
        this.blacklist.add(this.user);
      }  
    } else if (k.equals("right") && !this.animationsRunning()) {
      if (this.user.right.isCorridor) {
        if (this.displayingUserPath) {
          this.user.color = new Color(134,175,240);
        }
        this.playerSteps += 1;
        this.user = this.user.right.nodeTwo;
        this.blacklist.add(this.user);
      }  
    }
  }

  // EFFECT:
  // Performs tasks on the maze on tick that mutate the maze
  public void onTick() {
    if (this.buildingMaze) {
      animateMazeCreation();
    }
    if (this.depthSearching) {
      search();
    }
    if (this.breadthSearching) {
      search();
    }
  }

  // Produces a WorldScene of the current maze
  public WorldScene makeScene() {
    int x = MazeWorld.MAZE_SIZE_X * (MazeWorld.NODE_SIZE + (MazeWorld.EDGE_SIZE))
        + MazeWorld.EDGE_SIZE;
    int y = MazeWorld.MAZE_SIZE_Y * (MazeWorld.NODE_SIZE + (MazeWorld.EDGE_SIZE))
        + MazeWorld.EDGE_SIZE;
    int offset = MazeWorld.NODE_SIZE + MazeWorld.EDGE_SIZE;
    int buffer = (offset / 2) + MazeWorld.EDGE_SIZE;
    int scoreboardHeight = 74;

    WorldScene base = new WorldScene(x,y + scoreboardHeight);
    WorldImage background = new RectangleImage(x, y, OutlineMode.SOLID, Color.GRAY);
    WorldImage scoreboard = new RectangleImage(x, scoreboardHeight, 
        OutlineMode.SOLID, new Color(157,197,187));

    WorldImage breadthFirstStats = new TextImage("Breadth First Search Steps: " 
        + Integer.toString(this.breadthFirstSteps), 12, Color.WHITE);
    WorldImage depthFirstStats = new TextImage("Depth First Search Steps: " 
        + Integer.toString(this.depthFirstSteps), 12, Color.WHITE);
    WorldImage playerStats = new TextImage("Player Search Steps: " 
        + Integer.toString(this.playerSteps), 12, Color.WHITE);

    base.placeImageXY(background, x / 2, y / 2);
    for (int c = 0; c < this.maze.size(); c += 1) {
      Node mazeCell = this.maze.get(c);
      base.placeImageXY(mazeCell.render(), (mazeCell.x * offset) + buffer, 
          (mazeCell.y * offset) + buffer);
    }

    WorldImage u = new RectangleImage(MazeWorld.NODE_SIZE, MazeWorld.NODE_SIZE, OutlineMode.SOLID, 
        Color.BLACK);
    base.placeImageXY(u, user.x * offset + buffer - 1, user.y * offset + buffer - 1);
    if (this.user.id == this.maze.get(this.maze.size() - 1).id) {
      this.highlight(this.user);
      WorldImage winner = new TextImage("You Win!", MazeWorld.MAZE_SIZE_X * 2, FontStyle.BOLD, 
          Color.WHITE);
      base.placeImageXY(winner, x / 2, y / 2);
    }
    base.placeImageXY(scoreboard, x / 2, y + (scoreboardHeight / 2) - 1);
    base.placeImageXY(breadthFirstStats, 5 + (int) (breadthFirstStats.getWidth() / 2), y + 10);
    base.placeImageXY(depthFirstStats, 5 + (int) (depthFirstStats.getWidth() / 2), y + 20 
        + (int) breadthFirstStats.getHeight());
    base.placeImageXY(playerStats, 5 + (int) (playerStats.getWidth() / 2), y + 30 
        + (int) breadthFirstStats.getHeight() + (int) depthFirstStats.getHeight());
    return base;
  }
}