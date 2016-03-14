package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("positionId")
public class Position {
    private long positionId;
    private long customerId;
    private long fundId;
    private long shares;

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getFundId() {
        return fundId;
    }

    public void setFundId(long fundId) {
        this.fundId = fundId;
    }

    public long getShares() {
        return shares;
    }

    public void setShares(long shares) {
        this.shares = shares;
    }

}
