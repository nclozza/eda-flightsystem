package Graph;

import java.sql.Time;
import java.util.List;

/**
 * The Flight class is an implementation of a flight consisting of the airplane's airline, the flight's number, a list
 * of the days on which the flight departs, the departure airport, the arrival airport, the time of departure, the
 * flight's duration and its pricing. It is necessary to keep in mind that there is no airplane implementation and if
 * there is any mention of such it is only to help the reader understand the context.
 * The Flight class will be used as an arc to connect two airports in a directed graph. A flight's 'weight' will be
 * given by either its price or its duration.
 */
public class Flight {
    /**
     * The name of the airplane's airline.
     */
    private String airline;
    /**
     * The flight number is a flight's ID under the same airline. There can exist two flights with the same flight
     * number from different airlines, but there cannot be any two flights with the same flight number under the same
     * airline.
     */
    private int flightNr;
    /**
     * The list of days on which the flight departs. The days will be represented by two character strings as such:
     * "Lu" (lunes), "Ma" (martes), "Mi" (miercoles), "Ju" (jueves), "Vi" (viernes), "Sa" (sabado) and "Do" (domingo).
     */
    private List<String> flightDays;
    /**
     * The origin or departure airport is the the flight's tail in the directed graph.
     */
    private Airport origin;
    /**
     * The destination or arrival airport is the flight's head in the directed graph.
     */
    private Airport destination;
    /**
     * The time at which the flight departs given in 24hs hour:minutes format (HH:MM).
     */
    private Time departureTime;
    /**
     * The duration of the flight given in the following format [hours]h[minutes]m.
     */
    private Time duration;
    /**
     * The flight's price is a real number equal or bigger than 0 with at most two decimals.
     */
    private double price;


    public Flight(String airline, int flightNr, List<String> flightDays, Airport origin, Airport destination,
                  Time departureTime, Time duration, double price) {
        this.airline = airline;
        this.flightNr = flightNr;
        this.flightDays = flightDays;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.duration = duration;
        // The product and division by 10^2 is made to round up to two decimal places.
        this.price = price * 100 / 100;
    }

    
}
