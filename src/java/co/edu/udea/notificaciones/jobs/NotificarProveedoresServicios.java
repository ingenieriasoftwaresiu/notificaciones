/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udea.notificaciones.jobs;

import co.edu.udea.notificaciones.dao.EnvioMailDAO;
import co.edu.udea.notificaciones.dao.impl.EnvioMailDAOimpl;
import co.edu.udea.notificaciones.dto.ParametroMail;
import co.edu.udea.notificaciones.exception.GIDaoException;
import com.csvreader.CsvReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author jorge.correa
 */
public class NotificarProveedoresServicios {
    
    public static void main(String[] args){

        String strNombreArchivo=null, strFirma=null, strSubFirma=null, strNombre=null, strEmail=null, strFila=null, strRutaArchivo=null, strMensaje="", strRemitente=null, strFile="";
        String subject, strConsecutivo;
        String[] strTemp=null;
        Integer intUsuariosNotificados =0, intCont;
        
        ParametroMail parametroMail = null; 
        EnvioMailDAO envioMailDAO = new EnvioMailDAOimpl();
               
        strFirma = "Gestión Compras, Contratación y Distribución";
        strSubFirma = "Administración de la SIU";
        strRemitente = "Gestión Compras ASIU";
        strFile = "Oficios_";
        intCont = 126;
                
        // message info        
        subject = "Evaluación a proveedores que suministran servicios a la Sede de Investigación Universitaria SIU – Universidad de Antioquia 2019";        
        
        try{
            CsvReader usuarios_import = new CsvReader("E:\\Google Drive UdeA\\Ingeniería de Software\\Desarrollo\\Notificación Evaluación Proveedores\\Evaluación de proveedores 2019\\Servicios\\Oficios2019.csv");
            //usuarios_import.readHeaders();
            
            while (usuarios_import.readRecord()){
                strFila = usuarios_import.get(0).toString();  
                strTemp = strFila.split(";");
                strNombre = strTemp[0].trim();           
                strEmail = strTemp[1].trim();             
                                                                                                           
                new GIDaoException("Proveedor: " + strNombre + ", Correo: " + strEmail);
                strConsecutivo = "";
                
                if (intCont > 9 && intCont <= 99){
                    strConsecutivo = "0" + intCont.toString();                             
                }else{
                    if (intCont <= 9){
                        strConsecutivo = "00" + intCont.toString();       
                    }else{
                        strConsecutivo = intCont.toString();
                    }
                }                
                                               
                strNombreArchivo = strFile + strConsecutivo + ".pdf";
                strRutaArchivo = "E:\\Google Drive UdeA\\Ingeniería de Software\\Desarrollo\\Notificación Evaluación Proveedores\\Evaluación de proveedores 2019\\Servicios\\Oficios";
                strRutaArchivo = strRutaArchivo + "\\" + strNombreArchivo;
                
                strMensaje += "Cordial saludo señores " + strNombre + "," + "\n\n";
                strMensaje += "A continuación enviamos la evaluación realizada durante el año 2019 a los proveedores que suministran servicios a la Sede de Investigación Universitaria - Universidad de Antioquia, donde se valoraron aspectos como:"+ "\n\n";
                strMensaje += "- Oportunidad: Cumplimiento en la entrega del servicio. "+ "\n";
                strMensaje += "- Devoluciones: Cumplimiento en las condiciones de calidad pactadas."+ "\n";
                strMensaje += "- Calidad del servicio prestado: Atención y respuesta a requerimientos."+ "\n\n";
                strMensaje += "​Los proveedores que obtengan resultados inferiores al 80% deberán enviar las acciones correctivas pertinentes para mejorar su servicio."+ "\n\n";
                strMensaje += "De antemano muchas gracias por la atención y estamos atentos a cualquier sugerencia u observación al respecto."+ "\n\n";
                strMensaje += "Atentamente,"+ "\n\n";
                
                strMensaje += strFirma + "\n";
                strMensaje += strSubFirma;
                
                //strEmail = "jorge.correaj@udea.edu.co";
                                
                parametroMail = new ParametroMail();
                parametroMail.setAsunto(subject);
                parametroMail.setMensaje(strMensaje);
                parametroMail.setDestinatario(strEmail);
                parametroMail.setNombreArchivo(strNombreArchivo);
                parametroMail.setRutaArchivo(strRutaArchivo);
                parametroMail.setRemitente(strRemitente);
                
                if (parametroMail != null){
                    try{
                        envioMailDAO.sendMailAttach(parametroMail);
                        new GIDaoException("Notificación enviada!");
                    }catch(GIDaoException e){
                        new GIDaoException("Se generó en error enviando la notificación al proveedor con nombre " +  strNombre, e);
                    }
                }
                
                intUsuariosNotificados++;
                intCont++;
                                      
                strFila = null;
                strTemp = null;
                strNombre = null;
                strEmail = null;
                parametroMail = null;
                strMensaje = "";
                strNombreArchivo = null;
                strRutaArchivo = null;
            }
            
            usuarios_import.close();            
            new GIDaoException("Total proveedores notificados: " + intUsuariosNotificados);
            
        }catch (FileNotFoundException e) { 
            new GIDaoException("No se encontró el archivo CSV en la ruta especificada: " + e.getMessage(), e);
        }catch(IOException ioe){
            new GIDaoException("Se generó un error al leer el archivo CSV: " + ioe.getMessage(), ioe);
        }                
    }
}
