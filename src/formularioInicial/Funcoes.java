/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularioInicial;

import javax.swing.JOptionPane;

/**
 *
 * @author andre
 */
public class Funcoes {
    
    public static void sair(){
        Object[] options = { "Sair", "Não" };
        int replaced = JOptionPane.showOptionDialog(null, "Deseja sair do Programa", "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        String result = "?";
        switch (replaced) {
        case JOptionPane.CLOSED_OPTION:
            result = "Closed";
            break;
        case JOptionPane.NO_OPTION:
            result = "No";
            break;
        case JOptionPane.YES_OPTION:
            result = "Yes";
        break;
        default:
        ;
        }
        if (result.equals("Yes")){
            System.exit(0);
        }       
    System.out.println("Replace? " + result);
    }
    public static int voltar(String aux, String opcaoUm, String opcaoDois){
        Object[] options = { opcaoUm, opcaoDois };
        int replaced = JOptionPane.showOptionDialog(null, aux, "Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        String result = "?";
        switch (replaced) {
        case JOptionPane.CLOSED_OPTION:
            result = "Closed";
            break;
        case JOptionPane.NO_OPTION:
            result = "No";
            break;
        case JOptionPane.YES_OPTION:
            result = "Yes";
        break;
        default:
        ;
        }
        if (result.equals("Yes")){
            return 1;
        }else{
            return 0;
        }
    }
}
