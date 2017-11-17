package Graph;

import InputHandler.ConsoleReader;
import InputHandler.ValidateData;
import Outputs.KMLOutput;
import Outputs.TextOutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class MainHandler {

    private static final String EXIT_COMMAND = "exit";
    private FlightSystem flightSystem;

    public MainHandler() {
        flightSystem = new FlightSystem();
    }

    /**
     * The handler's main task, running the project asking the user for input and translating that action to an
     * operation to the FlightSystem.
     */
    public void runCode() {
        String input = null;
        boolean textFile = false;
        boolean KML = false;
        String fileName = null;

        while (true) {
            System.out.println("Enter some text, or '" + EXIT_COMMAND + "' to quit");
            System.out.print("> ");

            try {
                input = ConsoleReader.readingFromConsole();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] aux = input.split(" ");
            switch (aux[0]) {
                case "insert":
                    if (aux.length >= 2) {
                        switch (aux[1]) {
                            case "airport":
                                if ((aux.length != 5)) System.out.println("Wrong input");
                                else {
                                    ValidateData.validateName(aux[2]); //aux[2] is name
                                    ValidateData.validateLat(aux[3]); //aux[3] is lat
                                    ValidateData.validateLng(aux[4]); //aux[4] is long

                                    Double lat = Double.parseDouble(aux[3]);
                                    Double lon = Double.parseDouble(aux[4]);

                                    if (flightSystem.getAirport(aux[2]) != null) {
                                        System.out.println("Airport already exists");
                                    } else {
                                        System.out.println("Inserted airport " + aux[2]);
                                        flightSystem.addAirport(aux[2], lat, lon);
                                    }
                                }
                                break;
                            case "all":
                                if (aux.length >= 3) {
                                    if (aux[2].equals("airports")) {
                                        if (aux.length == 5) {
                                            String path = aux[3];
                                            if (aux[4].equals("replace") || aux[4].equals("append")) {
                                                System.out.println((aux[4].equals("replace") ? "Replacing" : "Appending")
                                                                    + " all airports");
                                                LinkedList<String> airports = processingFile(path);
                                                addAirportsFromFile(airports, aux[4].equals("replace")); //aux[4] is append or replace
                                            } else {
                                                System.out.println("Wrong input");
                                            }
                                        }
                                        else {
                                            System.out.println("Wrong input");
                                        }

                                    } else if (aux[2].equals("flights")) {
                                        if (aux.length == 5) {
                                            String path = aux[3];
                                            if (aux[4].equals("replace") || aux[4].equals("append")) {
                                                System.out.println((aux[4].equals("replace") ? "Replacing" : "Appending")
                                                                    + " all flights");
                                                LinkedList<String> flights = processingFile(path);
                                                addFlightsFromFile(flights, aux[4].equals("replace")); //aux[4] is append or replace
                                            } else {
                                                System.out.println("Wrong input");
                                            }
                                        } else {
                                            System.out.println("Wrong input");
                                        }
                                    } else {
                                        System.out.println("Wrong input");
                                    }
                                }
                                break;
                            case "flight":
                                if (!(aux.length == 10)) System.out.println("Wrong input");
                                else {
                                    boolean state1 = ValidateData.validateName(aux[2]) && ValidateData.validateFlightNumber(aux[3]);
                                    boolean state2 = ValidateData.validateDay(aux[4]) && ValidateData.validateOrigin(aux[5], flightSystem);
                                    boolean state3 = ValidateData.validateDestiny(aux[6], flightSystem) && ValidateData.validateDepartureTime(aux[7]);
                                    boolean state4 = ValidateData.validateFlightDuration(aux[8]) && ValidateData.validatePrice(aux[9]);

                                    if (!(state1 && state2 && state3 && state4))
                                        System.out.println("Wrong input");
                                    else {
                                        String aeroName = aux[2];
                                        Integer flightNumber = Integer.parseInt(aux[3]);
                                        String[] days = aux[4].split("-");
                                        ArrayList<String> daysList = new ArrayList<>();

                                        daysList.addAll(Arrays.asList(days));

                                        String origName = aux[5];
                                        String destName = aux[6];

                                        boolean notFound = true;
                                        Airport airportAuxOrigin = null;
                                        int originIndex;
                                        for (originIndex = 0; originIndex < flightSystem.getAirportList().size() && notFound; originIndex++) {
                                            if (flightSystem.getAirportList().get(originIndex).getName().equals(origName))
                                                airportAuxOrigin = flightSystem.getAirportList().get(originIndex);
                                            notFound = false;
                                        }

                                        if (notFound) {
                                            System.out.println("Origin not valid");
                                            break;
                                        }

                                        notFound = true;
                                        Airport airportAuxDestination = null;
                                        for (int i = 0; i < flightSystem.getAirportList().size() && notFound; i++) {
                                            if (flightSystem.getAirportList().get(i).getName().equals(destName))
                                                airportAuxDestination = flightSystem.getAirportList().get(i);
                                            notFound = false;
                                        }

                                        if (notFound) {
                                            System.out.println("Destination not valid");
                                            break;
                                        }

                                        String[] aux2 = aux[7].split(":");
                                        Integer hour = Integer.parseInt(aux2[0]);
                                        Integer min = Integer.parseInt(aux2[1]);
                                        String duration[] = aux[8].split("h");
                                        duration[1] = duration[1].replace("m", "");
                                        Integer longInH = Integer.parseInt(duration[0]);
                                        Integer longInM = Integer.parseInt(duration[1]);
                                        Double price = Double.valueOf(aux[9]);

                                        if (flightSystem.containsFlight(aeroName, flightNumber)) {
                                            System.out.println("That flight already exists");

                                        } else {
                                            try {
                                                flightSystem.addFlight(aeroName, flightNumber, daysList, origName,
                                                        destName, (hour*60)+min,
                                                        (longInH*60)+longInM, price);
                                            } catch (Exception e) {
                                                System.out.println("Exception found");
                                            }

                                            System.out.println("Inserting flight " + aux[2] + " - " + aux[3]);
                                        }
                                    }
                                }
                                break;
                            default:
                                System.out.println("Wrong input");
                        }
                    } else {
                        System.out.println("Wrong input");
                    }
                    break;
                case "delete":
                    if (aux.length >= 2) {
                        switch (aux[1]) {
                            case "airport":
                                if (!(aux.length == 3)) System.out.println("Wrong input");
                                else {
                                    if (flightSystem.deleteAirport(aux[2])) {
                                        System.out.println("Deleting airport " + aux[2]);

                                    } else {
                                        System.out.println("Airport " + aux[2] + " does not exist");
                                    }
                                }
                                break;
                            case "flight":
                                System.out.println(aux[2]);
                                System.out.println(aux[3]);
                                if (!(aux.length == 4)) {
                                    System.out.println("Wrong input");
                                } else if (!(ValidateData.validateName(aux[2]) && ValidateData.validateFlightNumber(aux[3]))) {
                                    flightSystem.deleteFlight(aux[2], Integer.parseInt(aux[3]));
                                } else {
                                    System.out.println("The flight you want to delete does not exist");
                                }

                                break;
                            case "all":
                                if (!(aux.length == 3)) System.out.println("Wrong input");
                                else {

                                    switch (aux[2]) {
                                        case "airports":
                                            System.out.println("Deleting all airports");
                                            flightSystem.deleteAllAirports();
                                            break;
                                        case "flights":
                                            System.out.println("Deleting all flights");
                                            flightSystem.deleteAllFlights();
                                            break;
                                        default:
                                            System.out.println("Wrong input");
                                            break;
                                    }
                                }
                                break;
                            default:
                                System.out.println("Wrong input");
                                break;
                        }
                    } else {
                        System.out.println("Wrong input");
                    }
                    break;
                case "findRoute":

                    if (aux.length == 5
                        && ValidateData.validateName(aux[1]) //origin
                        && ValidateData.validateName(aux[2]) //destination
                        && (aux[3].equals("ft") || aux[3].equals("pr") || aux[3].equals("tt"))
                        && ValidateData.validateDay(aux[4]) /*days*/) {

                        String origin = aux[1];
                        String destination = aux[2];
                        String days[] = aux[4].split("-");
                        LinkedList<String> daysList = new LinkedList<>();
                        daysList.addAll(Arrays.asList(days));

                        Itinerary itinerary = null;

                        try {
                            itinerary = flightSystem.setItinerary(origin, destination, daysList, aux[3]);
                        } catch (Exception e) {
                            System.out.println("Wrong Airports");
                        }

                        if (itinerary == null) {
                            System.out.println("Couldn't find an appropriate flight itinerary");

                        } else {
                            outputHandler(itinerary, textFile, fileName, KML);
                        }

                    } else {
                        System.out.println("Wrong input");
                    }
                    break;

                case "worldTrip":

                    if (aux.length == 4
                        && ValidateData.validateName(aux[1]) //origin
                        && (aux[2].equals("ft") || aux[2].equals("pr") || aux[2].equals("tt"))
                        && ValidateData.validateDay(aux[3]) /*days*/) {

                        String origin = aux[1];
                        String days[] = aux[3].split("-");
                        LinkedList<String> daysList = new LinkedList<>();
                        daysList.addAll(Arrays.asList(days));

                        Itinerary itinerary = null;

                        try {
                            itinerary = flightSystem.setItinerary(origin, origin, daysList, aux[2]);
                        } catch (Exception e) {
                            System.out.println("Wrong Airports");
                        }

                        outputHandler(itinerary, textFile, fileName, KML);

                    } else {
                        System.out.println("Wrong input");
                    }
                    break;

                case "exit":
                    System.out.print("See you soon\n");
                    return;

                case "outputFormat":
                    if (aux.length != 3) {
                        System.out.println("Wrong input");
                    } else {
                        switch (aux[1]) {
                            case "text":
                                KML = false;
                                break;
                            case "KML":
                                KML = true;
                                break;
                            default:
                                System.out.println("Wrong input");
                                break;
                        }

                        switch (aux[2]) {
                            case "stdout":
                                textFile = false;
                                break;
                            default:
                                textFile = true;
                                fileName = aux[2];
                                System.out.println("Output save in: " + aux[2]);
                                break;
                        }
                    }
                    break;
                default:
                    System.out.println("Wrong input");
                    break;
            }

        }

    }

    private void addAirportsFromFile(LinkedList<String> airportsFromFile, boolean clear) {
        if (clear) {
            flightSystem.deleteAllAirports();
        }
        try {
            for (String eachAirport : airportsFromFile) {
                String aux[] = eachAirport.split("#");

                if (flightSystem.getAirport(aux[0]) == null) {
                    flightSystem.addAirport(aux[0], Double.parseDouble(aux[1]), Double.parseDouble(aux[2]));
                }
            }
        } catch (Exception e){
            System.out.println("File not found");
        }

    }

    private void addFlightsFromFile(LinkedList<String> flightsFromFile, boolean clear) {
        if (clear) {
            flightSystem.deleteAllFlights();
        }

        try {
            for (String eachFlight : flightsFromFile) {
                String aux[] = eachFlight.split("#");

                String days[] = aux[2].split("-");
                LinkedList<String> daysList = new LinkedList<>();
                daysList.addAll(Arrays.asList(days));

                String departureHour[] = aux[5].split(":");
                int departureTime = (Integer.parseInt(departureHour[0])*60) + Integer.parseInt(departureHour[1]);

                String duration[] = aux[6].split("h");
                duration[1] = duration[1].replace("m", "");
                int durationTime = (Integer.parseInt(duration[0])*60) + Integer.parseInt(duration[1]);

                if (flightSystem.containsFlight(aux[0], Integer.parseInt(aux[1]))) {
                    System.out.println("Flight " + aux[0] + " " + aux[1] + " already exist");
                } else {
                    try {
                        flightSystem.addFlight(aux[0], Integer.parseInt(aux[1]), daysList, aux[3], aux[4],
                                departureTime, durationTime, Double.parseDouble(aux[7]));
                    } catch (Exception e) {
                        System.out.println("Wrong airports for that flight.");
                    }
                }
            }
        }catch (Exception e){
            System.out.println("File not found");
        }
    }

    private LinkedList<String> processingFile(String path) {
        try {
            String[] aux = System.getProperty("os.name").split("");
            String absolutePath = null;
            if (aux[0].equals("Windows")) {
                absolutePath = new File(Paths.get(".").toAbsolutePath().normalize().toString()).getAbsolutePath();
                absolutePath += "\\" + path;
            }else {
                absolutePath = new File(Paths.get(".").toAbsolutePath().normalize().toString()).getAbsolutePath();
                absolutePath += "/" + path;
            }
            BufferedReader br = new BufferedReader(new FileReader(absolutePath));
            String line = br.readLine();
            LinkedList<String> ret = new LinkedList<>();
            while (line != null) {
                ret.add(line);
                line = br.readLine();
            }
            return ret;
        } catch (IOException e) {
            System.out.println("File not found");
        }
        return null;
    }

    private void outputHandler(Itinerary itinerary, boolean textFile, String fileName, boolean KML) {
        if (itinerary != null) {
            if (KML) {
                KMLOutput kmlOutput = new KMLOutput();
                LinkedList<Flight> flights = new LinkedList<>();

                for (ItineraryFlightInfo each : itinerary.getFlights()) {
                    flights.add(each.getFlight());
                }

                kmlOutput.createKML(flights, fileName, textFile);

            } else {
                StringBuilder ret = new StringBuilder();
                ret.append("Precio#").append(itinerary.getTotalPrice()).append(System.getProperty("line.separator"));
                ret.append("TiempoVuelo#").append(itinerary.getFlightTime().toString()).append(System.getProperty("line.separator"));
                ret.append("TiempoTotal#").append(itinerary.getTotalFlightTime().toString()).append(System.getProperty("line.separator"));

                for (ItineraryFlightInfo each : itinerary.getFlights()) {
                    ret.append(each.getFlight().getOrigin().getName()).append("#")
                        .append(each.getFlight().getAirline()).append("#")
                        .append(each.getFlight().getFlightNr()).append("#")
                        .append(each.getArrivalDay().getDayName()).append("#")
                        .append(each.getFlight().getDestination().getName()).append("\n");
                }

                if (textFile) {
                    TextOutput textOutput = new TextOutput();
                    textOutput.createText(ret.toString(), fileName);
                } else {
                    System.out.println(ret);
                }
            }
        } else {
            if (textFile) {
                TextOutput textOutput = new TextOutput();
                textOutput.createText("NotFound", fileName);
            } else {
                System.out.println("NotFound");
            }
        }
    }


    public FlightSystem getFlightSystem() {
        return flightSystem;
    }
}