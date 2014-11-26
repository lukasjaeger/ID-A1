package de.tu_darmstadt.id_a1;

public class WaitThread extends Thread{
	int periode;
	
	public WaitThread(int periode){
		this.periode = periode;
	}
	@Override
	public void run(){
		super.run();
		try {
			this.sleep(periode);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
