package Graph;

/**
 * This class is used for keeping track of the flight chosen in each step of the journey and the day it departs
 * As many flights have several days they depart, the day set in this class is the "day chosen" by the itinerary
 * in which the flight will be taken.
 */

public class ItineraryFlightInfo {

    private Flight flight;
    private Day departureDay;
    private Time departureTime;
    private Day arrivalDay;
    private Time arrivalTime;

    final static int HOURS = 24;
    final static int MINUTES = 60;
    final static int WEEKDAYS = 7;

    public ItineraryFlightInfo(Flight flight, Day day){
        this.flight = flight;
        this.departureDay = day;
        this.departureTime = flight.getDepartureTime();
        this.arrivalDay = calculateArrivalDay(this);
        this.arrivalTime = calculateArrivalTime(this);
    }

    public ItineraryFlightInfo(Flight flight){
        this.flight = flight;
        this.departureDay = null;
        this.departureTime = flight.getDepartureTime();
        this.arrivalTime = null;
        this.arrivalDay = null;
    }

    private Day calculateArrivalDay(ItineraryFlightInfo flight){
        double arrivalDayAndTime = departureDay.getDayNumber()*HOURS*MINUTES + departureTime.getAllMinutes() +
                flight.getFlight().getDuration().getAllMinutes();
        return new Day((int)arrivalDayAndTime/HOURS/MINUTES);
    }

    private Time calculateArrivalTime(ItineraryFlightInfo flight){
        double arrivalDayAndTime = departureDay.getDayNumber()*HOURS*MINUTES + departureTime.getAllMinutes() +
                flight.getFlight().getDuration().getAllMinutes();
        int arrivalHour = (int) (arrivalDayAndTime/MINUTES) - arrivalDay.getDayNumber()*HOURS;
        int arrivalMinutes = (int) (arrivalDayAndTime%MINUTES);
        return new Time(arrivalHour, arrivalMinutes);
    }

    Flight getFlight(){return flight;}

    Day getDepartureDay(){return departureDay;}

    Day getArrivalDay(){return arrivalDay;}

    Time getDepartureTime(){return departureTime;}

    Time getArrivalTime(){return arrivalTime;}

    void setDepartureDay(Day day){
        this.departureDay = day;
        this.departureTime = flight.getDepartureTime();
        this.arrivalDay = calculateArrivalDay(this);
        this.arrivalTime = calculateArrivalTime(this);
    }
}
