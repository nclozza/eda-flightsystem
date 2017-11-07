package Graph;

import java.util.*;

/**
 * The FlightSystem class is a class that represents de Graph class in a standard graph structure.
 * It contains a list of all the "nodes" (airports) and is in charge of adding new ones.
 * It also contains the methods that iterate over the graph in different ways.
 */

public class FlightSystem {
    private List<Airport> airports;

    public FlightSystem(){
        airports = new LinkedList<>();
    }

    public void addAirport(String name, float latitude, float longitude) {
        Airport a = new Airport(name, latitude, longitude);
        airports.add(a);
    }

    public void addAirport(Airport airport){
        airports.add(airport);
    }

    public boolean deleteAirport(String airportName){
        for (Airport a : airports){
            if (a.getName().equals(airportName)) {
                airports.remove(a);
                return true;
            }
        }
        return false;
    }

    public void deleteAllAirports(){airports.clear();}

    public void deleteAllFlights(){
        for (Airport a : airports){
            a.deleteAllFlights();
        }
    }

    public Airport getAirport(String airport) {
        for (Airport eachAirport : airports) {
            if (airport.equals(eachAirport.getName())) {
                return eachAirport;
            }
        }
        return null;
    }

    public List<Airport> getAirports(){
        return airports;
    }

    public void clearMarks() {
        for (Airport a : airports) {
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


        public priorityQueueAirport(Airport airport, double price, Flight previousFlight, priorityQueueAirport previousAirport) {
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

        public double getPrice() {
            return price;
        }

        public Time getTime() {
            return time;
        }

        public Airport getAirport() {
            return airport;
        }

        public priorityQueueAirport minPath( Airport destination, List<String> days, Comparator<priorityQueueAirport> comparator ) {
            if (this.getAirport() == null || destination == null) {
                System.out.println("Origin or destination not specified");
                return null;
            }

            clearMarks();
            PriorityQueue<priorityQueueAirport> priorityQueue = new PriorityQueue<>(comparator);
            priorityQueueAirport aux = null;

            /**
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

            /**
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

    public Itinerary setItinerary(Airport origin, Airport destination, List<String> days, String priority){

        priorityQueueAirport originPQ = new priorityQueueAirport(origin, 0, null,  null);
        priorityQueueAirport node;

        if (priority.equals("pr"))
             node = originPQ.minPath(destination, days, new priceComparator());

        else if (priority.equals("ft"))
            node = originPQ.minPath(destination, days, new timeComparator());

        else
            return null;

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

        Itinerary itinerary = new Itinerary(price, cummulativeTotalFlightTime, cummulativeFlightTime, flights);

        return itinerary;
    }

    public void recursivePath(priorityQueueAirport node, ArrayList<ItineraryFlightInfo> flights){
        if(node == null || node.previousFlight == null)
            return;

        flights.add(new ItineraryFlightInfo(node.previousFlight));
        recursivePath(node.previousAirport, flights);
    }

    public static void main(String[]args){

        double price1 = 100;
        double price2 = 200;
        double price3 = 500;
        double price4 = 600;

        Graph.Time departure = new Graph.Time(0,100);
        Graph.Time duration = new Graph.Time(0,200);
        List<String> days1 = new ArrayList<>();
        days1.add("Lu");
        Airport origin1 = new Airport("BUE",0,1);
        Airport destination1 = new Airport("PAR",2,3);

        List<String> days2 = new ArrayList<>();
        days2.add("Mi");
        Airport destination2 = new Airport("LON",2,3);

        List<String> days3 = new ArrayList<>();
        days3.add("Mi");
        Airport destination3 = new Airport("MOS",2,3);

        List<String> days4 = new ArrayList<>();
        days4.add("Ju");

        Flight flight1 = new Flight("AA",1234,days1,origin1,destination1,departure,duration,price1);
        Flight flight2 = new Flight("AF",678,days2,destination1,destination2,departure,duration,price2);
        Flight flight3 = new Flight("BA",789,days3,destination2,destination3,departure,duration,price3);
        Flight flight4 = new Flight("AMA",2324,days4,destination2,destination3,departure,duration,price4);


        FlightSystem f = new FlightSystem();
        f.addAirport(origin1);

        f.addAirport(destination1);
        f.addAirport(destination2);
        f.addAirport(destination3);

        f.getAirports().get(0).addFlight(flight1);
        f.getAirports().get(1).addFlight(flight2);
        f.getAirports().get(2).addFlight(flight3);
        f.getAirports().get(2).addFlight(flight4);

        Itinerary it = f.setItinerary(f.getAirports().get(0), f.getAirports().get(3), days1, "pr");

        System.out.println(it.getTotalPrice());

        for (ItineraryFlightInfo flight : it.getFlights()) {
            System.out.println(flight.getFlight().getOrigin().getName());
            System.out.println(flight.getFlight().getDestination().getName());
        }

    }
}
