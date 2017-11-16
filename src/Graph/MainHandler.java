package Graph;

import InputHandler.ConsoleReader;
import InputHandler.ValidateData;
import Outputs.KMLOutput;
import Outputs.TextOutput;


import java.io.*;
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

                  Integer lat = Integer.parseInt(aux[3]);
                  Integer lon = Integer.parseInt(aux[4]);

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
                      LinkedList<String> airports = processingFile(path);
                      addAirportsFromFile(airports, aux[4].equals("replace")); //aux[4] is append or replace
                      System.out.println(aux[4].toUpperCase() + " all airports");
                    } else
                      System.out.println("Wrong input");
                  } else if (aux[2].equals("flights")) {
                    if (aux.length == 5) {
                      String path = aux[3];
                      LinkedList<String> flights = processingFile(path);
                      addFlightsFromFile(flights, aux[4].equals("replace")); //aux[4] is append or replace
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

                    Flight auxFlight = new Flight(aeroName, flightNumber, daysList, airportAuxOrigin,
                        airportAuxDestination, new Time(hour, min), new Time(longInH, longInM), price);

                    System.out.println("Inserting flight " + aux[2] + " - " + aux[3]);

                    flightSystem.getAirportList().get(originIndex).addFlight(auxFlight);
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

                  switch (aux[2]) {
                    case "airport":
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
          //Sintaxis: findRoute [origen] [destino] [prioridad{ft|pr|tt}] [diasSemana]
          if (aux.length == 5
                && ValidateData.validateName(aux[1]) //origen
                  && ValidateData.validateName(aux[2]) //destino
                    && (aux[3].equals("ft") || aux[3].equals("pr") || aux[3].equals("tt"))
                      && ValidateData.validateDay(aux[4]) /*days*/) {

            String origin = aux[1];
            String destination = aux[2];
            String days[] = aux[4].split("-");
            LinkedList<String> daysList = new LinkedList<>();
            daysList.addAll(Arrays.asList(days));

            Itinerary itinerary = flightSystem.setItinerary(origin, destination, daysList, aux[3]);

            if (itinerary == null) {
                System.out.println("Can't find an appropriate flight itinerary");

            } else {
                outputHandler(itinerary, textFile, fileName, KML);
            }

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

    for (String eachAirport : airportsFromFile) {
      String aux[] = eachAirport.split("#");

      if (flightSystem.getAirport(aux[0]) == null) {
        flightSystem.addAirport(aux[0], Double.parseDouble(aux[1]), Double.parseDouble(aux[2]));
      }
    }
  }

  private void addFlightsFromFile(LinkedList<String> flightsFromFile, boolean clear) {
    if (clear) {
      flightSystem.deleteAllFlights();
    }

    for (String eachFlight : flightsFromFile) {
      String aux[] = eachFlight.split("#");

      String days[] = aux[2].split("-");
      LinkedList<String> daysList = new LinkedList<>();
      daysList.addAll(Arrays.asList(days));

      String departureHour[] = aux[5].split(":");
      double departureTime = Double.parseDouble(departureHour[0]) + Double.parseDouble(departureHour[1]);

      String duration[] = aux[6].split("h");
      duration[1] = duration[1].replace("m", "");
      double durationTime = Double.parseDouble(duration[0]) + Double.parseDouble(duration[1]);

      flightSystem.addFlight(aux[0], Integer.parseInt(aux[1]), daysList, aux[3], aux[4],
                                departureTime, durationTime, Double.parseDouble(aux[7]));

    }
  }

  private LinkedList<String> processingFile(String path) {
    try {
      String absolutePath = new File("").getAbsolutePath();
      absolutePath += "/src/Data/Input/" + path;
      BufferedReader br = new BufferedReader(new FileReader(absolutePath));
      String line = br.readLine();
      LinkedList<String> ret = new LinkedList<>();
      while (line != null) {
        if (!ValidateData.validateLineFile(line, flightSystem)) {
          System.out.println("Wrong file format");
        }
        ret.add(line);
        line = br.readLine();
      }
      return ret;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void outputHandler(Itinerary itinerary, boolean textFile, String fileName, boolean KML) {
      if (textFile) {
          if (KML) {
              KMLOutput kmlOutput = new KMLOutput();
              LinkedList<Flight> flights = new LinkedList<>();

              for (ItineraryFlightInfo each : itinerary.getFlights()) {
                  flights.add(each.getFlight());
              }

              kmlOutput.createKML(flights, fileName);

          } else {
              String ret = "";
              ret += "Precio#" + itinerary.getTotalPrice() + "\n";
              ret += "TiempoVuelo#" + itinerary.getFlightTime().toString() + "\n";
              ret += "TiempoTotal#" + itinerary.getTotalFlightTime().toString() + "\n\n";

              for (ItineraryFlightInfo each : itinerary.getFlights()) {
                  ret += each.getFlight().getOrigin().getName()+ "#"
                      + each.getFlight().getAirline() + "#"
                      + each.getFlight().getFlightNr() + "#"
                      + each.getArrivalDay().getDayName() + "#"
                      + each.getFlight().getDestination().getName() + "\n";
              }

              TextOutput textOutput = new TextOutput();
              textOutput.createText(itinerary != null, ret, fileName);
          }

      } else {
          System.out.println(itinerary);
      }

  }

  public FlightSystem getFlightSystem() {
    return flightSystem;
  }
}