import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

class finalProject {
    private static int currClassID;
    private static String currClass;
    private static String currTerm;
    private static String currSection;
    private static String currDescription;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println();
			System.out.println("JDBC driver loaded");
			System.out.println();

            Connection conn = makeConnection("53306", "finalProject","Minfilia1178");

            switch(args[0]) {
                case "new-class":
                    System.out.println("Creating new class...");
                    System.out.println("\n\n[Result Format]\n\n");
                    System.out.println("ID:Course Number:Term:Section:Description\n\n");
                    createClass(conn, args[1], args[2], args[3], args[4]);
                    break;
                case "list-classes":
                    System.out.println("listing classes...");
                    System.out.println("\n\n[Result Format]\n\n");
                    System.out.println("ID:Course Number:Term:Section:Description:NumberOfStudents\n\n");
                    listClasses(conn);
                    break;
                case "select-class":
                    System.out.println("Selecting class...");
                    if (args.length == 2) {
                        activateClass(conn, args[1], null, null);
                        System.out.println("Class has been selected!");
                    } else if (args.length == 3) {
                        activateClass(conn, args[1], args[2], null);
                        System.out.println("Class has been selected!");
                    } else if (args.length == 4) {
                        activateClass(conn, args[1], args[2], args[3]);
                        System.out.println("Class has been selected!");
                    }
                    break;
                case "show-class":
                    break;
                case "show-categories":
                    break;
                case "add-category":
                    break;
                case "show-assignment":
                    break;
                case "add-assignment":
                    break;
                case "add-student":
                    System.out.println("Adding student to current class...");
                    if(args.length > 2){
                        addStudent(conn, args[1], args[2], args[3], args[4]);
                    }else {
                        editStudent(conn, args[1]);
                    }
                    break;
                case "show-students":
                    if(args.length > 1){
                        System.out.println("Showing all students...");
                        showAllStudents(conn);
                    }else{
                        System.out.println("Showing specific student...");
                        showStudents(conn, args[1]);
                    }
                    break;
                case "grade":
                    System.out.println("Updating grade...");
                    gradeAssignment(conn, args[1], args[2], args[3]);
                    break;
                case "student-grades":
                    break;
                case "gradebook":
                    break;
            }

            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Connection function to 
     * @param port
     * @param database
     * @param password
     * @return
     */
    public static Connection makeConnection(String port, String database, String password) {
        try {
            Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:" + port+ "/" + database+
						"?verifyServerCertificate=false&useSSL=true", "msandbox",
						password);
				// Do something with the Connection
				System.out.println("Database " + database +" connection succeeded!");
				System.out.println();
				return conn;
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        }
        return null;
    }

    /**
     * Run query function
     * @param conn
     */
    public static void runQuery(Connection conn) {

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT * FROM class;");
            // Now do something with the ResultSet ....
            
            boolean rowsLeft = true;
            rs.first();
            while (rowsLeft) {
                System.out.println(rs.getInt(1) 
                        + ":" + rs.getString(2) 
                        + ":" + rs.getString(3) 
                        + ":" + rs.getInt(4)
                        + ":" + rs.getString(5)
                        + ":" + rs.getString(6)
                        + ":" + rs.getString(7));
                        rowsLeft = rs.next();
            }

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }

    public static void createClass(Connection conn, String num, String term, String sectionNum, String description) {
        
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("Insert INTO class (class_courseNum," +
            " class_term, class_sectionNum, class_description)" +
            " VALUES(?, ?, ?, ?);");
            stmt.setString(1, num);
            stmt.setString(2, term);
            stmt.setInt(3, Integer.parseInt(sectionNum));
            stmt.setString(4, description);
            
            stmt.execute();

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (stmt != null) 
            {
                try 
                {
                    stmt.close();
                } 
                catch (SQLException sqlEx) 
                {
                } // ignore
                stmt = null;
            }
        }
    }

    public static void listClasses(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("Select c.*, Count(s.class_id) From class c " +
                " JOIN students s ON s.class_id = c.class_id " +
                " GROUP BY c.class_id;");

            boolean rowsLeft = true;

            rs.first();
            while (rowsLeft) {
                System.out.println(rs.getInt(1) 
                        + ":" + rs.getString(2) 
                        + ":" + rs.getString(3) 
                        + ":" + rs.getInt(4)
                        + ":" + rs.getString(5)
                        + ":" + rs.getInt(6));
                        rowsLeft = rs.next();
            }

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }

    public static void activateClass(Connection conn, String courseNum, String term, String sectionNum) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String cond = "";
        boolean hasResult = false;
        int whichCond = 0;

        try {
            if (sectionNum != null && term != null) {
                cond = "class_courseNum = \"" + courseNum + "\" AND class_term = \"" + term + "\" AND class_sectionNum = \"" + sectionNum + "\"";
                whichCond = 3;
            } else if (term != null) {
                cond = "class_courseNum = \"" + courseNum + "\" AND class_term = \"" + term + "\"";
                whichCond = 2;
            } else {
                cond = "class_courseNum = \"" + courseNum + "\"";
                whichCond = 1;
            }
            stmt = conn.prepareStatement("SELECT * FROM class WHERE " + cond);
            
            hasResult = stmt.execute();

            if (hasResult) {
                rs = stmt.getResultSet();
                rs.first();
                if (whichCond == 2 && rs.next()) {
                    System.out.println("There are multiple sections for " + courseNum + " " + term);
                } else if (whichCond == 1 && rs.next()) {
                    rs.first();
                    String recent = "";
                    boolean hasNext = true;

                    while (hasNext) {
                        if (recent == null) {
                            currClassID = rs.getInt(1);
                            currClass = rs.getString(2);
                            currTerm = rs.getString(3);
                            currSection = Integer.toString(rs.getInt(4));
                            currDescription = rs.getString(5);
                            recent = rs.getString(2).substring(2, rs.getString(2).length());
                        } else if (Integer.parseInt(recent) < Integer.parseInt(rs.getString(2).substring(2,rs.getString(2).length()))) {
                            currClassID = rs.getInt(1);
                            currClass = rs.getString(2);
                            currTerm = rs.getString(3);
                            currSection = Integer.toString(rs.getInt(4));
                            currDescription = rs.getString(5);
                            recent = rs.getString(2).substring(2, rs.getString(2).length());
                        }
                        hasNext = rs.next();
                    }
                } else {
                    currClassID = rs.getInt(1);
                    currClass = rs.getString(2);
                    currTerm = rs.getString(3);
                    currSection = Integer.toString(rs.getInt(4));
                    currDescription = rs.getString(5);
                }
            }

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }

    public static void showActiveClass(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT * FROM class;");

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }

    public static void showCategories(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT * FROM class;");

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }

    public static void addCategory(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT * FROM class;");

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }

    public static void showAssignment(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT * FROM class;");

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }

    public static void addAssignment(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT * FROM class;");

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }

    /**
     * adding student to db and enrolling them in currClass, if they exist already just enroll them into class
     * and update stored name if not same and print a warning for name change
     *
     * @param conn
     */
    public static void addStudent(Connection conn, String username, String studentid, String last, String first) {
        PreparedStatement stmt = null;
        Statement checkStmt = null;
        ResultSet rs = null;

        try {
            checkStmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = checkStmt.executeQuery("SELECT * FROM students WHERE students_IDnum = " + studentid);

            if(rs != null){ //student exists
                rs = stmt.executeQuery("SELECT students_firstName, students_lastName FROM students WHERE students_IDnum = " + studentid);

                if(rs.getString(1) != first){ //checking if first name is consistent
                    stmt = conn.prepareStatement("UPDATE students SET students_firstName = " + first);
                    System.out.println("WARNING: " + username + "'s first name is being updated.");
                    stmt.execute();
                }

                if(rs.getString(2) != last){
                    stmt = conn.prepareStatement("UPDATE students SET students_lastName = " + last);
                    System.out.println("WARNING: " + username + "'s last name is being updated.");
                    stmt.execute();
                }
                stmt = conn.prepareStatement("UPDATE students SET class_id = " + currClass + "WHERE students_IDnum =" + studentid);
                stmt.execute();

            }else{ //student doesn't exist
                stmt = conn.prepareStatement("insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id)" +
                        "values (?, ?, ?, ?, ?); ");
                stmt.setString(1, first);
                stmt.setString(2, last);
                stmt.setString(3, username);
                stmt.setInt(4, Integer.parseInt(studentid));
                stmt.setString(5, Integer.toString(currClassID));
                stmt.execute();
            }


        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }

    /**
     * adding existing student to current class, if not existing then fails
     * @param conn
     */
    public static void editStudent(Connection conn, String username) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery("SELECT * FROM students WHERE students_username =" + username);

            if(rs != null){ //therefore student exists
                stmt = conn.prepareStatement("UPDATE students SET class_id = " + currClass + "WHERE username =" + username);
                stmt.execute();
            }else{
                System.out.println("ERROR: This user does not exist.");
            }

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }

    /**
     * showing ALL students in currClass
     * @param conn
     */
    public static void showAllStudents(Connection conn) {

        PreparedStatement stmt = null;

        try {
            String temp = "SELECT * FROM class" +
                    "JOIN students on students.class_id = class.class_id" +
                    "WHERE class_courseNum = " + currClass;
            if(currTerm != null){
                temp += "AND class_term = " + currTerm;
                if(currSection != null){ //if sectionNUM exists
                    temp += "and class_sectionNum = " + currSection;
                }
            }

            temp += ";";

            stmt = conn.prepareStatement(temp);
            stmt.execute();

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (stmt != null) 
            {
                try 
                {
                    stmt.close();
                } 
                catch (SQLException sqlEx) 
                {
                } // ignore
                stmt = null;
            }
        }
    }

    /**
     * showing specific students via their name/username, regardless if in currClass
     * @param conn
     */
    public static void showStudents(Connection conn, String name) {

        PreparedStatement stmt = null;

        try {
            name = name.toLowerCase();
            String temp = "SELECT * FROM class" +
                    "JOIN students on students.class_id = class.class_id" +
                    "WHERE students.students_name LIKE '%" + name + "%' OR students.students_username LIKE '%" + name + "%';";

            stmt = conn.prepareStatement(temp);
            stmt.execute();

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException sqlEx)
                {
                } // ignore
                stmt = null;
            }
        }
    }


    /**
     * assign assignment's grade for student, replace student's grade if exists, if new grade exceeds maxpoints, print warning and set to max
     * @param conn
     */
    public static void gradeAssignment(Connection conn, String assignmentName, String username, String grade) {
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT assignments_pointValue FROM assignments WHERE assignments_name =" + assignmentName + ";");

            if(rs.getInt(1) > Integer.parseInt(grade)){
                System.out.println("WARNING: The grade you are trying to input exceed the number of points configured (" + rs.getInt(1) + ").");
            }else{
                ps = conn.prepareStatement("UPDATE students" +
                        "JOIN assignedHW on assignedHW.students_id = students.students_id" +
                        "JOIN assignments on assignments.assignments_id = assignedHW.assignments_id" +
                        "SET assignedHW.assignedHW_grade = " + grade + "WHERE students.students_username =" + username +
                        "AND assignments.assignments_name = " + assignmentName + ";");
            }

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }


    /**
     * show students current grade; all assignments grouped by category, with student's grade if they have one
     * show subtotals for each category and overall grade
     *
     * scale category weights so that they sum to 100
     *
     * Need to show two versions:
     *  one total grade based on total possible points (plus ones that dont have a grade yet)
     *  attempted grade based on points they currently have
     * @param conn
     */
    public static void showStudentsGrades(Connection conn, String username) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT * FROM class;");

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }

    /**
     * Show current class's gradebook: students (username, ID, and name) and their total grades in the class
     *
     * scale category weights so that they sum to 100
     *
     * Need to show two versions:
     *  one total grade based on total possible points (plus ones that dont have a grade yet)
     *  attempted grade based on points they currently have
     *
     * @param conn
     */
    public static void gradebook(Connection conn) {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("");

        } catch (SQLException ex) {
            // handle any errors
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException sqlEx)
                {
                } // ignore
                stmt = null;
            }
        }
    }



}