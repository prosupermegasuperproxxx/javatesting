import java.util.TreeMap;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Random;

import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

class MyProgressUI extends BasicProgressBarUI {
    public static final Color bluex      = new Color(120, 30, 255);

    private Rectangle r = new Rectangle();

    @Override
    protected void paintDeterminate(Graphics g, JComponent c) {

        if (!(g instanceof Graphics2D)) {
            return;
        }


        Insets b = progressBar.getInsets(); // area for border
        int barRectWidth = progressBar.getWidth() - (b.right + b.left);
        int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);

        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        int cellLength = getCellLength();
        int cellSpacing = getCellSpacing();
        // amount of progress to draw
        int amountFull = getAmountFull(b, barRectWidth, barRectHeight);

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(progressBar.getForeground());

        TreeMap<Integer, Integer> r2    = ((MyJProgressBar) progressBar).ranges2;

//            for (int i = 0; i < rng.length; i++) {
////                    g2.drawLine(b.left, (barRectHeight/2) + b.top,
////                            amountFull + b.left, (barRectHeight/2) + b.top);
//                //g2.drawLine();
//            }
          /*  if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
                // draw the cells
                if (cellSpacing == 0 && amountFull > 0) {
                    // draw one big Rect because there is no space between cells
                    g2.setStroke(new BasicStroke((float)barRectHeight,
                            BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                } else {
                    // draw each individual cell
                    g2.setStroke(new BasicStroke((float)barRectHeight,
                            BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                            0.f, new float[] { cellLength, cellSpacing }, 0.f));
                }

                if(c.getComponentOrientation().isLeftToRight()) {
//                    if (BasicGraphicsUtils.isLeftToRight(c)) {
                    g2.drawLine(b.left, (barRectHeight/2) + b.top,
                            amountFull + b.left, (barRectHeight/2) + b.top);
                } else {
                    g2.drawLine((barRectWidth + b.left),
                            (barRectHeight/2) + b.top,
                            barRectWidth + b.left - amountFull,
                            (barRectHeight/2) + b.top);
                }

            } else { // VERTICAL
                // draw the cells
                if (cellSpacing == 0 && amountFull > 0) {
                    // draw one big Rect because there is no space between cells
                    g2.setStroke(new BasicStroke((float)barRectWidth,
                            BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                } else {
                    // draw each individual cell
                    g2.setStroke(new BasicStroke((float)barRectWidth,
                            BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                            0f, new float[] { cellLength, cellSpacing }, 0f));
                }

                g2.drawLine(barRectWidth/2 + b.left,
                        b.top + barRectHeight,
                        barRectWidth/2 + b.left,
                        b.top + barRectHeight - amountFull);
            }*/

//            if (cellSpacing == 0 && amountFull > 0) {
//                // draw one big Rect because there is no space between cells
//            } else {
//                // draw each individual cell
//            }
        Stroke oldStroke = g2.getStroke();
        Stroke newStroke = new BasicStroke((float)barRectHeight,
                BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
            g2.setStroke(newStroke);
//                g2.setStroke(new BasicStroke((float)barRectHeight,
//                        BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
//                        0.f, new float[] { cellLength, cellSpacing }, 0.f));

//            g2.drawLine(b.left, (barRectHeight/2) + b.top,
//                    amountFull + b.left, (barRectHeight/2) + b.top);
//            g2.drawLine(b.left, (barRectHeight/2) + b.top + 10,
//                    amountFull + b.left, (barRectHeight/2) + b.top +10);

//            b.left += 250;
//            g2.drawLine(b.left, (barRectHeight/2) + b.top,
////                    250 + b.left, (barRectHeight/2) + b.top);
//            g2.drawLine(10, (barRectHeight/2) + b.top,
//                    barRectWidth-10, (barRectHeight/2) + b.top);

//            int i1 = barRectWidth / 3;
//            Random rr = new Random();
//            for (int i = 0; i < 3; i++) {
//
//
////                        rr.nextInt(i1-50) + i1*i, (barRectHeight/2) + b.top);
//            }
        if(r2!=null && r2.size()>0) {
            final int[] l = {0, 0, 0, 0};
            l[1] = r2.lastKey();
            r2.forEach((max, val) -> {
                l[2] += (l[0] - val);
                l[3] += (max - val);
                if (max > 0) {
                    double per = ((0D + l[0]) / l[1]); //double per = (l[0] * 100D / l[1]) / 100D;
                    int lP = (int) Math.round(barRectWidth * per);
                    per = (((double) val) / l[1]);
                    int rP = (int) Math.round(barRectWidth * per);

                    g2.setStroke(newStroke);
                    g2.drawLine(lP, (barRectHeight / 2) + b.top,
                            rP, (barRectHeight / 2) + b.top);

                    if (max < l[1]) {
                        g2.setStroke(oldStroke);
                        per = (((double) max) / l[1]);
                        int bP = (int) Math.round(barRectWidth * per);
                        g2.drawLine(bP, barRectHeight + b.top,
                                bP, (0) + b.top);
                    }
                }
                l[0] = max;
            });
//            amountFull = (int)Math.round(width *
//                    progressBar.getPercentComplete());

            g2.setColor(bluex);
            g2.setStroke(oldStroke);
            double per = ((-l[2]) / (0D + l[0])); //double per = (l[0] * 100D / l[1]) / 100D;
            int lP = (int) Math.round(barRectWidth * per);

            g2.drawLine(b.left, (barRectHeight / 2) + b.top,
                    lP + b.left, (barRectHeight / 2) + b.top);

            per = ((l[0] - l[3]) / (0D + l[0])); //double per = (l[0] * 100D / l[1]) / 100D;
            lP = (int) Math.round(barRectWidth * per);

            g2.drawLine(b.left, (barRectHeight / 2) + b.top + 5,
                    lP + b.left, (barRectHeight / 2) + b.top + 5);
        }

        // Deal with possible text painting
        if (progressBar.isStringPainted()) {
            paintString(g, b.left, b.top,
                    barRectWidth, barRectHeight,
                    amountFull, b);
        }
    }

    @Override
    protected void paintIndeterminate(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        r = getBox(r);
//                progressBar.ranges
        g.setColor(progressBar.getForeground());
        g.fillOval(r.x, r.y, r.width, r.height);
    }
}

public class MyJProgressBar extends JProgressBar {

        public TreeMap<Integer, Integer> ranges2 = new TreeMap<>();

        public MyJProgressBar() {
            this.setUI(new MyProgressUI());
            this.setForeground(Color.blue);
            this.setIndeterminate(false);
        }

    }
