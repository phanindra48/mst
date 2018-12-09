package pxp180031;

//Starter code for LP5

import rbk.Graph.Vertex;
import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
import rbk.Graph.Timer;

import pxp180031.BinaryHeap.Index;
import pxp180031.BinaryHeap.IndexedHeap;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;
import java.io.FileNotFoundException;
import java.security.GeneralSecurityException;
import java.io.File;

public class MST extends GraphAlgorithm<MST.MSTVertex> {
  String algorithm;
  public long wmst;
  List<Edge> mst;

  MST(Graph g) {
    super(g, new MSTVertex((Vertex) null));
  }

  public static class MSTVertex implements Index, Comparable<MSTVertex>, Factory {
    boolean seen; // set to true if vertex is already seen
    Vertex parent;
    int rank;
    int d;
    Vertex vertex;

    MSTVertex(Vertex u) {
      seen = false;
      parent = u;
      rank = 0;
      vertex = u;
      d = 0;
    }

    public MSTVertex make(Vertex u) {
      return new MSTVertex(u);
    }

    public void putIndex(int index) {
    }

    public int getIndex() {
      return 0;
    }

    public int compareTo(MSTVertex other) {
      return 0;
    }

    // public Vertex find() {
    //   if (this != get(parent)) {
    //     get(u).parent = find(get(u).parent);
    //   }
    //   return null;
    // }

    // public void union(Vertex rv, Vertex ru) {
    // }

  }

  public Vertex find(Vertex u) {
    if (get(u).parent.getName() != u.getName()) {
      get(u).parent = find(get(u).parent);
    }
    return get(u).parent;
  }

  public void union(Vertex ru, Vertex rv) {
    if (get(ru).rank > get(rv).rank) {
      get(rv).parent = ru;
    } else if (get(ru).rank < get(rv).rank) {
      get(ru).parent = rv;
    } else {
      get(ru).rank++;
      get(rv).parent = ru;
    }
  }

  public long kruskal() {
    algorithm = "Kruskal";
    Edge[] edgeArray = g.getEdgeArray();
    mst = new LinkedList<>();
    wmst = 0;

    Arrays.sort(edgeArray);
    for (Edge e : edgeArray) {
      Vertex u = e.fromVertex();
      Vertex v = e.otherEnd(u);

      Vertex ru = find(u);
      Vertex rv = find(v);
      if (!ru.equals(rv)) {
        mst.add(e);
        wmst += e.getWeight();
        union(ru, rv);
      }
    }
    return wmst;
  }

  public long prim3(Vertex s) {
    algorithm = "Indexed Heap";

    for (Vertex u : g) {
      get(u).seen = false;
      get(u).parent = null;
      get(u).d = Integer.MAX_VALUE;
    }

    get(s).d = 0;
    wmst = 0;
    IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());

    for (Vertex u : g) {
      q.add(get(u));
    }
    System.out.println(q);
    while (!q.isEmpty()) {
      MSTVertex uMSTVertex = q.remove();
      Vertex u = uMSTVertex.vertex;
      if (!get(u).seen) {
        get(u).seen = true;
        wmst = wmst + get(u).d;
        for (Edge e : g.incident(u)) {
          Vertex v = e.otherEnd(u);
          if (!get(v).seen && e.getWeight() < get(v).d) {
            get(v).d = e.getWeight();
            get(v).parent = u;
            q.decreaseKey(get(v));
          }
        }
      }
    }
    return wmst;
  }

  public long prim2(Vertex s) {
    algorithm = "PriorityQueue<Vertex>";
    for (Vertex u : g) {
      get(u).seen = false;
      get(u).parent = null;
      get(u).d = Integer.MAX_VALUE;
    }

    get(s).d = 0;
    wmst = 0;
    PriorityQueue<MSTVertex> q = new PriorityQueue<>();
    q.add(get(s));

    while (!q.isEmpty()) {
      Vertex u = q.remove().vertex;
      if (!get(u).seen) {
        get(u).seen = true;
        wmst = wmst + get(u).d;
        for (Edge e : g.incident(u)) {
          Vertex v = e.otherEnd(u);
          if (!get(v).seen && e.getWeight() < get(v).d) {
            get(v).d = e.getWeight();
            get(v).parent = u;
            q.add(get(v));
          }
        }
      }
    }
    return wmst;
  }

  public long prim1(Vertex s) {
    algorithm = "PriorityQueue<Edge>";

    for (Vertex u : g) {
      get(u).seen = false;
      get(u).parent = null;
    }
    get(s).seen = true;
    wmst = 0;
    PriorityQueue<Edge> q = new PriorityQueue<>();

    for (Edge e : g.incident(s)) {
      q.add(e);
    }

    while (!q.isEmpty()) {
      Edge e = q.remove();
      Vertex u = e.fromVertex();
      Vertex v = (get(u).seen) ? e.otherEnd(u) : u;
      if (get(v).seen) {
        continue;
      }

      get(v).seen = true;
      get(v).parent = u;
      wmst = wmst + e.getWeight();

      for (Edge e2 : g.incident(v)) {
        if (!get(e2.otherEnd(v)).seen) {
          q.add(e2);
        }
      }
    }
    return wmst;
  }

  public static MST mst(Graph g, Vertex s, int choice) {
    MST m = new MST(g);
    switch (choice) {
    case 0:
      m.kruskal();
      break;
    case 1:
      m.prim1(s);
      break;
    case 2:
      m.prim2(s);
      break;
    default:
      m.prim3(s);
      break;
    }
    return m;
  }

  public static void main(String[] args) throws FileNotFoundException {
    // String string = "5 7   1 2 2   1 3 6   2 4 3   2 5 5   2 3 8   3 5 9   4 5 7";
    // Scanner in = new Scanner(string);
    Scanner in;
    int choice = 0;
    if (args.length == 0 || args[0].equals("-")) {
      in = new Scanner(System.in);
    } else {
      File inputFile = new File(args[0]);
      System.out.println(inputFile.getName());
      in = new Scanner(inputFile);
    }

    if (args.length > 1) { choice = Integer.parseInt(args[1]); }

    Graph g = Graph.readGraph(in);
    Vertex s = g.getVertex(1);

    Timer timer = new Timer();
    MST m = mst(g, s, choice);
    System.out.println(m.algorithm + "\n" + m.wmst);
    System.out.println(timer.end());
  }
}
