package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Solver {
    private final ArrayList<WorldState> path;
    private int moves;

    private static class SearchNode {
        private final WorldState state;
        private final int moves;
        private final int movesAddEstimatedDistanceToGoal;
        private final SearchNode previousSearchNode;

        /**
         * Constructor for start search none
         */
        SearchNode(WorldState state) {
            this.previousSearchNode = null;
            this.state = state;
            this.moves = 0;
            this.movesAddEstimatedDistanceToGoal = this.moves + state.estimatedDistanceToGoal();
        }

        /**
         * Constructor for search node other than start search node.
         */
        SearchNode(SearchNode previousSearchNode, WorldState state) {
            this.previousSearchNode = previousSearchNode;
            this.state = state;
            this.moves = previousSearchNode.moves + 1;
            this.movesAddEstimatedDistanceToGoal = this.moves + state.estimatedDistanceToGoal();
        }

        public WorldState state() {
            return state;
        }

        public int priority() {
            return movesAddEstimatedDistanceToGoal;
        }

        public SearchNode parent() {
            return previousSearchNode;
        }
    }

    public Solver(WorldState initial) {
        MinPQ<SearchNode> searchNodePQ = new MinPQ<>(new Comparator<SearchNode>() {
            public int compare(SearchNode n1, SearchNode n2) {
                return n1.priority() - n2.priority();
            }
        });

        SearchNode start = new SearchNode(initial);
        SearchNode lastNode = start;

        searchNodePQ.insert(start);
        while (!searchNodePQ.isEmpty()) {
            lastNode = searchNodePQ.delMin();
            WorldState state = lastNode.state();
            if (state.isGoal()) {
                break;
            }
            for (WorldState neighbor : state.neighbors()) {
                if (lastNode == start || !neighbor.equals(lastNode.parent().state())) {
                    searchNodePQ.insert(new SearchNode(lastNode, neighbor));
                }
            }
        }

        path = new ArrayList<>();
        path.add(lastNode.state());
        SearchNode node = lastNode;
        moves = 0;
        while (node.previousSearchNode != null) {
            moves++;
            node = node.previousSearchNode;
            path.add(node.state());
        }
        Collections.reverse(path);

//        System.out.println(enqueCount);  // for test

    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return path;
    }
}
