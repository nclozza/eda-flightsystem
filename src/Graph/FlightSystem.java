package Graph;

import java.util.LinkedList;
import java.util.List;

/**
 * The System class is a class that represents de Graph class in a standard graph structure.
 * It contains a list of all the "nodes" (airports) and is in charge of adding new ones.
 * It also contains the methods that iterate over the graph in different ways.
 */
public class FlightSystem {
    private List<Airport> airports;

    public FlightSystem(){
        airports = new LinkedList<>();
    }

    public void addAirport(String name, float latitude, float longitude) {
        Airport a = new Airport(name, latitude, longitude);
        airports.add(a);
    }

    public List<Airport> getAirports(){
        return airports;
    }
}
