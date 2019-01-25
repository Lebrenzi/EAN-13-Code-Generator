import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;

public class MainApplication extends JFrame{

    //Main Panes
    private JPanel main_pane, header_pane;

    //Functional Panes
    private JPanel top_text_pane, top_button_pane, image_pane, size_pane;

    //Aligning Panes
    private JPanel left_aligning_pane, right_aligning_pane;

    //Buttons
    private JButton generate, print, save_as;

    //Labels
    private JLabel enter = new JLabel("Enter EAN-13 data");



    //Text Field
    private JTextField numeric_row;

    //Barcode object
    private Barcode barcode;

    //Image to print
    private BufferedImage barcode_image;



    public MainApplication(String title) throws HeadlessException {
        super(title);
        this.setSize(new Dimension(640,480));

        main_pane = new JPanel(new BorderLayout());
        this.add(main_pane);
        header_pane = new JPanel(new BorderLayout());
        main_pane.add(header_pane, BorderLayout.NORTH);


        image_pane = new JPanel();
        image_pane.setBackground(new Color(255,255,255));
        main_pane.add(image_pane, BorderLayout.CENTER);

        left_aligning_pane = new JPanel();
        main_pane.add(left_aligning_pane, BorderLayout.LINE_START);

        right_aligning_pane = new JPanel();
        main_pane.add(right_aligning_pane, BorderLayout.LINE_END);

        size_pane = new JPanel(new FlowLayout());
        main_pane.add(size_pane, BorderLayout.SOUTH);

        setTopPane();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void setTopPane(){

        top_text_pane = new JPanel(new FlowLayout());
        header_pane.add(top_text_pane);

        numeric_row = new JTextField(15);
        top_text_pane.add(enter);
        top_text_pane.add(numeric_row);

        //Setting Buttons
        top_button_pane = new JPanel(new FlowLayout());
        header_pane.add(top_button_pane, BorderLayout.SOUTH);

        generate = new JButton("Generate EAN-13 code");
        generate.addActionListener(new GenerateListener());
        top_button_pane.add(generate);

        save_as = new JButton("Save Code as Image");
        save_as.addActionListener(new SaveAsListener());
        top_button_pane.add(save_as);

        print = new JButton("Print Barcode");
        print.addActionListener(new PrintListener());
        top_button_pane.add(print);


    }
    
    public void generateBarCode() throws BarcodeException {

        String data = numeric_row.getText();
        if(data.length() != 12) JOptionPane.showMessageDialog(this, "Code should be 12 digits long");
        else{
            image_pane.removeAll();
            barcode = BarcodeFactory.createEAN13(data);
            image_pane.add(barcode);
            this.validate();
        }

    }

    public void printBarcode() throws PrinterException {
        PrinterJob printer = PrinterJob.getPrinterJob();
        printer.setPrintable(barcode);
        if(printer.printDialog())
        {
            printer.print();
        }
    }

    public void saveAsFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);
        try {
            if (userSelection == JFileChooser.APPROVE_OPTION) {

                File f = new File(fileChooser.getSelectedFile()+".jpg");
                BarcodeImageHandler.saveJPEG(barcode, f);
            }
        }
        catch(Exception e1){
            JOptionPane.showMessageDialog(MainApplication.this, "Save declined");
        }
    }

    class GenerateListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                MainApplication.this.generateBarCode();
            }
            catch (BarcodeException e1){
                JOptionPane.showMessageDialog(MainApplication.this, "Only digits allowed");
            };
        }
    }

    class SaveAsListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                saveAsFile();
            }
            catch (Exception e1){e1.printStackTrace();};
        }
    }

    class PrintListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                printBarcode();

            }
            catch (Exception e1){

                JOptionPane.showMessageDialog(MainApplication.this, "Print denied");
            };
        }
    }

}
