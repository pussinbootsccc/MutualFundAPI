
package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databeans.Employee;

public class EmployeeDAO extends GenericDAO<Employee> {
	public EmployeeDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(Employee.class, tableName, pool);

	}

	// To fetch a specific employee by given ID.
	public Employee getByEmployeeId(long employeeId) throws RollbackException {
		return read(employeeId);
	}

	// To fetch a list of all employees.
	public Employee[] getAllEmployee() throws RollbackException {
		Employee[] em = match();
		return em;
	}

	// To fetch the employee by reading the given userName.
	public Employee getByUserName(String userName) throws RollbackException {
		Employee[] emlist = match(MatchArg.equals("userName", userName));
		if (emlist.length != 0) {
			return emlist[0];
		// No matched employee, bean is null
		} else {
			return null;
		}
	}

	// To create a new employee
	public void createEmployee(Employee em) throws RollbackException {
		try {
			Transaction.begin();
			if (getByUserName(em.getUserName()) != null) {
				throw new RollbackException("This username already exists");
			}
			create(em);
			Transaction.commit();
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}

	// To change password of a given employee by userName
	public void setPassword(String userName, String password) throws RollbackException {
		try {
			Transaction.begin();
			// Fetch the employee from database;
			Employee dbEm = getByUserName(userName);
			if (dbEm == null) {
				throw new RollbackException("Employee " + userName + " no longer exists");
			}
			// Reset password
			dbEm.setPassword(password);
			// Update the database.
			update(dbEm);
			Transaction.commit();
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}
}
