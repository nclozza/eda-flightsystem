package Graph;

import InputHandler.ConsoleReader;
import InputHandler.ValidateData;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
                      LinkedList<String> airports = processingFile(path);
                      addAirportsFromFile(airports, aux[4] == "replace"); //aux[4] is append or replace
                      System.out.println(aux[4].toUpperCase() + " all airports");
                    } else
                      System.out.println("Wrong input");
                  } else if (aux[2].equals("flights")) {
                    if (aux.length == 5) {
                      String path = aux[3];
                      LinkedList<String> flights = processingFile(path);
                      addFlightsFromFile(flights, aux[4] == "replace"); //aux[4] is append or replace
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

                    for (int i = 0; i < days.length; i++)
                      daysList.add(days[i]);

                    String origName = aux[5];
                    String destName = aux[6];

                    boolean notFound = true;
                    Airport airportAuxOrigin = null;
                    int originIndex;
                    for (originIndex = 0; originIndex < flightSystem.getAirports().size() && notFound; originIndex++) {
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
                    for (int i = 0; i < flightSystem.getAirports().size() && notFound; i++) {
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
                    String duration[] = aux[8].split("h");
                    duration[1] = duration[1].replace("m", "");
                    Integer longInH = Integer.parseInt(duration[0]);
                    Integer longInM = Integer.parseInt(duration[1]);
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
                else if (!(ValidateData.validateName(aux[2]) && ValidateData.validateFlightNumber(aux[3]))) {
                  System.out.println("Deleting flight " + aux[2] + " - " + aux[3]);
                } else {
                  System.out.println("The flight you want to delete does not exist");
                }

                break;
              case "all":
                if (!(aux.length == 3)) System.out.println("Wrong input");
                else {

                  if (aux[2].equals("airport")) {
                    System.out.println("Deleting all airports");
                    flightSystem.deleteAllAirports();
                  } else if (aux[2].equals("flights")) {
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
          } else {
            System.out.println("Wrong input");
          }
          break;
        case "findRoute":
          //Sintaxis: findRoute [origen] [destino] [prioridad{ft|pr|tt}] [diasSemana]
          if (aux.length == 5
                && ValidateData.validateName(aux[1]) //origen
                  && ValidateData.validateName(aux[2]) //destino
                    && (aux[3].equals("ft") || aux[3].equals("pr") || aux[3].equals("tt"))
                      && ValidateData.validateDay(aux[4]) /*days*/) {

            Airport origin = flightSystem.getAirport(aux[1]);
            Airport destination = flightSystem.getAirport(aux[2]);
            String days[] = aux[4].split("-");
            LinkedList<String> daysList = new LinkedList<>();
            for (String eachDay : days) {
              daysList.add(eachDay);
            }

            Itinerary itinerary = flightSystem.setItinerary(origin, destination, daysList, aux[3]);

            System.out.println(itinerary.getTotalPrice());
          } else {
            System.out.println("Wrong input");
          }
          break;
        case "worldTrip":
          System.out.println(aux[0]);
          //Sintaxis: worldTrip [origen] [prioridad{ft|pr|tt}] [diasSemana]
          //Aca solo falta validar o ver como haces para validar lo de prioridad
          break;
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
          break;
      }

    }

  }

  private void addAirportsFromFile(LinkedList<String> airportsFromFile, boolean clear) {
    if (clear) {
      flightSystem.deleteAllAirports();
    }
    for (String eachAirport : airportsFromFile) {
      String aux[] = eachAirport.split("#");
      if (flightSystem.getAirport(aux[0]) != null) {
        Airport airport = new Airport(aux[0], Double.parseDouble(aux[1]), Double.parseDouble(aux[2]));
        flightSystem.addAirport(airport);
      }
    }
  }

  private void addFlightsFromFile(LinkedList<String> flightsFromFile, boolean clear) {
    if (clear) {
      flightSystem.deleteAllAirports();
    }
    for (String eachFlight : flightsFromFile) {
      String aux[] = eachFlight.split("#");

      String days[] = aux[2].split("-");
      LinkedList<String> daysList = new LinkedList<>();
      for (String eachDay : days) {
        daysList.add(eachDay);
      }

      Airport airportOrigin = flightSystem.getAirport(aux[3]);
      Airport airportDestination = flightSystem.getAirport(aux[4]);

      String departureHour[] = aux[5].split(":");
      Time departureTime = new Time(Integer.parseInt(departureHour[0]), Integer.parseInt(departureHour[1]));

      String duration[] = aux[6].split("h");
      duration[1] = duration[1].replace("m", "");
      Time durationTime = new Time(Integer.parseInt(duration[0]), Integer.parseInt(duration[1]));

      if (airportOrigin == null || airportDestination == null) {
        System.out.println("Some Airport it's not supported");
      } else {
        Flight flight = new Flight(aux[0], Integer.parseInt(aux[1]), daysList, airportOrigin, airportDestination, departureTime, durationTime, Double.parseDouble(aux[7]));
        airportOrigin.addFlight(flight);
      }
    }
  }

  private LinkedList<String> processingFile(String path) {
    try {
      String absolutePath = new File("").getAbsolutePath();
      absolutePath += "/src/Data/" + path;
      BufferedReader br = new BufferedReader(new FileReader(absolutePath));
      String line = br.readLine();
      LinkedList<String> ret = new LinkedList<>();
      while (line != null) {
        if (!ValidateData.validateLineFile(line, flightSystem)) {
          System.out.println("Wrong file format");
        }
        //Here we should save this line somewhere
        ret.add(line);
        line = br.readLine();
      }
      return ret;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public FlightSystem getFlightSystem() {
    return flightSystem;
  }

  public static void main(String[] args) {
    MainHandler mainHandler = new MainHandler();
    mainHandler.runCode();
  }

}