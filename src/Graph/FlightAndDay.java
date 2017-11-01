package Graph;

/**
 * Created by Bianca on 1/11/2017.
 */

/**
 * This class is used for keeping track of the flight chosen in each step of the journey and the day it departs
 * As many flights have several days they depart, the day set in this class is the "day chosen" by the itinerary
 * in which the flight will be taken.
 */

public class FlightAndDay {

    private Flight flight;
    private Day day;

    public FlightAndDay (Flight flight, Day day){
        this.flight = flight;
        this.day = day;
    }

    public Flight getFlight(){return flight;}

    public Day getDay(){return day;}

    public void setDay(Day day){this.day = day;}
}
