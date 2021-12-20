package com.demo.resy;

import javafx.collections.ObservableList;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//CYPHER

//DELETE ALL
//MATCH (n) DETACH DELETE n

//CONSTRAINTS & CREATION USER
//CREATE (n:User {username: '', email: '', password: ''})"

//CREATE CONSTRAINT ON (n:User) ASSERT n.username IS UNIQUE
//CREATE CONSTRAINT ON (n:User) ASSERT n.email IS UNIQUE
//CREATE CONSTRAINT ON (n:User) ASSERT n.id IS UNIQUE

//CONSTRAINTS & CREATION JOB
//CREATE (j:Job {jobname: '', description: '', durationOfactivity: ''})"



//CONSTRAINTS & CREATION SKILLS
//CREATE (n:Skill {skillname: '', description: '', category:''})


//RELATIONSHIPS
//MATCH (u:User{username:'root'}), (s:Skill{skillname:'php'})
//WHERE NOT (u)-[:has_skill]->(s)
//CREATE (u)-[rsu:has_skill]->(s)
//CREATE (s)-[rus:has_user]->(u)
//RETURN type(rsu)


//DELETE RELATIONSHIPS
//MATCH(s:Skill{skillname:'php'})-[r:has_user]-(u:User{username:'root'})
//DELETE r
//MATCH(s:Skill{skillname:'php'})-[r:has_skill]-(u:User{username:'root'})
//DELETE r

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

    private static List<Record> hilfsMethodeJobs(Transaction tx) {
        return tx.run("MATCH(j:Job)\n" +
                "RETURN id(j), j.jobtitle, j.company, j.jobdescription, j.location, j.experience, j.salary, j.rating").list();
    }

    private static List<Record> hilfsMethodeUnwind(Transaction tx, Record rec) {
        return tx.run("MATCH(j:Job{jobname:"+rec.get("j.jobname")+"})\n" +
                "UNWIND j.jobskills AS js\n" +
                "RETURN DISTINCT js").list();
    }

    /**
     * @param tx
     * @return List<Record>
     */
    private static List<Record> hilfsMethodeUserSkills(Transaction tx) {
        String username = Main.activeUser.getUsername();
        return tx.run("MATCH(u:User{username:'"+username+"'})-[r:has_skill]->(s:Skill)\n" +
                "RETURN s.skillname, s.description, s.category").list();
    }

    private static List<Record> hilfsMethodeSkillCategorys(Transaction tx) {
        String username = Main.activeUser.getUsername();
        return tx.run("MATCH(s:Skill)\n" +
                "RETURN DISTINCT s.category").list();
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
    public void createOffer(final Job input) {
        try (Session session = driver.session()) {
            String registerUser = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction){
                    Result result = transaction.run("CREATE (j:Job{jobtitle:'"+input.getJobtitle()+"',company:'"+input.getCompany()+"',location:'"+input.getLocation()+"',experience:'"+input.getExperience()+"',salary:'"+input.getSalary()+"', jobdescription:'"+input.getJobdescription()+"', rating:'0'})");
                    System.out.println("Job Created");
                    return null;

                }
            });
        }
    }

    public void deleteOffer(final Job job){
        try (Session session = driver.session()) {
            final String id = job.getJobid();
            String delete = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    Result result = transaction.run("MATCH(j:Job) WHERE id(j) = "+id+" DETACH DELETE j");
                    return null;
                }
            });
        }
    }

    /**
     * DEAD FUNCTION
     * @param input
     */
    public void createOfferRelationship(final Job input) {
        /*try (Session session = driver.session()) {
            final String jn = input.getJobname();
            final ObservableList<String> skills_selected = input.getJobskills();
            int size = skills_selected.size();
            String outputstring="";
            for(int i=0;i<size;i++){
                if(i<size-1)outputstring+="s.skillname='"+skills_selected.get(i)+"' OR ";
                else outputstring+="s.skillname='"+skills_selected.get(i)+"'";
            }
            String finalOutputstring = outputstring;
            String registerUser = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction){
                    Result result = transaction.run("MATCH(j:Job),(s:Skill)\n" +
                            "WHERE j.jobname='"+jn+"' AND ("+ finalOutputstring +")\n" +
                            "CREATE (j)-[r:needs_skill]->(s)");
                    System.out.println("Job-Relationship created.");
                    return null;

                }
            });
        }*/
    }
    /**
     * DEAD FUNCTION
     * @param input
     */
    public String returnList(ObservableList<String> input){
        String outputstring = "";
        int size = input.size();
        for(int i=0;i<size;i++) {
            if(i<size-1)outputstring+="'"+input.get(i)+"', ";
            else outputstring+="'"+input.get(i)+"'";



        }

        return outputstring;
    }

    /**
     * Erstellt eine Beziehung zwischen dem Nutzer und einem Skill.
     * @param activeUser
     * @param selectedSkill
     */
    public void createSkillRelationship(final User activeUser, final String selectedSkill){
        try(Session session = driver.session()){
            final String username = activeUser.getUsername();
            final String skill = selectedSkill;
            String registerUser = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    Result result = transaction.run("\n" +
                            "MATCH (u:User{username:'"+username+"'}), (s:Skill{skillname:'"+skill+"'})\n" +
                            "WHERE NOT (u)-[:has_skill]->(s)\n" +
                            "CREATE (u)-[rsu:has_skill]->(s)\n" +
                            "CREATE (s)-[rus:has_user]->(u)\n" +
                            "RETURN type(rsu) ");
                    return null;
                }
            });
        }

    }

    public void deleteSkillUserRelationships(final User activeUser, final String selectedSkill){
        try(Session session = driver.session()){
            final String username = activeUser.getUsername();
            final String skill = selectedSkill;
            String registerUser = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    Result result = transaction.run("\n" +
                            "MATCH(s:Skill{skillname:'"+skill+"'})-[r:has_user]-(u:User{username:'"+username+"'})\n" +
                            "DELETE r");
                    return null;
                }
            });
        }

        try(Session session = driver.session()){
            final String username = activeUser.getUsername();
            final String skill = selectedSkill;
            String registerUser = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    Result result = transaction.run("\n" +
                            "MATCH(s:Skill{skillname:'"+skill+"'})-[r:has_skill]-(u:User{username:'"+username+"'})\n" +
                            "DELETE r");
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
    //TODO: Methode verbessern
    public void readJobs() {
        Main.jobList.removeAll(Main.jobList);
        try (Session session = driver.session()) {
            List<Record> puffer = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result job = transaction.run("MATCH(j:Job)\n" +
                            "RETURN id(j), j.jobtitle, j.company, j.jobdescription, j.location, j.experience, j.salary, j.rating");
                    return hilfsMethodeJobs(transaction);
                }
            });

            try {
                for (Record item : puffer) {
                    Map<String, Object> map = item.asMap();
                    String ratingS = map.get("j.rating").toString();
                    int rating = Integer.parseInt(ratingS);
                    Job j = new Job(map.get("j.jobtitle").toString(), map.get("j.company").toString(), map.get("j.location").toString(), map.get("j.experience").toString(), map.get("j.salary").toString(), map.get("j.jobdescription").toString(), rating);
                    j.setJobid(map.get("id(j)").toString());
                    Main.jobList.add(j);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("Error in loading data.");
            }


        }
    }

    public void readUserSkills() {
        Main.userSkillsList.removeAll(Main.userSkillsList);
        String username = Main.activeUser.getUsername();
        try (Session session = driver.session()) {
            List<Record> puffer = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result result = transaction.run("MATCH(u:User{username:'"+username+"'})-[r:has_skill]->(s:Skill)\n" +
                            "RETURN s.skillname, s.description, s.category");
                    return hilfsMethodeUserSkills(transaction);
                }
            });

            for (Record item : puffer) {
                Map<String, Object> map = item.asMap();
                Skill s = new Skill((String) map.get("s.skillname"), (String) map.get("s.description"), (String) map.get("s.category"));
                Main.userSkillsList.add(s);
            }


        }
    }



}