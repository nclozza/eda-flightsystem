package Graph;

import java.util.*;

/**
 * The FlightSystem class is a class that represents de Graph class in a standard graph structure.
 * It contains a list of all the "nodes" (airportList) and is in charge of adding new ones.
 * It also contains the methods that iterate over the graph in different ways.
 */

public class FlightSystem {
    private List<Airport> airportList;
    private HashMap<String, Airport> airportHashMap;

    FlightSystem(){
        airportList = new LinkedList<>();
        airportHashMap = new HashMap<>();
    }

    void addAirport(String name, double latitude, double longitude) {
        Airport a = new Airport(name, latitude, longitude);
        airportList.add(a);
        airportHashMap.put(name, a);
    }

    public void addAirport(Airport airport) {
        airportList.add(airport);
        airportHashMap.put(airport.getName(), airport);
    }

    boolean deleteAirport(String airportName) {
        if (airportHashMap.containsKey(airportName)) {
            airportHashMap.remove(airportName);

            for (Airport a : airportList) {
                if (a.getName().equals(airportName)) {
                    airportList.remove(a);
                    return true;
                }
            }
        }

        return false;
    }

    void deleteAllAirports() {
        airportList.clear();
        airportHashMap.clear();
    }

    void deleteAllFlights(){
        for (Airport a : airportList){
            a.deleteAllFlights();
        }
    }

    Airport getAirport(String airportName) {
        return airportHashMap.get(airportName);
    }

    public List<Airport> getAirportList() {
        return airportList;
    }

    public void addFlight(Flight flight) {
        if (!(airportHashMap.containsKey(flight.getOrigin().getName())
                && airportHashMap.containsKey(flight.getDestination().getName()))) {
            throw new IllegalArgumentException("Wrong airports for that flight");
        }

        airportHashMap.get(flight.getOrigin().getName()).addFlight(flight);
    }

    void addFlight(String airline, int flightNr, List<String> flightDays, String origin, String destination,
                   Time departureTime, Time duration, double price) {
        if (!(airportHashMap.containsKey(origin) && airportHashMap.containsKey(destination))) {
            throw new IllegalArgumentException("Wrong airports for that flight");
        }

        Flight flight = new Flight(airline, flightNr, flightDays, airportHashMap.get(origin),
                                    airportHashMap.get(destination), departureTime, duration, price);

        airportHashMap.get(origin).addFlight(flight);
    }

    private void clearMarks() {
        for (Airport a : airportList) {
            a.visited = false;
        }
    }

    private class priceComparator implements Comparator<priorityQueueAirport> {
        @Override
        public int compare(priorityQueueAirport o1, priorityQueueAirport o2) {
            if (o1.getPrice() > o2.getPrice())
                return 1;
            else if (o1.getPrice() < o2.getPrice())
                return -1;
            else
                return 0;
        }
    }

    private class timeComparator implements Comparator<priorityQueueAirport> {
        @Override
        public int compare(priorityQueueAirport o1, priorityQueueAirport o2) {
            if (o1.getTime().getAllMinutes() > o2.getTime().getAllMinutes())
                return 1;
            else if (o1.getTime().getAllMinutes() < o2.getTime().getAllMinutes())
                return -1;
            else
                return 0;
        }
    }

    private class flightComparator implements Comparator<Flight> {
        @Override
        public int compare(Flight f1, Flight f2) {
            if (f1.getPrice() > f2.getPrice())
                return 1;
            else if (f1.getPrice() < f2.getPrice())
                return -1;
            else
                return 0;
        }
    }

    private class priorityQueueAirport {
        private Airport airport;
        private Flight previousFlight; //flight del que vino al aeropuerto
        private priorityQueueAirport previousAirport;
        private double price;
        private Time time;


        priorityQueueAirport(Airport airport, double price, Flight previousFlight, priorityQueueAirport previousAirport) {
            this.airport = airport;
            this.price = price;
            this.previousFlight = previousFlight;
            this.previousAirport = previousAirport;
        }

        public priorityQueueAirport(Airport airport, Time time, Flight previousFlight, priorityQueueAirport previousAirport) {
            this.airport = airport;
            this.time = time;
            this.previousFlight = previousFlight;
            this.previousAirport = previousAirport;
        }

        double getPrice() {
            return price;
        }

        Time getTime() {
            return time;
        }

        Airport getAirport() {
            return airport;
        }

        priorityQueueAirport minPath(Airport destination, List<String> days, Comparator<priorityQueueAirport> comparator) {
            if (this.getAirport() == null || destination == null) {
                System.out.println("Origin or destination not specified");
                return null;
            }

            clearMarks();
            PriorityQueue<priorityQueueAirport> priorityQueue = new PriorityQueue<>(comparator);
            priorityQueueAirport aux = null;

            /*
             * Modified Djikstra that only queues those flights which depart on one of the given days
             */

            List<Flight> originFlights = new ArrayList<>();
            originFlights.addAll(this.getAirport().getFlights());
            originFlights.sort(new flightComparator());

            int i;
            for (String day : days) {
                i = 0;
                while (!originFlights.get(i).getFlightDays().contains(day)) {
                    i++;
                }
                if (i != originFlights.size()) {
                    priorityQueue.offer(new priorityQueueAirport(originFlights.get(i).getDestination(),
                            originFlights.get(i).getPrice(), originFlights.get(i), this));
                }

            }
            this.getAirport().visited = true;

            /*
             * Dijkstra's algorithm starting from the second step with price priority
             */

            while (!priorityQueue.isEmpty()) {
                aux = priorityQueue.poll();

                if (aux.getAirport() == destination)
                    return aux;

                if (!aux.getAirport().visited) {
                    aux.getAirport().visited = true;

                    for (Flight f : aux.getAirport().getFlights()) {
                        if (!f.getDestination().visited)
                            priorityQueue.offer(new priorityQueueAirport(f.getDestination(),
                                    aux.getPrice() + f.getPrice(), f, aux));
                    }
                }
            }
            return aux;
        }
    }


    /**
     * Sets the itinerary according to the priority given. This method calls the algorithm that completes the transverse
     * of the graph.
     * The implementation is the same for pr and ft, the only thing that changes is the comparator
     */

    Itinerary setItinerary(Airport origin, Airport destination, List<String> days, String priority) {

        priorityQueueAirport originPQ = new priorityQueueAirport(origin, 0, null,  null);
        priorityQueueAirport node;

        switch (priority) {
            case "pr":
                node = originPQ.minPath(destination, days, new priceComparator());
                break;

            case "ft":
                node = originPQ.minPath(destination, days, new timeComparator());
                break;

            default:
                return null;
        }

        double price = node.getPrice();

        Time cummulativeFlightTime = new Time(0,0); //time without waiting between planes
        Time cummulativeTotalFlightTime = new Time(0,0); //time with wait between planes

        ArrayList<ItineraryFlightInfo> flights = new ArrayList<>();
        recursivePath(node, flights); //metemos la lista de flights en flights
        Collections.reverse(flights); //queremos la lista desde origin hasta destination y no al reves


        int i = 0;
        while(!flights.get(0).getFlight().getFlightDays().contains(days.get(i)))
            i++;
        //elijo arbitrariamente el primer dia de despegue para el primer vuelo

        flights.get(0).setDepartureDay(new Day(days.get(i)));
        cummulativeFlightTime.setMinutes(cummulativeFlightTime.getAllMinutes() +
                flights.get(0).getFlight().getDuration().getAllMinutes());
        //adds the duration of the first flight to the cummulative flight time

        cummulativeTotalFlightTime.setMinutes(cummulativeTotalFlightTime.getAllMinutes() +
                flights.get(0).getFlight().getDuration().getAllMinutes());
        //adds the duration of the first flight to the cummulative total flight time


        for(int j=1; j < flights.size(); j++){
            //adds the duration of the flight to the cummulative flight time
           cummulativeFlightTime.setMinutes(flights.get(j).getFlight().getDuration().getAllMinutes() +
                   cummulativeFlightTime.getAllMinutes());

            //ARBITRARILY choose the day the flight will depart
            flights.get(j).setDepartureDay(new Day (flights.get(j).getFlight().getFlightDays().get(0)));
            //Day and time the user arrived at the airport from the last flight
            Day arrivalDay = flights.get(j-1).getArrivalDay();
            Time arrivalTime = flights.get(j-1).getArrivalTime();

            double waitingMinutes = (flights.get(j).getDepartureDay().getDayNumber()* ItineraryFlightInfo.HOURS* ItineraryFlightInfo.MINUTES
                    + flights.get(j).getDepartureTime().getAllMinutes()) -
                    ((arrivalDay.getDayNumber()* ItineraryFlightInfo.HOURS* ItineraryFlightInfo.MINUTES + arrivalTime.getAllMinutes()));

            if (waitingMinutes < 0 ){
                waitingMinutes += ItineraryFlightInfo.WEEKDAYS* ItineraryFlightInfo.HOURS* ItineraryFlightInfo.MINUTES;
            }

            cummulativeTotalFlightTime.setMinutes(flights.get(j).getFlight().getDuration().getAllMinutes() +
                    waitingMinutes);
           }

        return new Itinerary(price, cummulativeTotalFlightTime, cummulativeFlightTime, flights);
    }

    private void recursivePath(priorityQueueAirport node, ArrayList<ItineraryFlightInfo> flights){
        if(node == null || node.previousFlight == null)
            return;

        flights.add(new ItineraryFlightInfo(node.previousFlight));
        recursivePath(node.previousAirport, flights);
    }
}
