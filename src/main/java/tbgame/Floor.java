package tbgame;

import java.awt.*;
import java.awt.geom.*;

class Floor extends GameObject {
    Floor(double width, double height, double x, double y, boolean isCollider, Color c, GamePanel p) {
        super(p, new Point2D.Double(width, height), new Point2D.Double(x, y), isCollider);
        color = c;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        Point2D.Double p1 = new Point2D.Double(location.x, location.y);
        Point2D.Double p2 = new Point2D.Double(location.x + size.x, location.y);

        g2.draw(new Line2D.Double(p1, p2));
    }


    public void update()
    {
        move();
        physics();
    }

    public void beforeRemove() {

    }
}