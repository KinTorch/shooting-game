package tbgame;

import java.awt.*;
import java.awt.geom.*;
import tbgame.Ladder.Edge;
import javax.swing.*;

class Match extends GameObject {

    boolean firing;
    boolean jumping;
    Toolkit tool;
    Image man;
    Image manRe;
    Timer timer;
    int hp = 100;
    Weapon gun;
    int index;

    String name;
    JLabel hpLabel = new JLabel();
    JLabel nameLabel = new JLabel();
    boolean shieldBuff = false;

    Match(int i, String path1, String path2, double width, double height, double x, double y, boolean isCollider,
            GamePanel p) {
        super(p, new Point2D.Double(width, height), new Point2D.Double(x, y), isCollider);
        index = i;
        tool = gp.getToolkit();
        man = tool.getImage(getClass().getResource("matchMan.png"));
        manRe = tool.getImage(getClass().getResource("matchMan_r.png"));
        acceleration.y = 0.3;
        gravity = true;
        double gx = location.x + (size.x - 6) * (direction + 1) / 2;
        double gy = (location.y + location.y + size.y) / 2 - 6;
        gun = new Weapon(this, new Point2D.Double(gx, gy), 5, gp);
        timer = new Timer(100, e -> gun.fire());
        nameLabel.setText(name);
        gp.add(hpLabel);
        gp.add(nameLabel);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (direction == 1) {
            g.drawImage(man, (int) location.x, (int) location.y, (int) size.x, (int) size.y, gp);
        } else if (direction == -1) {
            g.drawImage(manRe, (int) location.x, (int) location.y, (int) size.x, (int) size.y, gp);
        }

        if (shieldBuff) {
            g2.setColor(Color.GREEN);
            g2.draw(new Ellipse2D.Double(location.x - 15, location.y, size.x + 30, size.x + 30));
        }
    }

    public int isOnLadder() {
        int flag = -1;
        for (GameObject obj : colliders) {
            if (obj instanceof Edge) {
                return 1;
            } else if (obj instanceof Ladder) {
                flag = 0;
            }
        }
        return flag;
    }

    public void update() {
        keyProcess();

        if (hp <= 0) {
            // dead
            gp.remObjs.add(this);
            timer.stop();
            gp.remove(hpLabel);
            gp.remove(nameLabel);
            return;
        }

        for (GameObject col : colliders) {
            if (col instanceof Bullet) {
                Bullet bul = (Bullet) col;
                if (bul.weapon != gun) {
                    if (shieldBuff) {
                        bul.weapon = gun;
                        bul.speed.x *= -1;
                        bul.direction *= -1;
                        if (gp.remObjs.contains(bul)) {
                            gp.remObjs.remove(bul);
                        }
                        if (!gp.objs.contains(bul)) {
                            gp.preObjs.add(bul);
                        }
                        bul.location.x += speed.x;
                        continue;
                    }
                    hp -= bul.weapon.attack;
                    speed.x += 5 * bul.direction;
                    gp.remObjs.add(bul);
                }
            }
        }

        move();
        physics();

        int error = direction == -1 ? 21 : 3;
        hpLabel.setBounds((int) (location.x + error), (int) location.y - 20, 100, 20);
        hpLabel.setForeground(Color.RED);
        nameLabel.setBounds((int) (location.x + error), (int) location.y - 35, 100, 20);
        nameLabel.setForeground(Color.GRAY);
        hpLabel.setText("HP:" + String.valueOf(hp));
        nameLabel.setText("Player" + String.valueOf(index));

    }

    public void beforeRemove() {
        int winner = index == 1 ? 0 : 1;
        JLabel over = new JLabel("Player"+winner+" wins");
        over.setForeground(Color.ORANGE);
        over.setBounds(550, 300, 100, 30);
        gp.removeAll();
        gp.add(over);
        gp.objs.clear();
        
        try {
            Thread.sleep(1000);
            gp.repaint();
            Thread.sleep(2500);
        } catch (Exception e) {

        }
        App.fr.remove(gp);
        App.uPanel.setVisible(true);

    }

    public void keyProcess() {
        if (index == 0) {
            if (gp.keyStroke.get(68)) {
                if (direction == -1) {
                    location.x += 12;
                }
                speed.x = 4;
                direction = 1;
            } else if (gp.keyStroke.get(65)) {
                if (direction == 1) {
                    location.x -= 12;
                }
                speed.x = -4;
                direction = -1;
            }
            if (!gp.keyStroke.get(68) && !gp.keyStroke.get(65)) {
                speed.x = 0;
            }

            if (gp.keyStroke.get(87) && isOnLadder() != -1) {
                // System.out.println(isOnLadder());
                gravity = false;
                speed.y = -4;
            } else if (gp.keyStroke.get(87) && isOnLadder() == -1) {
                if (!gravity) {
                    speed.y = 0;
                    gravity = true;
                }

            }

            if (gp.keyStroke.get(83) && isOnLadder() == 0) {
                gravity = false;
                speed.y = 4;
            } else if (gp.keyStroke.get(83) && isOnLadder() == -1) {
                gravity = true;
            } else if (gp.keyStroke.get(83) && isOnLadder() == 1) {
                if (!gravity) {
                    speed.y = 0;
                    gravity = true;
                }
            }

            if (!gp.keyStroke.get(87) && !gp.keyStroke.get(83)) {

                if (isOnLadder() == -1) {
                    gravity = true;
                } else if (!gravity) {

                    speed.y = 0;
                }
            }

            if (gp.keyStroke.get(32) && !jumping && speed.y == 0) {
                speed.y = -2;
                acceleration.y = -0.2;
                jumping = true;
            } else if (!gp.keyStroke.get(32)) {
                jumping = false;
            }

            if (gp.keyStroke.get(70) && !timer.isRunning()) {
                firing = true;
                gun.fire();
                timer.start();
            } else if (!gp.keyStroke.get(70)) {
                firing = false;
                timer.stop();
            }
        } else {
            if (gp.keyStroke.get(39)) {
                if (direction == -1) {
                    location.x += 12;
                }
                speed.x = 4;
                direction = 1;
            } else if (gp.keyStroke.get(37)) {
                if (direction == 1) {
                    location.x -= 12;
                }
                speed.x = -4;
                direction = -1;
            }
            if (!gp.keyStroke.get(39) && !gp.keyStroke.get(37)) {
                speed.x = 0;
            }

            if (gp.keyStroke.get(38) && isOnLadder() != -1) {
                // System.out.println(isOnLadder());
                gravity = false;
                speed.y = -4;
            } else if (gp.keyStroke.get(38) && isOnLadder() == -1) {
                if (!gravity) {
                    speed.y = 0;
                    gravity = true;
                }

            }

            if (gp.keyStroke.get(40) && isOnLadder() == 0) {
                gravity = false;
                speed.y = 4;
            } else if (gp.keyStroke.get(40) && isOnLadder() == -1) {
                gravity = true;
            } else if (gp.keyStroke.get(40) && isOnLadder() == 1) {
                if (!gravity) {
                    speed.y = 0;
                    gravity = true;
                }
            }

            if (!gp.keyStroke.get(38) && !gp.keyStroke.get(40)) {

                if (isOnLadder() == -1) {
                    gravity = true;
                } else if (!gravity) {

                    speed.y = 0;
                }
            }

            if (gp.keyStroke.get(80) && !jumping && speed.y == 0) {
                speed.y = -2;
                acceleration.y = -0.2;
                jumping = true;
            } else if (!gp.keyStroke.get(80)) {
                jumping = false;
            }

            if (gp.keyStroke.get(79) && !timer.isRunning()) {
                firing = true;
                gun.fire();
                timer.start();
            } else if (!gp.keyStroke.get(79)) {
                firing = false;
                timer.stop();
            }
        }

    }

}