package Graph;

import java.util.List;

/**
 * The Airport class is an implementation of an airport simulation consisting of the airport's name, its location and a
 * list of the airport's outbound and return flights.
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
     * The list of flights that part from (outbound flight) or return to (return flight) the airport.
     */
    private List<Flight> flights;


    public Airport(String name, float latitude, float longitude) {
        this.name = name;
        position = new Coordinate(latitude, longitude);
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
