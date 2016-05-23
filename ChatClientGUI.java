/* Group: Dhrumil Patel, Deven Patel, Mitul Patel */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;


public class
ChatClientGUI extends JFrame {

    private OutputStream outputStream;

    public static void main(String[] args) {

        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels = javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx = 0; idx < installedLookAndFeels.length; idx++)
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        } catch (Exception e) {
        }
        //</editor-fold>

        /* Create and display the form */
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ChatClientGUI().setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public ChatClientGUI() throws IOException {

        initComponents();
        pack();
        // Center in the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation(new Point((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.width) / 2));

    }


    private void initComponents() throws IOException {

        String server = "localhost";
        int port = 2337;
        Socket socket = new Socket(server, port);
         outputStream = socket.getOutputStream();
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setTitle("ChatClientGUI");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                try {
                    exitForm(evt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel1.setPreferredSize(new Dimension(1000,500));
        jTextField1.setText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 430;
        gridBagConstraints.ipady = 274;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(jTextField1, gridBagConstraints);

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {""};

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 87;
        gridBagConstraints.ipady = 317;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 6, 0);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jTextField2.setText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 350;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jTextField2, gridBagConstraints);

        jButton1.setText("Send");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel1.add(jButton1, gridBagConstraints);


        String name = JOptionPane.showInputDialog(this, "What's your name?");
        outputStream.write((name + "\n").getBytes());
        outputStream.flush();

        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String line = jTextField2.getText();

                try {
                    outputStream.write((line + "\n").getBytes());
                    outputStream.flush();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if(line.equalsIgnoreCase("QUIT")){
                    System.exit(0);
                }
            }
        });


        Thread receivingThread = new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    String line;

                    while (true) {
                        line = reader.readLine();

                        if (line == null) break;
                        System.out.println(line);
                        if(line.startsWith("META:")){
                            line = line.replaceFirst("META:","");
                            String[] currentUsers = line.split(",");
                            jList1.setModel(new javax.swing.AbstractListModel<String>() {;

                                public int getSize() {
                                    return currentUsers.length;
                                }

                                public String getElementAt(int i) {
                                    return currentUsers[i];
                                }
                            });
                        }
                        else {
                            String oldText = jTextField1.getText();
                            String[] old = oldText.split("\n");
                            java.util.ArrayList<String> oo = new ArrayList<>();
                            Collections.addAll(oo, old);
                            oo.add(line);
                            while(oo.size()>10){
                                oo.remove(0);
                            }
                            String text = "";
                            for(String t:oo){
                                text= text + t +"\n";
                            }
                            jTextField1.setText(text);
                         }


                        Thread.sleep(1000);
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }

            }
        };
        receivingThread.start();

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>

    private void exitForm(java.awt.event.WindowEvent evt) throws IOException {
        outputStream.write(("QUIT" + "\n").getBytes());
        outputStream.flush();
        System.exit(0);
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextField1;
    private javax.swing.JTextField jTextField2;

}
