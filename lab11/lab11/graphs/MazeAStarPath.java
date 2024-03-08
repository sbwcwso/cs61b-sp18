package lab11.graphs;

/**
 * @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private final int s;
    private final int t;
    private boolean targetFound = false;
    private final Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /**
     * Estimate of the distance from v to the target.
     */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /**
     * Finds vertex estimated to be closest to target.
     */
    private int findMinimumUnmarked() {
        int minimumDistance = Integer.MAX_VALUE;
        int minimumV = t;
        int size = maze.V();
        for (int v = 0; v < size; v++) {
            if (!marked[v] && distTo[v] < Integer.MAX_VALUE) {
                int estimateDistance = distTo[v] + h(v);
                if (estimateDistance < minimumDistance) {
                    minimumDistance = estimateDistance;
                    minimumV = v;
                }
            }
        }
        return minimumV;
        /* You do not have to use this method. */
    }

    /**
     * Performs an A star search from vertex s.
     */
    private void astar(int s) {
        marked[s] = true;
        announce();

        if (s == t) {
            targetFound = true;
        }

        if (targetFound) {
            return;
        }

        for (int w : maze.adj(s)) {
            if (!marked[w]) {
                edgeTo[w] = s;
                distTo[w] = distTo[s] + 1;
            }
        }
        astar(findMinimumUnmarked());
    }

    @Override
    public void solve() {
        astar(s);
    }

}

