package tbgame;

import java.awt.*;
import java.awt.geom.*;

public class Weapon extends GameObject {

    Match soilder;
    // Point2D.Double gunPoint;
    Point2D.Double bulletSpeed = new Point2D.Double(10, 0);
    double bulletSize = 6;
    int attack;

    Weapon(Match m, Point2D.Double g, int a, GamePanel p) {
        super(p, new Point2D.Double(0, 0), g, false);
        soilder = m;
        attack = a;
    }

    public void fire() {
        Point2D.Double bs = new Point2D.Double(bulletSpeed.x * soilder.direction + soilder.speed.x * 0.1, 0);
        double bx = soilder.location.x + (soilder.size.x - 6) * (soilder.direction + 1) / 2;
        double by = (soilder.location.y + soilder.location.y + soilder.size.y) / 2 - 6;
        Point2D.Double gunPoint = new Point2D.Double(bx, by);
        gp.preObjs.add(new Bullet(this, bs, bulletSize, gunPoint, Color.ORANGE, gp));
    }

    public void paint(Graphics g) {
    }

    public void update() {

    }

    public void beforeRemove() {

    }
}
