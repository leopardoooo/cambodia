package CASche;

public class CASPrivatePara {
	public static final String MODE_NOTSUPPORT = "NOT_SUPPORT";
	public static final String MODE_SUPPORT = "SUPPORT";
	public static final String MODE_SAMETIME = "SAME_TIME";
	
	String name = null;
	String casType = null;
	String casSQL = null;
	int pool = 0;
	String addMode = MODE_NOTSUPPORT;
	String cancelMode = MODE_NOTSUPPORT;
	
	public String getAddMode(){
		return this.addMode;
	}
	
	public void setAddMode(String addMode){
		this.addMode = addMode;
	}

	public String getCancelMode(){
		return this.cancelMode;
	}
	
	public void setCancelMode(String cancelMode){
		this.cancelMode = cancelMode;
	}

	public String getCasType() {
		return casType;
	}
	public void setCasType(String casType) {
		this.casType = casType;
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCasSQL() {
		return casSQL;
	}
	public void setCasSQL(String casSQL) {
		this.casSQL = casSQL;
	}
	public int getPool() {
		return pool;
	}
	public void setPool(int pool) {
		this.pool = pool;
	}
}
