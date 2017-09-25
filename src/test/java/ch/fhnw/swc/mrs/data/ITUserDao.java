package ch.fhnw.swc.mrs.data;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.NoSuchColumnException;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.xml.sax.InputSource;

import ch.fhnw.swc.mrs.model.PriceCategory;
import ch.fhnw.swc.mrs.model.User;


public class ITUserDao extends DBTestCase {

	/** Class under test: UserDAO. */
	private UserDAO dao;
    private IDatabaseTester tester;
    private Connection connection;

    private static final String COUNT_SQL = "SELECT COUNT(*) FROM clients";
    private static final String DB_CONNECTION = "jdbc:hsqldb:mem:mrs";

	/** Create a new Integration Test object. */
	public ITUserDao() {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, HsqlDatabase.DB_DRIVER);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, DB_CONNECTION);
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "sa");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "");
	}    
    
	@Override
	protected void setUpDatabaseConfig(DatabaseConfig config) {
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		InputStream stream = this.getClass().getResourceAsStream("UserDaoTestData.xml");
		return new FlatXmlDataSetBuilder().build(new InputSource(stream));
	}

	static {
		try {
			new HsqlDatabase().initDB(DB_CONNECTION);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize a DBUnit DatabaseTester object to use in tests.
	 * 
	 * @throws Exception
	 *             whenever something goes wrong.
	 */
	public void setUp() throws Exception {
		super.setUp();
        PriceCategory.init();
        tester = new JdbcDatabaseTester(HsqlDatabase.DB_DRIVER, DB_CONNECTION);
		connection = getConnection().getConnection();
		dao = new SQLUserDAO(connection);
	}

	public void tearDown() throws Exception {
		connection.close();
		tester.onTearDown();
	}

    public void testDeleteNonexisting() throws Exception {
        // count no. of rows before deletion
        Statement s = connection.createStatement();
        ResultSet r = s.executeQuery(COUNT_SQL);
        r.next();
        int rows = r.getInt(1);
        assertEquals(3, rows);

        // Delete non-existing record
        User user = new User("Denzler", "Christoph", LocalDate.now());
        user.setId(42);
        dao.delete(user);
        
        r = s.executeQuery(COUNT_SQL);
        r.next();
        rows = r.getInt(1);
        assertEquals(2, rows);
    }
    
    
    public void testDelete() throws Exception {
        // count no. of rows before deletion
        Statement s = connection.createStatement();
        ResultSet r = s.executeQuery(COUNT_SQL);
        r.next();
        int rows = r.getInt(1);
        assertEquals(3, rows);

        // delete existing record
        User user = new User("Duck", "Donald", LocalDate.of(2013, 1, 13));
        user.setId(13);
        dao.delete(user);
        
        // Fetch database data after deletion
        IDataSet databaseDataSet = tester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("CLIENTS");

        InputStream stream = this.getClass().getResourceAsStream("UserDaoTestResult.xml");
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(stream);
        ITable expectedTable = expectedDataSet.getTable("CLIENTS");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    public void testGetAll() throws DatabaseUnitException, SQLException, Exception {
        List<User> userlist = dao.getAll();
        ITable actualTable = convertToTable(userlist);

        InputStream stream = this.getClass().getResourceAsStream("UserDaoTestData.xml");
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(stream);
        ITable expectedTable = expectedDataSet.getTable("CLIENTS");

        Assertion.assertEquals(expectedTable, actualTable);
    }
    
    public void testGetAllSingleRow() throws Exception {
        InputStream stream = this.getClass().getResourceAsStream("UserDaoSingleRowTest.xml");
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(stream);
        DatabaseOperation.CLEAN_INSERT.execute(tester.getConnection(), dataSet);

        List<User> userlist = dao.getAll();
        assertEquals(1, userlist.size());
        assertEquals("Bond", userlist.get(0).getName());
    }
    
    public void testGetAllEmptyTable() throws Exception {
    	InputStream stream = this.getClass().getResourceAsStream("UserDaoEmpty.xml");
        IDataSet dataSet = new XmlDataSet(stream);
        DatabaseOperation.CLEAN_INSERT.execute(tester.getConnection(), dataSet);

        List<User> userlist = dao.getAll();
        assertNotNull(userlist);
        assertEquals(0, userlist.size());
    }

    public void testGetById() throws SQLException {
        User user = dao.getById(42);
        assertEquals("Micky", user.getFirstName());
        assertEquals("Mouse", user.getName());
        assertEquals(42, user.getId());
    }

    public void testGetByName() throws SQLException {
        List<User> userlist = dao.getByName("Duck");
        assertEquals(2, userlist.size());
    }

    public void testUpdate() throws SQLException {
        // count no. of rows before operation
        Statement s = connection.createStatement();
        ResultSet r = s.executeQuery(COUNT_SQL);
        r.next();
        int rows0 = r.getInt(1);

        User daisy = dao.getById(13);
        daisy.setFirstName("Daisy");
        dao.saveOrUpdate(daisy);
        User actual = dao.getById(13);
        assertEquals(daisy.getFirstName(), actual.getFirstName());

        r = s.executeQuery(COUNT_SQL);
        r.next();
        int rows1 = r.getInt(1);
        assertEquals(rows0, rows1);
    }
    
    public void testSave() throws Exception {
        // count no. of rows before operation
        Statement s = connection.createStatement();
        ResultSet r = s.executeQuery(COUNT_SQL);
        r.next();
        int rows1 = r.getInt(1);

        User goofy = new User("Goofy", "Goofus", LocalDate.of(1936, 10, 12));
        dao.saveOrUpdate(goofy);
        User actual = dao.getById(goofy.getId());
        assertEquals(goofy.getFirstName(), actual.getFirstName());

        r = s.executeQuery(COUNT_SQL);
        r.next();
        int rows2 = r.getInt(1);
        assertEquals(rows1 + 1, rows2);
    }
    
	@SuppressWarnings("deprecation")
	private ITable convertToTable(List<User> userlist) throws Exception {
		ITableMetaData meta = new TableMetaData();
		DefaultTable t = new DefaultTable(meta);
		int row = 0;
		for (User u : userlist) {
			t.addRow();
			LocalDate d = u.getBirthdate();
			t.setValue(row, "id", u.getId());
			t.setValue(row, "name", u.getName());
			t.setValue(row, "firstname", u.getFirstName());
			t.setValue(row, "birthdate", new Date(d.getYear()-1900, d.getMonthValue()-1, d.getDayOfMonth()));
			row++;
		}
		return t;
	}

	private static final class TableMetaData implements ITableMetaData {

		private List<Column> cols = new ArrayList<>();

		TableMetaData() {
			cols.add(new Column("id", DataType.INTEGER));
			cols.add(new Column("name", DataType.VARCHAR));
			cols.add(new Column("firstname", DataType.VARCHAR));
			cols.add(new Column("birthdate", DataType.DATE));
		}

		@Override
		public int getColumnIndex(String colname) throws DataSetException {
			int index = 0;
			for (Column c : cols) {
				if (c.getColumnName().equals(colname.toLowerCase())) {
					return index;
				}
				index++;
			}
			throw new NoSuchColumnException(getTableName(), colname);
		}

		@Override
		public Column[] getColumns() throws DataSetException {
			return cols.toArray(new Column[4]);
		}

		@Override
		public Column[] getPrimaryKeys() throws DataSetException {
			Column[] cols = new Column[1];
			cols[0] = new Column("id", DataType.INTEGER);
			return cols;
		}

		@Override
		public String getTableName() {
			return "clients";
		}
	}
}
