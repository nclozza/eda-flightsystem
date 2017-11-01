package Graph;

/**
 * Created by cderienzo on 10/24/2017.
 */
public class Time {
    private double minutes;

    public Time(int hours, int minutes){
        this.minutes= hours*60 + minutes;
    }

    @Override
    public String toString() {
        if((minutes/10)<=0){
            return getHour()+"h"+0+getMinutes()+"m";
        }
        else {
            return getHour()+"h"+getMinutes()+"m";
        }

    }

    public String increaseDay(String previous, int numberOfDays){
        Day prev = new Day(previous);
        Day landing = new Day(prev.getDayNumber() + numberOfDays);
        return landing.getDayName();
    }


    public void setMinutes(double minutes){this.minutes = minutes;}

    public double getHour(){return (minutes/60);}

    public double getMinutes(){return (minutes%60);}
}
