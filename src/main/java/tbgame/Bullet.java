package tbgame;

import java.awt.*;
import java.awt.geom.*;

public class Bullet extends GameObject {

    Weapon weapon;

    Bullet(Weapon w, Point2D.Double v, double size, Point2D.Double location, Color c, GamePanel p) {
        super(p, new Point2D.Double(size, size), location, false);
        weapon = w;
        speed = v;
        color = c;
        direction = weapon.soilder.direction;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.draw(new Ellipse2D.Double(location.x, location.y, size.x, size.y));
    }

    public void update() {
        for (GameObject obj : colliders) {
            if (obj != weapon.soilder && obj.isCollider) {
                gp.remObjs.add(this);
            }
        }
        move();
        physics();

    }

    public void beforeRemove() {

    }
}