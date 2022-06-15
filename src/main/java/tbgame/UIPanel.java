package tbgame;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class UIPanel extends JPanel {
    JButton button = new JButton("START");;
    Toolkit tool;
    Image manual;

    UIPanel() {
        tool = this.getToolkit();
        manual = tool.getImage(getClass().getResource("manual.png"));        
        this.setOpaque(false);
        this.setLayout(null);
        button.setBounds(1010, 570, 150, 70);
        button.setBackground(Color.darkGray);
        button.setForeground(Color.gray);
        this.add(button);

    }

    public void paintComponent(Graphics g) {
        g.drawImage(manual, 0, -10, manual.getWidth(this), manual.getHeight(this), this);
    }
}