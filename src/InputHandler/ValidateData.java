package InputHandler;

public class ValidateData {

    public static boolean validateAero(String aero) {
        return aero.matches(".") && aero.length() > 0 && aero.length() <= 3;
    }

    public static boolean validateFlightNumber(String num) {
        try {
            Integer.parseInt(num);
        } catch (Exception e) {
            return false;
        }
        return false;
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
//    horaSalida hora de salida en formato 24 hs. Ej 01:03.
//    duracion: (xxh)(yym) con xx numero mayor o igual a 0 indicando las horas. yy numero entre 0 y 59 indicando minutos. las horas son opcionales, ej: 34m.
//            precio: precio en pesos. numero mayor o igual a 0 con a lo sumo 2 decimales.

}
