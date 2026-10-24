package io.github.nobodyasidentity.nobodylib;

import javax.swing.JOptionPane;

public class NobodyLibJarMain{
    public static void main(String[]args){
        JOptionPane.showMessageDialog(null,
            "This is a Fabric mod.\nPlease put it in your mods folder.",
            NobodyLib.NAME,
            JOptionPane.INFORMATION_MESSAGE);
    }
}