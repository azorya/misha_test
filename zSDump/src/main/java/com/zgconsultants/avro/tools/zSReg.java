package com.zgconsultants.avro.tools;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import org.apache.avro.Schema;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class zSReg {

    private final CachedSchemaRegistryClient schemaClient;
    private final Map<String,Object> mt2ss;

    zSReg (Map<String, Object> conf) {

        String url = (String)conf.get("schemaRegistry");
        mt2ss = (Map<String, Object>)conf.get("MsgType2SchemaSubjects");

        schemaClient = new CachedSchemaRegistryClient(url, 10);
    }

    void register(Schema schema) throws Exception {
        // getRegNames(schema).forEach(name -> schemaClient.register(name, schema));

        List<String> l = getSubjectNames(schema);

        for(String schemaSubject :l) {
            schemaClient.register(schemaSubject, schema);
        }
    }

    private List<String> getSubjectNames(Schema schema) {
        return (List<String>)mt2ss.getOrDefault(schema.getName(), new LinkedList<>());
    }
}
