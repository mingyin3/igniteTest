/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import java.util.List;
//import org.apache.ignite.examples.model.Organization;

/**
 * This example demonstrates the usage of Apache Ignite Persistent Store.
 * <p>
 * To execute this example you should start an instance of {@link //PersistentStoreExampleNodeStartup}
 * class which will start up an Apache Ignite remote server node with a proper configuration.
 * <p>
 * When {@code UPDATE} parameter of this example is set to {@code true}, the example will populate
 * the cache with some data and will then run a sample SQL query to fetch some results.
 * <p>
 * When {@code UPDATE} parameter of this example is set to {@code false}, the example will run
 * the SQL query against the cache without the initial data pre-loading from the store.
 * <p>
 * You can populate the cache first with {@code UPDATE} set to {@code true}, then restart the nodes and
 * run the example with {@code UPDATE} set to {@code false} to verify that Apache Ignite can work with the
 * data that is in the persistence only.
 * /Users/myin2/Downloads/apache-ignite-fabric-2.1.0-bin/examples/config/example-ignite.xml
 */
public class PersistentStoreTest {
    /** */
    private static final boolean UPDATE = false;

    /**
     * @param args Program arguments, ignored.
     * @throws Exception If failed.
     */
    public static void main(String[] args) throws Exception {
        Ignition.setClientMode(true);

        try (Ignite ig = Ignition.start("/Users/myin2/Downloads/igniteTest/src/main/config/persistent-store-node-1.xml")) {

            // Activate the cluster. Required to do if the persistent store is enabled because you might need
            // to wait while all the nodes, that store a subset of data on disk, join the cluster.
            ig.active(true);

            IgniteCache<Long, Organization> cache = ig.cache("organization");

            if (UPDATE) {
                System.out.println("Populating the cache...");

                try (IgniteDataStreamer<Long, Organization> streamer = ig.dataStreamer("organization")) {
                    streamer.allowOverwrite(true);

                    for (long i = 0; i < 1000000; i++) {
                        streamer.addData(i, new Organization("organization-"+i));
                        if (i > 0 && i % 10_000 == 0)
                            System.out.println("Done: " + i);
                    }
                }
            }

            // Run SQL without explicitly calling to loadCache().
            QueryCursor<List<?>> cur = cache.query(
                    new SqlFieldsQuery("select * from Organization where name like ?")
                            .setArgs("organization-54321"));

            System.out.println("SQL Result: " + cur.getAll());

            // Run get() without explicitly calling to loadCache().
            Organization org = cache.get(54321l);

            System.out.println("GET Result: " + org);
        }
    }
}