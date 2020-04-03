/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udea.notificaciones.dao;

import co.edu.udea.notificaciones.dto.ParametroMail;
import co.edu.udea.notificaciones.exception.GIDaoException;
import java.util.List;

/**
 *
 * @author jorge.correa
 */
public interface EnvioMailDAO {
    public void sendMail(ParametroMail parametroMail) throws GIDaoException;
    public void sendMailHTML(ParametroMail parametroMail) throws GIDaoException;
    public void sendMailAttach(ParametroMail parametroMail) throws GIDaoException;
    public void sendMailTwoAttaches(ParametroMail parametroMail) throws GIDaoException;
    public void sendBCCMultiple(ParametroMail parametroMail, List<String> destinatarios) throws GIDaoException;
    public void cerrarSesion() throws GIDaoException;
}
