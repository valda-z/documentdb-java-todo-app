package com.microsoft.azure.documentdb.sample.esb;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.sample.model.TodoItem;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.CreateTopicResult;
import com.microsoft.windowsazure.services.servicebus.models.GetTopicResult;
import com.microsoft.windowsazure.services.servicebus.models.TopicInfo;
import org.apache.log4j.Logger;

/**
 * Created by vazvadsk on 2016-10-18.
 */
public class TopicHelper {
    private static final Gson gson = new Gson();
    private static final Logger _log = Logger.getLogger(TopicHelper.class);
    public void sendToDo(TodoItem itm){
        Configuration config =
                ServiceBusConfiguration.configureWithSASAuthentication(
                        "valdadocdb",
                        "RootManageSharedAccessKey",
                        "p1TU57GFQtPouySan3qUJ+waRevE/6WyO2EXtcHX43M=",
                        ".servicebus.windows.net"
                );

        ServiceBusContract service = ServiceBusService.create(config);
        try {
            _log.info("topic: sending message...");

            //Create topic message
            BrokeredMessage message = new BrokeredMessage(gson.toJson(itm));
            //Append category information to message (or any other property
            message.setProperty("Category", itm.getCategory());
            //send message to topic
            service.sendTopicMessage("valdatopic1", message);
        } catch (ServiceException e) {
            _log.error("Error sending topic", e.fillInStackTrace());
        }
    }
}
