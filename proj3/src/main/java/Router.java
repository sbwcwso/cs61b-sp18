import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     *
     * @param g       The graph to use.
     * @param stlon   The longitude of the start location.
     * @param stlat   The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long startNodeId = g.closest(stlon, stlat);
        long endNodeId = g.closest(destlon, destlat);

        final Map<Long, Double> dstToStart = new HashMap<>();
        final Map<Long, Long> edgeTo = new HashMap<>();
        final Set<Long> markedNodes = new HashSet<>();
        final Map<Long, Double> priorities = new HashMap<>();
        PriorityQueue<Long> priorityQueue = new PriorityQueue<>(
            Comparator.comparingDouble(i -> priorities.getOrDefault(i, Double.MAX_VALUE)));
        List<Long> path = new LinkedList<>();

        priorityQueue.add(startNodeId);
        dstToStart.put(startNodeId, 0.0);
        while (!priorityQueue.isEmpty()) {
            Long v = priorityQueue.remove();
            markedNodes.add(v);
            if (v == endNodeId) {
                path.add(endNodeId);
                long nodeId = endNodeId;
                while (nodeId != startNodeId) {
                    nodeId = edgeTo.get(nodeId);
                    path.add(nodeId);
                }
                Collections.reverse(path);
                return path;
            }
            for (Long w : g.adjacent(v)) {
                if (markedNodes.contains(w)) {
                    continue;
                }
                double startTow = dstToStart.getOrDefault(w, Double.MAX_VALUE);
                double startTovTow = dstToStart.get(v) + g.distance(v, w);
                if (startTovTow < startTow) {
                    edgeTo.put(w, v);
                    dstToStart.put(w, startTovTow);
                    priorities.put(w, startTovTow + g.distance(w, endNodeId));
                    priorityQueue.add(w);
                }
            }
        }
        return path;  // path doesn't exist, return an empty list.
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     *
     * @param g     The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigationDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> navigationDirections = new LinkedList<>();
        assert route.size() > 1 : "The size of route must greater than or equal 1";
        Iterator<Long> iterator = route.iterator();
        long start = iterator.next();
        GraphDB.Way curWay = g.getNode(start).edges.get(route.get(1));
        String direction = NavigationDirection.DIRECTIONS[NavigationDirection.START];
        double distance = 0;
        long cur = start;
        long prev = start;
        while (iterator.hasNext()) {
            long next = iterator.next();
            GraphDB.Way nextWay = g.getNode(cur).edges.get(next);
            if (!curWay.name.equals(nextWay.name)) {
                navigationDirections.add(NavigationDirection.fromString(direction + " on "
                    + curWay.name + " and continue for " + distance + " " + "miles."
                ));

                double relativeBearing = g.bearing(cur, next) - g.bearing(prev, cur);
                if (relativeBearing > 180) {
                    relativeBearing -= 360;
                } else if (relativeBearing < -180) {
                    relativeBearing += 360;
                }
                if (relativeBearing > -15 && relativeBearing <= 15) {
                    direction = NavigationDirection.DIRECTIONS[NavigationDirection.STRAIGHT];
                } else if (relativeBearing > -30 && relativeBearing <= -15) {
                    direction = NavigationDirection.DIRECTIONS[NavigationDirection.SLIGHT_LEFT];
                } else if (relativeBearing > 15 && relativeBearing <= 30) {
                    direction = NavigationDirection.DIRECTIONS[NavigationDirection.SLIGHT_RIGHT];
                } else if (relativeBearing > -100 && relativeBearing <= -30) {
                    direction = NavigationDirection.DIRECTIONS[NavigationDirection.LEFT];
                } else if (relativeBearing > 30 && relativeBearing <= 100) {
                    direction = NavigationDirection.DIRECTIONS[NavigationDirection.RIGHT];
                } else if (relativeBearing <= -100) {
                    direction = NavigationDirection.DIRECTIONS[NavigationDirection.SHARP_LEFT];
                } else if (relativeBearing > 100) {
                    direction = NavigationDirection.DIRECTIONS[NavigationDirection.SHARP_RIGHT];
                }
                distance = 0;
                curWay = nextWay;
            }

            distance += g.distance(cur, next);
            prev = cur;
            cur = next;
        }

        navigationDirections.add(NavigationDirection.fromString(direction + " on "
            + curWay.name + " and continue for " + distance + " " + "miles."
        ));

        return navigationDirections;
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /**
         * Integer constants representing directions.
         */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /**
         * Number of directions supported.
         */
        public static final int NUM_DIRECTIONS = 8;

        /**
         * A mapping of integer values to directions.
         */
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /**
         * Default name for an unknown way.
         */
        public static final String UNKNOWN_ROAD = "unknown road";

        /* Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /**
         * The direction a given NavigationDirection represents.
         */
        int direction;
        /**
         * The name of the way I represent.
         */
        String way;
        /**
         * The distance along this way I represent.
         */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         *
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                switch (direction) {
                    case "Start":
                        nd.direction = NavigationDirection.START;
                        break;
                    case "Go straight":
                        nd.direction = NavigationDirection.STRAIGHT;
                        break;
                    case "Slight left":
                        nd.direction = NavigationDirection.SLIGHT_LEFT;
                        break;
                    case "Slight right":
                        nd.direction = NavigationDirection.SLIGHT_RIGHT;
                        break;
                    case "Turn right":
                        nd.direction = NavigationDirection.RIGHT;
                        break;
                    case "Turn left":
                        nd.direction = NavigationDirection.LEFT;
                        break;
                    case "Sharp left":
                        nd.direction = NavigationDirection.SHARP_LEFT;
                        break;
                    case "Sharp right":
                        nd.direction = NavigationDirection.SHARP_RIGHT;
                        break;
                    default:
                        return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
