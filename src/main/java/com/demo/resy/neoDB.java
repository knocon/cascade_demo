package com.demo.resy;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

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

    /**
     * Driver Connection.
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
     * Unnötiger Code.
     * @param Person
     */
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

    /**
     * Registriert einen User.
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
     *
     * @param username
     * @param email
     * @param password
     * @return Login successfull when result.hasNext().
     *          Andernfalls Login fail.
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
     * Folgender Code nur zum probieren & testen..
     */

    /**
     *
     * @param String: name
     * Leichte Version eines Imports in neo4j. Ohne Transaction!!
     */
    public void addPerson(String name) {
        try (Session session = driver.session()) {
            session.run("CREATE (a:Person {name: $name})", parameters("name", name));
        }
    }


    /**
     * Mögliche Lösung für Results.
     *
     *
     * @return List<Record>
     * @param Benötigt eine Hilfsmethode, um die List<Record> zu erzeugen.
     */

    public void readSkills() {
        try (Session session = driver.session()) {
            List<Record> puffer = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result skillname = transaction.run("MATCH(n:Skill) RETURN(n)");
                    return hilfsMethode(transaction);
                }
            });
            System.out.println(puffer);
        }
    }

    private static List<Record> hilfsMethode(Transaction tx) {
        return tx.run("MATCH (n:Skill) RETURN (n)").list();
    }

    /**
     * Probably Müll.
     */
    public void fillTable() {

        try (Session session = driver.session()) {
            String receiveDatas = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    Result result = transaction.run("MATCH (n:Skill) RETURN (n)");

                    return String.valueOf(result);

                }
            });
            System.out.println(receiveDatas);
        }


    }

    /**
     * Probably Müll
     * Read&Write Code.
     * @return
     */
    public int fillTable2() {

        try (Session session = driver.session()) {
            int skillcount = 0;
            List<Record> skills = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    return matchPersonNodes(transaction);
                }
            });

            for (final Record Skill : skills) {

                skillcount += session.writeTransaction(new TransactionWork<Integer>() {
                    @Override
                    public Integer execute(Transaction transaction) {
                        transaction.run("MATCH (n:Skill) RETURN (n)");
                        return 1;
                    }
                });

            }
            System.out.println(skillcount);
            return skillcount;
        }


    }

    private static List<Record> matchPersonNodes(Transaction tx) {
        return tx.run("MATCH (n:Skill) RETURN (n)").list();
    }
}