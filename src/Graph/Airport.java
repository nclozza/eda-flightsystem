package Graph;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;

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

    /**
     * Variable that marks a node if it was visited during a transversion of the graph
     */

    public boolean visited = false;


    public Airport(String name, double latitude, double longitude) {
        this.name = name;
        position = new Coordinate(latitude, longitude);
        flights = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    /**
     * Adds an outbound or return flight to the flights list connecting this airport with another. In terms of graph
     * theory it creates a new arc between this airport and another.
     * @param flight The outbound or return flight to be added to the flights list.
     */
    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    //public boolean deleteFlight(){}

    public void deleteAllFlights(){flights.clear();}

    /**
     * Two airports are said to be equal if their name is equal.
     * @param obj The object, preferably an airport, to be compared against this airport.
     * @return True if both airports have the same name, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != Airport.class) {
            return false;
        }

        return this.name.equals(((Airport)obj).getName());
    }

    /**
     * The airport's hashcode will be represented by its name's hashcode.
     * @return The airport's name's hashcode.
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Coordinate getPosition() {
        return position;
    }

    public List<Flight> getFlights(){
        return flights;
    }


}
