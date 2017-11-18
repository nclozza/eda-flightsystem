package Graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

class TestFlightSystem {

    @Test
    void mainTest() throws Exception {

        FlightSystem flightSystem = new FlightSystem();

        assertEquals(true, flightSystem.getAirportList().isEmpty(),
                        "AirportList must be empty when the FlightSystem it's initialized");
        assertEquals(0, flightSystem.getFlightsNumber(),
                        "FlightList must be empty when the FlightSystem it's initialized");

        flightSystem.addAirport("BUE", -34.602535, -58.368731);
        flightSystem.addAirport("PAR", 48.871264, 2.351074);
        flightSystem.addAirport("LON", 51.52797, -0.126343);
        flightSystem.addAirport("MOS", 55.751244, 37.618423);
        flightSystem.addAirport("DEL", 28.644800, 77.216721);

        assertEquals(5, flightSystem.getAirportList().size(),
                        "AirportList must have 5 items right now");
        assertEquals("BUE", flightSystem.getAirport("BUE").getName(),
                        "Airport BUE must be inserted");
        assertEquals(null, flightSystem.getAirport("BRA"),
                        "Airport BRA must not be in the airports list");

        LinkedList<String> flights = new LinkedList<>();
        flights.add("AA#1234#Lu-Ma-Vi#BUE#PAR#10:00#12h0m#100");
        flights.add("AF#6786#Ju-Vi-Sa#BUE#PAR#18:00#11h0m#120");
        flights.add("BA#7896#Lu-Ma-Sa#PAR#LON#12:00#9h0m#90");
        flights.add("AM#2324#Ma-Sa-Do#PAR#DEL#20:00#7h0m#200");
        flights.add("BB#2154#Lu#LON#MOS#10:30#4h0m#70");
        flights.add("DG#2156#Ma-Mi#MOS#DEL#11:25#3h0m#200");
        flights.add("EM#2157#Ju-Vi-Do#DEL#BUE#18:00#18h0m#100");
        flights.add("BC#8976#Ma-Mi#PAR#LON#03:00#12h0m#80");
        flights.add("MA#1654#Vi-Sa#PAR#MOS#07:00#7h0m#40");
        flights.add("EE#7180#Ma#BUE#DEL#12:00#19h0m#90");
        addFlights(flights, flightSystem);

        assertEquals(10, flightSystem.getFlightsNumber(),
                        "FlightList must have 11 items right now");
        assertEquals(true, flightSystem.containsFlight("AA", 1234),
                        "Flight AA 1234 must be inserted");
        assertEquals(false, flightSystem.containsFlight("AB", 0000),
                        "Flight AB 0000 must not be inserted");

        LinkedList<String> days = new LinkedList<>();
        days.add("Lu");
        days.add("Ma");
        days.add("Mi");
        days.add("Ju");
        days.add("Vi");
        days.add("Sa");
        days.add("Do");

        Itinerary itineraryTest = flightSystem.setItinerary("BUE", "PAR", days, "pr");
        assertEquals(100.0, itineraryTest.getTotalPrice(), "The price must be $100");
        assertEquals("12h0m", itineraryTest.getFlightTime().toString(), "The flight time must be 12h0m");

        itineraryTest = flightSystem.setItinerary("BUE", "PAR", days, "ft");
        assertEquals(120.0, itineraryTest.getTotalPrice(), "The price must be $120");
        assertEquals("11h0m", itineraryTest.getFlightTime().toString(), "The flight time must be 11h0m");

        itineraryTest = flightSystem.setItinerary("BUE", "BUE", days, "pr");
        assertEquals(550.0, itineraryTest.getTotalPrice(), "The price must be $550");
        assertEquals("49h0m", itineraryTest.getFlightTime().toString(), "The flight time must be 49h0m");

        itineraryTest = flightSystem.setItinerary("BUE", "BUE", days, "ft");
        assertEquals(580.0, itineraryTest.getTotalPrice(), "The price must be $580");
        assertEquals("45h0m", itineraryTest.getFlightTime().toString(), "The flight time must be 45h0m");

        flightSystem.deleteAllFlights();
        assertEquals(0, flightSystem.getFlightsNumber(),
                        "FlightList must be empty after deleting all the flights");

        flightSystem.deleteAllAirports();
        assertEquals(true, flightSystem.getAirportList().isEmpty(),
                        "AirportList must be empty after deleting all the airports");
    }

    private void addFlights(LinkedList<String> flights, FlightSystem flightSystem) {

        flightSystem.deleteAllFlights();

        for (String eachFlight : flights) {
            String aux[] = eachFlight.split("#");

            String days[] = aux[2].split("-");
            LinkedList<String> daysList = new LinkedList<>();
            daysList.addAll(Arrays.asList(days));

            String departureHour[] = aux[5].split(":");
            int departureTime = (Integer.parseInt(departureHour[0])*60) + Integer.parseInt(departureHour[1]);

            String duration[] = aux[6].split("h");
            duration[1] = duration[1].replace("m", "");
            int durationTime = (Integer.parseInt(duration[0])*60) + Integer.parseInt(duration[1]);

            try {
                flightSystem.addFlight(aux[0], Integer.parseInt(aux[1]), daysList, aux[3], aux[4],
                    departureTime, durationTime, Double.parseDouble(aux[7]));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}