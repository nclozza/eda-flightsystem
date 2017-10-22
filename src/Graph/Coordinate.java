package Graph;

/**
 * The Coordinate class serves as a geographical reference consisting of a latitude and a longitude.
 */
public class Coordinate {
    /**
     * The latitude is a real number between -90 and 90 that can have up to six decimals. A negative latitude indicates
     * south and a positive one indicates north.
     */
    private float latitude;
    /**
     * The longitude is a real number between -180 and 180 that can have up to six decimals. A negative longitude
     * indicates west and a positive one indicates east.
     */
    private float longitude;


    public Coordinate(float latitude, float longitude) {
        // The product and division by 10^6 is made to round up to six decimal places.
        this.latitude = (float) (latitude * Math.pow(10, 6) / Math.pow(10,6));
        this.longitude = (float) (longitude * Math.pow(10, 6) / Math.pow(10,6));
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
