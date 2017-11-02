package Graph;

/**
 * Created by Bianca on 31/10/2017.
 */
public class Day {
    private int dayNumber;
    private String dayName;

    public Day(String day){
        this.dayName = day;
        switch (day){
            case "Lu":
                this.dayNumber = 0;
                break;
            case "Ma":
                this.dayNumber = 1;
                break;
            case "Mi":
                this.dayNumber = 2;
                break;
            case "Ju":
                this.dayNumber = 3;
                break;
            case "Vi":
                this.dayNumber = 4;
                break;
            case "Sa":
                this.dayNumber = 5;
                break;
            case "Do":
                this.dayNumber = 6;
                break;
        }
    }

    public Day(int dayNumber){
        this.dayNumber = dayNumber;
        switch (dayNumber){
            case 0:
                this.dayName = "Lu";
                break;
            case 1:
                this.dayName = "Ma";
                break;
            case 2:
                this.dayName = "Mi";
                break;
            case 3:
                this.dayName = "Ju";
                break;
            case 4:
                this.dayName = "Vi";
                break;
            case 5:
                this.dayName = "Sa";
                break;
            case 6:
                this.dayName = "Do";
                break;
        }
    }

    public String getDayName(){return dayName;}

    public int getDayNumber(){return dayNumber;}
}
