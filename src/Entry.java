package todo_manager;

public class Entry implements Comparable<Entry>{
	
    private String name;
    private String startingDate;
    private String endingDate;
    private String startingTime;
    private String endingTime;
    private Boolean doneness;
    
   

	public Entry(String info){
        name = info;
        startingDate = null;
        endingDate = null;
        startingTime = null;
        endingTime = null;
        doneness = false;
    }
    
    public Entry(){
        name = null;
        startingDate = null;
        endingDate = null;
        startingTime = null;
        endingTime = null;
        doneness = false;
    }
    
    public Entry(String info, String startingDate, String endingDate, String startingTime, String endingTime ){
        name = info;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.doneness = false;
    }

	
    public String getName() {
		return name;
	}

	public void setName(String name) {
//		if (name.length() > 55 ){
//			throw new IllegalArgumentException();
//		}
		this.name = name;
	}

	public String getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(String startingDate) {
		this.startingDate = startingDate;
	}

	public String getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(String endingDate) {
		this.endingDate = endingDate;
	}

	public String getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(String startingTime) {
		this.startingTime = startingTime;
	}

	public String getEndingTime() {
		return endingTime;
	}

	public void setEndingTime(String endingTime) {
		this.endingTime = endingTime;
	}

	public Boolean getDoneness() {
		return doneness;
	}

	public void setDoneness(Boolean doneness) {
		this.doneness = doneness;
	}
	
	//@author A0098924M
	private long changeEndingDate(){
		
		long day = Integer.parseInt(this.endingDate.substring(0, 2));
		long month = Integer.parseInt(this.endingDate.substring(2, 4));
		long year = Integer.parseInt(this.endingDate.substring(4, 6));
	
		long time = Integer.parseInt(this.endingTime);
		
		long result= ((year*100000000) + (month*1000000) + (day*10000) + time);
		
		return result;
	}
    
	//@author A0098924M
	public int compareTo(Entry compareEntry) {
		 
		long compareDate = ((Entry) compareEntry).changeEndingDate(); 
 
		//ascending order
		if(this.changeEndingDate() - compareDate > 0){
			return 1;
		}else if(this.changeEndingDate() - compareDate == 0){
			return 0;
		}else{
			return -1;
		}
		//descending order
		//return compareQuantity - this.quantity;
 
	}	
    
	//@author A0098735M
	public Entry copy(){
		Entry exe = new Entry();
		exe.setName(this.name);
		exe.setStartingDate(this.startingDate);
		exe.setEndingDate(this.endingDate);
		exe.setStartingTime(this.startingTime);
		exe.setEndingTime(this.endingTime);
		exe.setDoneness(this.doneness);
		return exe;
	}
}