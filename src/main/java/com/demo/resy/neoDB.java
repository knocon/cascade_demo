package com.demo.resy;

import org.neo4j.driver.*;

import java.util.logging.Logger;

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

    public Driver getDriver() {
        return driver;
    }

    private final Driver driver;

    public neoDB(String uri, String user, String password, Config config) {
        // The driver is a long living object and should be opened during the start of your application
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password), config);
    }

    @Override
    public void close() throws Exception {
        // The driver object should be closed before the application ends.
        driver.close();
    }

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

    public void printGreeting(final String Person) {
        try (Session session = driver.session()) {
            String greeting = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    Result result = transaction.run("CREATE (n:message)");
                    return null;
                }
            });
        }
    }

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


}