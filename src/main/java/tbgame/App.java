package tbgame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.lang.Object.*;

import java.util.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.regex.*;
import java.awt.geom.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class App {

    static JFrame fr;
    static UIPanel uPanel;

    public static void main(String[] args) throws Exception {
        fr = new JFrame();
        fr.setSize(1200, 700);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.getContentPane().setBackground(new Color(0, 0, 0));
        fr.setResizable(false);
        uPanel = new UIPanel();
        uPanel.button.addActionListener(e -> {
            uPanel.setVisible(false);
            Thread t = new Thread(() -> {
                GamePanel gPanel = new GamePanel();
                fr.add(gPanel);
                gPanel.requestFocus();
                gPanel.update();
            });
            t.start();
        });
        fr.add(uPanel);
        fr.setVisible(true);

    }
}

@SuppressWarnings("serial")
class GamePanel extends JPanel implements KeyListener {
    HashMap<Integer, Boolean> keyStroke;

    ArrayList<GameObject> objs = new ArrayList<GameObject>();
    ArrayList<GameObject> preObjs = new ArrayList<GameObject>();
    ArrayList<GameObject> remObjs = new ArrayList<GameObject>();
    ArrayList<Item> itemList = new ArrayList<Item>();

    ArrayList<Point2D.Double> p = new ArrayList<Point2D.Double>();

    HashMap<Point2D.Double, Boolean> used = new HashMap<Point2D.Double, Boolean>();
    double timeRate = 1;

    GamePanel() {
        this.setOpaque(false);
        this.setLayout(null);
        keyStroke = new HashMap<Integer, Boolean>();
        keyStroke.put(68, false); // d
        keyStroke.put(65, false); // a
        keyStroke.put(32, false); // space
        keyStroke.put(70, false); // f
        keyStroke.put(87, false); // w
        keyStroke.put(83, false); // s
        keyStroke.put(37, false); // left
        keyStroke.put(38, false); // up
        keyStroke.put(39, false); // right
        keyStroke.put(40, false); // down
        keyStroke.put(79, false); // o
        keyStroke.put(80, false); // p
        load("map1.txt");
        addKeyListener(this);
    }

    public void load(String path) {
        java.util.List<String> lines = new java.util.ArrayList<String>();
        try {
            InputStream in = getClass().getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            for (int i = 0; i < 4; i++) {
                lines.add(reader.readLine());
            }
        } catch (Exception e) {
            System.err.println("map lost");
        }

        for (int i = 0; i < 4; i++) {
            String str = lines.get(i);
            Pattern concel = Pattern.compile("//");
            if (concel.matcher(str).find())
                continue;

            Pattern coordinate = Pattern.compile("([-0-9]\\d*)");
            Matcher matcher = coordinate.matcher(str);
            ArrayList<Double> info = new ArrayList<Double>();
            while (matcher.find()) {
                info.add(Double.parseDouble(matcher.group()));
            }
            double head = info.get(0);

            for (int j = 1; j <= info.size() - 1 && head < 3; j += 6) {
                boolean g = info.get(j + 4) == 1 ? true : false;
                Color c = Color.CYAN; // ini
                double t = info.get(j + 5);
                switch ((int) t) {
                case 0:
                    c = Color.CYAN;
                    break;
                case 1:
                    c = Color.ORANGE;
                    break;
                case 2:
                    c = Color.GRAY;
                    break;
                }
                switch ((int) head) {
                case 0:
                    objs.add(new Floor(info.get(j), info.get(j + 1), info.get(j + 2), info.get(j + 3), g, c, this));
                    break;
                case 1:
                    objs.add(new Wall(info.get(j), info.get(j + 1), info.get(j + 2), info.get(j + 3), g, c, this));
                    break;
                case 2:
                    objs.add(new Ladder(info.get(j), info.get(j + 1), info.get(j + 2), info.get(j + 3), g, c, this));
                    break;

                }

            }

            if (head == 3) {
                for (int j = 1; j < info.size() - 1; j += 2) {
                    p.add(new Point2D.Double(info.get(j), info.get(j + 1)));
                }
            }

        }
        objs.add(new Match(0, "matchMan.png", "matchMan_r.png", 49, 70, 10, 0, true, this));
        objs.add(new Match(1, "matchMan.png", "matchMan_r.png", 49, 70, 800, 0, true, this));
        itemList.add(new Item(this, "fast.png"));
        itemList.add(new Item(this, "heart.png"));
        itemList.add(new Item(this, "shield.png"));
        itemList.add(new Item(this, "arrow.png"));
        for (Point2D.Double point : p) {
            used.put(point, false);
        }
        itemInitializer();
        objs.addAll(itemList);
    }

    public void itemInitializer() {

        for (Item item : itemList) {
            if (!item.ini) {
                Runnable start = () -> {
                };
                ActionListener end = e -> {
                };
                item.locate();
                item.ini = true;
                if (item.path == "fast.png") {
                    start = () -> {
                        item.activated = true;
                        item.solider.speedRate = 2;
                        item.visible = false;
                        used.put(item.location, false);
                    };
                    end = e -> {
                        item.activated = false;
                        item.solider.speedRate = 1.0;
                        item.visible = true;
                        item.locate();
                        item.timer.stop();
                    };
                } else if (item.path == "heart.png") {
                    start = () -> {
                        item.activated = true;
                        item.solider.hp += 20;
                        if (item.solider.hp > 120) {
                            item.solider.hp = 120;
                        }
                        item.visible = false;
                        used.put(item.location, false);
                    };

                    end = e -> {
                        item.activated = false;
                        item.visible = true;
                        item.locate();
                        item.timer.stop();
                    };
                } else if (item.path == "shield.png") {
                    start = () -> {
                        item.activated = true;
                        item.visible = false;
                        item.solider.shieldBuff = true;
                        used.put(item.location, false);
                    };

                    end = e -> {
                        item.activated = false;
                        item.visible = true;
                        item.solider.shieldBuff = false;
                        item.locate();
                        item.timer.stop();
                    };
                } else if (item.path == "arrow.png") {
                    start = () -> {
                        item.activated = true;
                        item.visible = false;
                        item.solider.gun.bulletSpeed.x = 15;
                        used.put(item.location, false);
                    };

                    end = e -> {
                        item.activated = false;
                        item.visible = true;
                        item.solider.gun.bulletSpeed.x = 10;
                        item.locate();
                        item.timer.stop();
                    };
                }
                item.start = start;
                item.end = end;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        try {
            for (GameObject obj : objs) {
                if (obj.visible) {
                    obj.paint(g);
                }
            }
        } catch (Exception e) {
            System.out.println("nothing wrong, don't need to care");
        }
    }

    long start;
    volatile long end;

    public void update() {
        while (true) {
            end = start - 1;
            start = System.currentTimeMillis();

            for (GameObject obj : objs) {
                if (obj.location.x < -1000 || obj.location.x > 3000 || obj.location.y < -1000
                        || obj.location.y > 3000) {
                    remObjs.add(obj);
                    continue;
                }
                obj.update();
            }

            if (!preObjs.isEmpty()) {
                objs.addAll(preObjs);
                preObjs.clear();
            }

            for (GameObject obj : remObjs) {
                obj.beforeRemove();
                objs.remove(obj);
            }
            remObjs.clear();

            repaint();

            EventQueue.invokeLater(() -> {
                end = System.currentTimeMillis();
            });

            while (end < start) {
                continue;
            }

            try {
                long time = end - start;
                // /System.out.println(time);
                if (time < 16) {
                    Thread.sleep(16 - time);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void keyPressed(KeyEvent e) {
        // System.out.println(e.getKeyCode());
        if (keyStroke.containsKey(e.getKeyCode())) {
            keyStroke.put(e.getKeyCode(), true);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (keyStroke.containsKey(e.getKeyCode())) {
            keyStroke.put(e.getKeyCode(), false);
        }
    }

    public void keyTyped(KeyEvent e) {

    }

}
