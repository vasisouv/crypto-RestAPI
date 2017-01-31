package com.vasisouv.webservices2017.aem17.servlets;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.vasisouv.webservices2017.aem17.model.Message;
import com.vasisouv.webservices2017.aem17.model.MessageDatasource;
import com.vasisouv.webservices2017.aem17.model.MessageMapper;
import com.vasisouv.webservices2017.aem17.utils.DesEncrypter;
import com.vasisouv.webservices2017.aem17.utils.GeneralUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class EncryptServlet extends MainServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JSONObject jsonObject = readRequest(req);
        MessageMapper mapper = new MessageMapper();
        Message message = mapper.getMessageFromJson(jsonObject);
        DesEncrypter encrypter = new GeneralUtils().generateEncrypter();
        try {
            message.setText(encrypter.encrypt(message.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        MessageDatasource datasource = new MessageDatasource();
        String msgKey = datasource.saveMessageToDatastore(message, DatastoreServiceFactory.getDatastoreService());
        message.setId(msgKey);
        String result = getJsonResultOk();
        sendResponse(result, resp);
    }



}
