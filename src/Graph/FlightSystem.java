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
            double price1 = 0, price2 = 0;

            if (o1 instanceof Flight && o2 instanceof Flight) {
                price1 = ((Flight)o1).getPrice();
                price2 = ((Flight)o2).getPrice();
            } else if (o1 instanceof PQAirport && o2 instanceof PQAirport) {
                price1 = ((PQAirport)o1).getPrice();
                price2 = ((PQAirport)o2).getPrice();
            } else {
                // Throw Exception porque no es la clase correcta
            }

            if (price1 > price2) {
                return 1;
            } else if (price1 < price2) {
                return -1;
            } else {
                return 0;
            }
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
        private Flight previousFlight; //flight del que vino al aeropuerto
        private PQAirport previousAirport;
        private double price;
        private Time time;


        public PQAirport(Airport airport, double price, Flight previousFlight, PQAirport previousAirport) {
            this.airport = airport;
            this.price = price;
            this.previousFlight = previousFlight;
            this.previousAirport = previousAirport;
        }

        public PQAirport(Airport airport, Time time, Flight previousFlight, PQAirport previousAirport) {
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

        public PQAirport minPath(Airport destination, List<String> days, Comparator<PQAirport> pqComparator,
                                 Comparator<Flight> flightComparator) {
            if (this.getAirport() == null || destination == null) {
                System.out.println("Origin or destination not specified.\n");
                return null;
            }

            clearMarks();

            if ((airport == destination) && !existsHamiltonianCycle()) {
                return null;
            }

            /**
             * Modified Djikstra that only queues flights which depart on one of the given days
             */

            PriorityQueue<PQAirport> priorityQueue = new PriorityQueue<>(pqComparator);
            List<Flight> originFlights = new ArrayList<>();
            originFlights.addAll(airport.getFlights());
            originFlights.sort(flightComparator);

            for (String day : days) {
                int i = 0;

                while (!originFlights.get(i).getFlightDays().contains(day)) {
                    i++;
                }

                if (i < originFlights.size()) {
                    if (pqComparator instanceof PriceComparator) {
                        priorityQueue.offer(new PQAirport(originFlights.get(i).getDestination(),
                                originFlights.get(i).getPrice(), originFlights.get(i), this));
                    } else if (pqComparator instanceof TimeComparator) {
                        priorityQueue.offer(new PQAirport(originFlights.get(i).getDestination(),
                                originFlights.get(i).getDepartureTime(), originFlights.get(i), this));
                    }
                }
            }

            Set<Airport> visited = new HashSet<>();

            //this.getAirport().visited = true;
            visited.add(airport);

            /**
             * Dijkstra's algorithm starting from the second step with price priority
             */

            while (!priorityQueue.isEmpty()) {
                PQAirport currentPQAirport = priorityQueue.poll();

                if (currentPQAirport.getAirport() == destination) {
                    if (visited.size() == airportList.size()) {
                        return currentPQAirport;
                    }
                } else if (!currentPQAirport.getAirport().visited) {
                    // currentPQAirport.getAirport().visited = true;
                    visited.add(currentPQAirport.getAirport());

                    for (Flight f : currentPQAirport.getAirport().getFlights()) {
                        Airport nextAirport = f.getDestination();

                        if (!visited.contains(nextAirport)) {
                            if (pqComparator instanceof PriceComparator) {
                                double priceSum = currentPQAirport.getPrice() + f.getPrice();

                                priorityQueue.offer(new PQAirport(nextAirport, priceSum, f, currentPQAirport));
                            } else if (pqComparator instanceof TimeComparator) {
                                Time timeSum = currentPQAirport.getTime().addTime(f.getDepartureTime());

                                priorityQueue.offer(new PQAirport(nextAirport, timeSum, f, currentPQAirport));
                            }
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
    }

    /**
     * Sets the itinerary according to the priority given. This method calls the algorithm that completes the transverse
     * of the graph.
     * The implementation is the same for pr and ft, the only thing that changes is the comparator
     */

    Itinerary setItinerary(Airport origin, Airport destination, List<String> days, String priority) {

        PQAirport originPQ = new PQAirport(origin, 0, null,  null);
        PQAirport node;

        switch (priority) {
            case "pr":
                node = originPQ.minPath(destination, days, new PriceComparator<>(), new PriceComparator<>());
                break;

            case "ft":
                node = originPQ.minPath(destination, days, new TimeComparator<>(), new TimeComparator<>());
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

    private void recursivePath(PQAirport node, ArrayList<ItineraryFlightInfo> flights){
        if(node == null || node.previousFlight == null)
            return;

        flights.add(new ItineraryFlightInfo(node.previousFlight));
        recursivePath(node.previousAirport, flights);
    }
}
