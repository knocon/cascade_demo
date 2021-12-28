package com.demo.resy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
                "RETURN id(n), n.skillname, n.skilldescription").list();
    }

    private static List<Record> hilfsMethodeJobs(Transaction tx) {
        return tx.run("MATCH(j:Job)\n" +
                "RETURN id(j), j.jobtitle, j.company, j.jobdescription, j.location, j.experience, j.salary, j.likes, j.keywords").list();
    }

    private static List<Record> hilfsMethodeUser(Transaction tx) {
        return tx.run("MATCH(u:User)\n" +
                "RETURN id(u), u.country, u.email, u.firstname, u.lastname , u.password, u.plz, u.town, u.username, u.gender").list();
    }

    private static List<Record> hilfsMethodeJobsLiked(Transaction tx) {
        return tx.run("MATCH(u:User)-[l:likes]->(j:Job)\n" +
                "WHERE u.username='"+Main.activeUser.getUsername()+"'\n" +
                "RETURN id(j), j.jobtitle, j.company, j.keywords, j.likes, j.location, j.salary, j.experience, j.jobdescription").list();
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
                "RETURN s.skillname, s.skilldescription").list();
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
                    try {
                        Result result = transaction.run("CREATE (j:Job{jobtitle:'"+input.getJobtitle()+"',company:'"+input.getCompany()+"',location:'"+input.getLocation()+"',experience:'"+input.getExperience()+"',salary:'"+input.getSalary()+"', jobdescription:'"+input.getJobdescription()+"', likes:0, keywords:'"+Main.keywordGen.generateKeywordsJob(input)+"'})");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Job Created");
                    return null;

                }
            });
        }
    }

    public void likeOffer(final Job job, User active){
        try (Session session = driver.session()) {
            final String id = job.getJobid();
            String delete = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    Result result = transaction.run("MATCH (u:User{username:'"+active.getUsername()+"'}), (j:Job) WHERE id(j)="+id+" AND NOT (u)-[:likes]->(j) CREATE (u)-[l:likes]->(j) SET j.likes = j.likes+1");
                    return null;
                }
            });
        }
    }

    public void dislikeOffer(final Job job, User active){
        try (Session session = driver.session()) {
            final String id = job.getJobid();
            String delete = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction transaction) {
                    Result result = transaction.run("MATCH (u:User{username:'"+active.getUsername()+"'})-[l:likes]-(j:Job) WHERE id(j)="+id+" SET j.likes = j.likes-1 DELETE l");
                    return null;
                }
            });
        }
    }

    public void updateAllOffers(ObservableList<Job> input) {
        try (Session session = driver.session()) {
            for (Job j : input) {
                String jid = j.getJobid();
                String registerUser = session.writeTransaction(new TransactionWork<String>() {
                    @Override
                    public String execute(Transaction transaction){
                        try {
                            Result result = transaction.run("MATCH(j:Job) WHERE id(j)="+jid+" SET j.keywords ='"+Main.keywordGen.generateKeywordsJob(j)+"'");
                            System.out.println("Job:"+jid+" updated.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return null;

                    }
                });
            }
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

    public ObservableList<Job> randomJobs(ObservableList<Job> input){
        Random rand = new Random();
        ObservableList<Job> output = FXCollections.observableArrayList();

        for(int i=0; i<10; i++){
            int randomIndex = rand.nextInt(input.size());
            Job j = input.get(randomIndex);
            output.add(j);
            input.remove(randomIndex);
        }

        return output;
    }

    public ObservableList<Skill> randomSkills(ObservableList<Skill> input){
        Random rand = new Random();
        ObservableList<Skill> copy = FXCollections.observableArrayList();
        copy.addAll(input);
        ObservableList<Skill> output = FXCollections.observableArrayList();

        for(int i=0; i<10; i++){
            int randomIndex = rand.nextInt(copy.size());
            Skill s = copy.get(randomIndex);
            output.add(s);
            copy.remove(randomIndex);
        }

        return output;
    }


    public void assignRandomSkillsToUsers() {

        for(User u : Main.userList){
            ObservableList<Skill> skills = randomSkills(Main.skillsList);

            try(Session session = driver.session()){
                String assignSkills = session.writeTransaction(new TransactionWork<String>() {
                    @Override
                    public String execute(Transaction transaction) {
                        Result result = transaction.run("\n" +
                                "MATCH (u:User{username:'"+u.getUsername()+"'}), (s0:Skill{skillname:'"+skills.get(0).getSkillname()+"'}), (s1:Skill{skillname:'"+skills.get(1).getSkillname()+"'}), (s2:Skill{skillname:'"+skills.get(2).getSkillname()+"'}), (s3:Skill{skillname:'"+skills.get(3).getSkillname()+"'}), (s4:Skill{skillname:'"+skills.get(4).getSkillname()+"'}),(s5:Skill{skillname:'"+skills.get(5).getSkillname()+"'}),(s6:Skill{skillname:'"+skills.get(6).getSkillname()+"'}),(s7:Skill{skillname:'"+skills.get(7).getSkillname()+"'}),(s8:Skill{skillname:'"+skills.get(8).getSkillname()+"'}), (s9:Skill{skillname:'"+skills.get(9).getSkillname()+"'})\n" +
                                "CREATE (u)-[:has_skill]->(s0)\n" +
                                "CREATE (u)-[:has_skill]->(s1)\n" +
                                "CREATE (u)-[:has_skill]->(s2)\n" +
                                "CREATE (u)-[:has_skill]->(s3)\n" +
                                "CREATE (u)-[:has_skill]->(s4)\n" +
                                "CREATE (u)-[:has_skill]->(s5)\n" +
                                "CREATE (u)-[:has_skill]->(s6)\n" +
                                "CREATE (u)-[:has_skill]->(s7)\n" +
                                "CREATE (u)-[:has_skill]->(s8)\n" +
                                "CREATE (u)-[:has_skill]->(s9)");

                        System.out.println(u.getId()+" created skill connection to:"+skills.get(0)+" "+skills.get(1)+" "+skills.get(2)+" "+skills.get(3)+" "+skills.get(4)+" "+skills.get(5)+" "+skills.get(6)+" "+skills.get(7)+" "+skills.get(8)+" "+skills.get(9));
                        System.out.println(Main.skillsList.size());
                        return null;
                    }
                });
            }

        }

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
                            "RETURN id(n), n.skillname, n.skilldescription");
                    return hilfsMethode(transaction);
                }
            });

            for (Record item : puffer) {
                Map<String, Object> map = item.asMap();
                Skill s = new Skill((String) map.get("n.skillname"), (String) map.get("n.skilldescription"));
                Main.skillsList.add(s);
            }


        }
    }
    public void readJobs() {
        Main.jobList.removeAll(Main.jobList);
        try (Session session = driver.session()) {
            List<Record> puffer = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result job = transaction.run("MATCH(j:Job)\n" +
                            "RETURN id(j), j.jobtitle, j.company, j.jobdescription, j.location, j.experience, j.salary, j.likes, j.keywords");
                    return hilfsMethodeJobs(transaction);
                }
            });

            try {
                for (Record item : puffer) {
                    Map<String, Object> map = item.asMap();
                    String ratingS = map.get("j.likes").toString();
                    int likes = Integer.parseInt(ratingS);
                    Job j = new Job(map.get("j.jobtitle").toString(), map.get("j.company").toString(), map.get("j.location").toString(), map.get("j.experience").toString(), map.get("j.salary").toString(), map.get("j.jobdescription").toString(), likes);
                    j.setJobid(map.get("id(j)").toString());
                    j.setKeywords(map.get("j.keywords").toString().split(", "));
                    Main.jobList.add(j);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("Error in loading data.");
            }


        }
    }

    public void readUsers() {
        Main.userList.removeAll(Main.userList);
        try (Session session = driver.session()) {
            List<Record> puffer = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result user = transaction.run("MATCH(u:User)\n" +
                            "RETURN id(u), u.country, u.email, u.firstname, u.lastname , u.password, u.plz, u.town, u.username, u.gender");
                    return hilfsMethodeUser(transaction);
                }
            });

            try {
                for (Record item : puffer) {
                    Map<String, Object> map = item.asMap();
                    User u = new User(map.get("u.username").toString(), map.get("u.email").toString(), map.get("u.password").toString(), map.get("u.firstname").toString(), map.get("u.lastname").toString(), map.get("u.gender").toString(), map.get("u.country").toString(), map.get("u.town").toString(), map.get("u.plz").toString());
                    u.setId(map.get("id(u)").toString());
                    Main.userList.add(u);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("Error in loading data.");
            }


        }
    }

    public void readLikedJobs() {
        Main.likedJobList.removeAll(Main.likedJobList);

        try (Session session = driver.session()) {
            List<Record> puffer = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result job = transaction.run("MATCH(u:User)-[l:likes]->(j:Job)\n" +
                            "WHERE u.username='" + Main.activeUser.getUsername() + "'\n" +
                            "RETURN id(j), j.jobtitle, j.company, j.keywords, j.likes, j.location, j.salary, j.experience, j.jobdescription");
                    return hilfsMethodeJobsLiked(transaction);
                }
            });

            try {
                for (Record item : puffer) {

                    Map<String, Object> map = item.asMap();
                    String ratingS = map.get("j.likes").toString();
                    int likes = Integer.parseInt(ratingS);
                    Job j = new Job(map.get("j.jobtitle").toString(), map.get("j.company").toString(), map.get("j.location").toString(), map.get("j.experience").toString(), map.get("j.salary").toString(), map.get("j.jobdescription").toString(), likes);
                    j.setJobid(map.get("id(j)").toString());
                    j.setKeywords(map.get("j.keywords").toString().split(", "));
                    Main.likedJobList.add(j);
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
                            "RETURN s.skillname, s.skilldescription");
                    return hilfsMethodeUserSkills(transaction);
                }
            });

            for (Record item : puffer) {
                Map<String, Object> map = item.asMap();
                Skill s = new Skill((String) map.get("s.skillname"), (String) map.get("s.skilldescription"));
                Main.userSkillsList.add(s);
            }


        }
    }

    /**
     * Importiert das gestellte Datenset.
     * Import dauert ca 7-10Minuten.
     */


    public void createJobsCSV(){
        Path pathToFile = Paths.get("src/main/resources/com/demo/resy/jobdata/masterjobs.txt");

        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.ISO_8859_1)) {
            String line = br.readLine();
            while(line!=null){
                System.out.println(line);
                String[] attributes = line.split(";");
                String jobtitel = attributes[0];
                String company = attributes[1];
                String location = attributes[2];
                String experience = attributes[3];
                String salary = attributes[4];
                String jobdescription = attributes[5];

                String generateKeywords ="";
                generateKeywords+= company+","+jobtitel+","+location;


                try (Session session = driver.session()) {
                    String finalGenerateKeywords = generateKeywords;
                    String registerUser = session.writeTransaction(new TransactionWork<String>() {
                        @Override
                        public String execute(Transaction transaction){
                            try {
                                Result result = transaction.run("CREATE (j:Job{jobtitle:'"+jobtitel+"',company:'"+company+"',location:'"+location+"',experience:'"+experience+"',salary:'"+salary+"', jobdescription:'"+jobdescription+"', likes:0, keywords:'"+Main.keywordGen.generateKeywordsString(finalGenerateKeywords)+"'})");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Job Created");
                            return null;

                        }
                    });
                }

                line = br.readLine();}
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createSkillsCSV(){
        Path pathToFile = Paths.get("src/main/resources/com/demo/resy/jobdata/masterskills.txt");

        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.ISO_8859_1)) {
            String line = br.readLine();
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();

            while(line!=null&&line1!=null&&line2!=null&&line3!=null){

                String[] attributes = line.split(";");
                String skillname = attributes[0];
                String skilldescription = attributes[1];

                String[] attributes2 = line1.split(";");
                String skillname2 = attributes2[0];
                String skilldescription2 = attributes2[1];

                String[] attributes3 = line2.split(";");
                String skillname3 = attributes3[0];
                String skilldescription3 = attributes3[1];

                String[] attributes4 = line3.split(";");
                String skillname4 = attributes4[0];
                String skilldescription4 = attributes4[1];

                try (Session session = driver.session()) {
                    String registerUser = session.writeTransaction(new TransactionWork<String>() {
                        @Override
                        public String execute(Transaction transaction){

                            Result result = transaction.run("CREATE (:Skill{skillname:'"+skillname+"', skilldescription:'"+skilldescription+"'})"+
                                    "CREATE (:Skill{skillname:'"+skillname2+"', skilldescription:'"+skilldescription2+"'})\n" +
                                    "CREATE (:Skill{skillname:'"+skillname3+"', skilldescription:'"+skilldescription3+"'})\n" +
                                    "CREATE (:Skill{skillname:'"+skillname4+"', skilldescription:'"+skilldescription4+"'})");

                            System.out.println("Skill "+skillname+" created.");
                            return null;

                        }
                    });
                }
                line = br.readLine();
                line1 = br.readLine();
                line2 = br.readLine();
                line3 = br.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createUsersCSV(){
        Path pathToFile = Paths.get("src/main/resources/com/demo/resy/jobdata/masterusers.txt");
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.ISO_8859_1)) {
            String line = br.readLine();
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();

            while(line!=null&&line1!=null&&line2!=null&&line3!=null){

                String[] attributes = line.split(";");
                String gender = attributes[1];
                String firstname = attributes[2];
                String lastname = attributes[3];
                String password = attributes[4];
                String plz = attributes[5];
                String town = attributes[6];
                String email = attributes[7];
                String country = attributes[8];


                String[] attributes1 = line1.split(";");
                String gender1 = attributes1[1];
                String firstname1 = attributes1[2];
                String lastname1 = attributes1[3];
                String password1 = attributes1[4];
                String plz1 = attributes1[5];
                String town1 = attributes1[6];
                String email1 = attributes1[7];
                String country1 = attributes1[8];

                String[] attributes2 = line2.split(";");
                String gender2 = attributes2[1];
                String firstname2 = attributes2[2];
                String lastname2 = attributes2[3];
                String password2 = attributes2[4];
                String plz2 = attributes2[5];
                String town2 = attributes2[6];
                String email2 = attributes2[7];
                String country2 = attributes2[8];

                String[] attributes3 = line3.split(";");
                String gender3 = attributes3[1];
                String firstname3 = attributes3[2];
                String lastname3 = attributes3[3];
                String password3 = attributes3[4];
                String plz3 = attributes3[5];
                String town3 = attributes3[6];
                String email3 = attributes3[7];
                String country3 = attributes3[8];


                try (Session session = driver.session()) {
                    String registerUser = session.writeTransaction(new TransactionWork<String>() {
                        @Override
                        public String execute(Transaction transaction){

                            Result result = transaction.run("CREATE (:User{username:'"+lastname+firstname+"', email:'"+email+"', password:'"+password+"', firstname:'"+firstname+"', lastname:'"+lastname+"', gender:'"+gender+"', country:'"+country+"', town:'"+town+"', plz:'"+plz+"'})\n" +
                                    "CREATE (:User{username:'"+lastname1+firstname1+"', email:'"+email1+"', password:'"+password1+"', firstname:'"+firstname1+"', lastname:'"+lastname1+"', gender:'"+gender1+"', country:'"+country1+"', town:'"+town1+"', plz:'"+plz1+"'})\n"+
                                    "CREATE (:User{username:'"+lastname2+firstname2+"', email:'"+email2+"', password:'"+password2+"', firstname:'"+firstname2+"', lastname:'"+lastname2+"', gender:'"+gender2+"', country:'"+country2+"', town:'"+town2+"', plz:'"+plz2+"'})\n"+
                                    "CREATE (:User{username:'"+lastname3+firstname3+"', email:'"+email3+"', password:'"+password3+"', firstname:'"+firstname3+"', lastname:'"+lastname3+"', gender:'"+gender3+"', country:'"+country3+"', town:'"+town3+"', plz:'"+plz3+"'})\n");
                            System.out.println("User created.");
                            return null;

                        }
                    });
                }
                line = br.readLine();
                line1 = br.readLine();
                line2 = br.readLine();
                line3 = br.readLine();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //TODO: Create User -> Skill relationships randomly.


    //TODO: Find similar user.


}