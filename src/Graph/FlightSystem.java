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
    private HashMap<String, Flight> flightHashMap;
    /**
     * This set will contain the top 10 most requested flights but will accept a total amount of 25 paths. After the set
     * is full it will delete the 5 least requested flights to make space for new flights to gain "popularity", leaving
     * 10 other flights remaining along with the top 10 so that they may take the top spots.
     */
    private TreeSet<Path> requestedPaths;

    final static int TOP_REQUESTED = 10;
    final static int MAX_REQUESTED = 25;
    final static int LEAST_REQUESTED = 5;

    FlightSystem() {
        airportList = new LinkedList<>();
        airportHashMap = new HashMap<>();
        flightHashMap = new HashMap<>();
        requestedPaths = new TreeSet<>(new PathComparator());
    }

    void addAirport(String name, double latitude, double longitude) {
        Airport a = new Airport(name, latitude, longitude);
        airportList.add(a);
        airportHashMap.put(name, a);

        requestedPaths.clear(); // The top 10 paths list does not make sense anymore if the "graph" changed
    }

    boolean deleteAirport(String airportName) {
        if (airportHashMap.containsKey(airportName)) {
            airportHashMap.remove(airportName);

            for (Airport a : airportList) {
                if (a.getName().equals(airportName)) {
                    airportList.remove(a);

                    requestedPaths.clear(); // The top 10 paths list does not make sense anymore if the "graph" changed

                    return true;
                }
            }
        }

        return false;
    }

    void deleteAllAirports() {
        airportList.clear();
        airportHashMap.clear();
        requestedPaths.clear(); // The top 10 paths list does not make sense anymore if the "graph" changed
    }

    void deleteFlight(String airline, Integer flightNr) {
        Flight flight = flightHashMap.get(airline + flightNr);
        if (flight == null) {
            System.out.println("Wrong flight");

        } else {
            for (Flight eachFlight : flight.getOrigin().getFlights()) {
                if (eachFlight.getAirline().equals(airline) && eachFlight.getFlightNr() == flightNr) {
                    flight.getOrigin().getFlights().remove(eachFlight);
                    break;
                }
            }

            flightHashMap.remove(airline + flightNr);
        }
    }

    boolean containsFlight(String airline, Integer flightNr) {
        return flightHashMap.containsKey(airline + flightNr);
    }

    void deleteAllFlights(){
        for (Airport a : airportList){
            a.deleteAllFlights();
        }

        flightHashMap.clear();
        requestedPaths.clear(); // The top 10 paths list does not make sense anymore if the "graph" changed
    }

    Airport getAirport(String airportName) {
        return airportHashMap.get(airportName);
    }

    public List<Airport> getAirportList() {
        return airportList;
    }

    void addFlight(String airline, int flightNr, List<String> flightDays, String origin, String destination,
                   int departureTime, int duration, double price) throws Exception {
        if (!(airportHashMap.containsKey(origin) && airportHashMap.containsKey(destination))) {
            throw new Exception();
        }

        Flight flight = new Flight(airline, flightNr, flightDays, airportHashMap.get(origin),
                                    airportHashMap.get(destination), new Time(departureTime), new Time(duration), price);

        airportHashMap.get(origin).addFlight(flight);
        flightHashMap.put(airline + flightNr, flight);

        requestedPaths.clear(); // The top 10 paths list does not make sense anymore if the "graph" changed
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

        PQAirport(Airport airport, Flight flight, String day) {
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


        PQAirport(Airport airport, Flight previousFlight, PQAirport previousPQAirport, String day) {
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

        Airport getAirport() {
            return airport;
        }

        List<ItineraryFlightInfo> getItineraryFlightInfoList() {
            return itineraryFlightInfoList;
        }

        private int calculateWaitingTime(ItineraryFlightInfo itineraryFlightInfo, Day day, Flight flight) {
            int daysBetween = 0;
            int totalMinutesInOneDay = 24 * 60;

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

    private class Path {
        PQAirport pqOrigin;
        Airport origin, destination;
        Comparator<PQAirport> criteria;
        String departureDay;
        int requestFrecuency = 0;

        public Path(PQAirport pqOrigin, Comparator<PQAirport> criteria, String departureDay) {
            this.pqOrigin = pqOrigin;
            origin = pqOrigin.getAirport();
            List<ItineraryFlightInfo> flights = pqOrigin.getItineraryFlightInfoList();
            destination = flights.get(flights.size() - 1).getFlight().getDestination();
            this.criteria = criteria;
            this.departureDay = departureDay;
        }

        Airport getOrigin() {
            return origin;
        }

        Airport getDestination() {
            return destination;
        }

        Comparator<PQAirport> getCriteria() {
            return criteria;
        }

        String getDepartureDay() {
            return departureDay;
        }

        int getRequestFrecuency() {
            return requestFrecuency;
        }

        void updateRequestFrecuency() {
            requestFrecuency++;
        }

        PQAirport getPQAirport() {
            return pqOrigin;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != Path.class) {
                return false;
            }

            boolean sameOrigin = ((Path)obj).getOrigin().equals(this.getOrigin());
            boolean sameDestination = ((Path)obj).getDestination().equals(this.getDestination());
            boolean bothPriceCriteria = (((Path)obj).getCriteria() instanceof PriceComparator) &&
                    (criteria instanceof PriceComparator);
            boolean bothTimeCriteria = (((Path)obj).getCriteria() instanceof TimeComparator) &&
                    (criteria instanceof TimeComparator);
            boolean bothTotalTimeCriteria = (((Path)obj).getCriteria() instanceof TotalTimeComparator) &&
                    (criteria instanceof TotalTimeComparator);
            boolean sameCriteria = bothPriceCriteria || bothTimeCriteria || bothTotalTimeCriteria;
            boolean sameDepartureDay = ((Path)obj).getDepartureDay().equals(departureDay);


            return sameOrigin && sameDestination && sameCriteria && sameDepartureDay;
        }

        @Override
        public int hashCode() {
            return (origin.hashCode() * 13) + (destination.hashCode() * 17) + (departureDay.hashCode() * 37);
        }
    }

    private class PathComparator implements Comparator<Path> {
        @Override
        public int compare(Path o1, Path o2) {
            return o1.getRequestFrecuency() - o2.getRequestFrecuency();
        }
    }

    private PQAirport minPath(Airport origin, Airport destination, List<String> days,
                              Comparator<PQAirport> pqComparator) {

        clearMarks();

        boolean worldTrip = origin.equals(destination);

        /*
        if (worldTrip && !existsHamiltonianCycle()) {
            return null;
        }
        */

        /**
         * Modified Djikstra that only queues flights which depart on one of the given days
         */

        PriorityQueue<PQAirport> priorityQueue = new PriorityQueue<>(pqComparator);

        for (String eachDay : days) {
            for (Flight eachFlight : origin.getFlights()) {
                for (String eachFlightDay : eachFlight.getFlightDays()) {
                    if (eachDay.equals(eachFlightDay)) {
                        PQAirport pqAirport = new PQAirport(eachFlight.getDestination(), eachFlight, eachFlightDay);

                        if (worldTrip) {
                            pqAirport.visitedAirports.remove(origin);
                        }

                        PQAirport frequentPath = checkFrequentPaths(pqAirport, pqComparator);

                        if (frequentPath != null) {
                            return frequentPath;
                        }

                        priorityQueue.offer(pqAirport);
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
                        updateFrequentPaths(currentPQAirport, pqComparator);

                        return currentPQAirport;
                    }
                } else {
                    updateFrequentPaths(currentPQAirport, pqComparator);

                    return currentPQAirport;
                }
            }

            for (Flight eachFlight : currentPQAirport.airport.getFlights()) {
                if (!currentPQAirport.visitedAirports.contains(eachFlight.getDestination())) {
                    for (String eachDay : eachFlight.getFlightDays()) {
                        priorityQueue.offer(new PQAirport(eachFlight.getDestination(), eachFlight, currentPQAirport,
                                eachDay));
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

    private PQAirport checkFrequentPaths(PQAirport currentPQAirport, Comparator<PQAirport> pqComparator) {
        String departureDay = currentPQAirport.getItineraryFlightInfoList().get(0).getDepartureDay()
                .getDayName();
        Path thisPath = new Path(currentPQAirport, pqComparator, departureDay);

        if (requestedPaths.contains(thisPath)) {
            requestedPaths.remove(thisPath);
            thisPath.updateRequestFrecuency();
            requestedPaths.add(thisPath);

            return currentPQAirport;
        } else {
            return null;
        }
    }

    private void updateFrequentPaths(PQAirport currentPQAirport, Comparator<PQAirport> pqComparator) {
        String departureDay = currentPQAirport.getItineraryFlightInfoList().get(0).getDepartureDay()
                .getDayName();
        Path thisPath = new Path(currentPQAirport, pqComparator, departureDay);

        if (requestedPaths.size() > MAX_REQUESTED) {
            while (requestedPaths.size() > (MAX_REQUESTED - LEAST_REQUESTED)) {
                requestedPaths.pollLast();
            }
        }

        requestedPaths.add(thisPath);
    }

    /**
     * Sets the itinerary according to the priority given. This method calls the algorithm that completes the transverse
     * of the graph.
     * The implementation is the same for pr and ft, the only thing that changes is the comparator
     */

    Itinerary setItinerary(String origin, String destination, List<String> days, String priority) throws Exception {
        if (!(airportHashMap.containsKey(origin) && airportHashMap.containsKey(destination))) {
            throw new Exception();
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

        return node == null ? null : new Itinerary(node.price, node.totalTime, node.flightTime, node.itineraryFlightInfoList);
    }
}
