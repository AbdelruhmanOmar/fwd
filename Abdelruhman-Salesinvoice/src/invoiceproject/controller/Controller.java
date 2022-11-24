
package invoiceproject.controller;

import invoiceproject.model.Invoice;
import invoiceproject.model.invvocestablemode;
import invoiceproject.model.Items;
import invoiceproject.model.ItemsTableModel;
import invoiceproject.view.InvoiceFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Controller implements ActionListener, ListSelectionListener {
    
    private InvoiceFrame frame;
    public Controller (InvoiceFrame frame){
        this.frame = frame ;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actioncommand = e.getActionCommand();
        System.out.println("Action "+actioncommand);
        switch (actioncommand){
            case "Load File":
                loadFile();
                break;
            case "Save File":
                saveFile();
                break;
            case "Create New Invoice":
                createNewInvoice();
                break;           
            case "Delete Invoice":
                deleteInvoice();
                break;          
            case "Create New Item":
                createNewItem();
                break; 
            case "Delete Item":
                deleteItem();
                break;   
            case "Create Invoice Cancel":
                createInvoiceCancel();
                break;
            case "create Invoice OK":
                createInvoiceOK();
                break;
            case "Create Item OK":
                createItemOK();
                break;
            case "Create Item Cancel":
                createItemCancel();
                break;
                        
    }    
}
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = frame.getinvvocestable().getSelectedRow();
        if (selectedIndex !=-1){
        Invoice currentInovice = frame.getinvvoces().get(selectedIndex);
        frame.getinvvoceNumLabel().setText(""+currentInovice.getNum());
        frame.getinvvoceNumLabel().setText(""+currentInovice.getDate());
        frame.getinvvoceNumLabel().setText(""+currentInovice.getCustomer());
        frame.getinvvoceNumLabel().setText(""+currentInovice.getInvoiceTotal());
        ItemsTableModel itemsTableModel = new ItemsTableModel(currentInovice.getItems());
        frame.getItemsTable().setModel(itemsTableModel);
        itemsTableModel.fireTableDataChanged();
        }
    }
    
    private void loadFile() {
        JFileChooser fc = new JFileChooser();
        int result = fc.showOpenDialog(frame);
        try {
        if (result == JFileChooser.APPROVE_OPTION){
            String ph = fc.getSelectedFile().getPath();
            Path hPath = Paths.get(ph);
//            File headerFile = fc.getSelectedFile();
//            Path headerPath = Paths.get(headerFile.getAbsolutePath());
            try {
                List<String> invoiceHeaders = Files.readAllLines(hPath);
//                System.out.println("Invoces read");
                ArrayList<Invoice> invoiceArray = new ArrayList<>();
                for (String invoiceHeader : invoiceHeaders) {
                    String[] headerList = invoiceHeader.split(",");
                    int invoiceNum = Integer.parseInt(headerList[0]) ;
                    String invoiceDate = headerList[1];
                    String customerName = headerList[2];
                    Invoice invoice= new Invoice(invoiceNum, invoiceDate, customerName);
                    invoiceArray.add(invoice);
                }
//                System.out.println("Check#1");
                result = fc.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION){
                    String pi = fc.getSelectedFile().getPath();
                    Path itemPath = Paths.get(pi);
                    List<String> invoiceLines = Files.readAllLines(itemPath);
//                    System.out.println("Items Read");
                    for (String invoiceLine : invoiceLines) {
                        String[] lineList = invoiceLine.split(",");
                        int invoiceNum = Integer.parseInt(lineList[0]);
                        String itemName = lineList[1];
                        double itemPrice = Double.parseDouble(lineList[2]);
                        int count = Integer.parseInt(lineList[3]);
                        Invoice inv = null ; 
                        for (Invoice invoice:invoiceArray) {
                            if (invoice.getNum()== invoiceNum){
                                inv = invoice;
                                break;
                            }
                        }
                        Items item = new Items(itemName, itemPrice, count, inv);
                        inv.getItems().add(item);
                    }
//                    System.out.println("Check#2");
                }
                frame.setinvvoces(invoiceArray);
                invvocestablemode invoicesTableModel = new invvocestablemode(invoiceArray);
                frame.setInvoicesTableModel(invoicesTableModel);
                frame.getinvvocestable().setModel(invoicesTableModel);
                frame.getInvoicesTableModel().fireTableDataChanged();
                
            } catch (IOException ex) {
                ex.printStackTrace();}                                   
        }
        } catch (NumberFormatException e) {
            System.out.println(e+"\n Please Choose Correct Documents");}                                           
    }
   
    private void saveFile() {
        ArrayList<Invoice> invoices = frame.getinvvoces();
        String headers = "";
        String lines = "";
        for (Invoice invoice : invoices){
            String invCSV = invoice.getAsCSV();
            headers += invCSV;
            headers += "\n";
            
            for (Items items : invoice.getItems()){
                String itemCSV = items.getAsCSV();
                lines += itemCSV;
                lines += "\n";                
            }
        }
        try {    
            JFileChooser fs = new JFileChooser();
            int result = fs.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION){
                File headerSaver = fs.getSelectedFile();                       
                FileWriter headerfw = new FileWriter(headerSaver);           
                headerfw.write(headers);
                headerfw.flush();
                headerfw.close();
                result = fs.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION){
                    File itemSaver = fs.getSelectedFile();         
                    FileWriter linerfw = new FileWriter(itemSaver);           
                    linerfw.write(lines);
                    linerfw.flush();
                    linerfw.close();
                }
            }
        }
        catch (Exception ex) {}        
    }                                                                                               
    private void createNewInvoice() { 
        frame.getInvDialog().setVisible(true);            
    }

    private void deleteInvoice() {
        int selectedRow = frame.getinvvocestable().getSelectedRow();
        if (selectedRow != -1){
            frame.getinvvoces().remove(selectedRow);
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }                      

    private void createNewItem() {
        frame.getItemDialog().setVisible(true);
    }

    private void deleteItem() {
        int selectedRow = frame.getItemsTable().getSelectedRow();
        int selectedInv = frame.getinvvocestable().getSelectedRow();
        if (selectedRow != -1 && selectedInv != -1){
            Invoice invoice = frame.getinvvoces().get(selectedInv);
            invoice.getItems().remove(selectedRow);
            ItemsTableModel itemsTableModel = new ItemsTableModel(invoice.getItems());
            frame.getItemsTable().setModel(itemsTableModel);
            itemsTableModel.fireTableDataChanged(); 
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createInvoiceCancel() {
        frame.getInvDialog().setVisible(false);
        frame.getInvDialog().dispose();
        frame.getInvDateField().setText(null);
        frame.getCustNameField().setText(null);
    }

    private void createInvoiceOK() {
        String date = frame.getInvDateField().getText();
        String customer = frame.getCustNameField().getText();
        int num = frame.getNextInvoiceNum();        
        Invoice invoice = new Invoice(num, date, customer);
        frame.getinvvoces().add(invoice);
        frame.getInvoicesTableModel().fireTableDataChanged();
        frame.getInvDialog().setVisible(false);
        frame.getInvDialog().dispose();
        frame.getInvDateField().setText(null);
        frame.getCustNameField().setText(null);     
    }

    private void createItemOK() {
        String item = frame.getItemNameField().getText();
        String countString = frame.getItemCountField().getText();
        String priceString = frame.getItemPriceField().getText();
        int count = Integer.parseInt(countString);
        double price = Double.parseDouble(priceString);
        int selectedInvoice = frame.getinvvocestable().getSelectedRow();
        if (selectedInvoice != -1){
            Invoice invoice = frame.getinvvoces().get(selectedInvoice);
            Items items = new Items(item , price, count, invoice);
            invoice.getItems().add(items);
            ItemsTableModel itemsTableModel = new ItemsTableModel(invoice.getItems());
            frame.getItemsTable().setModel(itemsTableModel);
            itemsTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
            
        }                        
        frame.getItemDialog().setVisible(false);
        frame.getItemDialog().dispose();
        frame.getItemNameField().setText(null);
        frame.getItemCountField().setText(null);
        frame.getItemPriceField().setText(null);
              
    }

    private void createItemCancel() {
        frame.getItemDialog().setVisible(false);
        frame.getItemDialog().dispose(); 
        frame.getItemNameField().setText(null);
        frame.getItemCountField().setText(null);
        frame.getItemPriceField().setText(null);
    }
}
