package tbgame;

import java.awt.*;
import java.awt.geom.*;

class Wall extends GameObject {
    Wall(double width, double height, double x, double y, boolean isCollider, Color c, GamePanel p) {
        super(p, new Point2D.Double(width, height - 10), new Point2D.Double(x, y + 5), isCollider);
        color = c;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        Point2D.Double p1 = new Point2D.Double(location.x + 10, location.y - 5);
        Point2D.Double p2 = new Point2D.Double(location.x + 10, location.y + size.y + 5);

        g2.draw(new Line2D.Double(p1, p2));
    }

    public void update() {

    }

    public void beforeRemove() {

    }

}