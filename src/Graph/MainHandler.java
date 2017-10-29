package Graph;

import InputHandler.ConsoleReader;
import InputHandler.ValidateData;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class MainHandler {

    private static final String EXIT_COMMAND = "exit";

    public MainHandler(){

    }

    /**
     * The handler's main task, running the project asking the user for input and translating that action to an
     * operation to the AVL tree, the blockchain or both.
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

                if (aux[0].equals("insert")) {
                    if(aux[1].equals("airport")) {
                        if ( ! (aux.length == 5)) System.out.println("Wrong input");
                        else{
                            ValidateData.validateName(aux[3]); //aux[3] is name
                            ValidateData.validateLat(aux[4]); //aux[4] is lat
                            ValidateData.validateLon(aux[5]); //aux[5] is long
                            //I should call here something like insertAirport(aux[3], aux[4], aux[5]);
                        }
                    } else if(aux[1].equals("all")) {
                        if (aux[2].equals("airports")) {
                            if (aux.length == 5){
                                String path = aux[3];
                                processingFile(path, aux[4]); //aux[4] is append or replace
                            } else
                                System.out.println("Wrong input");
                        } else if (aux[2].equals("flight")) {
                            if (aux.length == 5) {
                                String path = aux[3];
                                processingFile(path, aux[4]); //aux[4] is append or replace
                            } else
                                System.out.println("Wrong input");
                        }
                    } else if (aux[1].equals("flight")) {
                        if ( !(aux.length == 10)) System.out.println("Wrong input");
                    } else
                        System.out.println("Wrong input");
                } else if (aux[0].equals("delete")) {
                    if (aux[1].equals("airport")) {
                        if (!(aux.length == 3)) System.out.println("Wrong input");
                    } else if (aux[1].equals("flight")) {
                        if (!(aux.length == 4)) System.out.println("Wrong input");
                    } else if (aux[1].equals("all")) {
                        if (!(aux.length == 3)) System.out.println("Wrong input");
                        else{
                            if (aux[2].equals("airport")) {

                            } else if (aux[2].equals("flight")) {

                            } else
                                System.out.println("Wrong input");
                        }
                    } else
                        System.out.println("Wrong input");
                } else if (aux[0].equals("findRoute")) {
                    //Sintaxis: findRoute [origen] [destino] [prioridad{ft|pr|tt}] [diasSemana]
                    //Aca solo falta validar o ver como haces para validar lo de prioridad
                } else if (aux[0].equals("worldTrip")) {
                    System.out.println(aux[0]);
                    //Sintaxis: worldTrip [origen] [prioridad{ft|pr|tt}] [diasSemana]
                    //Aca solo falta validar o ver como haces para validar lo de prioridad
                } else if (input.equals("exit")) {
                    System.out.println("Adios");
                    break;
                } else if (aux[0].equals("outputFormat")) {
                    if (aux.length != 3) System.out.println("Wrong input");
                    if (aux[1].equals("text")) {
                        //Do something
                    } else if (aux[1].equals("KML")) {
                        //Do something
                    }else
                        System.out.println("Wrong input");

                    if (aux[2].equals("archivo")) {
                        //Do something
                    } else if (aux[2].equals("stdout")) {
                        //Do something
                    } else
                        System.out.println("Wrong input");
                }
                else {
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
                if (!ValidateData.validateLineFile(line)){
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

}