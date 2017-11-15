package InputHandler;

import Graph.Airport;
import Graph.FlightSystem;

public class ValidateData {

    //In this class we validate data

    public static boolean validateName(String aero) {
        return aero.matches("[a-zA-Z]{1,3}");
    }

    public static boolean validateLat(String lat){
        Double aux;
        try {
            aux = Double.parseDouble(lat);
            return aux >= -90 && aux <= 90;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validateLon(String lat){
        Double aux;
        try {
            aux = Double.parseDouble(lat);
            return aux >= -180 && aux <= 180;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validateFlightNumber(String num) {
        Integer aux;
        try {
            aux = Integer.parseInt(num);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validateLong(String longTime) {
        String aux[] = longTime.split("h");
        String hour = aux[0];
        String min = aux[1];
        min = min.replace("m", "");
        //We assume a flight cant last more than 99hs
        Integer auxH = Integer.parseInt(hour);
        if ( ! (auxH>= 0) ) return false;
        Integer auxM = Integer.parseInt(min);
        return auxM >= 0 && auxM <= 59;
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
        System.out.println(origin);
        for (Airport a : flightSystem.getAirportList())
            if (a.getName().equals(origin)) {
                return true;
            }
        return false;
    }

    public static boolean validateDestiny(String destiny, FlightSystem flightSystem) {
        System.out.println(destiny);
        for (Airport a : flightSystem.getAirportList())
            if (a.getName().equals(destiny))
                return true;
        return false;
    }

    public static boolean validateTime(String time) {
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
            boolean check3 = validateDestiny(aux[4], flightSystem) && validateTime(aux[5]);
            boolean check4 = validateLong(aux[6]) && validatePrice(aux[7]);

            return check && check2 && check3 && check4;
        } else if (aux.length == 3) {
            return validateName(aux[0]) && validateLat(aux[1]) && validateLon(aux[2]);
        } else {
            return false;
        }
    }

}
