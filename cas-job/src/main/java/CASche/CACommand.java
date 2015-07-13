package CASche;

public class CACommand {
	String userID;
	String custID;
	String casID;
	String casType;
	String stbID;
	String cardID;
	String cmdType;
	String controlID;
	String authBeginDate;
	String authEndDate;
	String detailParams;
	String areaID;
	int  count = 1;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getCustID() {
		return custID;
	}
	public void setCustID(String custID) {
		this.custID = custID;
	}
	public String getAreaID() {
		return areaID;
	}
	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}

	
	public String getCasID() {
		return casID;
	}
	public void setCasID(String casID) {
		this.casID = casID;
	}
	public String getCasType() {
		return casType;
	}
	public void setCasType(String casType) {
		this.casType = casType;
	}
	public String getStbID() {
		return stbID;
	}
	public void setStbID(String stbID) {
		this.stbID = stbID;
	}
	public String getCardID() {
		return cardID;
	}
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
	public String getCmdType() {
		return cmdType;
	}
	public void setCmdType(String cmdType) {
		this.cmdType = cmdType;
	}
	public String getControlID() {
		return controlID;
	}
	public void setControlID(String controlID) {
		this.controlID = controlID;
	}
	public String getAuthBeginDate() {
		return authBeginDate;
	}
	public void setAuthBeginDate(String authBeginDate) {
		this.authBeginDate = authBeginDate;
	}
	public String getAuthEndDate() {
		return authEndDate;
	}
	public void setAuthEndDate(String authEndDate) {
		this.authEndDate = authEndDate;
	}
	public String getDetailParams() {
		return detailParams;
	}
	public void setDetailParams(String detailParams) {
		this.detailParams = detailParams;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
