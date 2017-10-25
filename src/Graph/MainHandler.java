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
                            ValidateData.validateAerpName(aux[3]);
                        }
                    } else if(aux[1].equals("all")) {
                        if (aux[2].equals("airports")){
                            if (aux.length == 5){
                                String path = aux[3];
                                processingFile(path, aux[4]);
                            } else System.out.println("Wrong input");
                        } else if (aux[2].equals("flight")){
                            if (aux.length == 5){
                                String path = aux[3];
                                processingFile(path, aux[4]);
                            } else System.out.println("Wrong input");
                        }
                    } else if (aux[1].equals("flight")) {
                        if ( !(aux.length == 10)) System.out.println("Wrong input");
                    } else System.out.println("Wrong input");
                    //Sintaxis: insert airport [nombre] [lat] [lng]
                    //Sintaxis: insert all airports FILE [append|replace]
                    //Sintaxis: insert flight [aerolinea] [nroVuelo] [diasSemana] [origen] [destino]
                    //[horaSalida] [duracion] [precio]
                    //Sintaxis: insert all flight FILE [append|replace]
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

                            } else System.out.println("Wrong input");
                        }
                    } else System.out.println("Wrong input");
                    //Sintaxis: delete airport [nombre]
                    //Sintaxis: delete all airport
                    //Sintaxis: delete flight [aerolinea] [nroVuelo]
                    //Sintaxis: delete all flight
                } else if (aux[0].equals("findRoute")) {
                    //Sintaxis: findRoute [origen] [destino] [prioridad{ft|pr|tt}] [diasSemana]
                } else if (aux[0].equals("worldTrip")) {
                    //Sintaxis: worldTrip [origen] [prioridad{ft|pr|tt}] [diasSemana]
                } else if (input.equals("exit")) {
                    System.out.println("Adios");
                    break;
                } else if (aux[0].equals("outputFormat")){
                    if (aux.length != 3) System.out.println("Wrong format");
                    //Sintaxis: outputFormat [tipo{text|KML}] [output{archivo|stdout}]
                }
                else {
                    System.out.println("Wrong input");
                }


            }

    }

    public void processingFile(String path, String option){
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/tomas-pc/eda-flightsystem/src/" + path));
            String line = br.readLine();
            while (line != null) {
                System.out.println(line);
                //Aca habria que validar cada linea
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