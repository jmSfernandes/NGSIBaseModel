import java.util.Date;

public class Accelerometer extends NGSIBaseModel {

    double x;
    double y;
    double z;

    Date t;

    public Accelerometer(){}

    public Accelerometer(double x, double y, double z, Date t){
        this.x=x;
        this.y=y;
        this.z=z;
        this.t=t;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Date getT() {
        return t;
    }

    public void setT(Date t) {
        this.t = t;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

}
