package Outputs;


import Graph.Flight;
import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by cderienzo on 10/24/2017.
 */
public class KMLOutput {
    public void createKML(java.util.List<Flight> flights) throws IOException{
        final Kml kml = new Kml();
        Document document = kml.createAndSetDocument().withName("Itinerary");
        LineString path = new LineString();
        path.setTessellate(true);

        for(Flight f : flights){
            document.createAndAddPlacemark().withName(f.getOrigin().getName()).withOpen(Boolean.TRUE)
                .createAndSetPoint().addToCoordinates(f.getOrigin().getPosition().getLatitude(),f.getOrigin().getPosition().getLongitude());
            path.addToCoordinates(f.getOrigin().getPosition().getLatitude(),f.getOrigin().getPosition().getLongitude());

        }
        Style style = document.createAndAddStyle().withId("PathStyle");
        LineStyle pathSyle = style.createAndSetLineStyle().withColor("ff33ccff").withWidth(3);

        document.createAndAddPlacemark().withStyleUrl("#PathStyle").setGeometry(path);
        kml.marshal(new File("itineraryKML.kml"));
    }

    /*public static void main(String[] args) throws IOException {
        KMLOutput kmlOutput = new KMLOutput();

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
        Airport origin1 = new Airport("BUE", -34.8133,-58.4753);
        Airport destination1 = new Airport("PAR",48.7261,2.36411);

        List<String> days2 = new ArrayList<>();
        days2.add("Mi");
        Airport destination2 = new Airport("LON",51,0);

        List<String> days3 = new ArrayList<>();
        days3.add("Mi");
        Airport destination3 = new Airport("MOS",55,37);

        List<String> days4 = new ArrayList<>();
        days4.add("Ju");
        Airport destination4 = new Airport("BER",52,13);

        Flight flight1 = new Flight("AA",1234,days1,origin1,destination1,departure,duration,price1);
        Flight flight2 = new Flight("AF",678,days2,destination1,destination2,departure,duration,price2);
        Flight flight3 = new Flight("BA",789,days3,destination2,destination3,departure,duration,price3);
        Flight flight4 = new Flight("AMA",2324,days4,destination3,destination4,departure,duration,price4);
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
        kmlOutput.createKML(flights);
    }*/
}
