package tbgame;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

abstract class GameObject {
    boolean gravity = false;
    Point2D.Double size;
    Point2D.Double location;
    int direction = 1;
    Point2D.Double speed = new Point2D.Double(0, 0);
    Point2D.Double acceleration = new Point2D.Double(0, 0);
    boolean isCollider;
    GamePanel gp;
    Color color;
    boolean visible = true;
    double speedRate = 1;
    ArrayList<GameObject> colliders;

    GameObject(GamePanel p, Point2D.Double s, Point2D.Double l, boolean c) {
        gp = p;
        size = s;
        location = l;
        isCollider = c;
        collision();
    }

    public void physics() {
        collision();
        if (gravity) {
            boolean flag = true;
            if (!colliders.isEmpty()) {
                for (GameObject obj : colliders) {
                    if (obj instanceof Floor && obj.isCollider && isCollider) {
                        double error = location.y + size.y - obj.location.y;
                        if (error < 25) {
                            location.y -= error;
                            speed.y = 0;
                            flag = false;
                            break;
                        }
                    }
                }
            }

            if (flag) {
                speed.y += acceleration.y;

                if (acceleration.y > 0.3) {
                    acceleration.y -= 0.02;
                } else if (acceleration.y < 0.3) {
                    acceleration.y += 0.02;
                }
            }

        }

        if (isCollider) {
            for (GameObject obj : colliders) {

                if (obj instanceof Wall && obj.isCollider) {

                    location.x -= speed.x * gp.timeRate * speedRate;
                    break;
                }

            }
        }

    }

    public void move() {
        location.x += speed.x * gp.timeRate * speedRate;
        location.y += speed.y * gp.timeRate * speedRate;
    }

    public void collision() {
        ArrayList<GameObject> rlist = new ArrayList<GameObject>();
        for (GameObject obj : gp.objs) {
            if (obj == this)
                continue;
            double x1 = obj.location.x;
            double x2 = obj.location.x + obj.size.x;
            double y1 = obj.location.y;
            double y2 = obj.location.y + obj.size.y;
            double x3 = location.x;
            double x4 = location.x + size.x;
            double y3 = location.y;
            double y4 = location.y + size.y;

            double zx = Math.abs(x1 + x2 - x3 - x4);
            double x = Math.abs(x1 - x2) + Math.abs(x3 - x4);
            double zy = Math.abs(y1 + y2 - y3 - y4);
            double y = Math.abs(y1 - y2) + Math.abs(y3 - y4);
            if (zx <= x && zy <= y)
                rlist.add(obj);
        }

        colliders = rlist;
    }

    public abstract void paint(Graphics g);

    public abstract void update();

    public abstract void beforeRemove();

}