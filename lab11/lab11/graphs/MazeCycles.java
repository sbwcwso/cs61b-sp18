package lab11.graphs;

/**
 * @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private final Maze maze;
    private boolean loopFound = false;
    private final int[] parents;
    private int lastVisited;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        parents = new int[maze.V()];
    }

    @Override
    public void solve() {
        dfs(0);
        if (loopFound) {
            edgeTo[lastVisited] = parents[lastVisited];
            announce();
            int v = parents[lastVisited];
            while (v != lastVisited) {
                int parent = parents[v];
                edgeTo[v] = parent;
                announce();
                v = parent;
            }
        }
    }

    // Helper methods go here
    private void dfs(int v) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                parents[w] = v;
                dfs(w);
                if (loopFound) {
                    return;
                }
            } else if (parents[v] != w) {
                loopFound = true;
                lastVisited = w;
                parents[w] = v;
                return;
            }

        }
    }
}

