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
public class NotificarCaldas {
    public static void main(String[] args){

        String strNombreArchivo=null, strFirma=null, strSubFirma=null, strNombre=null, strEmail=null, strFila=null, strRutaArchivo=null, strMensaje="", strRemitente=null;
        String strNombreArchivo2=null, strRutaArchivo2=null, subject=null, strInstitucion=null;
        String[] strTemp=null;
        Integer intUsuariosNotificados =0, intProcesados=0;
        
        ParametroMail parametroMail = null; 
        EnvioMailDAO envioMailDAO = new EnvioMailDAOimpl();
               
        strFirma = "Luis F García";
        strSubFirma = "Secretario Capítulo de Antioquia";
        strInstitucion = "Academia Colombiana de Ciencias Exactas, Físicas y Naturales";
        strRemitente = "Luis F García";
        
        subject = "Invitación Jornadas Caldas";
                                        
        try{
            CsvReader usuarios_import = new CsvReader("C:\\WebApps\\Notificaciones\\caldas.csv");
            //usuarios_import.readHeaders();
            
            while (usuarios_import.readRecord()){
                strFila = usuarios_import.get(0).toString();  
                strTemp = strFila.split(";");
                strNombre = strTemp[0].trim();
                strEmail = strTemp[5].trim();
                                                  
                //strEmail = "jorge.correaj@udea.edu.co";
                                
                new GIDaoException("Invitado " + (intProcesados+1) + " :" + strNombre + ", Correo: " + strEmail);
                
                if (!strEmail.equals("")){
                    /*strNombreArchivo2 = "Modelo carta.docx";
                    strRutaArchivo2 = "C:\\WebApps\\Notificaciones";
                    strRutaArchivo2 = strRutaArchivo2 + "\\" + strNombreArchivo2;
                                                            
                    strNombreArchivo = "Programación.pdf";
                    strRutaArchivo = "C:\\WebApps\\Notificaciones";
                    strRutaArchivo = strRutaArchivo + "\\" + strNombreArchivo;*/
                                        
                    strMensaje += "Cordial saludo," + "\n\n";
                    strMensaje += "Debido a un error técnico involuntario la invitación a participar en las Jornadas Caldas que se envió el 15 de Marzo pasado no estaba dirigida a cada uno de los invitados. Les pido disculpas y les reitero que el Capítulo de Antioquia de la Academia Colombiana de Ciencias Exactas, Físicas y Naturales estaría muy honrado con su presencia el próximo 31 de Marzo en el Parque Explora para la conmemoración del bicentenario de la muerte de Francisco José de Caldas."+ "\n\n";
                    strMensaje += "De antemano muchas gracias por su comprensión."+ "\n\n";
                    strMensaje += "Atentamente,"+ "\n\n";

                    strMensaje += strFirma + "\n";
                    strMensaje += strSubFirma + "\n";
                    strMensaje += strInstitucion;

                    parametroMail = new ParametroMail();
                    parametroMail.setAsunto(subject);
                    parametroMail.setMensaje(strMensaje);
                    parametroMail.setDestinatario(strEmail);
                    parametroMail.setNombreArchivo(strNombreArchivo);
                    parametroMail.setRutaArchivo(strRutaArchivo);
                    parametroMail.setNombreArchivo2(strNombreArchivo2);
                    parametroMail.setRutaArchivo2(strRutaArchivo2);
                    parametroMail.setRemitente(strRemitente);

                    if (parametroMail != null){
                        try{
                            envioMailDAO.sendMail(parametroMail);
                            new GIDaoException("Notificación enviada!");
                        }catch(GIDaoException e){
                            new GIDaoException("Se generó en error enviando la notificación al proveedor con nombre " +  strNombre, e);
                        }
                    }
                    
                    intUsuariosNotificados++;              
                    
                    if (intUsuariosNotificados.toString().equals("30")){
                        break;
                    }
                                        
                }else{
                    new GIDaoException("No tiene correo");
                }
                
                intProcesados++;
                                                        
                strFila = null;
                strTemp = null;
                strNombre = "";
                strEmail = "";
                parametroMail = null;
                strMensaje = "";
                strNombreArchivo = null;
                strRutaArchivo = null;               
            }
                                    
            try{
                usuarios_import.close();          
                envioMailDAO.cerrarSesion();
                new GIDaoException("Total invitados notificados: " + intUsuariosNotificados);
                new GIDaoException("Total invitados procesados: " + intProcesados);
            }catch(GIDaoException e){
                new GIDaoException("Se generó un error al cerrar la sesión del correo", e);
            }            
            
        }catch (FileNotFoundException e) { 
            new GIDaoException("No se encontró el archivo CSV en la ruta especificada: " + e.getMessage(), e);
        }catch(IOException ioe){
            new GIDaoException("Se generó un error al leer el archivo CSV: " + ioe.getMessage(), ioe);
        }                
    }
}
