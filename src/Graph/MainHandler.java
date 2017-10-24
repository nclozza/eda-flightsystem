package Graph;

import InputHandler.ConsoleReader;

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
                    //Sintaxis: insert airport [nombre] [lat] [lng]
                    //Sintaxis: insert all airports FILE [append|replace]
                    //Sintaxis: insert flight [aerolinea] [nroVuelo] [diasSemana] [origen] [destino]
                    //[horaSalida] [duracion] [precio]
                    //Sintaxis: insert all flight FILE [append|replace]
                } else if (aux[0].equals("delete")) {
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
                } else {
                    System.out.println("Wrong input");
                }


            }

    }

}