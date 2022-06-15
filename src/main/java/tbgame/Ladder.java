package tbgame;

import java.awt.*;
import java.awt.geom.*;

class Ladder extends GameObject {
    Ladder(double width, double height, double x, double y, boolean isCollider, Color c, GamePanel p) {
        super(p, new Point2D.Double(width, height), new Point2D.Double(x, y), isCollider);
        color = c;
        gp.objs.add(new Edge(p, width, 20, x, y + height, false));
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        Point2D.Double p1 = new Point2D.Double(location.x - 5, location.y);
        Point2D.Double p2 = new Point2D.Double(location.x + size.x + 5, location.y);
        Point2D.Double p3 = new Point2D.Double(location.x - 5, location.y + size.y);
        Point2D.Double p4 = new Point2D.Double(location.x + size.x + 5, location.y + size.y);
        g2.draw(new Line2D.Double(p1, p3));
        g2.draw(new Line2D.Double(p2, p4));
    }

    public void update() {

    }

    public void beforeRemove() {

    }

    class Edge extends GameObject {

        Edge(GamePanel p, double width, double height, double x, double y, boolean c) {
            super(p, new Point2D.Double(width, height), new Point2D.Double(x, y), c);
            visible = false;
        }

        public void paint(Graphics g) {

        }

        public void update() {

        }

        public void beforeRemove() {

        }

    }
}