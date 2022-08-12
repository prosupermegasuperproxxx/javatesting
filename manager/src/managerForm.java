import java.awt.event.*;
import javax.swing.border.*;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.*;
/*
 * Created by JFormDesigner on Sat Jul 30 12:15:22 GMT+03:00 2022
 */



/**
 * @author Me
 */
public class managerForm extends JFrame {
    public static  DefaultTableModel rows_cur_model = null;
    public static  DefaultTableModel rows_hist_model;
    public static final Vector<Vector<Object>> rows_cur = new Vector<>();
    public static final Vector<Vector<Object>> rows_hist = new Vector<>();
    public static final Vector<Object> cols = new Vector<>();
    private static final int PROGRESS_COLUMN = 1;
    private static final String[] columnNames = {"filename", "progress", "filesize loaded", "filesize", "url", "savepath"};

    {
        Collections.addAll(cols, columnNames);


    }

    final GridLayout gly = new GridLayout();
    int toolbarButtonW = 78;
    int toolbarBCount = 1;

    /** GetScreenSize <br><br>
     * использование НАПРИМЕР: frame.setSize(GetScreenSize.getWidth() / 2,GetScreenSize.getHeight() >> 1);<br><br>
     * singleton non_thread_safe. В ПОТОКАХ НЕ БЕЗОПАСНЫЙ. ведёт себя неправильно в многопоточной среде.<br><br>
     * Несколько потоков могут одновременно вызвать метод получения singleton и создать сразу несколько экземпляров объекта.
     */
    public final static class GetScreenSize { //  использование НАПРИМЕР: frame.setSize(GetScreenSize.getWidth() / 2,GetScreenSize.getHeight() >> 1);
        // singleton non_thread_safe. В ПОТОКАХ НЕ БЕЗОПАСНЫЙ. ведёт себя неправильно в многопоточной среде.
        // Несколько потоков могут одновременно вызвать метод получения singleton и создать сразу несколько экземпляров объекта.
        private static GetScreenSize instance;
        private static Insets insets;
        private static int width;  private static int height;
        private GetScreenSize() { if (instance == null) {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            DisplayMode getDisplayMode = gd.getDisplayMode();
//            public DisplayMode getDisplayMode() {
//                GraphicsConfiguration gc = getDefaultConfiguration();
//                Rectangle r = gc.getBounds();
//                ColorModel cm = gc.getColorModel();
//                return new DisplayMode(r.width, r.height, cm.getPixelSize(), 0);
//            }
            width = getDisplayMode.getWidth();
            height = getDisplayMode.getHeight();
            instance = this;

            insets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());
//            insets = Toolkit.getDefaultToolkit().getScreenInsets(new java.awt.Component(){}.getGraphicsConfiguration());

        } }
        public static int getWidth()  { if (instance == null)  instance = new GetScreenSize(); return width; }
        public static int getHeight() { if (instance == null)  instance = new GetScreenSize(); return height; }
        public static int getWidthMinusWinToolbar() { if (instance == null)  instance = new GetScreenSize(); return width-(insets.left+insets.right); }
        public static int getHeightMinusWinToolbar() { if (instance == null)  instance = new GetScreenSize(); return height-(insets.top+insets.bottom); }
    }


    public void setupRanges(TreeMap<Integer, Integer> ranges2) {
        TableColumn col = table_current.getColumnModel().getColumn(1);
        ((ProgressRenderer) col.getCellRenderer()).ranges2 = ranges2;
        // ((ProgressRenderer)table1.getCellRenderer(0, 1)).ranges2 = ranges2;
    }

    class EventHandler {
        volatile boolean eventNotificationNotReceived;
        void waitForEventAndHandleIt() {
            while ( eventNotificationNotReceived ) {
                java.lang.Thread.onSpinWait();
            }
            readAndProcessEvent();
        }

        void readAndProcessEvent() {
            // Read event from some source and process it
              //. . .
        }
    }


    public void loadTables() {
        rows_cur_model = tableFromFile("current.txt", table_current, rows_cur);
        rows_hist_model = tableFromFile("history.txt", table_history, rows_hist);

        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(rows_cur_model);
        table_current.setRowSorter(sorter);
        ActionListener l = e -> {
            if(e!=null) {
                String s = e.getActionCommand();
                if (s == null || !s.equalsIgnoreCase("comboBoxEdited"))
                    return;
            } //System.out.println("2");
            String text = c_filter_current.getEditor().getItem().toString();
            // cancelled thread
            try {
                sorter.setRowFilter(RowFilter.regexFilter(text));
            } catch (PatternSyntaxException pse) {
                System.err.println("Bad regex pattern");
            }

        };


        if(c_filter_current.getEditor()!=null)
        c_filter_current.getEditor().getEditorComponent().addKeyListener(
                new KeyListener() {
                    @Override public void keyTyped(KeyEvent e) {}

                    @Override public void keyPressed(KeyEvent e) {}

                    @Override public void keyReleased(KeyEvent e) {
                        l.actionPerformed(null);
                    }
                }
        );



        c_filter_current.addActionListener(l);
//        Vector<Object> xcv = new Vector<>();
//        Collections.addAll(xcv, columnNames);
//        rows_cur.add(xcv);

//      JTable table = new JTable(model);

    }

    private static DefaultTableModel tableFromFile(String path, JTable table, Vector<Vector<Object>> rows_vector) {
        System.out.println(path);
        ArrayList<String> list = new ArrayList<>(100);
        loader.FileReadToList(path, list);
        listXyVector(list, rows_vector, true);

        DefaultTableModel rows_model = new DefaultTableModel(rows_vector, managerForm.cols);
        {
//            @Override public Class<?> getColumnClass(int column) {
////                return column == 1 ? DefaultComboBoxModel.class : String.class;
//                return column == 1 ? managerForm.ProgressRenderer.class : String.class;
//            }
        };
        table.setModel(rows_model);
        table.setRowHeight(24);
        table.setAutoCreateRowSorter(false);
        TableColumn col =  table.getColumnModel().getColumn(PROGRESS_COLUMN);
        col.setCellRenderer(new ProgressRenderer(0, 100));
        col.setCellEditor(null);//new ComboCellEditor());
        return rows_model;
    }

    private static void listXyVector(ArrayList<String> list, Vector<Vector<Object>> vector2D, boolean direct) {
        if(direct) {
            vector2D.clear();
            if(list.isEmpty()) return;
            list.forEach(s -> {
//                "z4d4kWk.jpg", "9", "", "", "https://i.imgur.com/z4d4kWk.jpg", "."
                String[] vals = s.split("\", \"");
                vals[0] = vals[0].substring(1);  // удалить кавычку
                vals[vals.length-1] = vals[vals.length-1].substring(0,vals[vals.length-1].length()-1); // удалить кавычку

                Vector<Object> xcv = new Vector<>();
                Collections.addAll(xcv, vals);

                vector2D.add(xcv);
            });

        } else {
            list.clear();
            if(vector2D.isEmpty()) return;
            StringBuilder xcv = new StringBuilder(200);
            vector2D.forEach(s -> {
//                "z4d4kWk.jpg", "9", "", "", "https://i.imgur.com/z4d4kWk.jpg", "."
//                String[] vals = s.split("\", \"");
//                vals[0] = vals[0].substring(1);  // удалить кавычку
//                vals[vals.length-1] = vals[vals.length-1].substring(vals[vals.length-1].length()-1); // удалить кавычку

                
                xcv.delete(0, xcv.length());
                xcv.append("\"");
                xcv.append( s.get(0) );
                for (int i = 1; i < s.size(); i++) {
                    xcv.append("\", \"");
                    xcv.append( s.get(i) );
                }
                xcv.append("\"");
                list.add(xcv.toString());
            });
        }
    }




    public managerForm() {
        initComponents();
//        initCustom();
//        loadTables();



        int width = GetScreenSize.getWidthMinusWinToolbar();
        int height = GetScreenSize.getHeightMinusWinToolbar();

        if(width>800)
            width>>=1;
        if(height>600)
            height>>=1;

        setSize(width, height );
//        pack();
//        setLocationRelativeTo(null);
        setLocationRelativeTo(null);

        panel1.setLayout(gly);
        toolbarBCount = panel1.getComponentCount();
//        toolbarW = panel1.getWidth();
//        if(toolbarW<panel1.getComponentCount()/gly.getRows() * toolbarButtonW)


    }

    private void panel1ComponentResized(ComponentEvent e) {
        final int w = panel1.getWidth();
//        int r = (int) Math.ceil((0.+toolbarBCount) / (int)(Math.floor((0.+w) / toolbarButtonW)));
        int r = w / toolbarButtonW;
//        int rows = 1;
//        if(toolbarBCount > r)
//        {
            int rows = toolbarBCount / r;
            if(rows*r<toolbarBCount) // Math.ceil  // узнали что % остаток от деления больше > 0
                rows++;
//            if(rows<1)
//                rows =1;
//        }
        if(gly.getRows()!=rows)
            gly.setRows(rows);
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        tabbedPane2 = new JTabbedPane();
        panel3 = new JPanel();
        scrollPane1 = new JScrollPane();
        table_current = new JTable();
        panel2 = new JPanel();
        panel1 = new JPanel();
        b_addURL = new JButton();
        b_delete = new JButton();
        b_start = new JButton();
        b_pause = new JButton();
        b_start_all = new JButton();
        b_pause_all = new JButton();
        b_toHistory = new JButton();
        c_filter_current = new JComboBox();
        c_url = new JComboBox();
        tabbedPane1 = new JTabbedPane();
        scrollPane2 = new JScrollPane();
        textArea1 = new JTextArea();
        panel4 = new JPanel();
        scrollPane3 = new JScrollPane();
        table_history = new JTable();
        panel5 = new JPanel();
        panel6 = new JPanel();
        b_reload = new JButton();
        b_delete2 = new JButton();
        c_filter_history = new JComboBox();
        c_url2 = new JComboBox();
        tabbedPane3 = new JTabbedPane();
        scrollPane4 = new JScrollPane();
        textArea2 = new JTextArea();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("web bytes importer");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== tabbedPane2 ========
        {

            //======== panel3 ========
            {
                panel3.setLayout(new BorderLayout());

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(table_current);
                }
                panel3.add(scrollPane1, BorderLayout.CENTER);

                //======== panel2 ========
                {
                    panel2.setLayout(new BorderLayout());

                    //======== panel1 ========
                    {
                        panel1.addComponentListener(new ComponentAdapter() {
                            @Override
                            public void componentResized(ComponentEvent e) {
                                panel1ComponentResized(e);
                            }
                        });
                        panel1.setLayout(new GridLayout());

                        //---- b_addURL ----
                        b_addURL.setText("b_addURL");
                        panel1.add(b_addURL);

                        //---- b_delete ----
                        b_delete.setText("b_delete");
                        panel1.add(b_delete);

                        //---- b_start ----
                        b_start.setText("b_start");
                        panel1.add(b_start);

                        //---- b_pause ----
                        b_pause.setText("b_pause");
                        panel1.add(b_pause);

                        //---- b_start_all ----
                        b_start_all.setText("b_start_all");
                        panel1.add(b_start_all);

                        //---- b_pause_all ----
                        b_pause_all.setText("b_pause_all");
                        panel1.add(b_pause_all);

                        //---- b_toHistory ----
                        b_toHistory.setText("b_toHistory");
                        panel1.add(b_toHistory);
                    }
                    panel2.add(panel1, BorderLayout.CENTER);

                    //---- c_filter_current ----
                    c_filter_current.setEditable(true);
                    c_filter_current.setToolTipText("filter");
                    panel2.add(c_filter_current, BorderLayout.PAGE_END);

                    //---- c_url ----
                    c_url.setEditable(true);
                    c_url.setToolTipText("input url");
                    panel2.add(c_url, BorderLayout.PAGE_START);
                }
                panel3.add(panel2, BorderLayout.NORTH);

                //======== tabbedPane1 ========
                {

                    //======== scrollPane2 ========
                    {

                        //---- textArea1 ----
                        textArea1.setRows(5);
                        scrollPane2.setViewportView(textArea1);
                    }
                    tabbedPane1.addTab("info", scrollPane2);
                }
                panel3.add(tabbedPane1, BorderLayout.SOUTH);
            }
            tabbedPane2.addTab("current", panel3);

            //======== panel4 ========
            {
                panel4.setLayout(new BorderLayout());

                //======== scrollPane3 ========
                {
                    scrollPane3.setViewportView(table_history);
                }
                panel4.add(scrollPane3, BorderLayout.CENTER);

                //======== panel5 ========
                {
                    panel5.setLayout(new BorderLayout());

                    //======== panel6 ========
                    {
                        panel6.addComponentListener(new ComponentAdapter() {
                            @Override
                            public void componentResized(ComponentEvent e) {
                                panel1ComponentResized(e);
                            }
                        });
                        panel6.setLayout(new GridLayout());

                        //---- b_reload ----
                        b_reload.setText("b_reload");
                        panel6.add(b_reload);

                        //---- b_delete2 ----
                        b_delete2.setText("b_delete");
                        panel6.add(b_delete2);
                    }
                    panel5.add(panel6, BorderLayout.CENTER);

                    //---- c_filter_history ----
                    c_filter_history.setEditable(true);
                    c_filter_history.setToolTipText("filter");
                    panel5.add(c_filter_history, BorderLayout.PAGE_END);

                    //---- c_url2 ----
                    c_url2.setEditable(true);
                    c_url2.setToolTipText("input url");
                    panel5.add(c_url2, BorderLayout.PAGE_START);
                }
                panel4.add(panel5, BorderLayout.NORTH);

                //======== tabbedPane3 ========
                {

                    //======== scrollPane4 ========
                    {

                        //---- textArea2 ----
                        textArea2.setRows(5);
                        scrollPane4.setViewportView(textArea2);
                    }
                    tabbedPane3.addTab("info", scrollPane4);
                }
                panel4.add(tabbedPane3, BorderLayout.SOUTH);
            }
            tabbedPane2.addTab("history", panel4);
        }
        contentPane.add(tabbedPane2, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    public JTabbedPane tabbedPane2;
    public JPanel panel3;
    public JScrollPane scrollPane1;
    public JTable table_current;
    public JPanel panel2;
    public JPanel panel1;
    public JButton b_addURL;
    public JButton b_delete;
    public JButton b_start;
    public JButton b_pause;
    public JButton b_start_all;
    public JButton b_pause_all;
    public JButton b_toHistory;
    public JComboBox c_filter_current;
    public JComboBox c_url;
    public JTabbedPane tabbedPane1;
    public JScrollPane scrollPane2;
    public JTextArea textArea1;
    public JPanel panel4;
    public JScrollPane scrollPane3;
    public JTable table_history;
    public JPanel panel5;
    public JPanel panel6;
    public JButton b_reload;
    public JButton b_delete2;
    public JComboBox c_filter_history;
    public JComboBox c_url2;
    public JTabbedPane tabbedPane3;
    public JScrollPane scrollPane4;
    public JTextArea textArea2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
