package Graph;

import InputHandler.ConsoleReader;
import InputHandler.ValidateData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


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
                                    ValidateData.validateLon(aux[4]); //aux[4] is long

                                    Integer lat = Integer.parseInt(aux[3]);
                                    Integer lon = Integer.parseInt(aux[4]);

                                    Airport auxAirport = new Airport(aux[2], lat, lon);

                                    if (flightSystem.getAirports().contains(auxAirport)) {
                                        System.out.println("Airport already exists");
                                    } else {
                                        System.out.println("Inserted airport " + aux[2]);
                                        flightSystem.addAirport(auxAirport);
                                    }
                                }
                                break;
                            case "all":
                                if (aux.length >= 3) {
                                    if (aux[2].equals("airports")) {
                                        if (aux.length == 5) {
                                            String path = aux[3];
                                            processingFile(path, aux[4]); //aux[4] is append or replace
                                            System.out.println(aux[4].toUpperCase() + " all airports");
                                        } else
                                            System.out.println("Wrong input");
                                    } else if (aux[2].equals("flight")) {
                                        if (aux.length == 5) {
                                            String path = aux[3];
                                            processingFile(path, aux[4]); //aux[4] is append or replace
                                            System.out.println(aux[4].toUpperCase() + " all flights");
                                        } else
                                            System.out.println("Wrong input");
                                    }
                                }
                                break;
                            case "flight":
                                if (!(aux.length == 10)) System.out.println("Wrong input");
                                else {
                                    boolean state1 = ValidateData.validateName(aux[2]) && ValidateData.validateFlightNumber(aux[3]);
                                    boolean state2 = ValidateData.validateDay(aux[4]) && ValidateData.validateOrigin(aux[5], flightSystem);
                                    boolean state3 = ValidateData.validateDestiny(aux[6], flightSystem) && ValidateData.validateTime(aux[7]);
                                    boolean state4 = ValidateData.validateLong(aux[8]) && ValidateData.validatePrice(aux[9]);
                                    if (!(state1 && state2 && state3 && state4))
                                        System.out.println("Wrong input");
                                    else {
                                        String aeroName = aux[2];
                                        Integer flightNumber = Integer.parseInt(aux[3]);
                                        String[] days = aux[4].split("-");
                                        ArrayList<String> daysList = new ArrayList<>();

                                        for (int i=0; i<days.length; i++)
                                            daysList.add(days[i]);

                                        String origName = aux[5];
                                        String destName = aux[6];

                                        boolean notFound = true;
                                        Airport airportAuxOrigin = null;
                                        int originIndex;
                                        for (originIndex = 0; originIndex<flightSystem.getAirports().size() && notFound; originIndex++){
                                            if (flightSystem.getAirports().get(originIndex).getName().equals(origName))
                                                airportAuxOrigin = flightSystem.getAirports().get(originIndex);
                                                notFound = false;
                                        }

                                        if (notFound) {
                                            System.out.println("Origin not valid");
                                            break;
                                        }

                                        notFound = true;
                                        Airport airportAuxDestination = null;
                                        for (int i = 0; i<flightSystem.getAirports().size() && notFound; i++){
                                            if (flightSystem.getAirports().get(i).getName().equals(destName))
                                                airportAuxDestination = flightSystem.getAirports().get(i);
                                            notFound = false;
                                        }

                                        if (notFound) {
                                            System.out.println("Destination not valid");
                                            break;
                                        }

                                        String[] aux2 = aux[7].split(":");
                                        Integer hour = Integer.parseInt(aux2[0]);
                                        Integer min = Integer.parseInt(aux2[1]);
                                        char[] aux3 = aux[8].toCharArray(); //We assume a flight cant last more than 99hs
                                        Integer longInH = Integer.parseInt(String.valueOf(aux[1]) + String.valueOf(aux[2]));
                                        Integer longInM = Integer.parseInt(String.valueOf(aux[6]) + String.valueOf(aux[7]));
                                        Double price = Double.valueOf(aux[9]);

                                        Flight auxFlight = new Flight(aeroName, flightNumber, daysList, airportAuxOrigin,
                                                airportAuxDestination, new Time(hour, min), new Time(longInH, longInM), price);

                                        System.out.println("Inserting flight " + aux[2] + " - " + aux[3]);

                                        flightSystem.getAirports().get(originIndex).addFlight(auxFlight);
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
                                    if (flightSystem.deleteAirport(aux[2]))
                                        System.out.println("Deleting airport " + aux[2]);
                                    else
                                        System.out.println("Airport " + aux[2] + " does not exist");
                                }
                                break;
                            case "flight":
                                if (!(aux.length == 4)) System.out.println("Wrong input");
                                else if(!(ValidateData.validateName(aux[2]) && ValidateData.validateFlightNumber(aux[3]))) {
                                System.out.println("Deleting flight " + aux[2] + " - " + aux[3]);
                            } else{
                                System.out.println("The flight you want to delete does not exist");
                            }

                        break;
                        case "all":
                            if (!(aux.length == 3)) System.out.println("Wrong input");
                            else {

                                if (aux[2].equals("airport")) {
                                    System.out.println("Deleting all airports");
                                    flightSystem.deleteAllAirports();
                                } else if (aux[2].equals("flight")) {
                                    System.out.println("Deleting all flights");
                                    flightSystem.deleteAllFlights();
                                } else
                                    System.out.println("Wrong input");
                            }
                            break;
                        default:
                            System.out.println("Wrong input");
                            break;
                    }
            }
                         else{
                System.out.println("Wrong input");
            }
            break;
            case "findRoute":
                System.out.println(aux[0]);
                //Sintaxis: findRoute [origen] [destino] [prioridad{ft|pr|tt}] [diasSemana]
                //Aca solo falta validar o ver como haces para validar lo de prioridad
            case "worldTrip":
                System.out.println(aux[0]);
                //Sintaxis: worldTrip [origen] [prioridad{ft|pr|tt}] [diasSemana]
                //Aca solo falta validar o ver como haces para validar lo de prioridad
            case "exit":
                System.out.print("See you soon");
                return;
            case "outputFormat":
                if (aux.length != 3) {
                    System.out.println("Wrong input");
                } else {
                    if (aux[1].equals("text")) {
                        //Do something
                    } else if (aux[1].equals("KML")) {
                        //Do something
                    } else
                        System.out.println("Wrong input");

                    if (aux[2].equals("archivo")) {
                        //Do something
                    } else if (aux[2].equals("stdout")) {
                        //Do something
                    } else
                        System.out.println("Wrong input");
                }
                break;
            default:
                System.out.println("Wrong input");
        }

    }

}

    private void processingFile(String path, String option){
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/tomas-pc/eda-flightsystem/src/" + path));
            String line = br.readLine();
            while (line != null) {
                System.out.println(line);
                if (!ValidateData.validateLineFile(line, flightSystem)){
                    System.out.println("Wrong file format");
                    return;
                }
                //Here we should save this line somewhere
                line = br.readLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        if (option.equals("append")) {

        } else if (option.equals("replace")) {

        }
        else System.out.println("Wrong input");
    }

    public FlightSystem getFlightSystem(){return flightSystem;}

    public static void main(String[] args) {
        MainHandler mainHandler = new MainHandler();
        mainHandler.runCode();
    }

}