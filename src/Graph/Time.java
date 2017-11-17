package Graph;

public class Time {
    private int minutes;

    Time(int hours, int minutes){
        this.minutes = (hours * 60) + minutes;
    }

    Time(int totalMinutes) {
        this.minutes = totalMinutes;
    }

    @Override
    public String toString() {
        if((minutes/10)<=0){
            return getHour() + "h" + 0 + getMinutes() + "m";
        }
        else {
            return getHour() + "h" + getMinutes() + "m";
        }
    }

    private int getHour() {
        return minutes/60;
    }

    private int getMinutes() {
        return minutes%60;
    }

    int getAllMinutes() {
        return minutes;
    }
}
