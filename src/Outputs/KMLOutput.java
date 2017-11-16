package Outputs;


import Graph.Flight;
import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class KMLOutput {
    public void createKML(List<Flight> flights, String fileName) {
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

        try {
            String absolutePath = new File("").getAbsolutePath();
            absolutePath += "/src/Data/Output/" + fileName;

            kml.marshal(new File(absolutePath));
        } catch (FileNotFoundException e) {
            System.out.println("Some problem occurred with the KML output file");
        }
    }
}
