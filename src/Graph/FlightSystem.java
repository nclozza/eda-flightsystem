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

    private class PriceComparator implements Comparator<PQAirport> {
        @Override
        public int compare(PQAirport pqAirport1, PQAirport pqAirport2) {
            double price1 = pqAirport1.getPrice();
            double price2 = pqAirport2.getPrice();

            return (int) (price1 - price2);
        }
    }

    private class TimeComparator implements Comparator<PQAirport> {
        @Override
        public int compare(PQAirport pqAirport1, PQAirport pqAirport2) {
            double minutes1 = pqAirport1.getTime().getAllMinutes();
            double minutes2 = pqAirport2.getTime().getAllMinutes();

            return (int) (minutes1 - minutes2);
        }
    }

    private class TotalTimeComparator implements Comparator<PQAirport> {
        @Override
        public int compare(PQAirport pqAirport1, PQAirport pqAirport2) {
            double totalMinutes1 = pqAirport1.getTotalTime().getAllMinutes();
            double totalMinutes2 = pqAirport2.getTotalTime().getAllMinutes();

            return (int) (totalMinutes1 - totalMinutes2);
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

        Time getTotalTime() {
            return totalTime;
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

    public PQAirport minPath(Airport origin, Airport destination, List<String> days, Comparator<PQAirport> pqComparator) {

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
                        priorityQueue.offer(new PQAirport(eachFlight.getDestination(), eachFlight, currentPQAirport, eachDay));
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
                node = minPath(originAirport, destinationAirport, days, new PriceComparator());
                break;

            case "ft":
                node = minPath(originAirport, destinationAirport, days, new TimeComparator());
                break;

            case "tt":
                node = minPath(originAirport, destinationAirport, days, new TotalTimeComparator());
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

        List<String> days1 = new ArrayList<>();
        days1.add("Lu");

        List<String> days7 = new ArrayList<>();
        days7.add("Lu");
        days7.add("Ma");

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

        FlightSystem f = mainHandler.getFlightSystem();

        f.addAirport("BUE",0,1);
        f.addAirport("PAR",2,3);
        f.addAirport("LON",2,3);
        f.addAirport("MOS",2,3);

        f.addFlight("AA",1234, days1, "BUE", "PAR", departure, duration, 100);
        f.addFlight("AF",6786, days2, "PAR", "LON", departure, duration, 200);
        f.addFlight("BA",7896, days3, "LON", "MOS", departure, duration, 500);
        f.addFlight("AM",2324, days4, "BUE", "MOS", departure, duration2,600);
        f.addFlight("BB",2154, days1, "BUE", "LON", departure, duration2, 200);

        /**
         * Cuando la prioridad es "pr" entonces devuelve el vuelo directo entre BUE y MOS
         * Si la prioridad es "ft" entonces devuelve BUE - PAR - LON - MOS
         */
        Itinerary it = f.setItinerary("BUE", "LON", days1, "tt");

        System.out.println(it.getTotalPrice());
        System.out.println(it.getFlightTime());
        System.out.println(it.getTotalFlightTime());

        for (ItineraryFlightInfo flight : it.getFlights()) {
            System.out.println(flight.getFlight().getOrigin().getName());
            System.out.println(flight.getFlight().getDestination().getName());
        }
    }
}
