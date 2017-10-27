package Outputs;

/**
 * Created by cderienzo on 10/24/2017.
 */
public class Time {
    int hour,minute;
    public Time(int hour, int minutes){
        this.hour=hour;
        this.minute=minutes;
    }

    @Override
    public String toString() {
        if((minute/10)<=0){
            return hour+"h"+0+minute+"m";
        }
        else {
            return hour+"h"+minute+"m";
        }

    }
}
