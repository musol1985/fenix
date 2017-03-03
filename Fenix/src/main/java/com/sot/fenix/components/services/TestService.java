package com.sot.fenix.components.services;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Test.TestModel;
import com.sot.fenix.components.models.Test.TestSubModel;
import com.sot.fenix.dao.TestDAO;
import com.sot.fenix.templates.service.ACentroIdService;

@Service
public class TestService extends ACentroIdService<TestDAO, TestModel> {
	final static Logger log = LogManager.getLogger(TestService.class);
	
	public static long max;
	
	@Autowired
	protected MongoTemplate mt;
	
	@SuppressWarnings("unused")
	public void generarTestsModels(String texto, Centro centro, int submodelsSize, String textSubmodels, int postSize){
		log.error("#####################################");
		log.error("generando test "+texto);
		TestModel t=new TestModel();
		t.setTexto(texto);
		t.setSubs(new ArrayList<TestSubModel>());
		log.error("generando subs "+submodelsSize);
		long to=System.currentTimeMillis();
		for(int i=0;i<submodelsSize;i++){			
			t.getSubs().add(generarSub(textSubmodels, i));
		}
		log.error("subs generados en: "+(System.currentTimeMillis()-to)+"ms");
		getDAO().save(t);
		log.error("test generado en: "+(System.currentTimeMillis()-to)+"ms MAX: "+max);
		
		long min=99999999999999l;
		long max=0;
		long media=0;
		long totalPosts=System.currentTimeMillis();
		
		for(int i=0;i<postSize;i++){
			TestSubModel ts=generarSub("nuevo", t.getSubs().size()+1);
			
			long to2=System.currentTimeMillis();
			
			if(false){
				t.getSubs().add(ts);
				getDAO().save(t);
			}else{
				saveSubTests(t, ts);
			}
			
			long nt=(System.currentTimeMillis()-to2);
			
			if(nt<min)min=nt;
			if(nt>max)max=nt;
			media+=nt;
		}
		media=media/postSize;
		
		log.error("Posts ("+postSize+")-> Total: "+(System.currentTimeMillis()-totalPosts)+"ms, min:"+min+", max: "+max+", media: "+media);
		log.error("Total ("+t.getSubs().size()+") en: "+(System.currentTimeMillis()-to)+"ms MAX: "+max);
		log.error("#####################################");
		
		if((System.currentTimeMillis()-to)>max)
			max=(System.currentTimeMillis()-to);
	}
	
	public TestSubModel generarSub(String txt, int i){
		TestSubModel tsm=new TestSubModel();
		tsm.setTextoSub(txt+i);
		return tsm;
	}
	
	private boolean saveSubTests(TestModel test, TestSubModel subModels) {
       int count= mt.updateFirst(
            Query.query(Criteria.where("id").is(test.getId())), 
            new Update().push("subs", subModels), TestModel.class).getN();
       
       if(count>0){
    	   test.getSubs().add(subModels);
    	   return true;
       }
       return false;
    }
}
