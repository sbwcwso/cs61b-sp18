import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private final int maxDepth;
    private final double baseLonDPP;
    private final double[] pictureLonDPP, pictureLons, pictureLats;
    private double ullon, ullat, lrlon, lrlat, w, h;
    private String[][] renderGrid;
    private double rasterUlLon, rasterUlLat, rasterLrLon, rasterLrLat;
    private int depth, maxPic;
    private boolean querySuccess;

    public Rasterer() {
        maxDepth = 7;
        double baseLon = MapServer.ROOT_LRLON - MapServer.ROOT_ULLON;
        double baseLat = MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT;
        baseLonDPP = baseLon / MapServer.TILE_SIZE;
        pictureLonDPP = new double[maxDepth + 1];
        pictureLons = new double[maxDepth + 1];
        pictureLats = new double[maxDepth + 1];

        int scale = 1;
        for (int i = 0; i < maxDepth + 1; i++) {
            pictureLonDPP[i] = baseLonDPP / scale;
            pictureLats[i] = baseLat / scale;
            pictureLons[i] = baseLon / scale;
            scale *= 2;
        }
    }

    private void handleParams(Map<String, Double> params) {
        ullon = params.get("ullon");
        ullat = params.get("ullat");
        lrlon = params.get("lrlon");
        lrlat = params.get("lrlat");
        w = params.get("w");
        h = params.get("h");

        // check the params
        if (!(((ullon < lrlon) && (ullat > lrlat)) && w > 0 && h > 0)) {
            querySuccess = false;
            return;
        }

        // check if query is out of scope
        if (!((ullon < MapServer.ROOT_LRLON && lrlon > MapServer.ROOT_ULLON)
                && (ullat > MapServer.ROOT_LRLAT && lrlat < MapServer.ROOT_ULLAT))) {
            querySuccess = false;
            return;
        }

        // confirm the depth
        double lonDPP = (lrlon - ullon) / w;
        maxPic = 1;
        for (depth = 0; depth < maxDepth; depth++) {
            if (pictureLonDPP[depth] < lonDPP) {
                break;
            }
            maxPic *= 2;
        }

        // bound the query size to the scope
        ullon = Math.max(ullon, MapServer.ROOT_ULLON);
        ullat = Math.min(ullat, MapServer.ROOT_ULLAT);
        lrlon = Math.min(lrlon, MapServer.ROOT_LRLON);
        lrlat = Math.max(lrlat, MapServer.ROOT_LRLAT);

        querySuccess = true;
    }

    private void getRenderGrid() {
        int xPics, yPics;
        int xMin, yMin, xMax, yMax;
        xMin = (int) Math.floor((ullon - MapServer.ROOT_ULLON) / pictureLons[depth]);
        xMax = (int) Math.floor((lrlon - MapServer.ROOT_ULLON) / pictureLons[depth]);
        yMin = (int) Math.floor((MapServer.ROOT_ULLAT - ullat) / pictureLats[depth]);
        yMax = (int) Math.floor((MapServer.ROOT_ULLAT - lrlat) / pictureLats[depth]);
        xMax = Math.min(xMax, maxPic - 1);
        yMax = Math.min(yMax, maxPic - 1);
        xPics = xMax - xMin + 1;
        yPics = yMax - yMin + 1;

        rasterUlLon = MapServer.ROOT_ULLON + xMin * pictureLons[depth];
        rasterLrLon = MapServer.ROOT_ULLON + (xMax + 1) * pictureLons[depth];
        rasterUlLat = MapServer.ROOT_ULLAT - yMin * pictureLats[depth];
        rasterLrLat = MapServer.ROOT_ULLAT - (yMax + 1) * pictureLats[depth];

        String picPrefix = "d" + depth + "_";
        renderGrid = new String[yPics][xPics];

        for (int y = 0; y < yPics; y++) {
            for (int x = 0; x < xPics; x++) {
                renderGrid[y][x] = picPrefix + "x" + (xMin + x) + "_y" + (yMin + y) + ".png";
            }
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();
        handleParams(params);

        if (querySuccess) {
            // choose the right picture to cover the request region
            getRenderGrid();
        }

        results.put("render_grid", renderGrid);
        results.put("raster_ul_lon", rasterUlLon);
        results.put("raster_ul_lat", rasterUlLat);
        results.put("raster_lr_lon", rasterLrLon);
        results.put("raster_lr_lat", rasterLrLat);
        results.put("depth", depth);
        results.put("query_success", querySuccess);

        return results;
    }

}
