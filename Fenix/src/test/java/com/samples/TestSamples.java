package com.samples;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sot.fenix.components.rest.SamplesREST;
import com.sot.fenix.dao.SampleDAO;
import com.sot.fenix.db.SamplesRepo;
import com.samples.config.TestDBConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SamplesREST.class, TestDBConfig.class})
public class TestSamples {
	@Autowired
    private SamplesREST samplesREST;
	
	@Autowired
    private SamplesRepo samples;
	
	
	@Before
	public void create() {
		List<SampleDAO> samples=new ArrayList<SampleDAO>();
		
		samples.add(new SampleDAO("A2", "value1"));
		samples.add(new SampleDAO("A3", "value44"));
		samples.add(new SampleDAO("A4", "value003"));
		
		this.samples.save(samples);
	}
	
	@After
	public void  drop(){
		samples.deleteAll();
	}
	
	
	@Test
    public void testSamplesDB(){
		List<SampleDAO> list =samples.findAll();
		assertEquals(3, list.size());
	}

    
    
    @Test
    public void testGetSampleWS() throws Exception {
	    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(samplesREST).build();

	    mockMvc.perform(get("/sample/A2"))
	    		//.andDo(print())
	    		.andExpect(status().isOk())
	    		.andExpect(jsonPath("$.value", containsString("value1")));
    }
}
