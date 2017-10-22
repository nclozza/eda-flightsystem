package Graph;

import java.util.LinkedList;
import java.util.List;

/**
 * The Airport class is an implementation of an airport consisting of the airport's name, its location and a list of the
 * airport's outbound and return flights.
 * The Airport class will be used as a node in a directed graph. The arcs that part from or arrive to a node are set in
 * the flights list, each flight will consist of an origin and a destination indicating the arc's direction.
 */
public class Airport {
    /**
     * The airport's name is represented by three uppercase letters.
     */
    private String name;
    /**
     * The airport's position consisting of a latitude and a longitude. For more info read the Coordinate class
     * implementation.
     */
    private Coordinate position;
    /**
     * The list of flights that part from (outbound flight) or return to (return flight) the airport. An outbound flight
     * will indicate an arc that parts from this airport, which will in turn work as the flight's tail. A return flight
     * will indicate an arc that arrives to this airport which will in turn work as the flight's head.
     */
    private List<Flight> flights;


    public Airport(String name, float latitude, float longitude) {
        this.name = name;
        position = new Coordinate(latitude, longitude);
        flights = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    /**
     * Adds an outbound or return flight to the flights list connecting this airport with another.
     * @param flight The outbound or return flight to be added to the flights list.
     */
    public void addFlight(Flight flight) {
        flights.add(flight);
    }
}
