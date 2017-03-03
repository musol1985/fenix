package test.fenix.stress.template;

import java.util.ArrayList;
import java.util.List;

import com.sot.fenix.components.models.templates.AModelId;

public abstract class ThreadsManager<M extends Object>{

	private int FINALIZADOS=0;
	private List<Thread> threads=new ArrayList<Thread>();
	private boolean finish;
	private long t;
	private AssertionError error;
	
	public ThreadsManager(){
		for(int i=0;i<getMaxThreads();i++){
			StressThread st=new StressThread(this, i);
			threads.add(new Thread(st,st.getThreadName()));
		}
	}
	
	public abstract int getMaxThreads();
	public abstract List<M> getAllModels();
	public abstract void onFinish(long time)throws AssertionError;
	public abstract void onDoAction(M model) throws Exception;

	
	public void start()throws AssertionError{	
		
		t=System.currentTimeMillis();
		finish=false;
		for(Thread t:threads){
			t.start();
		}
		
		while(!finish && error==null){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Main Thread finish: "+(System.currentTimeMillis()-t)+"ms");
		if(error!=null){
			throw error;
		}		
	}

	
	public void onThreadFinish(StressThread<M> stressThread) throws Exception{
		synchronized (threads) {
			System.out.println("Thread "+stressThread.getThreadName()+"Finalizado: "+(System.currentTimeMillis()-t)+"ms");
			FINALIZADOS++;
			if(FINALIZADOS==threads.size()){
				System.out.println("All Threads Finalizados: "+(System.currentTimeMillis()-t)+"ms");
				try{
					onFinish((System.currentTimeMillis()-t));
				}catch(AssertionError e){
					error=e;
				}
				finish=true;
			}
		}
	}
	
	


	
}