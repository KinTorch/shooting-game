package tbgame;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

class Item extends GameObject {
    Toolkit tool;
    Image item;
    Timer timer;
    boolean activated = false;
    Runnable start;
    ActionListener end;
    boolean ini = false;
    String path;
    Match solider;

    Item(GamePanel p, String pa) {
        super(p, new Point2D.Double(25, 25), new Point2D.Double(500, 610), false);
        tool = gp.getToolkit();
        path = pa;
        item = tool.getImage(getClass().getResource(path));
    }

    public void paint(Graphics g) {
        g.drawImage(item, (int) location.x, (int) location.y, (int) size.x, (int) size.y, gp);
    }

    public void update() {
        collision();
        acceleration.y = 3;
        move();
        physics();
        if (!activated) {
            for (GameObject obj : colliders) {
                if (obj instanceof Match) {
                    solider = (Match) obj;
                    Thread t = new Thread(start);
                    t.start();
                    timer = new Timer(8000, end);
                    timer.start();
                }
            }
        }
    }

    public void locate() {
        Random random = new Random();
        int r;
        do {
            r = random.nextInt(gp.p.size());
        } while (gp.used.get(gp.p.get(r)));
        location = gp.p.get(r);
        gp.used.put(location, true);

    }

    public void beforeRemove() {

    }
}