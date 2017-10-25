package InputHandler;

public class ValidateData {

    //In this class we validate data

    public static boolean validateName(String aero) {
        return aero.matches(".") && aero.length() > 0 && aero.length() <= 3;
    }

    public static boolean validateLat(String lat){
        Integer aux;
        try {
            aux = Integer.parseInt(lat);
            if (aux >= -90 && aux <= 90){
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validateLon(String lat){
        Integer aux;
        try {
            aux = Integer.parseInt(lat);
            if (aux >= -180 && aux <= 180){
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validateFlightNumber(String num) {
        Integer aux;
        try {
            aux = Integer.parseInt(num);
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean validateLong(String longTime){
        char[] aux = longTime.toCharArray();
        //We assume a flight cant last more than 99hs
        Integer auxH = Integer.parseInt(String.valueOf(aux[1]) + String.valueOf(aux[2]));
        if ( ! (auxH>= 0) ) return false;
        Integer auxM = Integer.parseInt(String.valueOf(aux[6]) + String.valueOf(aux[7]));
        if ( !(auxM >= 0 && auxM <= 59)) return false;

        return true;
    }

    public static boolean validatePrice(String price){
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
        for (int i = 0; i < aux.length; i++)
            if (!(aux[i].equals("Lu") || aux[i].equals("Ma") || aux[i].equals("Mi") || aux[i].equals("Ju") || aux[i].equals("Vi")
                    || aux[i].equals("Sa") || aux[i].equals("Do")))
                return false;
        return true;
    }

    public static boolean validateOrigin(String origin) {
        return false;
        //Dont know how to do it, yet
    }

    public static boolean validateDestiny(String destiny){
        return false;
        //Dont know how to do it yet
    }

    //    origen: aeropuerto partida (debe existir)
//    destino: aeropuerto llegada (debe existir).

    public static boolean validateTime(String time){
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


//    duracion: (xxh)(yym) con xx numero mayor o igual a 0 indicando las horas. yy numero entre 0 y 59 indicando minutos. las horas son opcionales, ej: 34m.
//            precio: precio en pesos. numero mayor o igual a 0 con a lo sumo 2 decimales.

}
