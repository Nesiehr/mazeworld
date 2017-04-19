import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import javalib.worldimages.AboveAlignImage;
import javalib.worldimages.AlignModeX;
import javalib.worldimages.AlignModeY;
import javalib.worldimages.BesideAlignImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;
import tester.Tester;

// Tests the classes and methods within MazeWorld
class ExamplesMaze {
  MazeWorld m;

  Node n1;
  Node n2;
  Node n3;
  Node n4;
  Node n5;
  Node n6;
  Node n7;
  Node n8;
  Node n9;
  Node n10;
  Node n11;
  Node n12;
  Node n13;
  Node n14;
  Node n15;
  Node n16;

  void initNodes() {
    this.n1 = new Node(1, 0, 0, Color.LIGHT_GRAY);
    this.n2 = new Node(2, 1, 0, Color.LIGHT_GRAY);
    this.n3 = new Node(3, 2, 0, Color.LIGHT_GRAY);
    this.n4 = new Node(4, 3, 0, Color.LIGHT_GRAY);
    this.n5 = new Node(5, 0, 1, Color.LIGHT_GRAY);
    this.n6 = new Node(6, 1, 1, Color.LIGHT_GRAY);
    this.n7 = new Node(7, 2, 1, Color.LIGHT_GRAY);
    this.n8 = new Node(8, 3, 1, Color.LIGHT_GRAY);
    this.n9 = new Node(9, 0, 2, Color.LIGHT_GRAY);
    this.n10 = new Node(10, 1, 2, Color.LIGHT_GRAY);
    this.n11 = new Node(11, 2, 2, Color.LIGHT_GRAY);
    this.n12 = new Node(12, 3, 2, Color.LIGHT_GRAY);
    this.n13 = new Node(13, 0, 3, Color.LIGHT_GRAY);
    this.n14 = new Node(14, 1, 3, Color.LIGHT_GRAY);
    this.n15 = new Node(15, 2, 3, Color.LIGHT_GRAY);
    this.n16 = new Node(16, 3, 3, Color.LIGHT_GRAY);
  }

  void initEdges() {
    this.initNodes();
    this.n1.linkRight(n2);
    this.n2.linkRight(n3);
    this.n3.linkRight(n4);
    this.n5.linkRight(n6);
    this.n6.linkRight(n7);
    this.n7.linkRight(n8);
    this.n9.linkRight(n10);
    this.n10.linkRight(n11);
    this.n11.linkRight(n12);
    this.n13.linkRight(n14);
    this.n14.linkRight(n15);
    this.n15.linkRight(n16);

    this.n1.linkBottom(n5);
    this.n2.linkBottom(n6);
    this.n3.linkBottom(n7);
    this.n4.linkBottom(n8);
    this.n5.linkBottom(n9);
    this.n6.linkBottom(n10);
    this.n7.linkBottom(n11);
    this.n8.linkBottom(n12);
    this.n9.linkBottom(n13);
    this.n10.linkBottom(n14);
    this.n11.linkBottom(n15);
    this.n12.linkBottom(n16);
    
    this.n1.right.isCorridor = true;
    this.n2.bottom.isCorridor = true;
    this.n6.bottom.isCorridor = true;
    this.n10.right.isCorridor = true;
    this.n5.bottom.isCorridor = true;
    this.n9.bottom.isCorridor = true;
    this.n9.right.isCorridor = true;
    this.n11.bottom.isCorridor = true;
    this.n14.right.isCorridor = true;
    this.n7.bottom.isCorridor = true;
    this.n3.bottom.isCorridor = true;
    this.n3.right.isCorridor = true;
    this.n4.bottom.isCorridor = true;
    this.n8.bottom.isCorridor = true;
    this.n12.bottom.isCorridor = true;
    
    this.n2.left.isCorridor = true;
    this.n6.top.isCorridor = true;
    this.n10.top.isCorridor = true;
    this.n11.left.isCorridor = true;
    this.n9.top.isCorridor = true;
    this.n13.top.isCorridor = true;
    this.n10.left.isCorridor = true;
    this.n15.top.isCorridor = true;
    this.n15.left.isCorridor = true;
    this.n11.top.isCorridor = true;
    this.n7.top.isCorridor = true;
    this.n4.left.isCorridor = true;
    this.n8.top.isCorridor = true;
    this.n12.top.isCorridor = true;
    this.n16.top.isCorridor = true;
  }  

  void initMazeWorld() {
    this.m = new MazeWorld();
  }

  //////////////// TESTS FOR EDGE /////////////// 

  void testRenderEdge(Tester t) {
    this.initEdges();
    
    t.checkExpect(this.n1.right.render(2, 10, Color.LIGHT_GRAY), new RectangleImage(2, 10, 
        OutlineMode.SOLID, 
          Color.LIGHT_GRAY));
    t.checkExpect(this.n1.bottom.render(10, 2, Color.LIGHT_GRAY), new RectangleImage(10, 2, 
        OutlineMode.SOLID, Color.GRAY));
    t.checkExpect(this.n2.right.render(2, 10, Color.LIGHT_GRAY), new RectangleImage(2, 10, 
        OutlineMode.SOLID, Color.GRAY));
    t.checkExpect(this.n2.bottom.render(2, 10, Color.LIGHT_GRAY), new RectangleImage(2, 10, 
        OutlineMode.SOLID, Color.LIGHT_GRAY));
  }
  
  void testSameEdge(Tester t) {
    this.initEdges();
    
    t.checkExpect(this.n1.right.sameEdge(this.n1.right), true);
    t.checkExpect(this.n1.right.sameEdge(this.n2.left), true);
    t.checkExpect(this.n1.right.sameEdge(this.n5.top), false);
  }
  
  
  
  //////////////// TESTS FOR NODE /////////////// 
  
  void testLink(Tester t) {
    this.initEdges();
    
    t.checkExpect(this.n1.right.nodeTwo, this.n2);
    t.checkExpect(this.n6.left.nodeTwo, this.n5);
    this.n1.linkRight(this.n6);
    t.checkExpect(this.n1.right.nodeTwo, this.n6);
    t.checkExpect(this.n6.left.nodeTwo, this.n1);
    
    t.checkExpect(this.n2.bottom.nodeTwo, this.n6);
    t.checkExpect(this.n7.top.nodeTwo, this.n3);
    this.n2.linkBottom(this.n7);
    t.checkExpect(this.n2.bottom.nodeTwo, this.n7);
    t.checkExpect(this.n7.top.nodeTwo, this.n2);
  }

  void testRenderNode(Tester t) {
    this.initEdges();
    
    WorldImage n1Render = new RectangleImage(MazeWorld.NODE_SIZE, MazeWorld.NODE_SIZE, 
        OutlineMode.SOLID, Color.LIGHT_GRAY);
    n1Render = new BesideAlignImage(AlignModeY.MIDDLE, n1Render, 
        this.n1.right.render(MazeWorld.EDGE_SIZE, MazeWorld.NODE_SIZE, Color.LIGHT_GRAY));
    n1Render = new AboveAlignImage(AlignModeX.LEFT, n1Render, 
        this.n1.bottom.render(MazeWorld.NODE_SIZE, MazeWorld.EDGE_SIZE, Color.LIGHT_GRAY));
    
    t.checkExpect(this.n1.render(), n1Render);
    
    WorldImage n2Render = new RectangleImage(MazeWorld.NODE_SIZE, MazeWorld.NODE_SIZE, 
        OutlineMode.SOLID, Color.LIGHT_GRAY);
    n2Render = new BesideAlignImage(AlignModeY.MIDDLE, n2Render, 
        this.n2.right.render(MazeWorld.EDGE_SIZE, MazeWorld.NODE_SIZE, Color.LIGHT_GRAY));
    n2Render = new AboveAlignImage(AlignModeX.LEFT, n2Render, 
        this.n2.bottom.render(MazeWorld.NODE_SIZE, MazeWorld.EDGE_SIZE, Color.LIGHT_GRAY));
    
    t.checkExpect(this.n2.render(), n2Render);
  }

  //////////////// TESTS FOR MAZEWORLD ///////////////

  void testHashmapReplaceLink(Tester t) {
    this.initMazeWorld();
    this.initEdges();

    HashMap<Node, Node> hm1 = new HashMap<Node, Node>();
    hm1.put(this.n1, this.n1);
    hm1.put(this.n2, this.n2);
    hm1.put(this.n3, this.n2);
    hm1.put(this.n4, this.n4);

    t.checkExpect(hm1.get(this.n1), this.n1);
    t.checkExpect(hm1.get(this.n2), this.n2);
    t.checkExpect(hm1.get(this.n3), this.n2);
    t.checkExpect(hm1.get(this.n4), this.n4);

    m.hashmapReplaceLink(hm1, this.n1, this.n2);

    t.checkExpect(hm1.get(this.n1), this.n2);
    t.checkExpect(hm1.get(this.n2), this.n2);
    t.checkExpect(hm1.get(this.n3), this.n2);
    t.checkExpect(hm1.get(this.n4), this.n4);

    m.hashmapReplaceLink(hm1, this.n2, this.n4);

    t.checkExpect(hm1.get(this.n1), this.n4);
    t.checkExpect(hm1.get(this.n2), this.n4);
    t.checkExpect(hm1.get(this.n3), this.n4);
    t.checkExpect(hm1.get(this.n4), this.n4);
  }

  void testAnimateMazeCreation(Tester t) {
    this.initMazeWorld();
    
    for (Node n : this.m.maze) {
      t.checkExpect(n.left.isCorridor, false);
      t.checkExpect(n.right.isCorridor, false);
      t.checkExpect(n.top.isCorridor, false);
      t.checkExpect(n.bottom.isCorridor, false);
    }
    
    for (int i = 0; i <= m.maze.size() / 50; i++) {
      m.animateMazeCreation();
    }
    
    for (Node n : this.m.maze) {
      t.checkExpect(n.left.isCorridor || n.right.isCorridor 
          || n.top.isCorridor || n.bottom.isCorridor, true);
    }
  }
  
  void testInitMaze(Tester t) {
    this.initMazeWorld();

    for (int i = 0; i < MazeWorld.MAZE_SIZE_X * MazeWorld.MAZE_SIZE_Y; i += 1) {
      t.checkExpect(m.maze.get(i).id, i);
      t.checkNumRange(m.maze.get(i).x, 0, MazeWorld.MAZE_SIZE_X);
      t.checkNumRange(m.maze.get(i).y, 0, MazeWorld.MAZE_SIZE_Y);
    }
  }

  void testCreateMaze(Tester t) {
    this.initMazeWorld();

    for (Node n : m.maze) {
      if (n.x < MazeWorld.MAZE_SIZE_X - 1) {
        t.checkExpect(n.right.nodeTwo.id, n.id + 1);
      }
      if (n.y < MazeWorld.MAZE_SIZE_Y - 1) {
        t.checkExpect(n.bottom.nodeTwo.id, n.id + MazeWorld.MAZE_SIZE_X);
      }
    }
  }
  
  void testSearch(Tester t) {
    this.initMazeWorld();
    
    Node first = this.m.maze.get(0).right.nodeTwo;
    Node second = this.m.maze.get(0).bottom.nodeTwo;
    t.checkExpect(first.color, Color.LIGHT_GRAY);
    t.checkExpect(second.color, Color.LIGHT_GRAY);
    
    // TODO
    //this.m.search(); 
    
    Color c = new Color(134,175,240);
    //t.checkExpect(first.color.equals(c) || second.color.equals(c), true);
  }
  
  void testHighlight(Tester t) {
    this.initMazeWorld();
    Node first = this.m.maze.get(0);
    Node second = this.m.maze.get(1);
    Node third = this.m.maze.get(2);
    t.checkExpect(second.color, Color.LIGHT_GRAY);
    t.checkExpect(third.color, Color.LIGHT_GRAY);
    
    this.m.backtrace.put(second, first);
    this.m.backtrace.put(third, second);
    
    this.m.highlight(this.m.maze.get(2));
    
    t.checkExpect(second.color, new Color(53,107,197));
    t.checkExpect(third.color, Color.LIGHT_GRAY);
  }
  
  void testHeatMap(Tester t) {
    //TODO
  }
  
  void testReset(Tester t) {
    this.initMazeWorld();
    
    this.m.mazeCorridors = new ArrayList<Edge>();
    this.m.mazeCorridors.add(this.n1.bottom);
    this.m.blacklist = new ArrayList<Node>();
    this.m.blacklist.add(this.n1);
    this.m.backtrace = new HashMap<Node, Node>();
    this.m.backtrace.put(this.n1, this.n2);
    this.m.user = this.m.maze.get(1);
    this.m.depthSearching = true;
    this.m.breadthSearching = true;
    this.m.displayingHeatmap = true;
    this.m.buildingMaze = false;
    
    this.m.reset();
    
    t.checkExpect(this.m.mazeCorridors, new ArrayList<Edge>());
    t.checkExpect(this.m.blacklist, new ArrayList<Node>());
    t.checkExpect(this.m.backtrace, new HashMap<Node, Node>());
    t.checkExpect(this.m.user, this.m.maze.get(0));
    t.checkExpect(this.m.depthSearching, false);
    t.checkExpect(this.m.breadthSearching, false);
    t.checkExpect(this.m.displayingHeatmap, false);
    t.checkExpect(this.m.buildingMaze, true);
  }
  
  void testNewMaze(Tester t) {
    this.initMazeWorld();
    
    int s = this.m.maze.size();    
    this.m.newMaze();
    t.checkExpect(this.m.maze.size(), s);
  }
  
  void testRunSearch(Tester t) {
    this.initMazeWorld();
    
    t.checkExpect(this.m.blacklist, new ArrayList<Node>());
    t.checkExpect(this.m.backtrace, new HashMap<Node,Node>());
    ICollection<Node> stack = new Stack<Node>();
    this.m.runSearch(stack);
    t.checkExpect(this.m.blacklist, new ArrayList<Node>());
    t.checkExpect(this.m.backtrace, new HashMap<Node,Node>());
    stack.add(this.m.maze.get(0));
    t.checkExpect(this.m.worklist, stack);
    
    this.initMazeWorld();
    ICollection<Node> queue = new Queue<Node>();
    this.m.runSearch(queue);
    t.checkExpect(this.m.blacklist, new ArrayList<Node>());
    t.checkExpect(this.m.backtrace, new HashMap<Node,Node>());
    queue.add(this.m.maze.get(0));
    t.checkExpect(this.m.worklist, queue);
  }
  
  void testAnimationsRunning(Tester t) {
    this.initMazeWorld();
    this.m.buildingMaze = false;
    this.m.breadthSearching = false;
    this.m.depthSearching = false;
    t.checkExpect(m.animationsRunning(), false);
    
    this.m.buildingMaze = true;
    t.checkExpect(m.animationsRunning(), true);
    
    this.m.breadthSearching = true;
    t.checkExpect(m.animationsRunning(), true);
    
    this.m.depthSearching = true;
    t.checkExpect(m.animationsRunning(), true);
  }
  
  void testMaxBreadthFirstSteps(Tester t) {
    //TODO
  }

  void testMazeWorld(Tester t) {
    this.initMazeWorld();
    int x = MazeWorld.MAZE_SIZE_X * (MazeWorld.NODE_SIZE + (MazeWorld.EDGE_SIZE))
        + MazeWorld.EDGE_SIZE;
    int y = MazeWorld.MAZE_SIZE_Y * (MazeWorld.NODE_SIZE + (MazeWorld.EDGE_SIZE))
        + MazeWorld.EDGE_SIZE;
    int scoreboardHeight = 74;
    m.bigBang(x, y + scoreboardHeight, 0.1);
  }
}