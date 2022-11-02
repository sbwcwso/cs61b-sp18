public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    private static final double gravitational_constant = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        return Math.sqrt(Math.pow(xxPos - p.xxPos, 2) + Math.pow(yyPos - p.yyPos, 2));
    }

    public double calcForceExertedBy(Planet p) {
        return gravitational_constant * mass * p.mass / Math.pow(calcDistance(p), 2);
    }

    public double calcForceExertedByX(Planet p) {
        return calcForceExertedBy(p) * (p.xxPos - xxPos) / calcDistance(p);
    }

    public double calcForceExertedByY(Planet p) {
        return calcForceExertedBy(p) * (p.yyPos - yyPos) / calcDistance(p);
    }

    public double calcNetForceExertedByX(Planet[] planets) {
        double NetForceExertedByX = 0;
        for (Planet p : planets) {
            if (equals(p)) continue;
            NetForceExertedByX += calcForceExertedByX(p);
        }
        return NetForceExertedByX;
    }

    public double calcNetForceExertedByY(Planet[] planets) {
        double NetForceExertedByY = 0;
        for (Planet p : planets) {
            if (equals(p)) continue;
            NetForceExertedByY += calcForceExertedByY(p);
        }
        return NetForceExertedByY;
    }

    public void update(double dt, double fX, double fY) {
        xxVel += dt * (fX / mass);
        yyVel += dt * (fY / mass);
        xxPos += dt * xxVel;
        yyPos += dt * yyVel;
    }

    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }
}
