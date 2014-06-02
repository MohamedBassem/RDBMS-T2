package eg.edu.guc.dbms.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import eg.edu.guc.dbms.engine.DBApp;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.utils.btrees.BTreeAdopter;


public class DBAppTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws DBEngineException {
		DBApp dbEngine = new DBApp();
		//System.out.println(dbEngine.properties.getData());
		
		//testEngine_Meta(dbEngine); //PASSED
		//testEngine_MetaError(); //PASSED
		//testEngine_MetaDuplicate(); //PASSED
		// testEngine_MetaRefrence(); //PASSED
		// --------------------------------
		//testEngine_Insert(dbEngine); //PASSED
		//testEngine_DublicateIDInsert(dbEngine);//PASSED
		//testEngine_NonExistingColumnInsert(dbEngine); //PASSED
		// -------------------------------------
		//testEngine_Select(dbEngine); // PASSED
		//testEngine_SelectNoRecordsFound(dbEngine); // PASSED
		//testEngine_SelectNoTable(dbEngine); // PASSED
		// -------------------------------------
		//testEngine_DeleteOR(dbEngine); //PASSED
		//testEngine_DeleteAND(dbEngine); //PASSED
		// -------------------------------------
		//testEngine_CreateIndex(dbEngine); //PASSED
		//testEngine_NonExistingColumnCreateIndex(dbEngine); //PASSED
		// -------------------------------------
		// Custom tests :
		//testEngine_SelectAll(dbEngine); // PASSED
	}

     /*
      * This method should execute the insertion and the pages should show the inserted data
      */
	public static void testEngine_Insert(DBApp dbEngine) {
		

		try {
			
			String tableName = "Department";

			Hashtable<String, String> htblColNameValue_ = new Hashtable<String, String>();
			htblColNameValue_.put("Name", "Accounting");
			htblColNameValue_.put("Location", "test");
			dbEngine.insertIntoTable(tableName, htblColNameValue_);
			
			htblColNameValue_.put("Name", "HR");
			htblColNameValue_.put("Location", "test");
			dbEngine.insertIntoTable(tableName, htblColNameValue_);
			
			htblColNameValue_.put("Name", "IT");
			htblColNameValue_.put("Location", "test");
			dbEngine.insertIntoTable(tableName, htblColNameValue_);
			 
			htblColNameValue_.put("Name", "Software");
			htblColNameValue_.put("Location", "test");
			dbEngine.insertIntoTable(tableName, htblColNameValue_);
			
			
			tableName = "Employee";

			Hashtable<String, String> htblColNameValue = new Hashtable<String, String>();
			htblColNameValue.put("ID", (new Integer(1)).toString());
			htblColNameValue.put("Name", "Stevenson Morris");
			htblColNameValue.put("Dept", "Software");
			htblColNameValue.put("Start_Date", "12-12-2030");
			dbEngine.insertIntoTable(tableName, htblColNameValue);
			
			htblColNameValue.put("ID", (new Integer(2)).toString());
			htblColNameValue.put("Name", "John Smith");
			htblColNameValue.put("Dept", "Accounting");
			htblColNameValue.put("Start_Date", "12-08-2030");

			dbEngine.insertIntoTable(tableName, htblColNameValue);

			htblColNameValue.put("ID", (new Integer(3)).toString());
			htblColNameValue.put("Name", "Mary Smith");
			htblColNameValue.put("Dept", "IT");
			htblColNameValue.put("Start_Date", "12-08-2030");

			dbEngine.insertIntoTable(tableName, htblColNameValue);
			
			htblColNameValue.put("ID", (new Integer(4)).toString());
			htblColNameValue.put("Name", "Tom Robinson");
			htblColNameValue.put("Dept", "Accounting");
			htblColNameValue.put("Start_Date", "12-09-2010");

			dbEngine.insertIntoTable(tableName, htblColNameValue);
			htblColNameValue.put("ID", (new Integer(5)).toString());
			htblColNameValue.put("Name", "Adam Mathew");
			htblColNameValue.put("Dept", "HR");
			htblColNameValue.put("Start_Date", "2-09-2020");

			dbEngine.insertIntoTable(tableName, htblColNameValue);
			htblColNameValue.put("ID", (new Integer(6)).toString());
			htblColNameValue.put("Name", "Andrea Willson");
			htblColNameValue.put("Dept", "IT");
			htblColNameValue.put("Start_Date", "4-10-2013");

			dbEngine.insertIntoTable(tableName, htblColNameValue);

		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method should throws an exception because there are duplicate entries with the same primary key 
	 */
	public static void testEngine_DublicateIDInsert(DBApp dbEngine) {
		

		try {
			
			String tableName = "Department";

			Hashtable<String, String> htblColNameValue_ = new Hashtable<String, String>();
			htblColNameValue_.put("Name", "Accounting");
			htblColNameValue_.put("Location", "test");
			dbEngine.insertIntoTable(tableName, htblColNameValue_);
			
			htblColNameValue_.put("Name", "HR");
			htblColNameValue_.put("Location", "test");
			dbEngine.insertIntoTable(tableName, htblColNameValue_);
			
			htblColNameValue_.put("Name", "IT");
			htblColNameValue_.put("Location", "test");
			dbEngine.insertIntoTable(tableName, htblColNameValue_);
			
			htblColNameValue_.put("Name", "Software");
			htblColNameValue_.put("Location", "test");
			dbEngine.insertIntoTable(tableName, htblColNameValue_);
			
			tableName = "Employee";

			Hashtable<String, String> htblColNameValue = new Hashtable<String, String>();
			htblColNameValue.put("ID", (new Integer(1)).toString());
			htblColNameValue.put("Name", "Stevenson Morris");
			htblColNameValue.put("Dept", "Software");
			htblColNameValue.put("Start_Date", "12-12-2030");
			
			dbEngine.insertIntoTable(tableName, htblColNameValue); 
			
			htblColNameValue.put("ID", (new Integer(2)).toString());
			htblColNameValue.put("Name", "John Smith");
			htblColNameValue.put("Dept", "Accounting");
			htblColNameValue.put("Start_Date", "12-08-2030");

			dbEngine.insertIntoTable(tableName, htblColNameValue);

			htblColNameValue.put("ID", (new Integer(3)).toString());
			htblColNameValue.put("Name", "Mary Smith");
			htblColNameValue.put("Dept", "IT");
			htblColNameValue.put("Start_Date", "12-08-2030");

			dbEngine.insertIntoTable(tableName, htblColNameValue);
			
			htblColNameValue.put("ID", (new Integer(3)).toString());
			htblColNameValue.put("Name", "Tom Robinson");
			htblColNameValue.put("Dept", "Accounting");
			htblColNameValue.put("Start_Date", "12-09-2010");

			dbEngine.insertIntoTable(tableName, htblColNameValue);

		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}
	/*
	 * This method should throw an exception because the insertion consists of
	 *  a column name "End_Date" does not exist
	 */
	public static void testEngine_NonExistingColumnInsert(DBApp dbEngine) {
		// Test

		try {
			
			String tableName = "Department";

			Hashtable<String, String> htblColNameValue_ = new Hashtable<String, String>();
			htblColNameValue_.put("Name", "Accounting");
			htblColNameValue_.put("Location", "test");
			dbEngine.insertIntoTable(tableName, htblColNameValue_);
			
			htblColNameValue_.put("Name", "HR");
			htblColNameValue_.put("Location", "test");
			dbEngine.insertIntoTable(tableName, htblColNameValue_);
			
			htblColNameValue_.put("Name", "IT");
			htblColNameValue_.put("Location", "test");
			dbEngine.insertIntoTable(tableName, htblColNameValue_);
			
			htblColNameValue_.put("Name", "Software");
			htblColNameValue_.put("Location", "test");
			dbEngine.insertIntoTable(tableName, htblColNameValue_);
			
			tableName = "Employee";

			Hashtable<String, String> htblColNameValue = new Hashtable<String, String>();
			htblColNameValue.put("ID", (new Integer(1)).toString());
			htblColNameValue.put("Name", "Stevenson Morris");
			htblColNameValue.put("Dept", "Software");
			htblColNameValue.put("Start_Date", "12-12-2030");
			
			dbEngine.insertIntoTable(tableName, htblColNameValue);
			
			htblColNameValue.put("ID", (new Integer(2)).toString());
			htblColNameValue.put("Name", "John Smith");
			htblColNameValue.put("Dept", "Accounting");
			htblColNameValue.put("End_Date", "12-08-2030");

			dbEngine.insertIntoTable(tableName, htblColNameValue);


		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}
	/*
	 * This method should result on the deletions of all the records that have the 
	 * Dept value equals IT or Accounting; this entries should have a deletion marker 
	 */
	public static void testEngine_DeleteOR(DBApp dbEngine) {
		

		try {
			String tableName = "Employee";
			Hashtable<String, String> htblColNameValue = new Hashtable<String, String>();
			//htblColNameValue.put("Dept", "IT");
			htblColNameValue.put("Dept", "Accounting");

			dbEngine.deleteFromTable(tableName, htblColNameValue, "OR");

		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This method should result on the deletions of all the record that has 
	 * this two conditions satisfied and this record should have a deletion marker. 
	 */
	public static void testEngine_DeleteAND(DBApp dbEngine) {
		

		try {
			String tableName = "Employee";
			Hashtable<String, String> htblColNameValue = new Hashtable<String, String>();
			htblColNameValue.put("Name", "John Smith");
			htblColNameValue.put("Dept", "Accounting");

			dbEngine.deleteFromTable(tableName, htblColNameValue, "AND");

		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}
	
	
  /*
   *This method should print all the rows numbers of the records that 
   *match the selection 
   */
	public static void testEngine_Select(DBApp dbEngine) {

		try {
			String tableName = "Employee";
			Hashtable<String, String> htblColNameValue = new Hashtable<String, String>();
			htblColNameValue.put("Dept", "Accounting");
			htblColNameValue.put("ID", "3");
			htblColNameValue.put("Name", "Adam Mathew");

			Iterator iter = dbEngine.selectFromTable(tableName,
					htblColNameValue, "OR");
			while (iter.hasNext()) {
				Hashtable<String, String> obj = (Hashtable<String, String>) iter.next();
				System.out.println(obj);
			}

		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}
	
	  /*
	   *This method should print no records found since no
	   * records matched the selection
	   */
		public static void testEngine_SelectNoRecordsFound(DBApp dbEngine) {

			try {
				String tableName = "Employee";
				Hashtable<String, String> htblColNameValue = new Hashtable<String, String>();
				htblColNameValue.put("Dept", "Management");

				Iterator iter = dbEngine.selectFromTable(tableName,
						htblColNameValue, "OR");
				if(iter==null){
					System.out.println("No records found");
				   return;
				}
				
				while (iter.hasNext()) {
					Hashtable<String, String> obj = (Hashtable<String, String>) iter.next();
					System.out.println(obj);
				}
				
			} catch (DBEngineException e) {
				e.printStackTrace();
			}
		}
		
		  /*
		   *This method should throw an exception because the selection 
		   *is from a table "Employer" that does not exist.
		   */
			public static void testEngine_SelectNoTable(DBApp dbEngine) {

				try {
					String tableName = "Employer";
					Hashtable<String, String> htblColNameValue = new Hashtable<String, String>();
					htblColNameValue.put("Dept", "Management");

					Iterator iter = dbEngine.selectFromTable(tableName,
							htblColNameValue, "OR");
					while (iter.hasNext()) {
						//keep printing rows numbers
						 
					}
					
					

				} catch (DBEngineException e) {
					e.printStackTrace();
				}
			}
		
		
	
    /*
     * This method should create an index on the Dept attribute
     */
	public static void testEngine_CreateIndex(DBApp dbEngine) {

		try {
			String tableName = "Employee";

			dbEngine.createIndex(tableName, "Dept");
		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}
	/*
	 * This method should return an exception since 
	 * the requested index is on a variable that does not exist
	 */
	
	public static void testEngine_NonExistingColumnCreateIndex(DBApp dbEngine) {

		try {
			String tableName = "Employee";

			dbEngine.createIndex(tableName, "Experience");
		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method should create two tables Employee and Department where the Metadata
	 * file should hold all the information on these tables.
	 */
	public static void testEngine_Meta(DBApp dbEngine) {
		

		try {
			Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
			//htblColNameType.put("ID", "java.lang.Integer");
			htblColNameType.put("Name", "java.lang.String");
			htblColNameType.put("Location", "java.lang.String");

			Hashtable<String, String> htblColNameRefs = new Hashtable<String, String>();
			//htblColNameRefs.put("ID", "null");
			htblColNameRefs.put("Name", "null");
			htblColNameRefs.put("Location", "null");

			dbEngine.createTable("Department", htblColNameType,
					htblColNameRefs, "Name");
			
			String tableName = "Employee";
			htblColNameType = new Hashtable<String, String>();
			htblColNameType.put("ID", "java.lang.Integer");
			htblColNameType.put("Name", "java.lang.String");
			htblColNameType.put("Dept", "java.lang.String");
			htblColNameType.put("Start_Date", "java.util.Date");

			htblColNameRefs = new Hashtable<String, String>();
			htblColNameRefs.put("ID", "null");
			htblColNameRefs.put("Name", "null");
			htblColNameRefs.put("Dept", "Department.Name");
			htblColNameRefs.put("Start_Date", "null");

			dbEngine.createTable(tableName, htblColNameType, htblColNameRefs,
					"ID");

		
			dbEngine.saveAll();

		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method should throw an exception because the creation of the first table specifies
	 *  a column to be the primary key that does not exist 
	 */
	public static void testEngine_MetaError() {
		

		try {
			DBApp dbEngine = new DBApp();
			
			
			Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
			htblColNameType.put("ID", "java.lang.Integer");
			htblColNameType.put("Name", "java.lang.String");
			htblColNameType.put("Location", "java.lang.String");

			Hashtable<String, String> htblColNameRefs = new Hashtable<String, String>();
			htblColNameRefs.put("ID", "null");
			htblColNameRefs.put("Name", "null");
			htblColNameRefs.put("Location", "null");

			dbEngine.createTable("Department", htblColNameType,
					htblColNameRefs, "ID");
			
			String tableName = "Employee";
			htblColNameType = new Hashtable<String, String>();
			htblColNameType.put("ID", "java.lang.Integer");
			htblColNameType.put("Name", "java.lang.String");
			htblColNameType.put("Dept", "java.lang.String");
			htblColNameType.put("Start_Date", "java.util.Date");

			htblColNameRefs = new Hashtable<String, String>();
			htblColNameRefs.put("ID", "null");
			htblColNameRefs.put("Name", "null");
			htblColNameRefs.put("Dept", "Department.ID");
			htblColNameRefs.put("Start_Date", "null");

			dbEngine.createTable(tableName, htblColNameType, htblColNameRefs,
					"Age");

			dbEngine.saveAll();

		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This method should throw an exception because there can't be duplicate tables
	 */
	public static void testEngine_MetaDuplicate() {
		

		try {
			DBApp dbEngine = new DBApp();
			
			Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

			htblColNameType.put("ID", "java.lang.Integer");
			htblColNameType.put("Name", "java.lang.String");
			htblColNameType.put("Location", "java.lang.String");

			
			Hashtable<String, String> htblColNameRefs = new Hashtable<String, String>();

			htblColNameRefs.put("ID", "null");
			htblColNameRefs.put("Name", "null");
			htblColNameRefs.put("Location", "null");

			dbEngine.createTable("Employee", htblColNameType,
					htblColNameRefs, "ID");
			
			String tableName = "Employee";
			htblColNameType = new Hashtable<String, String>();
			htblColNameType.put("ID", "java.lang.Integer");
			htblColNameType.put("Name", "java.lang.String");
			htblColNameType.put("Dept", "java.lang.String");
			htblColNameType.put("Start_Date", "java.util.Date");

			htblColNameRefs = new Hashtable<String, String>();
			htblColNameRefs.put("ID", "null");
			htblColNameRefs.put("Name", "null");
			htblColNameRefs.put("Dept", "Department.ID");
			htblColNameRefs.put("Start_Date", "null");

			dbEngine.createTable(tableName, htblColNameType, htblColNameRefs,
					"ID");

		
			dbEngine.saveAll();

		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This method should throw an exception because the second table is referencing 
	 * another table "Company" that does not exist.
	 */
	public static void testEngine_MetaRefrence() {
		

		try {
			DBApp dbEngine = new DBApp();
			
			
			// Second table
			Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

		
			htblColNameType.put("ID", "java.lang.Integer");
			htblColNameType.put("Name", "java.lang.String");
			htblColNameType.put("Location", "java.lang.String");

			Hashtable<String, String> htblColNameRefs = new Hashtable<String, String>();
			htblColNameRefs.put("ID", "null");
			htblColNameRefs.put("Name", "null");
			htblColNameRefs.put("Location", "null");

			dbEngine.createTable("Department", htblColNameType,
					htblColNameRefs, "ID");
			
			String tableName = "Employee";
			htblColNameType = new Hashtable<String, String>();
			
			htblColNameType.put("ID", "java.lang.Integer");
			htblColNameType.put("Name", "java.lang.String");
			htblColNameType.put("Dept", "java.lang.String");
			htblColNameType.put("Start_Date", "java.util.Date");

			htblColNameRefs = new Hashtable<String, String>();
			htblColNameRefs.put("ID", "null");
			htblColNameRefs.put("Name", "null");
			htblColNameRefs.put("Dept", "Company.ID");
			htblColNameRefs.put("Start_Date", "null");

			dbEngine.createTable(tableName, htblColNameType, htblColNameRefs,
					"ID");

	
			dbEngine.saveAll();

		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}
	
	public static void testEngine_SelectAll(DBApp dbEngine){
		try {
			String tableName = "Employee";
			Hashtable<String, String> htblColNameValue = null;

			Iterator iter = dbEngine.selectFromTable(tableName,
					htblColNameValue, null);
			if(iter==null){
				System.out.println("No records found");
			   return;
			}
			
			while (iter.hasNext()) {
				Hashtable<String, String> obj = (Hashtable<String, String>) iter.next();
				System.out.println(obj);
			}
			
		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}



}
