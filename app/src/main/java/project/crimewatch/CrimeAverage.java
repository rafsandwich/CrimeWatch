/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crimeapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;


public class CrimeAverage extends JFrame implements ActionListener {
    
    JButton b1;
    JTextArea jt;
    
    CrimeAverage()
    {
        b1 = new JButton("Find Average");
        jt = new JTextArea(10,10);
        b1.setBounds(100,100,150,30);
        jt.setBounds(100,140,300,300);
        add(b1);
        add(jt);
        b1.addActionListener(this);
        setSize(500,500);
        setLayout(null);
        setVisible(true);
        setTitle("Crime Average rate within Oxfordshire");
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String line = "";
        HashMap<String, Integer> hm = new HashMap<String, Integer>();
        jt.setText("");
        jt.setText("Crime           Average");
        
        try{
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Sarah\\OneDrive\\Documents\\NetBeansProjects\\CrimeApp\\src\\thamesvally2.csv"));
            while((line = br.readLine()) != null)
            {
                String crime[] = line.split(",");
                if (hm.containsKey(crime[0]))
                {
                    int count = hm.get(crime[0]);
                    hm.put(crime[0],count + 1);
                   
                }
                
                else
                {
                    hm.put(crime[0],1);
                   
                }
                
                
            }
            
            for(String num : hm.keySet())
            {
                jt.append("\n"+num+"                               "+hm.get(num));
 
            }
            
            
        } 
        catch (IOException i) {
            // TODO Auto-generated catch block
            i.printStackTrace();

        }
        
        
    }
    
    public static void main(String args[])
    {
        new CrimeAverage();
    }
    
}

    