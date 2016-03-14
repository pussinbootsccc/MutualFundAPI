package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databeans.Position;

public class PositionDAO extends GenericDAO<Position> {

    public PositionDAO(String tableName, ConnectionPool cp) throws DAOException {
        super(Position.class, tableName, cp);
    }

    public void add(Position p) throws RollbackException {
        try {
            Transaction.begin();

            // Create a new ItemBean in the database with the next id number
            create(p);

            Transaction.commit();
        } finally {
            if (Transaction.isActive())
                Transaction.rollback();
        }
    }

    public void updatePosition(Position p) throws RollbackException {
        try {
            Transaction.begin();
            update(p);
            Transaction.commit();
        } finally {
            if (Transaction.isActive())
                Transaction.rollback();
        }
    }

    public void updateNoT(Position p) throws RollbackException {
        update(p);
    }

    public Position getByCustomerIdAndFundId(long customerId, long fundId) throws RollbackException {
        Position[] positions;
        positions = match(MatchArg.and(MatchArg.equals("customerId", customerId), MatchArg.equals("fundId", fundId)));
        if (positions.length != 0) {
            // System.out.println("id: " + positions[0].getPositionId() + "---Cid: " + positions[0].getCustomerId() +
            // "---Fid: " + positions[0].getFundId() + "---shares: " + positions[0].getShares());
            return positions[0];
        } else {
            return null;
        }
    }

    public Position getByCustomerId(long customerId) throws RollbackException {
        Position[] positions;
        positions = match(MatchArg.equals("customerId", customerId));
        return positions[0];
    }

    /**
     * Update the position in transaction day.type: 1 sell, 2 buy
     * @param fundId
     * @param customerId
     * @param amount
     * @param type
     * @throws RollbackException
     */
    public void updatePositionInTransactionDay(long fundId, long customerId, long shares, int type)
            throws RollbackException {
        /*
         * 1. sell if the position share > shares, deduct the position share if not =, delete the record;
         */

        if (type == 1) {
            Position position = getByCustomerIdAndFundId(customerId, fundId);
            // Here I assume that the position is exist!!! Dangerous

            if (position.getShares() > shares) {
                position.setShares(position.getShares() - shares);
                update(position);
            } else if (position.getShares() == shares) {
                delete(position.getPositionId());
            }
        }

        /*
         * 2. buy if the position exist, add the position share if not exist, create new record;
         */
        // System.out.println("position");
        if (type == 2) {
            Position position = getByCustomerIdAndFundId(customerId, fundId);
            // System.out.println(position.getPositionId());
            if (position != null) {
                position.setShares(position.getShares() + shares);
                update(position);
            } else {
                position = new Position();
                position.setCustomerId(customerId);
                position.setFundId(fundId);
                position.setShares(shares);
                create(position);
            }
        }

        return;
    }

}
