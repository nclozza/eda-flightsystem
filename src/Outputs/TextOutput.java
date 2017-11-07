package Outputs;

import Graph.Flight;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by cderienzo on 10/24/2017.
 Precio#xxxx.yy
 TiempoVuelo#hhxyym
 TiempoTotal#xxhyym
 (aeropuerto1)#(aerolinea1)#(nroVuelo1)#(dia)#(aeropuerto2)
 (aeropuerto2)#(aerolinea2)#(nroVuelo2)#(dia)#(aeropuerto3)
 Precio#5708.23
 TiempoVuelo#17h08m
 TiempoTotal#20h48m
 BUE#AA#1234#Lu#PAR
 PAR#AF#678#Mi#LON
 LON#BA#789#Mi#MOS
 MOS#AMA#2324#Ju#BER
 */

public class TextOutput {
    public void createText(boolean itineraryMade, String price, Graph.Time flightTime, Graph.Time totalTime, List<Flight> flights, List<String> days) throws IOException{
        BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File logFile = new File("itinerary_"+timeLog);

            // This will output the full path where the file will be written to...
            //System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            if(itineraryMade){
                writer.write("Precio#" + price);
                writer.newLine();
                writer.write("TiempoVuelo#" + flightTime.toString());
                writer.newLine();
                writer.write("TiempoTotal#" + totalTime.toString());
                writer.newLine();
                writer.newLine();
                for(int i=0; i<flights.size();i++){
                    Flight f= flights.get(i);
                    writer.write(f.getOrigin().getName()+"#"+ f.getAirline()+"#"+f.getFlightNr()+"#"+days.get(i)+"#"+f.getDestination().getName());
                    writer.newLine();
                }
            }
            else {
                writer.write("Not found");
            }



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    /*public static void main(String[] args) throws IOException {
        TextOutput textOutput = new TextOutput();

        String price = "5708.23";
        Graph.Time flightTime = new Graph.Time(17,8);
        Graph.Time totalTime = new Graph.Time(20,48);

        double price1 = 100;
        double price2 = 200;
        double price3 = 500;
        double price4 = 5708.23-price1-price2-price3;

        Graph.Time departure = new Graph.Time(0,100);
        Graph.Time duration = new Graph.Time(0,200);
        List<String> days1 = new ArrayList<>();
        days1.add("Lu");
        Airport origin1 = new Airport("BUE",0,1);
        Airport destination1 = new Airport("PAR",2,3);

        List<String> days2 = new ArrayList<>();
        days2.add("Mi");
        Airport origin2 = new Airport("PAR",0,1);
        Airport destination2 = new Airport("LON",2,3);

        List<String> days3 = new ArrayList<>();
        days3.add("Mi");
        Airport origin3 = new Airport("LON",0,1);
        Airport destination3 = new Airport("MOS",2,3);

        List<String> days4 = new ArrayList<>();
        days4.add("Ju");
        Airport origin4 = new Airport("MOS",0,1);
        Airport destination4 = new Airport("BER",2,3);

        Flight flight1 = new Flight("AA",1234,days1,origin1,destination1,departure,duration,price1);
        Flight flight2 = new Flight("AF",678,days2,origin2,destination2,departure,duration,price2);
        Flight flight3 = new Flight("BA",789,days3,origin3,destination3,departure,duration,price3);
        Flight flight4 = new Flight("AMA",2324,days4,origin4,destination4,departure,duration,price4);
        List<Flight> flights = new ArrayList<>();
        flights.add(flight1);
        flights.add(flight2);
        flights.add(flight3);
        flights.add(flight4);
        List<String> days = new ArrayList<>();
        days.add("Lu");
        days.add("Mi");
        days.add("Mi");
        days.add("Ju");

        textOutput.createText(false,price,flightTime,totalTime,flights,days);
    }*/
}
