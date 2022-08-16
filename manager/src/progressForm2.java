import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class progressForm2 {
    
    

    public static class ProgressBarUITest extends JPanel {
        MyJProgressBar jpb = new MyJProgressBar();
        ProgressBarUITest() {
//            this.setLayout( new GridLayout() );
            this.add(jpb);
            Dimension size = new Dimension(1000, 50);
            jpb.setMinimumSize( size );
            jpb.setPreferredSize( size );
            jpb.setMaximumSize( size );
//            this.setSize(1000, 50);
        }



        private void display() {
            JFrame f = new JFrame("ProgressBarUITest");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(this);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        }

        public void main(String[] args) {
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    ProgressBarUITest test = new ProgressBarUITest();
                    test.display();
//                    test.jpb.mini
                    test.jpb.setMaximum(3000);
                    test.jpb.setMinimum(0);
//                    test.jpb.setValue(1100);
                    test.jpb.ranges2.clear();
//                    test.jpb.ranges2.put(0,0);
//                    test.jpb.ranges2.put(750,700); // value 0
//                    test.jpb.ranges2.put(1000,950); // value 0
//                    test.jpb.ranges2.put(2000,2000-50); // value 0
//                    test.jpb.ranges2.put(3000,2950); // value 0
//                    test.jpb.ranges2.replace(test.jpb.ranges2.ceilingKey(3000-10), 2500); // value 0


//                    test.jpb.setValue(97);
//                    test.jpb.getRange(4);
//                    test.jpb.setRange(200,400,350);
//                  -5000,-5000,  3000,3000  
                }
            });
        }
    }

}
