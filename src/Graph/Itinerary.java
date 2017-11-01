package Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bianca on 31/10/2017.
 */
public class Itinerary {

    private double totalPrice;

    private Time totalFlightTime;

    private Time flightTime;

    private List<FlightAndDay> flights;


    public Itinerary(double price, Time totalFlightTime, Time flightTime, List<FlightAndDay> flights){
        this.totalPrice = price;
        this.totalFlightTime = totalFlightTime;
        this.flightTime = flightTime;
        this.flights = flights;
    }

    public Itinerary(){
        this.totalPrice = 0;
        this.totalFlightTime = null;
        this.flights = new ArrayList<>();
        this.flightTime = null;
    }

}
