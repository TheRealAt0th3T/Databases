import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

class finalProject {

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
                    } else if (args.length == 3) {
                        activateClass(conn, args[1], args[2], null);
                    } else if (args.length == 4) {
                        activateClass(conn, args[1], args[2], args[3]);
                    }
                    break;
                case "show-class":
                    System.out.println("Showing active class");
                    showActiveClass(conn);
                    break;
                case "show-categories":
                    System.out.println("Showing all categories...");
                    showCategories(conn);
                    break;
                case "add-category":
                    System.out.println("Adding new category...");
                    addCategory(conn, args[1], args[2]);
                    break;
                case "show-assignment":
                    System.out.println("Showing assignments...");
                    showAssignment(conn);
                    break;
                case "add-assignment":
                    System.out.println("Adding assignment...");
                    addAssignment(conn, args[1], args[2], args[3], args[4]);
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
                    if(args.length < 2){
                        System.out.println("Showing all students...");
                        showAllStudents(conn);
                    }else if (args.length > 1){
                        System.out.println("Showing specific student...");
                        showStudents(conn, args[1]);
                    }
                    break;
                case "grade":
                    System.out.println("Updating grade...");
                    gradeAssignment(conn, args[1], args[2], args[3]);
                    break;
                case "student-grades":
                    System.out.println("Showing student's grade");
                    showStudentsGrades(conn, args[1]);
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
    public void runQuery(Connection conn) {

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
        PreparedStatement setActive = null;
        PreparedStatement setNotActive = null;
        ResultSet rs = null;
        String cond = "";
        boolean hasResult = false;
        int whichCond = 0;
        int currClassId = 0;
        boolean canUpdate = true;

        try {
            if (sectionNum != null && term != null) {
                cond = "class_courseNum = ? AND class_term = ? AND class_sectionNum = ?;";
                whichCond = 3;
            } else if (term != null) {
                cond = "class_courseNum = ? AND class_term = ?;";
                whichCond = 2;
            } else {
                cond = "class_courseNum = ?;";
                whichCond = 1;
            }
            stmt = conn.prepareStatement("SELECT * FROM class WHERE " + cond, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            setNotActive = conn.prepareStatement("Update class set isActive = false where isActive = true OR isActive IS NULL;");
            setNotActive.execute();
            
            switch(whichCond) {
                case 1:
                    stmt.setString(1, courseNum);
                    break;
                case 2:
                    stmt.setString(1, courseNum);
                    stmt.setString(2, term);
                    break;
                case 3:
                    stmt.setString(1, courseNum);
                    stmt.setString(2, term);
                    stmt.setString(3, sectionNum);
                    break;
            }

            hasResult = stmt.execute();

            if (hasResult) {
                rs = stmt.getResultSet();
                rs.first();
                if (whichCond == 2 && rs.next()) {
                    System.out.println("There are multiple sections for " + courseNum + " " + term);
                } else if (whichCond == 1 && rs.next()) {
                    rs.first();
                    String recent = null;
                    String fullTerm = null;
                    boolean hasNext = true;

                    while (hasNext) {
                        if (recent == null) {
                            currClassId = rs.getInt(1);
                            recent = rs.getString(2).substring(2, rs.getString(2).length());
                            fullTerm = rs.getString(3);
                            hasNext = rs.next();
                        } else if (recent != null && fullTerm != rs.getString(3) && Integer.parseInt(recent) < Integer.parseInt(rs.getString(2).substring(2, rs.getString(2).length()))) {
                            currClassId = rs.getInt(1);
                            recent = rs.getString(2).substring(2, rs.getString(2).length());
                            fullTerm = rs.getString(3);
                            hasNext = rs.next();
                        } else {
                            System.out.println("There are multiple sections for " + courseNum);
                            hasNext = false;
                            canUpdate = false;
                        }
                    }
                    if (canUpdate) {
                        setActive = conn.prepareStatement("UPDATE class set isActive = true where class_id = ?");
                        setActive.setBoolean(1, true);
                        setActive.execute();
                        System.out.println("Class has been selected");
                    }
                } else {
                    rs.first();
                    currClassId = rs.getInt(1);
                    setActive = conn.prepareStatement("UPDATE class set isActive = true where class_id = ?");
                    setActive.setInt(1, currClassId);
                    setActive.execute();
                    System.out.println("Class has been selected");
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
            if (setActive != null) {
                try {
                    setActive.close();
                } catch (SQLException sqlEx) {
                    // ignore
                }
                setActive = null;
            }
            if (setNotActive != null) {
                try {
                    setNotActive.close();
                } catch (SQLException sqlEx) {
                    // ignore
                }
                setNotActive = null;
            }
        }
    }

    public static void showActiveClass(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("Select * from class where isActive = true");

            boolean rowsLeft = true;

            rs.first();
            while (rowsLeft) {
                System.out.println(rs.getInt(1) 
                        + ":" + rs.getString(2) 
                        + ":" + rs.getString(3) 
                        + ":" + rs.getInt(4)
                        + ":" + rs.getString(5));
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

    /**
     * list all categories and their weights
     * @param conn
     */
    public static void showCategories(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT categories_name, hasWeight_weight FROM categories" +
                    " JOIN hasWeight ON categories.categories_id = hasWeight.categories_id;");

            boolean hasNext = true;
            rs.first();
            while(hasNext){
                System.out.println("Category: " + rs.getString(1) + ", Weight: " + rs.getInt(2));
                hasNext = rs.next();
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
     * Adding a new category, no reference to a class
     * @param conn
     */
    public static void addCategory(Connection conn, String name, String weight) {
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean hasResult = false;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ps = conn.prepareStatement("insert into categories (categories_name) values (?);", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, name);
            ps.execute();

            ps = conn.prepareStatement("SELECT categories_id FROM categories WHERE categories_name = ? ;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, name);
            hasResult = ps.execute();

            if (hasResult) {
                rs = ps.getResultSet();
                rs.first();
                ps = conn.prepareStatement("insert into hasWeight (hasWeight_weight, categories_id) values (?, ?);", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, Integer.parseInt(weight));
                ps.setInt(2, rs.getInt(1));
                ps.execute();
            }

            System.out.println("Category was added.");

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
     * Showing list of assignments, their name, pointvalue, all in order by the category they belong to
     * @param conn
     */
    public static void showAssignment(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT assignments_name, assignments_pointValue, categories_id FROM assignments" +
                    " ORDER BY categories_id;");

            boolean hasNext = true;
            rs.first();
            while(hasNext){
                System.out.println("Assignment: " + rs.getString(1) + ", PointValue: " + rs.getInt(2) + ", CategoryID: " + rs.getInt(3));
                hasNext = rs.next();
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

    public static void addAssignment(Connection conn, String name, String cat, String descrip, String points) {
        PreparedStatement stmt = null;
        PreparedStatement stmtTwo = null;
        ResultSet rs = null;
        boolean hasResult = false;

        try {
            //check = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt = conn.prepareStatement("Select categories_id FROM categories WHERE categories_name = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, cat);
            hasResult = stmt.execute();
            
            if (hasResult) {
                rs = stmt.getResultSet();
                rs.beforeFirst();
            }

            if(hasResult && rs.next()){
                stmtTwo = conn.prepareStatement("insert into assignments" +
                    " (assignments_name, assignments_description, assignments_pointValue, categories_id) values (?, ?, ?, ?);", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmtTwo.setString(1, name);
                stmtTwo.setInt(3, Integer.parseInt(points));
                stmtTwo.setString(2, descrip);
                stmtTwo.setInt(4, rs.getInt(1));
                stmtTwo.execute();
                System.out.println("Assignment was added.");
            }else{
                System.out.println("ERROR: Category does not exist.");
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
     * adding student to db and enrolling them in currClass, if they exist already just enroll them into class
     * and update stored name if not same and print a warning for name change
     *
     * @param conn
     */
    public static void addStudent(Connection conn, String username, String studentid, String last, String first) {
        Statement stmt = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet student = null;
        int temp = -1;

        try {
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = stmt.executeQuery("Select class_id from class where isActive = true;");
                //System.out.println("HERE");

                if(rs != null){ //there exists a class
                    rs.first();
                    temp = rs.getInt(1);
                    System.out.println("current Class id is:" + temp);
                }

                stmt.close();

                ps = conn.prepareStatement("SELECT * FROM students WHERE students_username = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setString(1, username);
                boolean hasResult = ps.execute();

                if (hasResult) {
                    student = ps.getResultSet();
                    student.beforeFirst();
                }
                
                
                if(hasResult && student.next()){ //------------------------------------------------------------------------------------------
                    System.out.println("Student Exists. Adding to class now...");
                    ps = conn.prepareStatement("UPDATE students SET class_id = ? WHERE students_username = ?");
                    ps.setInt(1, temp);
                    ps.setString(2, username);
                    ps.execute();
                    System.out.println("Added to class");
                }else{
                    System.out.println("Student Does NOT Exists.");
                    ps = conn.prepareStatement("insert into students (students_firstName, students_lastName, students_username, students_IDnum, class_id) values (?, ?, ?, ?, ?);");
                    ps.setString(1, first);
                    ps.setString(2, last);
                    ps.setString(3, username);
                    ps.setInt(4, Integer.parseInt(studentid));
                    ps.setInt(5, temp);
                    ps.execute();
                    System.out.println("Student has been created and added to class.");
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
        Statement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rs = null;
        int temp = 0;

        try {
            String getActive = "SELECT class_id FROM class WHERE isActive = true";
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(getActive);
            rs.beforeFirst();
            if (rs.next()) {
                temp = rs.getInt(1);
            }
            

            stmt2 = conn.prepareStatement("SELECT * FROM students WHERE students_username = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt2.setString(1, username);
            boolean hasResult = stmt2.execute();

            if (hasResult) {
                rs = stmt2.getResultSet();
                rs.beforeFirst();
            }

            if(hasResult && rs.next()){ //therefore student exists
                stmt3 = conn.prepareStatement("UPDATE students SET class_id = ? WHERE students_username = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt3.setInt(1, temp);
                stmt3.setString(2, username);
                stmt3.execute();
                System.out.println("Student was updated.");
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
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt2 = null;
            }
            if (stmt3 != null) {
                try {
                    stmt3.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt3 = null;
            }
        }
    }

    /**
     * showing ALL students in currClass
     * @param conn
     */
    public static void showAllStudents(Connection conn) {

        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String num = "";
        try {
            //stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String getActive = "SELECT class_courseNum, class_sectionNum, class_term FROM class WHERE isActive = true";
            ps = conn.prepareStatement(getActive, ResultSet.TYPE_SCROLL_INSENSITIVE);
            //rs = stmt.executeQuery(getActive);
            boolean hasResults = ps.execute();

            if(hasResults){
                rs = ps.getResultSet();
                rs.next();
                num = rs.getString(1);
                ps = conn.prepareStatement("SELECT students_id, students_firstName, students_lastName, students_username, students_IDnum FROM class JOIN students on students.class_id = class.class_id WHERE class_courseNum = ? AND class_sectionNum = ? AND class_term = ?;");
                ps.setString(1, num);
                ps.setInt(2, rs.getInt(2));
                ps.setString(3, rs.getString(3));
                ps.execute();

                rs = ps.getResultSet();
                //rs.beforeFirst();
                System.out.println("StudentID, FirstName, LastName, Username, StudentIDnum");
                while (rs.next()) {
                    System.out.println(rs.getInt(1) + " , " + rs.getString(2) + " , " + rs.getString(3) + " , " + rs.getString(4) + ", " + rs.getDouble(5));
                }

            }

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
        PreparedStatement ps = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            name = name.toLowerCase();

            rs = stmt.executeQuery("SELECT students_id, students_firstName, students_lastName, students_username, students_IDnum FROM students WHERE students_firstName LIKE '%" +
                    name + "%' OR students_lastName LIKE '%" + name + "%' OR students_username LIKE '%" + name + "%';");

            System.out.println("StudentID, FirstName, LastName, Username, StudentIDnum");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " , " + rs.getString(2) + " , " + rs.getString(3) + " , " + rs.getString(4) + ", " + rs.getDouble(5));
            }

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
            ps = conn.prepareStatement("SELECT assignments_name, assignments_pointValue, assignedHW_grade, students_username FROM assignments" +
                            " JOIN assignedHW ON assignedHW.assignments_id = assignments.assignments_id" +
                    " JOIN students ON assignedHW.students_id = students.students_id WHERE assignments_name = ? AND students_username = ?;");
            ps.setString(1, assignmentName);
            ps.setString(2, username);
            ps.execute();

            //System.out.println("HERE");
            rs = ps.getResultSet();
            while(rs.next()) {
                if (rs.getInt(2) < Integer.parseInt(grade)) {
                    System.out.println("WARNING: The grade you are trying to input exceed the number of points configured (" + rs.getInt(2) + ").");
                    System.out.println("Setting points to max value.");
                    ps = conn.prepareStatement("UPDATE students" +
                            " JOIN assignedHW on assignedHW.students_id = students.students_id" +
                            " JOIN assignments on assignments.assignments_id = assignedHW.assignments_id" +
                            " SET assignedHW.assignedHW_grade = ? WHERE students.students_username = ?" +
                            " AND assignments.assignments_name = ?;");
                    ps.setInt(1, rs.getInt(2));
                    ps.setString(2, username);
                    ps.setString(3, assignmentName);
                    ps.execute();
                }else{
                    System.out.println("Updating...");
                    ps = conn.prepareStatement("UPDATE students" +
                            " JOIN assignedHW on assignedHW.students_id = students.students_id" +
                            " JOIN assignments on assignments.assignments_id = assignedHW.assignments_id" +
                            " SET assignedHW.assignedHW_grade = ? WHERE students.students_username = ?" +
                            " AND assignments.assignments_name = ?;");
                    ps.setInt(1, Integer.parseInt(grade));
                    ps.setString(2, username);
                    ps.setString(3, assignmentName);
                    ps.execute();
                }
                System.out.println("Grade is updated.");
            }

            /*
            if(rs.getInt(1) > Integer.parseInt(grade)){
                System.out.println("WARNING: The grade you are trying to input exceed the number of points configured (" + rs.getInt(1) + ").");
            }else{
                ps = conn.prepareStatement("UPDATE students" +
                        " JOIN assignedHW on assignedHW.students_id = students.students_id" +
                        " JOIN assignments on assignments.assignments_id = assignedHW.assignments_id" +
                        " SET assignedHW.assignedHW_grade = " + grade + "WHERE students.students_username =" + username +
                        " AND assignments.assignments_name = " + assignmentName + ";");
            }
            */
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
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean hasResult = false;

        try {
            stmt = conn.prepareStatement("SELECT c.categories_name, a.assignments_name, SUM((hw.assignedHW_grade * w.hasWeight_weight) / 100), a.assignments_pointValue" +
            " FROM categories c " +
            " JOIN assignments a ON a.categories_id = c.categories_id " +
            " JOIN assignedHW hw ON hw.assignments_id = a.assignments_id " +
            " JOIN students s ON s.students_id = hw.students_id " +
            " WHERE s.students_username = ? " +
            " GROUP by c.categories_name, a.assignments_name; ",ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, username);
            hasResult = stmt.execute();

            if (hasResult) {
                rs = stmt.getResultSet();
                rs.beforeFirst();

                System.out.println("[FORMAT]");
                System.out.println("Category | Assignment | Attempted | Max Possible Points");
                System.out.println("----------------------------------------------------------\n");

                while (rs.next()) {
                    System.out.println(
                        rs.getString(1) + "|" +
                        rs.getString(2) + "|" +
                        rs.getInt(3) + "|" +
                        rs.getInt(4) + "|"
                    );
                }

            } else {
                System.out.println("No student was found with this username: " + username);
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
    public void gradebook(Connection conn) {
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
