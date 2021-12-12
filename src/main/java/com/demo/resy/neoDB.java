package com.demo.resy;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.neo4j.driver.Values.parameters;

//CYPHER

//DELETE ALL
//MATCH (n) DETACH DELETE n

//CONSTRAINTS & CREATION USER
//CREATE (n:User {username: '', email: '', password: ''})"

//CREATE CONSTRAINT ON (n:User) ASSERT n.username IS UNIQUE
//CREATE CONSTRAINT ON (n:User) ASSERT n.email IS UNIQUE
//CREATE CONSTRAINT ON (n:User) ASSERT n.id IS UNIQUE


//CONSTRAINTS & CREATION SKILLS
//CREATE (n:Skill {skillname: '', description: '', category:''})


public class neoDB implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(neoDB.class.getName());
    private final Driver driver;

    public neoDB(String uri, String user, String password, Config config) {
        // The driver is a long living object and should be opened during the start of your application
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password), config);
    }

    /**
     * Driver Connection.
     *
     * @return
     */
    public static Driver Connection() {
        String uri = "neo4j+s://94c89272.databases.neo4j.io";
        String user = "neo4j";
        String password = "TwrZkJZ7UXqLmRYsytRGU0iJEVGo6o9OGhDqLPiiHOU";
        try (neoDB db = new neoDB(uri, user, password, Config.defaultConfig())) {
            System.out.print("session");
            return db.getDriver();
        } catch (Exception e) {
            System.out.println("Couldn't return neoDB object.");
            return null;
        }
    }

    /**
     * @param tx
     * @return List<Record>
     */
    private static List<Record> hilfsMethode(Transaction tx) {
        return tx.run("MATCH(n:Skill)\n" +
                "RETURN id(n), n.skillname, n.description, n.category").list();
    }

    public Driver getDriver() {
        return driver;
    }

    @Override
    public void close() throws Exception {
        // The driver object should be closed before the application ends.
        driver.close();
    }

    /**
     * Registriert einen User.
     *
     * @param user
     */
    public void registerUser(final User user) {
        try (Session session = driver.session()) {
            final String username = user.getUsername();
            final String email = user.getEmail();
            final String password = user.getPassword();
            String registerUser = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    Result result = transaction.run("CREATE (n:User {username: '" + username + "', email: '" + email + "', password: '" + password + "'})");
                    return null;
                }
            });
        }
    }

    /**
     * @param username
     * @param email
     * @param password
     * @return Login successfull when result.hasNext().
     * Andernfalls Login fail.
     */
    public boolean[] loginUser(final String username, final String email, final String password) {
        final boolean[] r = new boolean[1];
        try (Session session = driver.session()) {
            String loginUser = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    Result result = transaction.run("MATCH (n:User {username:'" + username + "', email:'" + email + "', password:'" + password + "'})\n" +
                            "RETURN (n:User)");
                    if (result.hasNext()) {
                        r[0] = true;
                    } else r[0] = false;
                    return String.valueOf(result);
                }
            });

        }
        return r;
    }

    /**
     * Mögliche Lösung für Results.
     *
     * @param
     * @return List<Record>
     */

    public void readSkills() {
        Main.skillsList.removeAll(Main.skillsList);
        try (Session session = driver.session()) {
            List<Record> puffer = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result skillname = transaction.run("MATCH(n:Skill)\n" +
                            "RETURN id(n), n.skillname, n.description, n.category");
                    return hilfsMethode(transaction);
                }
            });

            for (Record item : puffer) {
                Map<String, Object> map = item.asMap();
                Skill s = new Skill((String) map.get("n.skillname"), (String) map.get("n.description"), (String) map.get("n.category"));
                Main.skillsList.add(s);
            }


        }
    }


    /**
     * Folgender Code nur zum probieren & testen..
     */

    /**
     *
     */
    public void addPerson(String name) {
        try (Session session = driver.session()) {
            session.run("CREATE (a:Person {name: $name})", parameters("name", name));
        }
    }


}