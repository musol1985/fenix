package test.fenix.stress.template;

import java.util.ArrayList;
import java.util.List;

import com.sot.fenix.components.models.templates.AModelId;

public class StressThread<M extends Object> implements Runnable{
	private List<M> models;
	private ThreadsManager<M> thdManager;
	private int threadId;
	
	public StressThread(ThreadsManager<M> thdManager, int threadId){
		this.thdManager=thdManager;
		this.models=new ArrayList<M>();
		this.threadId=threadId;
		setModels(thdManager.getAllModels());
	}
	
	public void setModels(List<M> models){
		int countPerThread=models.size()/thdManager.getMaxThreads();
		int start=threadId*countPerThread;
		int end=threadId*countPerThread+countPerThread;
		
		for(int i=start;i<end;i++){
			this.models.add(models.get(i));
		}
	}

	
	@Override
	public void run() {
		try{
			System.out.println("Iniciando actions "+Thread.currentThread().getName());
			for(M m:models){
				thdManager.onDoAction(m);				
			}
			System.out.println("Fin actions "+Thread.currentThread().getName());
			thdManager.onThreadFinish(this);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}


	public List<M> getModels() {
		return models;
	}


	public ThreadsManager<M> getThdManager() {
		return thdManager;
	}


	public int getThreadId() {
		return threadId;
	}
	
	public String getThreadName(){
		return "StressThread"+threadId;
	}
}
