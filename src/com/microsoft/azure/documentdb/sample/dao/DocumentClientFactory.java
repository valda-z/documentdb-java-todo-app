package com.microsoft.azure.documentdb.sample.dao;

import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;

public class DocumentClientFactory {
    // TODO: provide valid URL address to DocumentDB database
    private static final String HOST = "https://<documentdb-name>.documents.azure.com:443/";
    // TODO: provide valid primary key for DocumentDB database
    private static final String MASTER_KEY = "<documentdb-key>";

    private static DocumentClient documentClient;

    public static DocumentClient getDocumentClient() {
        if (documentClient == null) {
            documentClient = new DocumentClient(HOST, MASTER_KEY,
                    ConnectionPolicy.GetDefault(), ConsistencyLevel.Session);
        }

        return documentClient;
    }

}
