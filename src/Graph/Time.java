package Graph;

/**
 * Created by cderienzo on 10/24/2017.
 */
public class Time {
    private double minutes;

    public Time(int hours, int minutes){
        this.minutes = (hours * 60) + minutes;
    }

    public Time(double totalMinutes) {
        this.minutes = totalMinutes;
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

    public void setMinutes(double minutes){this.minutes = minutes;}

    public int getHour(){return ((int) minutes/60);}

    public int getMinutes(){return ((int) minutes%60);}

    public double getAllMinutes(){return minutes;}

    public Time addTime(Time t) {
        return new Time(this.minutes + t.getAllMinutes());
    }
}
