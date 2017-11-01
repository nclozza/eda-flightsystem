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

    private class priorityQueueAirport {
        private Airport airport;
        private Flight previousFlight; //flight del que vino al aeropuerto
        private priorityQueueAirport previousAirport;
        private double price;
        private Time time;


        public priorityQueueAirport(Airport airport, double price, Flight previousFlight, priorityQueueAirport previousAirport){
            this.airport = airport;
            this.price = price;
            this.previousFlight = previousFlight;
            this.previousAirport = previousAirport;
        }

        public priorityQueueAirport(Airport airport, Time time){
            this.airport = airport;
            this.time = time;
            this.previousFlight = previousFlight;
            this.previousAirport = previousAirport;
        }

        public double getPrice(){return price;}

        public Time getTime(){return time;}

        public Airport getAirport(){return airport;}

        public priorityQueueAirport minPrice(Airport origin, Airport destination, List<String> days){
            if (origin == null || destination == null) {
                System.out.println("Origin or destiny not specified");
                return null;
            }

            priorityQueueAirport originNode = new priorityQueueAirport(origin, 0, null, null);

            clearMarks();
            PriorityQueue<priorityQueueAirport> priorityQueue = new PriorityQueue<>(new priceComparator());
            priorityQueueAirport aux = null;

            /**
             * Modified Djikstra that only queues those flights which depart on one of the given days
             */

            List<Flight> originFlights = new ArrayList<>();
            originFlights.addAll(origin.getFlights());
            originFlights.sort(new Comparator<Flight>() {
                @Override
                public int compare(Flight f1, Flight f2) {
                    if (f1.getPrice() > f2.getPrice())
                        return 1;
                    else if (f1.getPrice() < f2.getPrice())
                        return -1;
                    else
                        return 0;
                }
            });

            int i;
            for(String day : days){
                i=0;
                while(!originFlights.get(i).getFlightDays().contains(day)){
                    i++;
                }
                if(i!=originFlights.size()){
                    priorityQueue.offer(new priorityQueueAirport(originFlights.get(i).getDestination(),
                            originFlights.get(i).getPrice(), originFlights.get(i), originNode));
                }

            }
            origin.visited = true;

            /**
             * Dijkstra's algorithm starting from the second step with price priority
             */

            while (!priorityQueue.isEmpty()){
                aux = priorityQueue.poll();

                if (aux.getAirport() == destination)


                if (!aux.getAirport().visited){
                    aux.getAirport().visited = true;

                    for (Flight f : aux.getAirport().getFlights()){
                        if (!f.getDestination().visited)
                            priorityQueue.offer(new priorityQueueAirport(f.getDestination(),
                                    aux.getPrice() + f.getPrice(), f, aux));
                    }
                }
            }
            return aux;
        }

        public Itinerary setItinerary(Airport origin, Airport destination, List<String> days){
            //public Itinerary(double price, Time totalFlightTime, Time flightTime, List<Flight> flights)
            priorityQueueAirport node = minPrice(origin, destination, days);
            double price = node.getPrice();

            Time cummulativeFlightTime = new Time(0,0); //time without waiting between planes
            Time cummulativeTotalFlightTime = new Time(0,0); //time with wait between planes


            ArrayList<FlightAndDay> flights = new ArrayList<FlightAndDay>();
            recursivePath(node, flights); //metemos la lista de flights en flights
            Collections.reverse(flights); //queremos la lista desde origin hasta destination y no al reves


            int i = 0;
            while(!flights.get(0).getFlight().getFlightDays().contains(days.get(i)))
                i++;
            //elijo arbitrariamente el primer dia de despegue para el primer vuelo

            flights.get(0).setDay(new Day(days.get(i)));
            cummulativeFlightTime.setMinutes(cummulativeFlightTime.getMinutes() +
                    flights.get(0).getFlight().getDuration().getMinutes());
            //adds the duration of the first flight to the cummulative flight time

            cummulativeTotalFlightTime.setMinutes(cummulativeTotalFlightTime.getMinutes() +
                    flights.get(0).getFlight().getDuration().getMinutes());
            //adds the duration of the first flight to the cummulative total flight time


            for(int j=1; j < flights.size(); j++){
               cummulativeFlightTime.setMinutes(flights.get(j).getFlight().getDuration().getMinutes() +
                       cummulativeFlightTime.getMinutes());
               //sumar a flight time lo que le pasas a add
                flights.get(j).setDay(new Day (flights.get(j).getFlight().getFlightDays().get(0)));
                //set the day for the flight departure

               }


            Itinerary itinerary = new Itinerary(price, cummulativeTotalFlightTime, cummulativeFlightTime, flights);
            return itinerary;
        }


        public void recursivePath(priorityQueueAirport node, ArrayList<FlightAndDay> flights){
            if(node == null){
                return;
            }
            flights.add(new FlightAndDay(node.previousFlight, null));
            recursivePath(node.previousAirport, flights);
        }
    }


}
