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
            throw new IllegalArgumentException("Wrong airports for that flight.\n");
        }

        airportHashMap.get(flight.getOrigin().getName()).addFlight(flight);
    }

    void addFlight(String airline, int flightNr, List<String> flightDays, String origin, String destination,
                   Time departureTime, Time duration, double price) {
        if (!(airportHashMap.containsKey(origin) && airportHashMap.containsKey(destination))) {
            throw new IllegalArgumentException("Wrong airports for that flight.\n");
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

    private class PriceComparator<T> implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            double price1, price2;

            if (o1 instanceof Flight && o2 instanceof Flight) {
                price1 = ((Flight)o1).getPrice();
                price2 = ((Flight)o2).getPrice();

            } else if (o1 instanceof PQAirport && o2 instanceof PQAirport) {
                price1 = ((PQAirport)o1).getPrice();
                price2 = ((PQAirport)o2).getPrice();

            } else {
                throw new IllegalArgumentException("Wrong classes");
            }

            return (int) (price1 - price2);
        }
    }

    private class TimeComparator<T> implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            double minutes1 = 0, minutes2 = 0;

            if (o1 instanceof Flight && o2 instanceof Flight) {
                minutes1 = ((Flight)o1).getDepartureTime().getAllMinutes();
                minutes2 = ((Flight)o2).getDepartureTime().getAllMinutes();
            } else if (o1 instanceof PQAirport && o2 instanceof PQAirport) {
                minutes1 = ((PQAirport)o1).getTime().getAllMinutes();
                minutes2 = ((PQAirport)o2).getTime().getAllMinutes();
            } else {
                // Throw Exception porque no es la clase correcta
            }

            if (minutes1 > minutes2) {
                return 1;
            } else if (minutes1 < minutes2) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private class PQAirport {
        private Airport airport;
        private double price;
        private Time flightTime;
        private Time totalTime;
        private LinkedList<ItineraryFlightInfo> itineraryFlightInfoList;
        private HashSet<Airport> visitedAirports;

        public PQAirport(Airport airport, Flight flight, String day) {
            this.airport = airport;
            price = flight.getPrice();
            flightTime = new Time(flight.getDuration().getAllMinutes());
            totalTime = new Time(flight.getDuration().getAllMinutes());
            itineraryFlightInfoList = new LinkedList<>();
            itineraryFlightInfoList.add(new ItineraryFlightInfo(flight, new Day(day)));
            visitedAirports = new HashSet<>();
            visitedAirports.add(airport);
            visitedAirports.add(flight.getOrigin());
        }


        public PQAirport(Airport airport, Flight previousFlight, PQAirport previousPQAirport, String day) {
            this.airport = airport;
            price = previousFlight.getPrice() + previousPQAirport.price;
            flightTime = new Time(previousPQAirport.flightTime.getAllMinutes() + previousFlight.getDuration().getAllMinutes());
            totalTime = new Time(previousPQAirport.totalTime.getAllMinutes() + previousFlight.getDuration().getAllMinutes() +
                                calculateWaitingTime(previousPQAirport.itineraryFlightInfoList.getLast(), new Day(day), previousFlight));
            itineraryFlightInfoList = (LinkedList<ItineraryFlightInfo>) previousPQAirport.itineraryFlightInfoList.clone();
            itineraryFlightInfoList.add(new ItineraryFlightInfo(previousFlight, new Day(day)));
            visitedAirports = (HashSet<Airport>) previousPQAirport.visitedAirports.clone();
            visitedAirports.add(airport);
        }

        double getPrice() {
            return price;
        }

        Time getTime() {
            return flightTime;
        }

        private double calculateWaitingTime(ItineraryFlightInfo itineraryFlightInfo, Day day, Flight flight) {
            int daysBetween = 0;
            double totalMinutesInOneDay = 24 * 60;

            if (itineraryFlightInfo.getArrivalDay().getDayNumber() < day.getDayNumber()) {
                daysBetween = day.getDayNumber() - itineraryFlightInfo.getArrivalDay().getDayNumber();
            } else if (itineraryFlightInfo.getArrivalDay().getDayNumber() > day.getDayNumber()) {
                daysBetween = 7 - itineraryFlightInfo.getArrivalDay().getDayNumber() + day.getDayNumber();
            } else if (itineraryFlightInfo.getArrivalTime().getAllMinutes() > flight.getDepartureTime().getAllMinutes()) {
                daysBetween = 7;
            }

            return daysBetween * totalMinutesInOneDay +
                (totalMinutesInOneDay - itineraryFlightInfo.getArrivalTime().getAllMinutes()) +
                flight.getDepartureTime().getAllMinutes();
        }
    }

    public PQAirport minPath(Airport origin, Airport destination, List<String> days, Comparator<PQAirport> pqComparator,
                             Comparator<Flight> flightComparator) {

        clearMarks();

        boolean worldTrip = origin.equals(destination);

        if (worldTrip && !existsHamiltonianCycle()) {
            return null;
        }

        /**
         * Modified Djikstra that only queues flights which depart on one of the given days
         */

        PriorityQueue<PQAirport> priorityQueue = new PriorityQueue<>(pqComparator);

        for (String eachDay : days) {
            for (Flight eachFlight : origin.getFlights()) {
                for (String eachFlightDay : eachFlight.getFlightDays()) {
                    if (eachDay.equals(eachFlightDay)) {
                        priorityQueue.offer(new PQAirport(eachFlight.getDestination(), eachFlight, eachFlightDay));
                    }

                }
            }
        }

        /**
         * Dijkstra's algorithm starting from the second step with price priority
         */

        PQAirport currentPQAirport;

        while (!priorityQueue.isEmpty()) {
            currentPQAirport = priorityQueue.poll();

            if (currentPQAirport.airport.equals(destination)) {
                if (worldTrip) {
                    if (currentPQAirport.visitedAirports.size() == airportList.size()) {
                        return currentPQAirport;
                    }
                } else {
                    return currentPQAirport;
                }
            }

            for (Flight eachFlight : currentPQAirport.airport.getFlights()) {
                if (!currentPQAirport.visitedAirports.contains(eachFlight.getDestination())) {
                    for (String eachDay : eachFlight.getFlightDays()) {
                        priorityQueue.offer(new PQAirport(eachFlight.getDestination(), eachFlight,
                            currentPQAirport, eachDay)); //Checkear
                    }
                }
            }

        }

        // Our queue has been emptied and we haven't found our minimum valued path.
        return null;
    }

    private boolean existsHamiltonianCycle() {
        int minCycleVertices = 3;   // Minimum amount of vertices needed for a possible cycle existence in the graph.

        if (airportList.size() >= minCycleVertices) {
            for (Airport ap : airportList) {
                if (ap.getFlights().size() < (airportList.size() / 2)) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * Sets the itinerary according to the priority given. This method calls the algorithm that completes the transverse
     * of the graph.
     * The implementation is the same for pr and ft, the only thing that changes is the comparator
     */

    Itinerary setItinerary(String origin, String destination, List<String> days, String priority) {
        if (!(airportHashMap.containsKey(origin) && airportHashMap.containsKey(destination))) {
            throw new IllegalArgumentException("Wrong Airports");
        }

        Airport originAirport = airportHashMap.get(origin);
        Airport destinationAirport = airportHashMap.get(destination);

        PQAirport node;
        switch (priority) {
            case "pr":
                node = minPath(originAirport, destinationAirport, days, new PriceComparator<>(), new PriceComparator<>());
                break;

            case "ft":
                node = minPath(originAirport, destinationAirport, days, new TimeComparator<>(), new TimeComparator<>());
                break;

            default:
                return null;
        }

        return new Itinerary(node.price, node.totalTime, node.flightTime, node.itineraryFlightInfoList);
    }

    public static void main(String[] args) {
        MainHandler mainHandler = new MainHandler();

        Time departure = new Time(0,100);
        Time duration = new Time(0,200);

        Time duration2 = new Time(700);

        Airport bue = new Airport("BUE",0,1);
        Airport par = new Airport("PAR",2,3);
        Airport lon = new Airport("LON",2,3);
        Airport mos = new Airport("MOS",2,3);

        List<String> days1 = new ArrayList<>();
        days1.add("Lu");

        List<String> days2 = new ArrayList<>();
        days2.add("Mi");

        List<String> days3 = new ArrayList<>();
        days3.add("Mi");

        List<String> days4 = new ArrayList<>();
        days4.add("Ju");

        List<String> days5 = new ArrayList<>();
        days5.add("Vi");

        List<String> days6 = new ArrayList<>();
        days6.add("Lu");
        days6.add("Ju");

        Flight flight1 = new Flight("AA",1234, days1, bue, par, departure, duration, 100);
        Flight flight2 = new Flight("AF",6786, days2, par, lon, departure, duration, 200);
        Flight flight3 = new Flight("BA",7896, days3, lon, mos, departure, duration, 500);
        Flight flight4 = new Flight("AM",2324, days4, bue, mos, departure, duration2, 600);


        FlightSystem f = mainHandler.getFlightSystem();

        f.addAirport(bue);
        f.addAirport(par);
        f.addAirport(lon);
        f.addAirport(mos);

        f.getAirportList().get(0).addFlight(flight1);
        f.getAirportList().get(1).addFlight(flight2);
        f.getAirportList().get(2).addFlight(flight3);
        f.getAirportList().get(0).addFlight(flight4);

        /**
         * Cuando la prioridad es "pr" entonces devuelve el vuelo directo entre BUE y MOS
         * Si la prioridad es "ft" entonces devuelve BUE - PAR - LON - MOS
         */
        Itinerary it = f.setItinerary("BUE", "MOS", days6, "ft");

        System.out.println(it.getTotalPrice());
        System.out.println(it.getFlightTime());
        System.out.println(it.getTotalFlightTime().getAllMinutes());

        for (ItineraryFlightInfo flight : it.getFlights()) {
            System.out.println(flight.getFlight().getOrigin().getName());
            System.out.println(flight.getFlight().getDestination().getName());
        }
    }
}
