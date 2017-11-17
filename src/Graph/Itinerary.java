package Graph;

import java.util.ArrayList;
import java.util.List;

public class Itinerary {

    private double totalPrice;
    private Time totalFlightTime;
    private Time flightTime;
    private List<ItineraryFlightInfo> flights;
    private int requestRate = 1;

    public Itinerary(double price, Time totalFlightTime, Time flightTime, List<ItineraryFlightInfo> flights) {
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

    double getTotalPrice() {
        return totalPrice;
    }

    public Time getFlightTime() {
        return flightTime;
    }

    public Time getTotalFlightTime() {
        return totalFlightTime;
    }

    public List<ItineraryFlightInfo> getFlights() {
        return flights;
    }

    public void updateRequestRate() {
        requestRate++;
    }

    public int getRequestRate() {
        return requestRate;
    }

    @Override
    public String toString() {
        String ret = "From - To - Day - Departure - Duration - Price\n";
        for (ItineraryFlightInfo each : flights) {
            ret += each.getFlight().getOrigin().getName() + " - "
                + each.getFlight().getDestination().getName() + " - "
                + each.getDepartureDay().getDayName() + " - "
                + each.getDepartureTime() + " - "
                + each.getFlight().getDuration() + " - "
                + each.getFlight().getPrice() + "\n";
        }
        ret += "\n";
        ret += "Total Price: " + totalPrice + "\n";
        ret += "Flight Time: " + flightTime + "\n";
        ret += "Total Flight Time: " + totalFlightTime + "\n";

        return ret;
    }
}
