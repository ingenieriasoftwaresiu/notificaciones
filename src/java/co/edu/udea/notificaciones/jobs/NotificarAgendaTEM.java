/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udea.notificaciones.jobs;

import co.edu.udea.notificaciones.exception.GIDaoException;
import com.csvreader.CsvReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jorge.correa
 */
public class NotificarAgendaTEM {
    public static void main(String[] args){
        // SMTP info
        String host = "smtp.gmail.com";
        String port = "465";
        String mailFrom = "comunicacionessiu@udea.edu.co";
        String nameFrom = "Comunicaciones SIU";
        String password = "septiembretres";
        String strSaludo=null, strFirma=null, strSubFirma=null, strNombre=null, strEmail=null, strFila=null, mailTo=null, strApelativo=null;
        String[] strTemp=null;
        Integer intUsuariosNotificados =0;
        StringBuffer body = null;
               
        strFirma = "<b>Dirección Científica</b><br />";
        strSubFirma = "Administración de la SIU";
        
        Map<String, String> inlineImages = new HashMap<String, String>();
 
        // message info        
        String subject = "Agenda del acto de inauguración del Laboratorio de Microscopia Electrónica de Transmisión - TEM";
        
        try{
            CsvReader usuarios_import = new CsvReader("C:\\WebApps\\Notificaciones\\invitados4.csv");
            //usuarios_import.readHeaders();
            
            while (usuarios_import.readRecord()){
                strFila = usuarios_import.get(0);  
                strTemp = strFila.trim().split(";");
                strApelativo = strTemp[1];
                strNombre = strTemp[2];
                strEmail = strTemp[3];
                                           
                mailTo = strEmail.trim();
                strSaludo = "Cordial saludo " +strApelativo.trim() + " <b>"+ strNombre.toUpperCase().trim() + ".</b><br /><br />";
                strSaludo= new String(strSaludo.getBytes("iso-8859-1"),"UTF-8");  
                System.out.println("strSaludo: " + strSaludo);
                body = new StringBuffer("<html>");
                body.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head>");
                body.append("<body>"+strSaludo);    
                body.append("A continuación les relacionamos la agenda que se tendrá el día del acto de inauguración del Laboratorio de Microscopia Electrónica de Transmisión - TEM:<br /><br />");
                body.append("<div align='center'>");
                body.append("<img src=\"cid:image1\" width=\"80%\" height=\"80%\" /><br>");
                body.append("</div>");
                body.append("<br />");
                body.append("Esperamos contar con su valiosa presencia.<br /><br />");
                body.append("Cordialmente,<br /><br />");
                body.append(strFirma + strSubFirma + "</body></html>");

                // inline images                
                inlineImages.put("image1", "C:/WebApps/Notificaciones/notificaciones/web/Images/Agenda.jpg");

                try {
                    EmbeddedImageEmailUtil.send(host, port, nameFrom, mailFrom, password, mailTo,subject, body.toString(), inlineImages);
                    System.out.println("Email sent to: " + mailTo);         
                    System.out.println("");
                    intUsuariosNotificados++;
                } catch (Exception ex) {
                    System.out.println("Could not send email.");
                    ex.printStackTrace();
                }                
                                
                strFila = null;
                strTemp = null;
                strNombre =null;
                strEmail = null;
                mailTo = null;
                strSaludo = null;
                body = null;
                strApelativo = null;
            }         
            
            usuarios_import.close();            
            System.out.println("Usuarios notificados: " + intUsuariosNotificados);
            
        }catch (FileNotFoundException e) { 
            new GIDaoException("No se encontró el archivo CSV en la ruta especificada: " + e.getMessage(), e);
        }catch(IOException ioe){
            new GIDaoException("Se generó un error al leer el archivo CSV: " + ioe.getMessage(), ioe);
        }                
    }
}
