package com.microsoft.azure.documentdb.sample.dao;

import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;

public class DocumentClientFactory {
    private static final String HOST = "https://testvaldadb.documents.azure.com:443/";
    private static final String MASTER_KEY = "ZdvFlbd2UC9GsBfoPbNzRT6T4tWHxI0DfNSFY7p2FICdf62nROCInRy8eKaXzm0miVmn4eq3UMz20FawsghE7w==";

    private static DocumentClient documentClient;

    public static DocumentClient getDocumentClient() {
        if (documentClient == null) {
            documentClient = new DocumentClient(HOST, MASTER_KEY,
                    ConnectionPolicy.GetDefault(), ConsistencyLevel.Session);
        }

        return documentClient;
    }

}
