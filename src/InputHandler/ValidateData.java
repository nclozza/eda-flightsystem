package InputHandler;

import Graph.Airport;
import Graph.FlightSystem;

/**
 * The ValidateData class' purpose is to validate the input data given by the user.
 */
public class ValidateData {
    /**
     * Validates an airport's name passed as a String. Every airport shall be represented by three uppercase letters.
     * @param airport the airport's name.
     * @return  True if the airport's name is represented according to the previous said restriction, false otherwise.
     */
    public static boolean validateName(String airport) {
        return airport.matches("[a-zA-Z]{1,3}");
    }

    /**
     * Validates an airport's latitude passed as a String. The String should represent a real number between -90 and 90,
     * where negative numbers indicate south and positive ones indicate north.
     * @param latString the airport's latitude.
     * @return  True if the String represents a real number between -90 and 90, false otherwise.
     */
    public static boolean validateLat(String latString) {
        Double lat;

        try {
            lat = Double.parseDouble(latString);

            return lat >= -90 && lat <= 90;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates an airport's longitude passed as a String. The String should represent a real number between -180 and
     * 180, where negative numbers indicate west and positive ones indicate east.
     * @param lngString the airport's longitude.
     * @return  True if the String represents a real number between -180 and 180, false otherwise.
     */
    public static boolean validateLng(String lngString) {
        Double lng;

        try {
            lng = Double.parseDouble(lngString);

            return lng >= -180 && lng <= 180;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates an flight's ID number passed as a String. The String should represent a whole number equal or greater
     * than zero.
     * @param flightID the flight's ID.
     * @return  True if the String represents a whole number equal or greater than zero, false otherwise.
     */
    public static boolean validateFlightNumber(String flightID) {
        Integer num;

        try {
            num = Integer.parseInt(flightID);

            return num >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates the duration of the flight passed as a String.
     * @param timeString
     * @return
     */
    public static boolean validateFlightDuration(String timeString) {
        String timeArr[] = timeString.split("h");
        String hourString = timeArr[0];
        String minString = timeArr[1];

        minString = minString.replace("m", "");
        //We assume a flight cannot last more than 99hs.
        Integer hours = Integer.parseInt(hourString);

        if (hours < 0) {
            return false;
        }

        Integer minutes = Integer.parseInt(minString);

        return minutes >= 0 && minutes <= 59;
    }

    public static boolean validatePrice(String price) {
        try {
            Double p = Double.valueOf(price);
        } catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean validateDay(String days) {
        String[] aux = days.split("-");
        for (String each : aux)
            if (!(each.equals("Lu") || each.equals("Ma") || each.equals("Mi") || each.equals("Ju") || each.equals("Vi")
                || each.equals("Sa") || each.equals("Do")))
                return false;
        return true;
    }

    public static boolean validateOrigin(String origin, FlightSystem flightSystem) {
        for (Airport a : flightSystem.getAirportList())
            if (a.getName().equals(origin)) {
                return true;
            }
        return false;
    }

    public static boolean validateDestiny(String destiny, FlightSystem flightSystem) {
        for (Airport a : flightSystem.getAirportList())
            if (a.getName().equals(destiny))
                return true;
        return false;
    }

    public static boolean validateDepartureTime(String time) {
        String[] aux = time.split(":");
        Integer hour, min;
        try {
            hour = Integer.parseInt(aux[0]);
            min = Integer.parseInt(aux[1]);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public static boolean validateLineFile(String line, FlightSystem flightSystem) {
        //[aerolinea]#[nroVuelo]#[diasSemana]#[origen]#[destino]#[horaSalida]#[duracion]#[precio]
        //[nombre]#[lat]#[lng]
        String aux[] = line.split("#");
        if (aux.length == 8) {
            boolean check = validateName(aux[0]) && validateFlightNumber(aux[1]);
            boolean check2 = validateDay(aux[2]) && validateOrigin(aux[3], flightSystem);
            boolean check3 = validateDestiny(aux[4], flightSystem) && validateDepartureTime(aux[5]);
            boolean check4 = validateFlightDuration(aux[6]) && validatePrice(aux[7]);

            return check && check2 && check3 && check4;
        } else if (aux.length == 3) {
            return validateName(aux[0]) && validateLat(aux[1]) && validateLng(aux[2]);
        } else {
            return false;
        }
    }

}
